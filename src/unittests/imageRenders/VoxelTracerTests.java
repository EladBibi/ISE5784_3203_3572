package imageRenders;

import geometries.Geometries;
import geometries.Sphere;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import renderer.VoxelRayTracer;
import scene.Scene;

import static java.awt.Color.PINK;

public class VoxelTracerTests {
    /**
     * Camera builder for the renderings
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder();
    /**
     * The current name
     */
    Scene scene = new Scene("Chess Scene");
    /**
     * The inner folder for the images
     */
    private final String directoryName = "test scene/";

    /**
     * Regular Render of the scene with no antialiasing and no diffusive/reflective surfaces effects
     */
    @Test
    @Disabled
    public void noSpecialEffects() {
        Geometries geometries = new Geometries(new Sphere(new Point(5,0,5), 4).setEmission(new Color(PINK)),
                new Sphere(new Point(-5,0,-5), 4).setEmission(new Color(PINK)), new Sphere(Point.ZERO, 200));
        scene.setGeometries(geometries);
        scene.setLights(
        );
        Ray ray = new Ray(new Point(-2,0,-8), new Vector(0.0,3,2));
        VoxelRayTracer tracer = new VoxelRayTracer(scene);
        tracer.findClosestIntersection(ray);
        cameraBuilder
                .setRayTracer(new VoxelRayTracer(scene))
                .setFocusPoint(new Point(0,0, -50), Point.ZERO)
                .setVpDistance(10)
                .setVpSize(16, 16)
                .setImageWriter(new ImageWriter(directoryName + "test 1", 100, 100))
                .build()
                .rotate(0)
                .renderImage(3)
                .writeToImage();
    }
}
