package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

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
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? scene.background : calcColor(ray.findClosestGeoPoint(intersections), ray);
    }

    /**
     * Method that gives the color of a given point in the scene
     *
     * @param geoPoint a geo point in the 3D scene
     * @param ray      the ray that intersected the geo-point
     * @return the point's color
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(calcLocalEffects(geoPoint, ray));
    }

    /**
     * Method that gives the calculated color of the given geo-point, with
     * calculated diffusive &amp; specular light from all the light-sources based on the material
     *
     * @param gp  the geo-point containing the intersection point and intersected geometry object
     * @param ray the ray that intersected with the geo-point
     * @return the total, calculated color intensity with diffusion &amp; specular &amp; emission light
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        Color color = gp.geometry.getEmission();
        //point's normal is orthogonal to the ray - the point is not visible
        if (nv == 0)
            return color;
        Material material = gp.geometry.getMaterial();
        //iterating through all the light-sources in the scene
        for (LightSource light : scene.lights) {
            Vector l = light.getL(gp.point);
            double nl = alignZero(l.dotProduct(n));
            //if the point is visible to the camera
            if (nl * nv > 0) {
                Color iL = light.getIntensity(gp.point);
                color = color.add(iL.scale(calcDiffuse(material, nl)
                        .add(calcSpecular(material, n, l, nl, v))));
            }
        }
        return color;
    }

    /**
     * Helper method for calculating the specular light factor based on the given parameters
     *
     * @param material the material of the object we are working with
     * @param n        the normal of the intersection point
     * @param l        direction vector from the light-source to the intersection point
     * @param nl       the angle-Cos of the light-to-point vector and the point's normal vector
     * @param v        the direction of the ray that intersected with the point
     * @return the should-be factor for specular light calculation. calculated from the given parameters
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        double d = -2 * nl;
        Vector nInverted = n.scale(d);
        Vector r = l.add(nInverted);

        Vector vInverted = v.scale(-1);
        double factor = Math.abs(Math.pow(vInverted.dotProduct(r), material.nShininess));
        return material.kS.scale(factor);
    }

    /**
     * Helper method for calculating the Diffusional light factor based on the given parameters
     *
     * @param material the material we are working with
     * @param nl       the angle-Cos of the light-to-point vector and the point's normal vector
     * @return the should-be factor for Diffusional light calculation. calculated from the given parameters
     */
    private Double3 calcDiffuse(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }
}
