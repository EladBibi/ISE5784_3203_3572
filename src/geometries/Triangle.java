package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.compareSign;

/**
 * Represents a triangle in a 3D space
 *
 * @author Elad Bibi
 */
public class Triangle extends Polygon {

    /**
     * Constructor that takes three points to represent the triangle's 3 vertices in the plane
     *
     * @param p1 vertex NO 1
     * @param p2 vertex NO 2
     * @param p3 vertex NO 3
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point h = ray.getHead();
        Vector v = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(h);
        Vector v2 = vertices.get(1).subtract(h);
        Vector v3 = vertices.get(2).subtract(h);
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        if (compareSign(v.dotProduct(n1), v.dotProduct(n2)) && compareSign(v.dotProduct(n2), v.dotProduct(n3))) {
            return plane.findIntersections(ray);
        }
        return null;
    }
}

