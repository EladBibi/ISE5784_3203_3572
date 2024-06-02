package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;


/**
 * Interface which contain the intersection-finding method for our geometries
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public interface Intersectable {

    /**
     * Method that gives all the intersection points of a given ray with objects(geometries) in our 3D scene
     *
     * @param ray the ray that pierces through the scene, intersecting with 0 or more geometries
     * @return a list of points which are the ray's intersections with objects in the scene
     */
    public List<Point> findIntersections(Ray ray);
}
