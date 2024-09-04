package imageRenders;

import geometries.Geometries;
import geometries.Polygon;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.VoxelRayTracer;
import scene.Scene;

import static java.awt.Color.WHITE;

/**
 * A scene with 3 boxes containing diamonds in varying shapes and sizes
 * demonstrating the shading and lighting-model of our renderer. including
 * diffusive &amp; specular lighting and reflection &amp; transparency ray casts
 */
public class DiamondsScene {

    /**
     * The inner folder for the images
     */
    private final String directoryName = "diamonds scene/";

    /**
     * Camera builder for the renderings
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();

    /**
     * Render of scene with diamonds inside cubes, for stage 7 bonus
     */
    @Test
    @Disabled
    public void bonusImage() {
        final Scene scene = new Scene("Diamonds Scene");
        scene.geometries.add(buildScene());
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0d));
        Color spotColor1 = new Color(86, 135, 204);
        Color spotColor4 = new Color(59, 35, 21);
        Color pointColor1 = new Color(28, 97, 64);
        scene.setLights(
                new PointLight(pointColor1, new Point(-150, 0, -150))
                        .setKl(4E-5).setKq(2E-7),
                new SpotLight(spotColor1, new Point(-400, 80, 200), new Point(-100, 0, -100))
                        .setKl(4E-5).setKq(2E-7),
                new SpotLight(spotColor4, new Point(-1300, 300, -1300), new Point(0, 0, 0))
                        .setKl(4E-5).setKq(2E-7)
        );


        cameraBuilder
                .setRayTracer(new VoxelRayTracer(scene))
                .setFocusPoint(new Point(-1000, 1700, 4000), new Point(-100, -120, -100))
                .setVpDistance(800)
                .setVpSize(135, 240)
                .setImageWriter(new ImageWriter(directoryName + "voxel tracer 2", 1920, 1080))
                .build()
                .enableMultiThreading(4)
                .rotate(0)
                .renderImage()
                .writeToImage();
    }

    /**
     * Scene builder
     *
     * @return collection of geometries that form the scene
     */
    private Geometries buildScene() {
        //big diamond
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
        //small internal diamond
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
        //cube 1
        Point c1 = new Point(-200, -150, -250);
        Point c2 = new Point(-300, -150, -250);
        Point c3 = new Point(-300, -150, -350);
        Point c4 = new Point(-200, -150, -350);
        Point c5 = new Point(-200, -50, -350);
        Point c6 = new Point(-300, -50, -350);
        Point c7 = new Point(-300, -50, -250);
        Point c8 = new Point(-200, -50, -250);
        //cube 2
        Point cc1 = new Point(-400, -150, -110);
        Point cc2 = new Point(-500, -150, -110);
        Point cc3 = new Point(-500, -150, -190);
        Point cc4 = new Point(-400, -150, -190);
        Point cc5 = new Point(-400, -90, -190);
        Point cc6 = new Point(-500, -90, -190);
        Point cc7 = new Point(-500, -90, -110);
        Point cc8 = new Point(-400, -90, -110);
        //big cube
        Point b1 = new Point(-150, 200, -150);
        Point b2 = new Point(150, 200, -150);
        Point b3 = new Point(150, 200, 150);
        Point b4 = new Point(-150, 200, 150);
        Point b5 = new Point(-150, -150, 150);
        Point b6 = new Point(150, -150, 150);
        Point b7 = new Point(150, -150, -150);
        Point b8 = new Point(-150, -150, -150);

        //diamond inside cube
        Point d1 = new Point(-250, -60, -300);
        Point d2 = new Point(-280, -90, -300);
        Point d3 = new Point(-250, -90, -330);
        Point d4 = new Point(-220, -90, -300);
        Point d5 = new Point(-250, -90, -270);
        Point d6 = new Point(-250, -140, -300);


        Material bigDiamondMat = new Material().setKd(0.5d).setKs(0.5d).setShininess(600).setKt(0.85d).setKr(0.15);
        Material smallDiamondMat = new Material().setKd(0.8d).setKs(0.2d).setShininess(500).setKr(0.6).setKt(0.2);
        Material cubeDiamondMat = new Material().setKd(0.8d).setKs(0.2d).setShininess(500).setKr(0.7).setKt(0.2);
        Material cubeMat = new Material().setKd(0.5d).setKs(0.5d).setShininess(700).setKt(0.7d).setKr(0.2d);
        //  .setTransparencyBlur(7d, 80).setBlurLod(10);
        Material bigCubeMat = new Material().setKd(0.5d).setKs(0.5d).setShininess(700).setKt(0.9d);
        // .setTransparencyBlur(10d, 120).setBlurLod(10);
        Material planeMat = new Material().setKd(0.3d).setKs(0.3d).setShininess(300).setKr(0.3d).setKt(0.6);
        // .setReflectionBlur(20d, 600d).setBlurLod(10);

        Color cubeColor = Color.BLACK;
        Color smallDiamondColor = new Color(43, 25, 16);
        Color diamondCubeColor = new Color(9, 26, 43);

        Geometries smallCube = new Geometries(new Point(-500, 0, -200), new Polygon(cc1, cc2, cc3, cc4).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(cc5, cc6, cc7, cc8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(cc1, cc2, cc7, cc8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(cc2, cc3, cc6, cc7).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(cc3, cc4, cc5, cc6).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(cc1, cc4, cc5, cc8).setMaterial(cubeMat).setEmission(cubeColor));

        double planeRadius = 2000;

        return new Geometries(
                //new Plane(new Point(1, -151, 1), new Point(0, -151, 1), new Point(1, -151, 0)).setMaterial(planeMat),
                new Polygon(new Point(-planeRadius, -151, -planeRadius), new Point(-planeRadius, -151, planeRadius),
                        new Point(planeRadius, -151, planeRadius), new Point(planeRadius, -151, -planeRadius)).setMaterial(planeMat),
                //.setEmission(new Color(59, 50, 39)),
                //big cube
                new Polygon(b1, b2, b3, b4).setMaterial(bigCubeMat).setEmission(cubeColor),  // Top face
                new Polygon(b5, b6, b7, b8).setMaterial(bigCubeMat).setEmission(cubeColor),  // Bottom face
                new Polygon(b1, b2, b7, b8).setMaterial(bigCubeMat).setEmission(cubeColor),  // Front face
                new Polygon(b2, b3, b6, b7).setMaterial(bigCubeMat).setEmission(cubeColor),  // Right face
                new Polygon(b3, b4, b5, b6).setMaterial(bigCubeMat).setEmission(cubeColor),  // Back face
                new Polygon(b1, b4, b5, b8).setMaterial(bigCubeMat).setEmission(cubeColor),   // Left face
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
                new Triangle(k1, k, k2).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k2, k, k3).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k3, k, k4).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k4, k, k5).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k5, k, k6).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k6, k, k7).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k7, k, k8).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k8, k, k1).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k1, k9, k2).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k2, k9, k3).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k3, k9, k4).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k4, k9, k5).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k5, k9, k6).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k6, k9, k7).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k7, k9, k8).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                new Triangle(k8, k9, k1).setMaterial(smallDiamondMat).setEmission(smallDiamondColor),
                //cube1
                new Polygon(c1, c2, c3, c4).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c5, c6, c7, c8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c1, c2, c7, c8).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c2, c3, c6, c7).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c3, c4, c5, c6).setMaterial(cubeMat).setEmission(cubeColor),
                new Polygon(c1, c4, c5, c8).setMaterial(cubeMat).setEmission(cubeColor),
                //cube2
                smallCube,
                //diamond inside cube
                new Triangle(d2, d1, d3).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d3, d1, d4).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d4, d1, d5).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d5, d1, d2).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d2, d6, d3).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d3, d6, d4).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d4, d6, d5).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor),
                new Triangle(d5, d6, d2).setMaterial(cubeDiamondMat).setEmission(diamondCubeColor)
        );
    }
}
