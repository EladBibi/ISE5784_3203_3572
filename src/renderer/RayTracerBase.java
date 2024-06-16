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
    protected Scene scene;

    /**
     * Constructor that initializes the tracer with the given scene
     *
     * @param scene a scene for the tracer
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Tracing the given ray through the tracer's scene and returning the color for
     * the pixel in the view plane
     *
     * @param ray a ray cast from the camera through the view plane into the scene
     * @return the color for the pixel of the view plane which was in the ray's path
     */
    public abstract Color traceRay(Ray ray);
}
