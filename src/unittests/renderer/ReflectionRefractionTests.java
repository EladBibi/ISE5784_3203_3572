package renderer;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(Vector.BACKWARDS, Vector.UP)
            .setRayTracer(new SimpleRayTracer(scene));

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 1080, 1080))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        scene.geometries.add(
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setKt(new Double3(0.5, 0, 0))),
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(1d)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
                .setVpSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 1080, 1080))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 1080, 1080))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Bonus image
     */
    @Test
    public void testImage1() {
        Point a1 = new Point(100, 100, 100);
        Point top = new Point(0, 200, 0);
        Point a3 = new Point(150, 100, 0);
        Point a4 = new Point(100, 100, -100);
        Point a5 = new Point(0, 100, -150);
        Point a6 = new Point(-100, 100, -100);
        Point a7 = new Point(-150, 100, 0);
        Point a8 = new Point(-100, 100, 100);
        Point a9 = new Point(0, 100, 150);
        Point bottom = new Point(0, -150, 0);
        Point k = new Point(0, 100, 0);
        Point k1 = new Point(40, 40, 40);
        Point k2 = new Point(0, 40, 60);
        Point k3 = new Point(-40, 40, 40);
        Point k4 = new Point(-60, 40, 0);
        Point k5 = new Point(-40, 40, -40);
        Point k6 = new Point(0, 40, -60);
        Point k7 = new Point(40, 40, -40);
        Point k8 = new Point(60, 40, 0);
        Point k9 = new Point(0, -20, 0);

        Point c1 = new Point(-200, -150, -250);
        Point c2 = new Point(-300, -150, -250);
        Point c3 = new Point(-300, -150, -350);
        Point c4 = new Point(-200, -150, -350);
        Point c5 = new Point(-200, -50, -350);
        Point c6 = new Point(-300, -50, -350);
        Point c7 = new Point(-300, -50, -250);
        Point c8 = new Point(-200, -50, -250);
        Point cc1 = new Point(-330, -150, -270);
        Point cc2 = new Point(-430, -150, -270);
        Point cc3 = new Point(-430, -150, -330);
        Point cc4 = new Point(-330, -150, -330);
        Point cc5 = new Point(-330, -90, -330);
        Point cc6 = new Point(-430, -90, -330);
        Point cc7 = new Point(-430, -90, -270);
        Point cc8 = new Point(-330, -90, -270);

        Point s1 = new Point(350, -150, 0);
        Point s2 = new Point(340, -150, 20);
        Point s3 = new Point(340, -150, 40);
        Point s4 = new Point(340, -150, 60);
        Point s5 = new Point(332.03612, -150, 78.05912);
        Point s6 = new Point(320, -150, 100);
        Point s7 = new Point(300, -150, 120);
        Point s8 = new Point(286.52088, -150, 140.32397);
        Point s9 = new Point(275.15374, -150, 160);
        Point s10 = new Point(270, -150, 180);
        Point s11 = new Point(260, -150, 200);
        Point s12 = new Point(250, -150, 220);
        Point s13 = new Point(240, -150, 240);
        Point s14 = new Point(226.16019, -150, 260);
        Point s15 = new Point(210, -150, 275);
        Point s16 = new Point(200, -150, 295);
        Point s17 = new Point(190, -150, 313.94112);

        Material bigDiamondMat = new Material().setKd(0.2d).setKs(0.2d).setShininess(20).setKr(0.1d).setKt(0.9d);
        Material smallDiamondMat = new Material().setKd(0.3d).setKs(0.7d).setShininess(500).setKt(0.8);
        Material cubeMat = new Material().setKd(0.9d).setKs(0.9d).setShininess(700).setKt(0.7d).setKr(0d);
        Material planeMat = new Material().setKd(0.1d).setKs(0.2d).setShininess(300).setKr(0.2d);
        Material sphereMat = new Material().setKd(0.1d).setKs(0.1d).setShininess(30).setKr(0.2d).setKt(0.3d);
        Color cubeColor = Color.BLACK;
        double sphereRadius = 10d;

        scene.geometries.add(
                new Plane(new Point(1, -151, 1), Vector.UP).setMaterial(planeMat),
                //.setEmission(new Color(59, 50, 39)),
                //big diamond
                new Triangle(a1, top, a3).setMaterial(bigDiamondMat),
                new Triangle(a3, top, a4).setMaterial(bigDiamondMat),
                new Triangle(a4, top, a5).setMaterial(bigDiamondMat),
                new Triangle(a5, top, a6).setMaterial(bigDiamondMat),
                new Triangle(a6, top, a7).setMaterial(bigDiamondMat),
                new Triangle(a7, top, a8).setMaterial(bigDiamondMat),
                new Triangle(a8, top, a9).setMaterial(bigDiamondMat),
                new Triangle(a9, top, a1).setMaterial(bigDiamondMat),
                new Triangle(a1, bottom, a3).setMaterial(bigDiamondMat),
                new Triangle(a3, bottom, a4).setMaterial(bigDiamondMat),
                new Triangle(a4, bottom, a5).setMaterial(bigDiamondMat),
                new Triangle(a5, bottom, a6).setMaterial(bigDiamondMat),
                new Triangle(a6, bottom, a7).setMaterial(bigDiamondMat),
                new Triangle(a7, bottom, a8).setMaterial(bigDiamondMat),
                new Triangle(a8, bottom, a9).setMaterial(bigDiamondMat),
                new Triangle(a9, bottom, a1).setMaterial(bigDiamondMat),
                //little diamond
                new Triangle(k1, k, k2).setMaterial(smallDiamondMat),
                new Triangle(k2, k, k3).setMaterial(smallDiamondMat),
                new Triangle(k3, k, k4).setMaterial(smallDiamondMat),
                new Triangle(k4, k, k5).setMaterial(smallDiamondMat),
                new Triangle(k5, k, k6).setMaterial(smallDiamondMat),
                new Triangle(k6, k, k7).setMaterial(smallDiamondMat),
                new Triangle(k7, k, k8).setMaterial(smallDiamondMat),
                new Triangle(k8, k, k1).setMaterial(smallDiamondMat),
                new Triangle(k1, k9, k2).setMaterial(smallDiamondMat),
                new Triangle(k2, k9, k3).setMaterial(smallDiamondMat),
                new Triangle(k3, k9, k4).setMaterial(smallDiamondMat),
                new Triangle(k4, k9, k5).setMaterial(smallDiamondMat),
                new Triangle(k5, k9, k6).setMaterial(smallDiamondMat),
                new Triangle(k6, k9, k7).setMaterial(smallDiamondMat),
                new Triangle(k7, k9, k8).setMaterial(smallDiamondMat),
                new Triangle(k8, k9, k1).setMaterial(smallDiamondMat),
                //cube1
                new Polygon(c1, c2, c3, c4).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c5, c6, c7, c8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c1, c2, c7, c8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c2, c3, c6, c7).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c3, c4, c5, c6).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c1, c4, c5, c8).setMaterial(cubeMat).setEmission(cubeColor)
                //cube2
