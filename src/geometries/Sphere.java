package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    private final Point center;

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

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
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

        double t2 = alignZero(tm + th);
        // t2 > t1 always, therefore if t2 <= 0 => t1 <= 0 : no points
        if (t2 <= 0)
            return null;

        double t1 = alignZero(tm - th);
        return t1 <= 0 ? List.of(new GeoPoint(ray.getPoint(t2), this))
                : List.of(new GeoPoint(ray.getPoint(t1), this),
                new GeoPoint(ray.getPoint(t2), this));
    }
}
