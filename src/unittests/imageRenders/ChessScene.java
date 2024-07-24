package imageRenders;

import geometries.*;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

/**
 * Image renders for the 'mini-project stage 1'
 * demonstrating the image-improving multisampling algorithms implemented in the stage.
 * this class is for a chess scene, demonstrating the diffusive &amp; reflective surfaces
 * image-improving technique as well as the antialiasing edge-smoothing technique.
 */
public class ChessScene {
    /**
     * Camera builder for the renderings
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();
    /**
     * The chess scene
     */
    Scene scene = new Scene("Chess Scene");

    /**
     * The inner folder for the images
     */
    private final String directoryName = "chess scene/";

    /**
     * Material of the chess board
     */
    private Material boardMat = new Material().setKd(0.1).setKs(0.2).setShininess(400).setKr(0.6).setKt(0.1);
    /**
     * Material of the chess pieces
     */
    private Material chessPieceMat = new Material().setKd(0.5).setKs(0.3).setShininess(200).setKr(0.2);
    /**
     * Material of the main surface in the scene
     */
    private Material mainSurfaceMat = new Material().setKd(0.6).setKs(0.3).setShininess(200).setKr(0.1d);
    /**
     * Material of the window glass
     */
    private Material glassMat = new Material().setKs(0.1).setShininess(250).setKt(0.6).setKr(0.3);
    /**
     * Material of the moon
     */
    private Material moonMat = new Material().setKd(0.5).setKt(0d);
    /**
     * Material of the window panels
     */
    private Material winPanelMat = new Material().setKd(0.1).setKs(0.2).setKr(0.05d);

    /**
     * Regular Render of the scene with no antialiasing and no diffusive/reflective surfaces effects
     */
    @Test
    @Disabled
    public void noSpecialEffects() {
        scene.setGeometries(buildScene(new Point(3100, -1100, 5000), 130d));
        scene.setLights(
//                81, 171, 161    70, 122, 148
                new SpotLight(new Color(70, 47, 82), new Point(-650, 200, -750), new Point(550, 70, 550)),
//                new SpotLight(new Color(38, 36, 97), new Point(100, 100, -200), new Point(450, 0, 450))
                //new DirectionalLight(new Color(105, 82, 42), new Vector(-0.7,-0.2, 1))
                new PointLight(new Color(62, 130, 123), new Point(-800, 200, -20)).setKl(0.00000001),
                new SpotLight(new Color(59, 24, 40), new Point(0, 600, 0), new Point(450, 0, 450))
                        .setKq(0.0000001)
        );
        cameraBuilder
                .setRayTracer(new SimpleRayTracer(scene))
                .setFocusPoint(new Point(-900, 1050, -2200), new Point(425, 65, 450))
                .setVpDistance(600)
                .setVpSize(135, 240)
                .setImageWriter(new ImageWriter(directoryName + "test render 8", 800, 450))
                .build()
                .rotate(0)
                .renderImage()
                .writeToImage();
    }

    /**
     * Render of the scene with antialiasing and no diffusive/reflective surfaces effects
     */
    @Test
    @Disabled
    public void withAntiAliasing() {
        scene.setGeometries(buildScene(new Point(3100, -1100, 5000), 130d));
        scene.setLights(
//                81, 171, 161    70, 122, 148
                new SpotLight(new Color(70, 47, 82), new Point(-650, 200, -750), new Point(550, 70, 550)),
//                new SpotLight(new Color(38, 36, 97), new Point(100, 100, -200), new Point(450, 0, 450))
                //new DirectionalLight(new Color(105, 82, 42), new Vector(-0.7,-0.2, 1))
                new PointLight(new Color(62, 130, 123), new Point(-800, 200, -20)).setKl(0.00000001),
                new SpotLight(new Color(59, 24, 40), new Point(0, 600, 0), new Point(450, 0, 450))
                        .setKq(0.0000001)
        );
        cameraBuilder
                .setRayTracer(new SimpleRayTracer(scene))
                .setFocusPoint(new Point(-900, 1050, -2200), new Point(425, 65, 450))
                .setVpDistance(600)
                .setVpSize(135, 240)
                .setImageWriter(new ImageWriter(directoryName + "render 5 - with aa X27 no blur", 1280, 720))
                .build()
                .enableAntiAliasing(3, 27)
                .rotate(0)
                .renderImage()
                .writeToImage();

    }

    /**
     * Render of the scene with no antialiasing and with diffusive/reflective surfaces effects
     */
    @Test
    @Disabled
    public void withDiffusiveReflectiveSurfaces() {
        boardMat = new Material().setKd(0.1).setKs(0.2).setShininess(400).setKr(0.6).setKt(0.1)
                .setReflectionBlur(3, 9);
        mainSurfaceMat = new Material().setKd(0.6).setKs(0.3).setShininess(200).setKr(0.1d);
        glassMat = new Material().setKs(0.1).setShininess(250).setKt(0.6).setKr(0.3)
                .setReflectionBlur(1.5, 9).setTransparencyBlur(0.5, 9);

        scene.setGeometries(buildScene(new Point(3100, -1100, 5000), 130d));
        scene.setLights(
//                81, 171, 161    70, 122, 148
                new SpotLight(new Color(70, 47, 82), new Point(-650, 200, -750), new Point(550, 70, 550)),
//                new SpotLight(new Color(38, 36, 97), new Point(100, 100, -200), new Point(450, 0, 450))
                //new DirectionalLight(new Color(105, 82, 42), new Vector(-0.7,-0.2, 1))
                new PointLight(new Color(62, 130, 123), new Point(-800, 200, -20)).setKl(0.00000001),
                new SpotLight(new Color(59, 24, 40), new Point(0, 600, 0), new Point(450, 0, 450))
                        .setKq(0.0000001)
        );
        cameraBuilder
                .setRayTracer(new SimpleRayTracer(scene))
                .setFocusPoint(new Point(-900, 1050, -2200), new Point(425, 65, 450))
                .setVpDistance(600)
                .setVpSize(135, 240)
                .setImageWriter(new ImageWriter(directoryName + "render 6 - no aa with blur X9", 800, 450))
                .build()
                .rotate(0)
                .renderImage()
                .writeToImage();
    }

