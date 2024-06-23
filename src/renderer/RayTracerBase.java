package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract ray tracer
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public abstract class RayTracerBase {

    /**
     * The tracer's scene
     */
    protected final Scene scene;

    /**
     * Constructor that initializes the tracer with the given scene
     *
     * @param scene a scene for the tracer
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Tracing a given ray through the tracer's scene and returning the color for
     * ray
     *
     * @param ray a ray to trace
     * @return the color of the ray
     */
    public abstract Color traceRay(Ray ray);
}
