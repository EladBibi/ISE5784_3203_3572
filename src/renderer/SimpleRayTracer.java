package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Basic ray tracer
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor that initializes the tracer with the given scene
     *
     * @param scene a scene for the tracer
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        return intersections == null ? scene.background : calcColor(ray.findClosestPoint(intersections));
    }

    /**
     * Method that gives the color of a given point in the scene
     *
     * @param point a point in the 3D scene
     * @return the point's color
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}