    /**
     * Test Render of the scene with different setting presets
     */
    @Test
    @Disabled
    public void alternative() {
        boardMat = new Material().setKd(0.1).setKs(0.2).setShininess(400).setKr(0.6).setKt(0.1);
        //.setReflectionBlur(1.2, 81);
        mainSurfaceMat = new Material().setKd(0.6).setKs(0.3).setShininess(200).setKr(0.1d);

        scene.setGeometries(buildScene(new Point(3100, 430, 5000), 110d));
        scene.setLights(
//                81, 171, 161    70, 122, 148
                new SpotLight(new Color(85, 120, 75), new Point(0, 250, 0), new Point(200, 15, 200)),
                //new SpotLight(new Color(55, 97, 117), new Point(-200, 80, -200), new Point(450, 0, 450)),
                new DirectionalLight(new Color(79, 139, 168), new Vector(0.8, -0.3, 1))
//                new PointLight(new Color(62, 130, 123), new Point(-800, 200, -20)).setKl(0.00000001),
//                new SpotLight(new Color(59, 24, 40), new Point(0, 600, 0), new Point(450, 0, 450))
//                        .setKq(0.0000001)
        );
        cameraBuilder
                .setRayTracer(new SimpleRayTracer(scene))
                .setFocusPoint(new Point(-900, 300, -2200), new Point(400, 60, 420))
                .setVpDistance(600)
                .setVpSize(135, 240)
                .setImageWriter(new ImageWriter(directoryName + "render 7.6 - no effects", 1280, 720))
                .build()
                .rotate(0)
                .renderImage()
                .writeToImage();
    }

    /**
     * Method that builds the chess scene
     *
     * @param moonPosition the position of the moon
     * @param moonRadius   the radius of the moon
     * @return a geometries container which contains all the geometries of the chess scene
     */
    private Geometries buildScene(Point moonPosition, double moonRadius) {
        Geometries geometries = new Geometries();
        Color whiteColor = new Color(41, 41, 41);
        Color blackColor = new Color(41, 34, 0);

        Polygon surface = new Polygon(new Point(-6000, -20, 1100), new Point(6000, -20, 1100),
                new Point(6000, -20, -1100), new Point(-6000, -20, -1100));

        Intersectable blackPawn = buildPawn().setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable blackKing = buildKing().setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable blackQueen = buildQueen().setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable blackBishop = buildBishop().setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable blackRook = buildRook().setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable blackKnight = buildKnight(true).setEmission(blackColor).setMaterial(chessPieceMat);
        Intersectable whitePawn = buildPawn().setEmission(whiteColor).setMaterial(chessPieceMat);
        Intersectable whiteKing = buildKing().setEmission(whiteColor).setMaterial(chessPieceMat);
        Intersectable whiteQueen = buildQueen().setEmission(whiteColor).setMaterial(chessPieceMat);
        Intersectable whiteBishop = buildBishop().setEmission(whiteColor).setMaterial(chessPieceMat);
        Intersectable whiteRook = buildRook().setEmission(whiteColor).setMaterial(chessPieceMat);
        Intersectable whiteKnight = buildKnight(false).setEmission(whiteColor).setMaterial(chessPieceMat);
        geometries.add(
                surface.setMaterial(mainSurfaceMat),
                buildChessBoard().setMaterial(boardMat),
                buildWallWithWindow(mainSurfaceMat, moonPosition, moonRadius).moveCloneTo(new Point(0, 0, 1100)),
                whitePawn.moveCloneTo(new Point(50, 0, 150)),
                whitePawn.moveCloneTo(new Point(150, 0, 150)),
                whitePawn.moveCloneTo(new Point(250, 0, 150)),
                whitePawn.moveCloneTo(new Point(350, 0, 150)),
                whitePawn.moveCloneTo(new Point(450, 0, 150)),
                whitePawn.moveCloneTo(new Point(550, 0, 150)),
                whitePawn.moveCloneTo(new Point(650, 0, 150)),
                whitePawn.moveCloneTo(new Point(750, 0, 150)),
                whiteRook.moveCloneTo(new Point(50, 0, 50)),
                whiteRook.moveCloneTo(new Point(750, 0, 50)),
                whiteKnight.moveCloneTo(new Point(150, 0, 50)),
                whiteKnight.moveCloneTo(new Point(650, 0, 50)),
                whiteBishop.moveCloneTo(new Point(250, 0, 50)),
                whiteBishop.moveCloneTo(new Point(550, 0, 50)),
                whiteKing.moveCloneTo(new Point(350, 0, 50)),
                whiteQueen.moveCloneTo(new Point(450, 0, 50)),

                blackPawn.moveCloneTo(new Point(50, 0, 650)),
                blackPawn.moveCloneTo(new Point(150, 0, 650)),
                blackPawn.moveCloneTo(new Point(250, 0, 650)),
                blackPawn.moveCloneTo(new Point(350, 0, 650)),
                blackPawn.moveCloneTo(new Point(450, 0, 650)),
                blackPawn.moveCloneTo(new Point(550, 0, 650)),
                blackPawn.moveCloneTo(new Point(650, 0, 650)),
                blackPawn.moveCloneTo(new Point(750, 0, 650)),
                blackRook.moveCloneTo(new Point(50, 0, 750)),
                blackRook.moveCloneTo(new Point(750, 0, 750)),
                blackKnight.moveCloneTo(new Point(150, 0, 750)),
                blackKnight.moveCloneTo(new Point(650, 0, 750)),
                blackBishop.moveCloneTo(new Point(250, 0, 750)),
                blackBishop.moveCloneTo(new Point(550, 0, 750)),
                blackKing.moveCloneTo(new Point(350, 0, 750)),
                blackQueen.moveCloneTo(new Point(450, 0, 750))
        );
        return geometries;
    }

