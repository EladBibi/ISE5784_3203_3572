package primitives;

import geometries.Geometry;
import geometries.Intersectable.GeoPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a cubic voxel
 */
public class Voxel {
    /**
     * A collection of geometries whose bounding boxes intersect this voxel
     */
    public List<Geometry> geometries;

    /**
     * Constructor that initializes the geometries collection of the voxel
     */
    public Voxel() {
        geometries = new LinkedList<>();
    }

    /**
     * Trace the given ray through the geometries of this voxel and give all the found intersections
     *
     * @param ray         a ray for the tracin
     * @param maxDistance the maximum distance from the ray's head point we should look for intersections
     * @return a collection of the found intersection point between the ray and this voxel's geometries
     */
    public List<GeoPoint> findGeoIntersections(Ray ray, Double maxDistance) {
        List<GeoPoint> intersections = new LinkedList<>();
        for (Geometry geometry : geometries) {
            List<GeoPoint> geoPoints = geometry.findGeoIntersections(ray, maxDistance);
            if (geoPoints != null)
                intersections.addAll(geoPoints);
        }
        return intersections.isEmpty() ? null : intersections;
    }
}
