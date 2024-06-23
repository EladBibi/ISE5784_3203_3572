package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a Plane in a three-dimensional space
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Plane extends Geometry {

    /**
     * A point on the plane
     */
    private final Point q;
    /**
     * A normal vector to the plane
     */
    private final Vector normal;

    /**
     * Constructor that initializes the plane from the three given unique points
     *
     * @param p1 point NO 1 on the plane
     * @param p2 point NO 2 on the plane
     * @param p3 point NO 3 on the plane
     * @throws IllegalArgumentException if the three given points are not unique OR
     *                                  if the given points are linearly dependent
     */
    public Plane(Point p1, Point p2, Point p3) {

        this.q = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();

        if (this.normal.equals(Vector.ZERO))
            throw new IllegalArgumentException("The points are linearly dependent. cannot form a plane");
    }


    /**
     * Constructor that initializes the plane from the given point and vector
     *
     * @param q      a point on the plane
     * @param normal a vector that is orthogonal to the plane. will be normalized inside the constructor if it's not already
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = Util.compare(normal.length(), 1d) ? normal : normal.normalize();
    }

    /**
     * Gives the normal-vector for this plane
     *
     * @return the plane's normal-vector
     */
    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return this.normal;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector v = ray.getDirection();
        Point h = ray.getHead();
        double nv = normal.dotProduct(v);

        if (h.equals(q) || isZero((nv)))
            return null;

        Vector vhq = q.subtract(h);
        double t = alignZero(normal.dotProduct(vhq) / nv);
        return t <= 0 ? null : List.of(new GeoPoint(ray.getPoint(t), this));
    }
}