    /**
     * Builder method of the wall and window
     *
     * @param material     the material of the wall
     * @param moonPosition position of the moon
     * @param moonRadius   radius of the moon
     * @return geometries container with a wall, window in the wall, a moon in the
     * distance(through the window)
     */
    private Geometries buildWallWithWindow(Material material, Point moonPosition, double moonRadius) {
        double windowLedgeLength = 80;
        Point p1 = new Point(-5000, -500, 0);
        Point p2 = new Point(0, -500, 0);
        Point p3 = new Point(0, 2000, 0);
        Point p4 = new Point(-5000, 2000, 0);

        Point p5 = new Point(5000, -500, 0);
        Point p6 = new Point(900, -500, 0);
        Point p7 = new Point(900, 2000, 0);
        Point p8 = new Point(5000, 2000, 0);

        Point p9 = new Point(900, -500, 0);
        Point p10 = new Point(0, -500, 0);
        Point p11 = new Point(0, 50, 0);
        Point p12 = new Point(900, 50, 0);

        Point p13 = new Point(900, 950, 0);
        Point p14 = new Point(0, 950, 0);
        Point p15 = new Point(0, 2000, 0);
        Point p16 = new Point(900, 2000, 0);

        Point o1 = new Point(0, 950, windowLedgeLength);
        Point o2 = new Point(900, 950, windowLedgeLength);
        Point o3 = new Point(900, 50, windowLedgeLength);
        Point o4 = new Point(0, 50, windowLedgeLength);

        Point w1 = new Point(0, 950, windowLedgeLength / 2);
        Point w2 = new Point(900, 950, windowLedgeLength / 2);
        Point w3 = new Point(900, 50, windowLedgeLength / 2);
        Point w4 = new Point(0, 50, windowLedgeLength / 2);

        Point l1 = new Point(0, 210, (windowLedgeLength / 2) - 1);
        Point l2 = new Point(900, 215, (windowLedgeLength / 2) - 1);
        Point l3 = new Point(900, 200, (windowLedgeLength / 2) - 1);
        Point l4 = new Point(0, 200, (windowLedgeLength / 2) - 1);

        Point l5 = new Point(0, 370, (windowLedgeLength / 2) - 1);
        Point l6 = new Point(900, 370, (windowLedgeLength / 2) - 1);
        Point l7 = new Point(900, 360, (windowLedgeLength / 2) - 1);
        Point l8 = new Point(0, 360, (windowLedgeLength / 2) - 1);

        Point l9 = new Point(440, 950, (windowLedgeLength / 2) - 1);
        Point l10 = new Point(460, 950, (windowLedgeLength / 2) - 1);
        Point l11 = new Point(460, 50, (windowLedgeLength / 2) - 1);
        Point l12 = new Point(440, 50, (windowLedgeLength / 2) - 1);
        return new Geometries(
                new Polygon(p1, p2, p3, p4).setMaterial(material),
                new Polygon(p5, p6, p7, p8).setMaterial(material),
                new Polygon(p9, p10, p11, p12).setMaterial(material),
                new Polygon(p13, p14, p15, p16).setMaterial(material),

                new Polygon(o1, o2, p13, p14).setMaterial(material),
                new Polygon(o2, o3, p12, p13).setMaterial(material),
                new Polygon(o3, o4, p11, p12).setMaterial(material),
                new Polygon(o4, o1, p14, p11).setMaterial(material),
                //window glass and panels
                new Polygon(w1, w2, w3, w4).setMaterial(glassMat),
                new Polygon(l1, l2, l3, l4).setMaterial(winPanelMat),
                new Polygon(l5, l6, l7, l8).setMaterial(winPanelMat),
                new Polygon(l9, l10, l11, l12).setMaterial(winPanelMat),
                //moon
                new Sphere(moonPosition, moonRadius).setMaterial(moonMat).setEmission(new Color(133, 133, 133))
        );
    }

    /**
     * Build method for the rook piece
     *
     * @return a geometries container of a rook chess piece
     */
    private Geometries buildRook() {
        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);

        Point pl1 = new Point(17, 90, 17);
        Point pl2 = new Point(-17, 90, 17);
        Point pl3 = new Point(-17, 90, -17);
        Point pl4 = new Point(17, 90, -17);

