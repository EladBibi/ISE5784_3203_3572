package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;


/**
 * Interface which contain the intersection-finding method for our geometries
 *
 * @author Elad Bibi &amp;Pini Goldfraind
 */
public abstract class Intersectable {

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
            if (this == obj) return true;
            return (obj instanceof GeoPoint other)
                    && this.point.equals(other.point)
                    && this.geometry.equals(other.geometry);
        }

        @Override
        public String toString() {
            return "" + this.point + " geometry: " + this.geometry.getClass();
        }
    }

    /**
     * Method that gives all the intersection points of a given ray with objects(geometries) in our 3D scene
     *
     * @param ray the ray that pierces through the scene, intersecting with 0 or more geometries
     * @return a list of points which are the ray's intersections with objects in the scene
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Gives all the intersection points of a given ray with geometries in the scene as
     * geo-points(the intersection point, the intersected geometry)
     *
     * @param ray a ray we wish to trace its intersections
     * @return a list of geo points which are the intersections of the given ray
     * with the geometries in the scene. each geo-point will contain: (the intersection point, the intersected geometry)
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Helper method for getting all the intersection points of a given ray with geometries in the scene
     *
     * @param ray a ray we wish to trace its intersections
     * @return a list of geo points which are the intersections of the given ray
     * with the geometries in the scene. each geo-point will contain:
     * (the intersection point, the intersected geometry)
     */
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return null;
    }
}
