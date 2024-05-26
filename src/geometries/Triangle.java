package geometries;

import primitives.Point;
import primitives.Vector;

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
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
}