        Point top = new Point(0, 120, 0);

        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                //top pyramid
                new Triangle(pl1, pl2, top),
                new Triangle(pl2, pl3, top),
                new Triangle(pl3, pl4, top),
                new Triangle(pl4, pl1, top),
                //upper part
                new Triangle(pointAt18, pl1, pointAt36),
                new Triangle(pointAt36, pl1, pointAt54),
                new Triangle(pointAt54, pl1, pointAt72),
                new Triangle(pointAt72, pl1, pointAt90),
                new Triangle(pointAt90, pl1, pointAt108),
                new Triangle(pointAt90, pl2, pointAt108),
                new Triangle(pointAt90, pl2, pl1),
                new Triangle(pointAt108, pl2, pointAt126),
                new Triangle(pointAt126, pl2, pointAt144),
                new Triangle(pointAt144, pl2, pointAt162),
                new Triangle(pointAt162, pl2, pointAt180),
                new Triangle(pl3, pl2, pointAt180),
                new Triangle(pointAt180, pl3, pointAt198),
                new Triangle(pointAt198, pl3, pointAt216),
                new Triangle(pointAt216, pl3, pointAt234),
                new Triangle(pointAt234, pl3, pointAt252),
                new Triangle(pointAt252, pl3, pointAt270),
                new Triangle(pl4, pl3, pointAt270),
                new Triangle(pointAt270, pl4, pointAt288),
                new Triangle(pointAt288, pl4, pointAt306),
                new Triangle(pointAt306, pl3, pointAt324),
                new Triangle(pointAt324, pl4, pointAt342),
                new Triangle(pointAt342, pl4, pointAt18),
                new Triangle(pl1, pl4, pointAt18),
                new Polygon(pl1, pl2, pl3, pl4)
                //top wall
//                new Polygon(t1,t2,t3,t4),
//                new Polygon(t5,t6,t7,t8),
//                new Polygon(t9,t10,t11,t12),
//                new Polygon(t13,t14,t15,t16)
        );
    }

    /**
     * Build method for the knight piece
     *
     * @param invertForwardDir invert the forward direction of the knight piece
     * @return a geometries container of the chess piece
     */
    private Geometries buildKnight(boolean invertForwardDir) {
        double n = invertForwardDir ? -1 : 1;
        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);

        Point pl1 = new Point(10, 95, 30);
        Point pl2 = new Point(-10, 95, 30);
        Point pl3 = new Point(-10, 95, -30);
        Point pl4 = new Point(10, 95, -30);

        Point t1 = new Point(10.01, 94.03, n * 32);
        Point t2 = new Point(10.02, 93, n * 34);
        Point t3 = new Point(10.01, 92.03, n * 36);
        Point t4 = new Point(10.02, 91, n * 39);
        Point t5 = new Point(10.01, 89.03, n * 37);
        Point t6 = new Point(10.02, 88, n * 39);
        Point t7 = new Point(10.01, 87.03, n * 39);
        Point t8 = new Point(10.02, 90, n * 43);
        Point t9 = new Point(10.03, 97.03, n * 45);
        Point t10 = new Point(10.02, 103, n * 43);
        Point t11 = new Point(10.01, 110.03, n * 39);
        Point t12 = new Point(10.02, 117, n * 32);
        Point t13 = new Point(10.03, 120.03, n * 22);
        Point t14 = new Point(10.03, 123, n * 16);
        Point t15 = new Point(10.03, 128.03, n * 5);
        Point t16 = new Point(10.03, 135, n * -7);
        Point t17 = new Point(10.01, 140.03, n * -10);
        Point t18 = new Point(10.01, 146, n * -20);
        Point t19 = new Point(10.01, 147, n * -25);
        Point t20 = new Point(10.01, 146, n * -30);
        Point t21 = new Point(10.01, 133.03, n * -35);
        Point t22 = new Point(10.01, 121, n * -33);
        Point t23 = new Point(10.01, 105.03, n * -31);
        Point t24 = new Point(10.03, 98, n * -30);

        Point tt1 = new Point(-10.01, 94.03, n * 32);
        Point tt2 = new Point(-10.02, 93, n * 34);
        Point tt3 = new Point(-10.01, 92.03, n * 36);
        Point tt4 = new Point(-10.02, 91, n * 39);
        Point tt5 = new Point(-10.01, 89.03, n * 37);
        Point tt6 = new Point(-10.02, 88, n * 39);
        Point tt7 = new Point(-10.01, 87.03, n * 39);
        Point tt8 = new Point(-10.02, 90, n * 43);
        Point tt9 = new Point(-10.03, 97.03, n * 45);
        Point tt10 = new Point(-10.02, 103, n * 43);
        Point tt11 = new Point(-10.01, 110.03, n * 39);
        Point tt12 = new Point(-10.02, 117, n * 32);
        Point tt13 = new Point(-10.03, 120.03, n * 22);
        Point tt14 = new Point(-10.03, 123, n * 16);
        Point tt15 = new Point(-10.03, 128.03, n * 5);
        Point tt16 = new Point(-10.03, 135, n * -7);
        Point tt17 = new Point(-10.01, 140.03, n * -10);
        Point tt18 = new Point(-10.01, 146, n * -20);
        Point tt19 = new Point(-10.01, 147, n * -25);
        Point tt20 = new Point(-10.01, 146, n * -30);
        Point tt21 = new Point(-10.01, 133.03, n * -35);
        Point tt22 = new Point(-10.01, 121, n * -33);
        Point tt23 = new Point(-10.01, 105.03, n * -31);
        Point tt24 = new Point(-10.03, 98, n * -30);

        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                //upper part
                new Triangle(pointAt18, pl1, pointAt36),
                new Triangle(pointAt36, pl1, pointAt54),
                new Triangle(pointAt54, pl1, pointAt72),
                new Triangle(pointAt72, pl1, pointAt90),
                new Triangle(pointAt90, pl1, pointAt108),
                new Triangle(pointAt90, pl2, pointAt108),
                new Triangle(pointAt90, pl2, pl1),
                new Triangle(pointAt108, pl2, pointAt126),
                new Triangle(pointAt126, pl2, pointAt144),
                new Triangle(pointAt144, pl2, pointAt162),
                new Triangle(pointAt162, pl2, pointAt180),
                new Triangle(pl3, pl2, pointAt180),
                new Triangle(pointAt180, pl3, pointAt198),
                new Triangle(pointAt198, pl3, pointAt216),
                new Triangle(pointAt216, pl3, pointAt234),
                new Triangle(pointAt234, pl3, pointAt252),
                new Triangle(pointAt252, pl3, pointAt270),
                new Triangle(pl4, pl3, pointAt270),
                new Triangle(pointAt270, pl4, pointAt288),
                new Triangle(pointAt288, pl4, pointAt306),
                new Triangle(pointAt306, pl3, pointAt324),
                new Triangle(pointAt324, pl4, pointAt342),
                new Triangle(pointAt342, pl4, pointAt18),
                new Triangle(pl1, pl4, pointAt18),
                new Polygon(pl1, pl2, pl3, pl4),
                //the horse - one side
                new Triangle(pl1, t1, t2),
                new Triangle(pl1, t2, t3),
                new Triangle(pl1, t3, t4),
                new Triangle(pl1, t4, t5),
                new Triangle(pl1, t5, t6),
                new Triangle(pl1, t6, t7),
                new Triangle(pl1, t7, t8),
                new Triangle(pl1, t8, t9),
                new Triangle(pl1, t9, t10),
                new Triangle(pl1, t10, t11),
                new Triangle(pl1, t11, t12),
                new Triangle(pl1, t12, t13),
                new Triangle(pl1, t13, t14),
                new Triangle(pl1, t14, t15),
                new Triangle(pl1, pl4, t15),
                new Triangle(pl4, t15, t16),
                new Triangle(pl4, t16, t17),
                new Triangle(pl4, t17, t18),
                new Triangle(pl4, t18, t19),
                new Triangle(pl4, t19, t20),
                new Triangle(pl4, t20, t21),
                new Triangle(pl4, t21, t22),
                new Triangle(pl4, t22, t23),
                new Triangle(pl4, t23, t24),
                //other side
                new Triangle(pl2, tt1, tt2),
                new Triangle(pl2, tt2, tt3),
                new Triangle(pl2, tt3, tt4),
                new Triangle(pl2, tt4, tt5),
                new Triangle(pl2, tt5, tt6),
                new Triangle(pl2, tt6, tt7),
                new Triangle(pl2, tt7, tt8),
                new Triangle(pl2, tt8, tt9),
                new Triangle(pl2, tt9, tt10),
                new Triangle(pl2, tt10, tt11),
                new Triangle(pl2, tt11, tt12),
                new Triangle(pl2, tt12, tt13),
                new Triangle(pl2, tt13, tt14),
                new Triangle(pl2, tt14, tt15),
                new Triangle(pl2, pl3, tt15),
                new Triangle(pl3, tt15, tt16),
                new Triangle(pl3, tt16, tt17),
                new Triangle(pl3, tt17, tt18),
                new Triangle(pl3, tt18, tt19),
                new Triangle(pl3, tt19, tt20),
                new Triangle(pl3, tt20, tt21),
                new Triangle(pl3, tt21, tt22),
                new Triangle(pl3, tt22, tt23),
                new Triangle(pl3, tt23, tt24),

                new Triangle(t1, t2, tt1),
                new Triangle(tt1, tt2, t2),
                new Triangle(t2, t3, tt2),
                new Triangle(tt2, tt3, t3),
                new Triangle(t3, t4, tt3),
                new Triangle(tt3, tt4, t4),
                new Triangle(t4, t5, tt4),
                new Triangle(tt4, tt5, t5),
                new Triangle(t5, t6, tt5),
                new Triangle(tt5, tt6, t6),
                new Triangle(t6, t7, tt6),
                new Triangle(tt6, tt7, t7),
                new Triangle(t7, t8, tt7),
                new Triangle(tt7, tt8, t8),
                new Triangle(t8, t9, tt8),
                new Triangle(tt8, tt9, t9),
                new Triangle(t9, t10, tt9),
                new Triangle(tt9, tt10, t10),
                new Triangle(t10, t11, tt10),
                new Triangle(tt10, tt11, t11),
                new Triangle(t11, t12, tt11),
                new Triangle(tt11, tt12, t12),
                new Triangle(t12, t13, tt12),
                new Triangle(tt12, tt13, t13),
                new Triangle(t13, t14, tt13),
                new Triangle(tt13, tt14, t14),
                new Triangle(t14, t15, tt14),
                new Triangle(tt14, tt15, t15),
                new Triangle(t15, t16, tt15),
                new Triangle(tt15, tt16, t16),
                new Triangle(t16, t17, tt16),
                new Triangle(tt16, tt17, t17),
                new Triangle(t17, t18, tt17),
                new Triangle(tt17, tt18, t18),
                new Triangle(t18, t19, tt18),
                new Triangle(tt18, tt19, t19),
                new Triangle(t19, t20, tt19),
                new Triangle(tt19, tt20, t20),
                new Triangle(t20, t21, tt20),
                new Triangle(tt20, tt21, t21),
                new Triangle(t21, t22, tt21),
                new Triangle(tt21, tt22, t22),
                new Triangle(t22, t23, tt22),
                new Triangle(tt22, tt23, t23),
                new Triangle(t23, t24, tt23),
                new Triangle(tt23, tt24, t24)
        );
    }

    /**
     * Build method for the bishop piece
     *
     * @return a geometries container of the chess piece
     */
    private Geometries buildBishop() {
        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);

        Point pl1 = new Point(15, 95, 15);
        Point pl2 = new Point(-15, 95, 15);
        Point pl3 = new Point(-15, 95, -15);
        Point pl4 = new Point(15, 95, -15);

        Point t1 = new Point(10, 95, 10);
        Point t2 = new Point(-10, 95, 10);
        Point t3 = new Point(-10, 95, -10);
        Point t4 = new Point(10, 95, -10);
        Point tt1 = new Point(10, 130, 10);
        Point tt2 = new Point(-10, 130, 10);
        Point tt3 = new Point(-10, 130, -10);
        Point tt4 = new Point(10, 130, -10);

        Point top = new Point(0, 175, 0);

        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                //upper part
                new Triangle(pointAt18, pl1, pointAt36),
                new Triangle(pointAt36, pl1, pointAt54),
                new Triangle(pointAt54, pl1, pointAt72),
                new Triangle(pointAt72, pl1, pointAt90),
                new Triangle(pointAt90, pl1, pointAt108),
                new Triangle(pointAt90, pl2, pointAt108),
                new Triangle(pointAt90, pl2, pl1),
                new Triangle(pointAt108, pl2, pointAt126),
                new Triangle(pointAt126, pl2, pointAt144),
                new Triangle(pointAt144, pl2, pointAt162),
                new Triangle(pointAt162, pl2, pointAt180),
                new Triangle(pl3, pl2, pointAt180),
                new Triangle(pointAt180, pl3, pointAt198),
                new Triangle(pointAt198, pl3, pointAt216),
                new Triangle(pointAt216, pl3, pointAt234),
                new Triangle(pointAt234, pl3, pointAt252),
                new Triangle(pointAt252, pl3, pointAt270),
                new Triangle(pl4, pl3, pointAt270),
                new Triangle(pointAt270, pl4, pointAt288),
                new Triangle(pointAt288, pl4, pointAt306),
                new Triangle(pointAt306, pl3, pointAt324),
                new Triangle(pointAt324, pl4, pointAt342),
                new Triangle(pointAt342, pl4, pointAt18),
                new Triangle(pl1, pl4, pointAt18),
                new Polygon(pl1, pl2, pl3, pl4),
                //top rook wall
                new Polygon(t1, tt1, tt2, t2),
                new Polygon(t2, tt2, tt3, t3),
                new Polygon(t3, tt3, tt4, t4),
                new Polygon(t4, tt4, tt1, t1),
                //top sphere
                //new Sphere(new Point(0,100,0), 26),
                new Sphere(new Point(0, 140, 0), 20),
                //top pyramid
                new Triangle(tt1, top, tt2),
                new Triangle(tt2, top, tt3),
                new Triangle(tt3, top, tt4),
                new Triangle(tt4, top, tt1)
        );
    }

    /**
     * Build method for the king piece
     *
     * @return a geometries container of the chess piece
     */
    private Geometries buildKing() {

        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);

        Point pl1 = new Point(15, 120, 15);
        Point pl2 = new Point(-15, 120, 15);
        Point pl3 = new Point(-15, 120, -15);
        Point pl4 = new Point(15, 120, -15);

        Point pll1 = new Point(25, 165, 25);
        Point pll2 = new Point(-25, 165, 25);
        Point pll3 = new Point(-25, 165, -25);
        Point pll4 = new Point(25, 165, -25);

        Point top = new Point(0, 205, 0);
        Point dTop = new Point(0, 200, 0);
        Point t1 = new Point(10, 215, 10);
        Point t2 = new Point(-10, 215, 10);
        Point t3 = new Point(-10, 215, -10);
        Point t4 = new Point(10, 215, -10);
        Point topTop = new Point(0, 230, 0);

        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                //upper part
                new Triangle(pointAt18, pl1, pointAt36),
                new Triangle(pointAt36, pl1, pointAt54),
                new Triangle(pointAt54, pl1, pointAt72),
                new Triangle(pointAt72, pl1, pointAt90),
                new Triangle(pointAt90, pl1, pointAt108),
                new Triangle(pointAt90, pl2, pointAt108),
                new Triangle(pointAt90, pl2, pl1),
                new Triangle(pointAt108, pl2, pointAt126),
                new Triangle(pointAt126, pl2, pointAt144),
                new Triangle(pointAt144, pl2, pointAt162),
                new Triangle(pointAt162, pl2, pointAt180),
                new Triangle(pl3, pl2, pointAt180),
                new Triangle(pointAt180, pl3, pointAt198),
                new Triangle(pointAt198, pl3, pointAt216),
                new Triangle(pointAt216, pl3, pointAt234),
                new Triangle(pointAt234, pl3, pointAt252),
                new Triangle(pointAt252, pl3, pointAt270),
                new Triangle(pl4, pl3, pointAt270),
                new Triangle(pointAt270, pl4, pointAt288),
                new Triangle(pointAt288, pl4, pointAt306),
                new Triangle(pointAt306, pl3, pointAt324),
                new Triangle(pointAt324, pl4, pointAt342),
                new Triangle(pointAt342, pl4, pointAt18),
                new Triangle(pl1, pl4, pointAt18),
                new Polygon(pl1, pl2, pl3, pl4),
                //top diamond
                new Polygon(pl1, pl2, pll2, pll1),
                new Polygon(pl2, pl3, pll3, pll2),
                new Polygon(pl3, pl4, pll4, pll3),
                new Polygon(pl4, pl1, pll1, pll4),
                new Triangle(pll1, top, pll2),
                new Triangle(pll2, top, pll3),
                new Triangle(pll3, top, pll4),
                new Triangle(pll4, top, pll1),
                //top tiny sphere
                new Triangle(t1, t2, dTop),
                new Triangle(t2, t3, dTop),
                new Triangle(t3, t4, dTop),
                new Triangle(t4, t1, dTop),
                new Triangle(t1, t2, topTop),
                new Triangle(t2, t3, topTop),
                new Triangle(t3, t4, topTop),
                new Triangle(t4, t1, topTop)
        );
    }

    /**
     * Build method for the queen piece
     *
     * @return a geometries container of the chess piece
     */
    private Geometries buildQueen() {
        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);

        Point pl1 = new Point(15, 120, 15);
        Point pl2 = new Point(-15, 120, 15);
        Point pl3 = new Point(-15, 120, -15);
        Point pl4 = new Point(15, 120, -15);

        Point pll1 = new Point(25, 165, 25);
        Point pll2 = new Point(-25, 165, 25);
        Point pll3 = new Point(-25, 165, -25);
        Point pll4 = new Point(25, 165, -25);

        Point top = new Point(0, 205, 0);

        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                //upper part
                new Triangle(pointAt18, pl1, pointAt36),
                new Triangle(pointAt36, pl1, pointAt54),
                new Triangle(pointAt54, pl1, pointAt72),
                new Triangle(pointAt72, pl1, pointAt90),
                new Triangle(pointAt90, pl1, pointAt108),
                new Triangle(pointAt90, pl2, pointAt108),
                new Triangle(pointAt90, pl2, pl1),
                new Triangle(pointAt108, pl2, pointAt126),
                new Triangle(pointAt126, pl2, pointAt144),
                new Triangle(pointAt144, pl2, pointAt162),
                new Triangle(pointAt162, pl2, pointAt180),
                new Triangle(pl3, pl2, pointAt180),
                new Triangle(pointAt180, pl3, pointAt198),
                new Triangle(pointAt198, pl3, pointAt216),
                new Triangle(pointAt216, pl3, pointAt234),
                new Triangle(pointAt234, pl3, pointAt252),
                new Triangle(pointAt252, pl3, pointAt270),
                new Triangle(pl4, pl3, pointAt270),
                new Triangle(pointAt270, pl4, pointAt288),
                new Triangle(pointAt288, pl4, pointAt306),
                new Triangle(pointAt306, pl3, pointAt324),
                new Triangle(pointAt324, pl4, pointAt342),
                new Triangle(pointAt342, pl4, pointAt18),
                new Triangle(pl1, pl4, pointAt18),
                new Polygon(pl1, pl2, pl3, pl4),
                //top diamond
                new Polygon(pl1, pl2, pll2, pll1),
                new Polygon(pl2, pl3, pll3, pll2),
                new Polygon(pl3, pl4, pll4, pll3),
                new Polygon(pl4, pl1, pll1, pll4),
                new Triangle(pll1, top, pll2),
                new Triangle(pll2, top, pll3),
                new Triangle(pll3, top, pll4),
                new Triangle(pll4, top, pll1),
                //top tiny sphere
                new Sphere(new Point(0, 210, 0), 10)
        );
    }

    /**
     * Build method for the pawn piece
     *
     * @return a geometries container of the chess piece
     */
    private Geometries buildPawn() {
        Point point1 = new Point(40, 0, 0);
        Point point2 = new Point(38.04, 0, 12.36);
        Point point3 = new Point(32.36, 0, 23.51);
        Point point4 = new Point(23.51, 0, 32.36);
        Point point5 = new Point(12.36, 0, 38.04);
        Point point6 = new Point(0, 0, 40);
        Point point7 = new Point(-12.36, 0, 38.04);
        Point point8 = new Point(-23.51, 0, 32.36);
        Point point9 = new Point(-32.36, 0, 23.51);
        Point point10 = new Point(-38.04, 0, 12.36);
        Point point11 = new Point(-40, 0, 0);
        Point point12 = new Point(-38.04, 0, -12.36);
        Point point13 = new Point(-32.36, 0, -23.51);
        Point point14 = new Point(-23.51, 0, -32.36);
        Point point15 = new Point(-12.36, 0, -38.04);
        Point point16 = new Point(0, 0, -40);
        Point point17 = new Point(12.36, 0, -38.04);
        Point point18 = new Point(23.51, 0, -32.36);
        Point point19 = new Point(32.36, 0, -23.51);
        Point point20 = new Point(38.04, 0, -12.36);
        Point pointAt0 = new Point(40, 20, 0);
        Point pointAt18 = new Point(38.04, 20, 12.36);
        Point pointAt36 = new Point(32.36, 20, 23.51);
        Point pointAt54 = new Point(23.51, 20, 32.36);
        Point pointAt72 = new Point(12.36, 20, 38.04);
        Point pointAt90 = new Point(0, 20, 40);
        Point pointAt108 = new Point(-12.36, 20, 38.04);
        Point pointAt126 = new Point(-23.51, 20, 32.36);
        Point pointAt144 = new Point(-32.36, 20, 23.51);
        Point pointAt162 = new Point(-38.04, 20, 12.36);
        Point pointAt180 = new Point(-40, 20, 0);
        Point pointAt198 = new Point(-38.04, 20, -12.36);
        Point pointAt216 = new Point(-32.36, 20, -23.51);
        Point pointAt234 = new Point(-23.51, 20, -32.36);
        Point pointAt252 = new Point(-12.36, 20, -38.04);
        Point pointAt270 = new Point(0, 20, -40);
        Point pointAt288 = new Point(12.36, 20, -38.04);
        Point pointAt306 = new Point(23.51, 20, -32.36);
        Point pointAt324 = new Point(32.36, 20, -23.51);
        Point pointAt342 = new Point(38.04, 20, -12.36);


        Point top = new Point(0, 120, 0);
        return new Geometries(
                new Polygon(point1, pointAt0, pointAt18, point2),
                new Polygon(point2, pointAt18, pointAt36, point3),
                new Polygon(point3, pointAt36, pointAt54, point4),
                new Polygon(point4, pointAt54, pointAt72, point5),
                new Polygon(point5, pointAt72, pointAt90, point6),
                new Polygon(point6, pointAt90, pointAt108, point7),
                new Polygon(point7, pointAt108, pointAt126, point8),
                new Polygon(point8, pointAt126, pointAt144, point9),
                new Polygon(point9, pointAt144, pointAt162, point10),
                new Polygon(point10, pointAt162, pointAt180, point11),
                new Polygon(point11, pointAt180, pointAt198, point12),
                new Polygon(point12, pointAt198, pointAt216, point13),
                new Polygon(point13, pointAt216, pointAt234, point14),
                new Polygon(point14, pointAt234, pointAt252, point15),
                new Polygon(point15, pointAt252, pointAt270, point16),
                new Polygon(point16, pointAt270, pointAt288, point17),
                new Polygon(point17, pointAt288, pointAt306, point18),
                new Polygon(point18, pointAt306, pointAt324, point19),
                new Polygon(point19, pointAt324, pointAt342, point20),
                new Polygon(point20, pointAt342, pointAt0, point1),
                // Polygon for the bottom
                new Polygon(point1, point2, point3, point4, point5, point6,
                        point7, point8, point9, point10, point11, point12, point13, point14,
                        point15, point16, point17, point18, point19, point20),
                new Triangle(pointAt0, pointAt18, top),
                new Triangle(pointAt18, pointAt36, top),
                new Triangle(pointAt36, pointAt54, top),
                new Triangle(pointAt54, pointAt72, top),
                new Triangle(pointAt72, pointAt90, top),
                new Triangle(pointAt90, pointAt108, top),
                new Triangle(pointAt108, pointAt126, top),
                new Triangle(pointAt126, pointAt144, top),
                new Triangle(pointAt144, pointAt162, top),
                new Triangle(pointAt162, pointAt180, top),
                new Triangle(pointAt180, pointAt198, top),
                new Triangle(pointAt198, pointAt216, top),
                new Triangle(pointAt216, pointAt234, top),
                new Triangle(pointAt234, pointAt252, top),
                new Triangle(pointAt252, pointAt270, top),
                new Triangle(pointAt270, pointAt288, top),
                new Triangle(pointAt288, pointAt306, top),
                new Triangle(pointAt306, pointAt324, top),
                new Triangle(pointAt324, pointAt342, top),
                new Triangle(pointAt342, pointAt0, top),

                new Sphere(new Point(0, 115, 0), 20)
        );
    }

    /**
     * Build method for the chess board
     *
     * @return a geometries container of the chess board
     */
    private Geometries buildChessBoard() {
        Color blackCell = new Color(66, 62, 48);
        Color whiteCell = new Color(107, 106, 104);
        Geometry blackSqr = new Polygon(new Point(-50, 0, -50), new Point(50, 0, -50),
                new Point(50, 0, 50), new Point(-50, 0, 50)).setEmission(blackCell);
        Geometry whiteSqr = new Polygon(new Point(-50, 0, -50), new Point(50, 0, -50),
                new Point(50, 0, 50), new Point(-50, 0, 50)).setEmission(whiteCell);

        Point edge1 = new Point(0, -30, 0);
        Point edge2 = new Point(0, -30, 800);
        Point edge3 = new Point(800, -30, 800);
        Point edge4 = new Point(800, -30, 0);
        Point edge1up = new Point(0, 6, 0);
        Point edge2up = new Point(0, 6, 800);
        Point edge3up = new Point(800, 6, 800);
        Point edge4up = new Point(800, 6, 0);

        Polygon e1 = new Polygon(edge1, edge1up, edge2up, edge2);
        Polygon e2 = new Polygon(edge2, edge2up, edge3up, edge3);
        Polygon e3 = new Polygon(edge3, edge3up, edge4up, edge4);
        Polygon e4 = new Polygon(edge4, edge4up, edge1up, edge1);
        Polygon base = new Polygon(edge1, edge2, edge3, edge4);
        return new Geometries(
                e1, e2, e3, e4, base,
                whiteSqr.moveCloneTo(new Point(50, 0, 50)),
                blackSqr.moveCloneTo(new Point(50, 0, 150)),
                whiteSqr.moveCloneTo(new Point(50, 0, 250)),
                blackSqr.moveCloneTo(new Point(50, 0, 350)),
                whiteSqr.moveCloneTo(new Point(50, 0, 450)),
                blackSqr.moveCloneTo(new Point(50, 0, 550)),
                whiteSqr.moveCloneTo(new Point(50, 0, 650)),
                blackSqr.moveCloneTo(new Point(50, 0, 750)),

                blackSqr.moveCloneTo(new Point(150, 0, 50)),
                whiteSqr.moveCloneTo(new Point(150, 0, 150)),
                blackSqr.moveCloneTo(new Point(150, 0, 250)),
                whiteSqr.moveCloneTo(new Point(150, 0, 350)),
                blackSqr.moveCloneTo(new Point(150, 0, 450)),
                whiteSqr.moveCloneTo(new Point(150, 0, 550)),
                blackSqr.moveCloneTo(new Point(150, 0, 650)),
                whiteSqr.moveCloneTo(new Point(150, 0, 750)),

                whiteSqr.moveCloneTo(new Point(250, 0, 50)),
                blackSqr.moveCloneTo(new Point(250, 0, 150)),
                whiteSqr.moveCloneTo(new Point(250, 0, 250)),
                blackSqr.moveCloneTo(new Point(250, 0, 350)),
                whiteSqr.moveCloneTo(new Point(250, 0, 450)),
                blackSqr.moveCloneTo(new Point(250, 0, 550)),
                whiteSqr.moveCloneTo(new Point(250, 0, 650)),
                blackSqr.moveCloneTo(new Point(250, 0, 750)),

                blackSqr.moveCloneTo(new Point(350, 0, 50)),
                whiteSqr.moveCloneTo(new Point(350, 0, 150)),
                blackSqr.moveCloneTo(new Point(350, 0, 250)),
                whiteSqr.moveCloneTo(new Point(350, 0, 350)),
                blackSqr.moveCloneTo(new Point(350, 0, 450)),
                whiteSqr.moveCloneTo(new Point(350, 0, 550)),
                blackSqr.moveCloneTo(new Point(350, 0, 650)),
                whiteSqr.moveCloneTo(new Point(350, 0, 750)),

                whiteSqr.moveCloneTo(new Point(450, 0, 50)),
                blackSqr.moveCloneTo(new Point(450, 0, 150)),
                whiteSqr.moveCloneTo(new Point(450, 0, 250)),
                blackSqr.moveCloneTo(new Point(450, 0, 350)),
                whiteSqr.moveCloneTo(new Point(450, 0, 450)),
                blackSqr.moveCloneTo(new Point(450, 0, 550)),
                whiteSqr.moveCloneTo(new Point(450, 0, 650)),
                blackSqr.moveCloneTo(new Point(450, 0, 750)),

                blackSqr.moveCloneTo(new Point(550, 0, 50)),
                whiteSqr.moveCloneTo(new Point(550, 0, 150)),
                blackSqr.moveCloneTo(new Point(550, 0, 250)),
                whiteSqr.moveCloneTo(new Point(550, 0, 350)),
                blackSqr.moveCloneTo(new Point(550, 0, 450)),
                whiteSqr.moveCloneTo(new Point(550, 0, 550)),
                blackSqr.moveCloneTo(new Point(550, 0, 650)),
                whiteSqr.moveCloneTo(new Point(550, 0, 750)),

                whiteSqr.moveCloneTo(new Point(650, 0, 50)),
                blackSqr.moveCloneTo(new Point(650, 0, 150)),
                whiteSqr.moveCloneTo(new Point(650, 0, 250)),
                blackSqr.moveCloneTo(new Point(650, 0, 350)),
                whiteSqr.moveCloneTo(new Point(650, 0, 450)),
                blackSqr.moveCloneTo(new Point(650, 0, 550)),
                whiteSqr.moveCloneTo(new Point(650, 0, 650)),
                blackSqr.moveCloneTo(new Point(650, 0, 750)),

                blackSqr.moveCloneTo(new Point(750, 0, 50)),
                whiteSqr.moveCloneTo(new Point(750, 0, 150)),
                blackSqr.moveCloneTo(new Point(750, 0, 250)),
                whiteSqr.moveCloneTo(new Point(750, 0, 350)),
                blackSqr.moveCloneTo(new Point(750, 0, 450)),
                whiteSqr.moveCloneTo(new Point(750, 0, 550)),
                blackSqr.moveCloneTo(new Point(750, 0, 650)),
                whiteSqr.moveCloneTo(new Point(750, 0, 750))
        );
    }
}