//              new Polygon(cc1,cc2,cc3,cc4).setMaterial(cubeMat).setEmission(cubeColor),
//              new Polygon(c5,cc6,cc7,cc8).setMaterial(cubeMat).setEmission(cubeColor),
//              new Polygon(cc1,cc2,cc7,cc8).setMaterial(cubeMat).setEmission(cubeColor),
//              new Polygon(cc2,cc3,cc6,cc7).setMaterial(cubeMat).setEmission(cubeColor),
//              new Polygon(cc3,cc4,cc5,cc6).setMaterial(cubeMat).setEmission(cubeColor),
//              new Polygon(cc1,cc4,cc5,cc8).setMaterial(cubeMat).setEmission(cubeColor)
                //spheres
//              new Sphere(s1,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s2,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s3,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s4,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s5,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s6,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s7,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s8,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s9,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s10,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s11,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s12,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s13,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s14,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s15,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s16,sphereRadius).setMaterial(sphereMat),
//              new Sphere(s17,sphereRadius).setMaterial(sphereMat)
        );
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0d));

        Color spotColor1 = new Color(122, 81, 48);
        Color pointColor2 = new Color(256, 175, 109);
        Color spotColor3 = new Color(270, 263, 171);
        scene.setLights(
                new PointLight(pointColor2, new Point(0, 40, 0))
                        .setKl(4E-5).setKq(2E-7),
                new SpotLight(spotColor1, new Point(-453, 84, 200), Point.ZERO)
                        .setKl(4E-5).setKq(2E-7),
                new SpotLight(spotColor3, new Point(-374.41164, 400.63026, -879.30782), Point.ZERO)
                        .setKl(4E-5).setKq(2E-7)
//              new SpotLight(spotColor1, new Point(150, 0, -150), Point.ZERO)
//                      .setKl(4E-5).setKq(2E-7),
//              new SpotLight(spotColor1, new Point(150, 0, 150), Point.ZERO)
//                      .setKl(4E-5).setKq(2E-7),
//              new SpotLight(spotColor1, new Point(0, 250, 0), top)
//                      .setKl(4E-5).setKq(2E-7)
//              new SpotLight(spotColor3,new Point(-350,100,-350),new Point(-250,-150,-300))
//                      .setKl(4E-5).setKq(2E-7)
        );

        cameraBuilder
                .setFocusPoint(new Point(-2800, 2000, 3500), new Point(-140, 0, 0))
                .setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("bonusImage5", 800, 800))
                .build()
                .rotate(0)
                .renderImage()
                .writeToImage();
    }
}
