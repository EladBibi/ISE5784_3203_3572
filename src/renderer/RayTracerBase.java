package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Abstract ray tracer
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public abstract class RayTracerBase {

    /**
     * The default value for the maximum recursive iterations for each pixel in the ray tracing process
     */
    protected final static int DEFAULT_MAX_RECURSION_DEPTH = 8;
    /**
     * Maximum recursive iterations number for the current image render
     */
    protected static int maxRecursionLevel = DEFAULT_MAX_RECURSION_DEPTH;

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
     * Set the maximum recursion depth of the tracer
     * @param n the required maximum recursion depth value
     */
    protected void setMaxRecursionDepth(int n){
        maxRecursionLevel = n;
    }

    /**
     * Reset the maximum recursion depth value of the tracer to the default value
     */
    protected void resetMaxRecursionDepth(){
        maxRecursionLevel = DEFAULT_MAX_RECURSION_DEPTH;
    }

    /**
     * Tracing a given ray through the tracer's scene and returning the color for
     * ray
     *
     * @param ray a ray to trace
     * @return the color of the ray
     */
    public abstract Color traceRay(Ray ray);

    /**
     * Tracing a given beam of rays through the scene and returning its average color.
     *
     * @param beam a beam of rays. containing 1 or more rays
     * @return the average color of the beam. achieved through the tracing of each ray in the beam
     */
    public abstract Color traceBeam(List<Ray> beam);
}
