package lighting;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class LightsTests {
    /**
     * First scene for some of the tests
     */
    private final Scene scene1 = new Scene("Test scene");
    /**
     * Second scene for some of the tests
     */
    private final Scene scene2 = new Scene("Test scene")
            .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

    /**
     * First camera builder for some of the tests
     */
    private final Camera.Builder camera1 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene1))
            .setLocation(new Point(0, 0, 1000))
            //.setDirection(Point.ZERO, Vector.UP)
            .setDirection(Vector.BACKWARDS, Vector.UP)
            .setVpSize(150, 150).setVpDistance(1000);
    /**
     * Second camera builder for some of the tests
     */
    private final Camera.Builder camera2 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene2))
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Vector.BACKWARDS, Vector.UP)
            .setVpSize(200, 200).setVpDistance(1000);

    /**
     * Shininess value for most of the geometries in the tests
     */
    private static final int SHININESS = 301;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final double KD = 0.5;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final double KS = 0.5;
    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

    /**
     * Material for some of the geometries in the tests
     */
    private final Material material = new Material().setKd(KD3).setKs(KS3).setShininess(SHININESS);
    /**
     * Light color for tests with triangles
     */
    private final Color trianglesLightColor = new Color(800, 500, 250);
    /**
     * Light color for tests with sphere
     */
    private final Color sphereLightColor = new Color(800, 500, 0);
    /**
     * Color of the sphere
     */
    private final Color sphereColor = new Color(BLUE).reduce(2);

    /**
     * Center of the sphere
     */
    private final Point sphereCenter = new Point(0, 0, -50);
    /**
     * Radius of the sphere
     */
    private static final double SPHERE_RADIUS = 50d;

    /**
     * The triangles' vertices for the tests with triangles
     */
    private final Point[] vertices =
            {
                    // the shared left-bottom:
                    new Point(-110, -110, -150),
                    // the shared right-top:
                    new Point(95, 100, -150),
                    // the right-bottom
                    new Point(110, -110, -150),
                    // the left-top
                    new Point(-75, 78, 100)
            };
    /**
     * Position of the light in tests with sphere
     */
    private final Point sphereLightPosition = new Point(-50, -50, 25);
    /**
     * Light direction (directional and spot) in tests with sphere
     */
    private final Vector sphereLightDirection = new Vector(1, 1, -0.5);
    /**
     * Position of the light in tests with triangles
     */
    private final Point trianglesLightPosition = new Point(30, 10, -100);
    /**
     * Light direction (directional and spot) in tests with triangles
     */
    private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

    /**
     * The sphere in appropriate tests
     */
    private final Geometry sphere = new Sphere(sphereCenter, SPHERE_RADIUS)
            .setEmission(sphereColor).setMaterial(new Material().setKd(KD).setKs(KS).setShininess(SHININESS));
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
            .setMaterial(material);
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
            .setMaterial(material);

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    public void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

        camera1.setImageWriter(new ImageWriter("lightSphereDirectional", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    public void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition)
                .setKl(0.001).setKq(0.0002));

        camera1.setImageWriter(new ImageWriter("lightSpherePoint", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    public void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection)
                .setKl(0.001).setKq(0.0001));

        camera1.setImageWriter(new ImageWriter("lightSphereSpot", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of two triangles lighted by a directional light
     */
    @Test
    public void trianglesDirectional() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

        camera2.setImageWriter(new ImageWriter("lightTrianglesDirectional", 500, 500)) //
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of two triangles lighted by a point light
     */
    @Test
    public void trianglesPoint() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition)
                .setKl(0.001).setKq(0.0002));

        camera2.setImageWriter(new ImageWriter("lightTrianglesPoint", 500, 500)) //
                .build() //
                .renderImage() //
                .writeToImage(); //
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight
     */
    @Test
    public void trianglesSpot() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setKl(0.001).setKq(0.0001));

        camera2.setImageWriter(new ImageWriter("lightTrianglesSpot", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a narrow spotlight
     */
    @Test
    public void sphereSpotSharp() {
        scene1.geometries.add(sphere);
        scene1.lights
                .add(new FlashLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5), 15)
                        .setKl(0.001).setKq(0.00004));

        camera1.setImageWriter(new ImageWriter("lightSphereFlashLight", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of two triangles lighted by a narrow spotlight
     */
    @Test
    public void trianglesSpotSharp() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new FlashLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection, 15)
                .setKl(0.001).setKq(0.00004));

        camera2.setImageWriter(new ImageWriter("lightTrianglesFlashLight", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Tests for fun!
     */
    @Test
    public void miscellaneousTests() {
        Material material1 = new Material();
        //----------------------geometries----------------------
        material1.setKd(0.4d).setKs(4d).setShininess(20);
        geometries.Polygon polygon1 = new Polygon(
                new Point(1, 0, 0),
                new Point(2, 2, 0),
                new Point(2, 4, 0),
                new Point(-1, 6, 0),
                new Point(-3, 4, 0),
                new Point(-4, 1, 0),
                new Point(-1, -1, 0));
        polygon1.setMaterial(material1);
        polygon1.setEmission(new Color(84, 56, 40));

        Sphere sphere1 = new Sphere(new Point(0, 1, 500), 450);
        sphere1.setMaterial(material1)
                .setEmission(new Color(0, 0, 0));

        Plane plane1 = new Plane(new Point(5, 0, 0), new Point(0, 0, -7), new Point(2, 10, -3));
        plane1.setMaterial(material1)
                .setEmission(new Color(0, 0, 0));
        //----------------------lights----------------------
        DirectionalLight directionalLight1 = new DirectionalLight(
                new Color(237, 85, 71), new Vector(1, -0.2, -1));

        DirectionalLight directionalLight2 = new DirectionalLight(
                new Color(125, 39, 72), Vector.BACKWARDS);

        PointLight pointLight1 = new PointLight(
                new Color(181, 2, 38), new Vector(250, 5, 550));
        pointLight1.setKl(0.000001).setKq(0.000001).setKc(0.000001);
        PointLight pointLight2 = new PointLight(
                new Color(214, 111, 111), new Vector(300, 5, 450));
        pointLight1.setKl(0.000001).setKq(0.000001).setKc(0.000001);
        PointLight pointLight3 = new PointLight(
                new Color(61, 41, 25), new Vector(250, 0, 450));
        pointLight1.setKl(0.000001).setKq(0.000001).setKc(0.000001);

        FlashLight flashLight1 = new FlashLight(new Color(BLACK), new Point(1, 1, 1300),
                Vector.BACKWARDS, 10);
        flashLight1.setKl(1).setKq(1).setKc(1);

        //----------------------scene----------------------
        Scene testScene = new Scene("Cool Scene");
        testScene.setAmbientLight(new AmbientLight(new Color(145, 181, 154), 0d))
                .setBackground(new Color(BLACK))
                .setGeometries(new Geometries(sphere1, plane1))
                .setLights(directionalLight1, directionalLight2, pointLight3);
        SimpleRayTracer tracer = new SimpleRayTracer(testScene);

        //----------------------camera----------------------
        camera2.setImageWriter(new ImageWriter("FunImage2", 1920, 1080))
                .setLocationAndDirection(new Point(1, 1, 1300), Point.ZERO)
                .setVpSize(54, 96)
                .setVpDistance(10)
                .setRayTracer(tracer)
                .build()
                .renderImage()
                .writeToImage();
    }
}
