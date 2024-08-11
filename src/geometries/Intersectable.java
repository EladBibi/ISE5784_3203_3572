package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.Objects;


/**
 * Interface which contain the intersection-finding method for our geometries
 *
 * @author Elad Bibi &amp;Pini Goldfraind
 */
public abstract class Intersectable extends Movable implements Cloneable {

    /**
     * Internal GeoPoint class. contains a geometry object and a point ON the geometry object
     */
    public static class GeoPoint {
        /**
         * A geometry object (sphere, plane, polygon etc…)
         */
        public Geometry geometry;
        /**
         * A point that is ON the twin geometry object
         */
        public Point point;

        /**
         * Constructor that initializes a GeoPoint object with the given geometry and point.
         * note: the constructor will not check that the point is actually on the geometry
         *
         * @param point    a point in the 3D plane that is on the twin, given geometry object
         * @param geometry a geometry object
         */
        public GeoPoint(Point point, Geometry geometry) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // Check if both references are identical
            if (obj == null || getClass() != obj.getClass()) return false; // Ensure the object is a GeoPoint
            GeoPoint other = (GeoPoint) obj;
            return point.equals(other.point) && geometry.equals(other.geometry); // Compare relevant fields
        }

        @Override
        public int hashCode() {
            return Objects.hash(point, geometry);
        }

        @Override
        public String toString() {
            return this.point + " geometry: " + this.geometry.getClass();
        }
    }

    /**
     * Method that gives all the intersection points of a given ray with objects(geometries) in our 3D scene
     * without range limitation(up to infinity)
     *
     * @param ray the ray that pierces through the scene, intersecting with 0 or more geometries
     * @return a list of points which are the ray's intersections with objects in the scene
     */
    public final List<Point> findIntersections(Ray ray) {
        return findIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Gives all the intersection points of the ray with the scene that are within the given distance-range
     * from the ray's starting point
     *
     * @param ray         the ray that pierces through the scene, intersecting with 0 or more geometries
     * @param maxDistance distance range. how far from the ray's starting point we will look for intersections
     * @return a list of points which are the found intersection points inside the given distance range
     */
    public final List<Point> findIntersections(Ray ray, double maxDistance) {
        var geoList = findGeoIntersections(ray, maxDistance);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Gives all the intersection geo-points of a given ray with geometries in the scene
     * without distance-range limitation(will look for intersections up to infinity)
     *
     * @param ray a ray we wish to trace its intersections
     * @return a list of geo points which are the intersections of the given ray
     * with the geometries in the scene. each geo-point will contain: (the intersection point, the intersected geometry)
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Gives all the intersection points of the ray with the scene that are within the given distance-range
     * from the ray's starting point
     *
     * @param ray         a ray we wish to trace its intersections
     * @param maxDistance distance range. how far from the ray's starting point we will look for intersections
     * @return a list of geo points which are the found intersection points inside the given distance range.
     * each geo-point will contain: (the intersection point, the intersected geometry)
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Helper method for getting all the intersection points of a given ray with geometries in the scene
     *
     * @param ray         a ray we wish to trace its intersections
     * @param maxDistance distance range. how far from the ray's starting point we will look for intersections
     * @return a list of geo points which are the intersections of the given ray
     * with the geometries in the scene. each geo-point will contain:
     * (the intersection point, the intersected geometry)
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Calculates the minimum (lowest) x,y,z coordinates of this geometry object
     *
     * @return the minimum (lowest) x,y,z coordinates of this geometry object
     */
    public abstract Point getMinCoordinates();

    /**
     * Calculates the maximum (highest) x,y,z coordinates of this geometry object
     *
     * @return the maximum (highest) x,y,z coordinates of this geometry object
     */
    public abstract Point getMaxCoordinates();

    @Override
    public Intersectable moveCloneBy(double x, double y, double z) {
        return this.moveCloneTo(pivot.add(new Vector(x, y, z)));
    }

    /**
     * Gives a clone of the current object
     *
     * @return a clone of the current object
     */
    public final Intersectable getClone() {
        try {
            return (Intersectable) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed");
        }
    }
}
