package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a Plane in a three-dimensional space
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Plane extends Geometry {

    private final double DISTANCE = 99999;

    /**
     * A point on the plane
     */
    private Point q;
    /**
     * A normal vector to the plane
     */
    private Vector normal;

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
        try {
            this.normal = v1.crossProduct(v2).normalize();
        } catch (IllegalArgumentException zeroVectorEx) {
            throw new IllegalArgumentException("The points are linearly dependent. cannot form a plane");
        }
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
     * Constructor that initializes the plane from the given point and vector.
     * and with a pivot position, used for moving and rotating the geometry
     *
     * @param q      a point on the plane
     * @param normal a vector that is orthogonal to the plane. will be normalized inside the constructor if it's not already
     * @param pivot  the pivot position of the geometry. moving and rotating the object will be done
     *               around the pivot position
     */
    public Plane(Point q, Vector normal, Point pivot) {
        this(q, normal);
        this.pivot = pivot;
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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Vector v = ray.getDirection();
        Point h = ray.getHead();
        double nv = normal.dotProduct(v);
        if (h.equals(q) || isZero((nv)))
            return null;

        Vector vhq = q.subtract(h);
        double t = alignZero(normal.dotProduct(vhq) / nv);
        return t > 0 && alignZero(t - maxDistance) < 0 ? List.of(new GeoPoint(ray.getPoint(t), this)) : null;
    }

    @Override
    public Point getMinCoordinates() {
        return q.subtract(DISTANCE);
    }

    @Override
    public Point getMaxCoordinates() {
        return q.subtract(-DISTANCE);
    }

    @Override
    public Intersectable moveCloneTo(Point position) {
        Plane cloned = (Plane) this.getClone();
        if (position.equals(pivot))
            return cloned;
        Vector movement = position.subtract(pivot);
        cloned.pivot = position;
        cloned.q = q.add(movement);
        return cloned;
    }

    @Override
    public Intersectable cloneAndRotate(Vector rotationAxis, double degrees) {
        return this.getClone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references are identical
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure the object is a Plane
        Plane other = (Plane) obj;
        return q.equals(other.q) && normal.equals(other.normal); // Compare the point and the normal vector
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, normal); // Generate hash code based on the point and the normal vector
    }
}
