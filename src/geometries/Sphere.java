package geometries;

import primitives.BoundingBox;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;

/**
 * A sphere in a three-dimensional space, represented with a center point and radius
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere
     */
    private Point center;

    /**
     * Constructor that initializes the sphere from the given center-point and radius
     *
     * @param center point that represents the sphere's center
     * @param radius sphere's radius
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Constructor that initializes the sphere from the given center-point and radius.
     * and with a pivot position, used for moving and rotating the geometry
     *
     * @param center point that represents the sphere's center
     * @param radius sphere's radius
     * @param pivot  the pivot position of the geometry. moving and rotating the object will be done
     *               around the pivot position
     */
    public Sphere(Point center, double radius, Point pivot) {
        this(center, radius);
        this.pivot = pivot;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point h = ray.getHead();
        Vector v = ray.getDirection();

        Vector u;
        try {
            u = center.subtract(h);
        } catch (IllegalArgumentException ex) {//will get here if ray starts at the center
            return List.of(new GeoPoint(ray.getPoint(radius), this));
        }

        double tm = v.dotProduct(u);
        double dSquared = u.lengthSquared() - (tm * tm);
        double thSquared = alignZero(radiusSquared - dSquared);

        //on the edge of the sphere OR outside the sphere
        if (thSquared <= 0)
            return null;
        double th = Math.sqrt(thSquared);

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        //t2 > t1 therefore, it is enough to check t2 <= 0
        if (alignZero(t1 - maxDistance) >= 0 || t2 <= 0) // no intersections OR all points are outside the given range
            return null;

        if (t1 > 0) // there are two intersections: either one or both are inside the range
            return alignZero(t2 - maxDistance) >= 0
                    ? List.of(new GeoPoint(ray.getPoint(t1), this))
                    : List.of(new GeoPoint(ray.getPoint(t1), this), new GeoPoint(ray.getPoint(t2), this));
        // there is only one intersection: inside or outside the range
        return alignZero(t2 - maxDistance) >= 0 ? null : List.of(new GeoPoint(ray.getPoint(t2), this));
    }

    @Override
    public Point getMinCoordinates() {
        return center.subtract(radius);
    }

    @Override
    public Point getMaxCoordinates() {
        return center.subtract(-radius);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(this, getMinCoordinates(), getMaxCoordinates());
    }

    @Override
    public Intersectable moveCloneTo(Point position) {
        Sphere cloned = (Sphere) this.getClone();
        if (position.equals(pivot))
            return cloned;
        Vector movement = position.subtract(pivot);
        cloned.pivot = position;
        cloned.center = center.add(movement);
        return cloned;
    }

    @Override
    public Intersectable cloneAndRotate(Vector rotationAxis, double degrees) {
        return this.getClone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        // Compare relevant fields in Geometry
        // Example:
        Sphere other = (Sphere) obj;
        return this.radius == other.radius && this.center.equals(other.center);
    }

    @Override
    public int hashCode() {
        // Generate hash code based on relevant fields
        return Objects.hash(radius, center);
    }
}
