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
     * Static constant for the maximum recursive iterations for each pixel in the ray tracing process
     */
    private static final int MAX_CALC_COLOR_LEVEL = 5;
    /**
     * Static constant for the lowest distinguishable color intensity
     */
    private static final Double3 MIN_CALC_COLOR_K = new Double3(0.0001);
    /**
     * Static constant for the starting color intensity factor
     */
    private static final Double3 STARTING_K = Double3.ONE;

    /**
     * The grid size for the beam-casting algorithms.
     */
    private int gridSize = 2;

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
        GeoPoint intersection = findClosestIntersection(ray);
        return intersection == null ? scene.background :
                calcColor(intersection, ray);
    }

    /**
     * Method that gives the color of a given point in the scene
     *
     * @param geoPoint a geo point in the 3D scene
     * @param ray      the ray that intersected the geo-point
     * @return the point's color
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, STARTING_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * Method that gives the color of a given point in the scene
     *
     * @param geoPoint       a geo point in the scene
     * @param ray            the ray that intersected the geo point
     * @param iterationsLeft the amount of iterations left for the current thread
     * @param k              the current color intensity factor, will exit the recursion loop if it gets insignificantly low
     * @return the color for the pixel
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int iterationsLeft, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
        return iterationsLeft <= 1 ? color
                : color.add(calcGlobalEffects(geoPoint, ray, iterationsLeft, k));
    }

    /**
     * Calculates the global lighting effects of the given geo-point. effects such as reflection and transparency
     *
     * @param gp             the intersection geo-point
     * @param ray            the ray that intersected the geo-point
     * @param iterationsLeft the amount of iterations left for the current thread
     * @param k              the current color intensity factor, will exit the recursion loop if it gets insignificantly low
     * @return the calculated global color intensity for the given geo-point
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int iterationsLeft, Double3 k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);

        Material gpMat = gp.geometry.getMaterial();
        Double3 kkx = gpMat.kR.product(k);
        if (!kkx.lowerThan(MIN_CALC_COLOR_K)) {
            //generating a beam of rays in the general reflection direction and adding its average color
            Ray reflectedRay = constructReflectedRay(gp, ray);
            List<Ray> beam = reflectedRay.generateBeam(n, gridSize, gpMat.reflectionBlur, gpMat.reflectionBlurRange, gpMat.blurLod);
            color = color.add(calcAverageBeamColor(beam, iterationsLeft - 1, kkx).scale(gpMat.kR));
        }

        kkx = gpMat.kT.product(k);
        if (!kkx.lowerThan(MIN_CALC_COLOR_K)) {
            //generating a beam of rays in the general refraction direction and adding its average color
            Ray refractedRay = constructRefractedRay(gp, ray);
            List<Ray> beam = refractedRay.generateBeam(n, gridSize, gpMat.transparencyBlur, gpMat.transparencyBlurRange, gpMat.blurLod);
            color = color.add(calcAverageBeamColor(beam, iterationsLeft - 1, kkx).scale(gpMat.kT));
        }
        return color;
    }

    /**
     * Gives the average color of the given beam of rays. will only sum up the color of the
     * intersections and will ignore rays that miss (reach infinity)
     *
     * @param beam           the beam of rays
     * @param iterationsLeft the amount of iterations left for the current thread
     * @param k              the current color intensity factor, will exit the recursion loop if it gets insignificantly low
     * @return the average color of the beam of rays calculated from all the found intersections with the beam
     */
    private Color calcAverageBeamColor(List<Ray> beam, int iterationsLeft, Double3 k) {
        Color color = Color.BLACK;
        for (Ray ray : beam) {
            GeoPoint intersection = findClosestIntersection(ray);
            if (intersection != null)
                color = color.add(calcColor(intersection, ray, iterationsLeft - 1, k));
        }
        //TODO reducing by the expected rays count instead of the actual count
        return color.reduce(beam.size());
    }

    /**
     * Gives a refraction ray from the given intersection geo-point
     *
     * @param geoPoint an intersected geo point in the scene
     * @param ray      the intersecting ray
     * @return the refraction ray of the given ray through the geometry of the intersection point
     */
    private Ray constructRefractedRay(GeoPoint geoPoint, Ray ray) {
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        return new Ray(geoPoint.point, ray.getDirection(), n);
    }

    /**
     * Gives a reflection ray from the given intersection geo-point
     *
     * @param geoPoint an intersected geo point in the scene
     * @param ray      the intersecting ray
     * @return the reflection ray of the given ray from the geometry of the intersection point
     */
    private Ray constructReflectedRay(GeoPoint geoPoint, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double vn = v.dotProduct(n);

        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(geoPoint.point, r, n);
    }

    /**
     * Gives the closest intersection point from the ray-head to objects in the scene
     *
     * @param ray a ray to be traced
     * @return the closest intersection point from the given ray-head to objects in the scene,
     * null if the ray intersects nothing
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(intersections);
    }

    /**
     * Method that gives the calculated color of the given geo-point, with
     * calculated diffusive &amp; specular light from all the light-sources based on the material
     *
     * @param k   the current light intensity of the thread
     * @param gp  the geo-point containing the intersection point and intersected geometry object
     * @param ray the ray that intersected with the geo-point
     * @return the total, calculated color intensity with diffusion &amp; specular &amp; emission light
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
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
                Double3 ktr = transparency(gp, light, l, n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = light.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffuse(material, nl)
                            .add(calcSpecular(material, n, l, nl, v))));
                }
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

        double minusVR = -alignZero(v.dotProduct(r));
        return minusVR <= 0 ? Double3.ZERO : material.kS.scale(Math.pow(minusVR, material.nShininess));
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

    /**
     * Method that returns true if the given point is shaded from the given light
     *
     * @param gp    a geo-point which is the intersection point we wish to check shading for
     * @param light the light-source
     * @param l     the direction vector from the light-source to the intersection point
     * @param n     the normal-vector of the intersection point
     * @param nl    the angle-cos between the normal and the l vector
     * @return true if the point is unshaded, false otherwise
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("unused")
    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
        Vector pointToLightVector = l.scale(-1);
        Ray shadingRay = new Ray(gp.point, pointToLightVector, n);

        var intersections = scene.geometries.findGeoIntersections(shadingRay, light.getDistance(gp.point));
        if (intersections == null)
            return true;

        for (GeoPoint intersection : intersections) {
            if (intersection.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false;
        }
        return true;
    }

    /**
     * Gives the total transparency coefficient for the given geo-point.
     * in other words, how much light from the light source reaches the geo-point
     *
     * @param gp    a geo intersection point in the scene
     * @param light a light source in the scene
     * @param l     the direction vector from the light-source origin to the intersection point
     * @param n     the normal at the given intersection point
     * @return the factor of light from the light source that actually reaches the intersection point
     */
    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n) {
        Vector pointToLightVector = l.scale(-1);
        Ray shadingRay = new Ray(gp.point, pointToLightVector, n);

        Double3 ktr = Double3.ONE;
        var intersections = scene.geometries.findGeoIntersections(shadingRay, light.getDistance(gp.point));
        if (intersections == null)
            return ktr;

        for (GeoPoint intersection : intersections) {
            //summing the transparency factor of all the objects in the way
            ktr = ktr.product(intersection.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }
        return ktr;
    }
}
