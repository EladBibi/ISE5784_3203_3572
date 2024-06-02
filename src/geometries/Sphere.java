package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.compare;

/**
 * A sphere in a three-dimensional space, represented with a center point and radius
 *
 * @author Elad Bibi
 */
public class Sphere extends RadialGeometry {

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
    public List<Point> findIntersections(Ray ray) {
        Point h = ray.getHead();
        Vector v = ray.getDirection();

        Vector u;
        try {
            u = center.subtract(h);
        } catch (IllegalArgumentException ex) {//will get here if ray starts at the center
            return List.of(ray.getPoint(radius));
        }
        double tm = v.dotProduct(u);
        double d = alignZero(Math.sqrt(u.lengthSquared() - (tm * tm)));

        //on the edge of the sphere OR outside the sphere
        if (compare(d, radius) || d > radius)
            return null;

        double th = Math.sqrt((radius * radius) - (d * d));
        double t1 = alignZero(tm - th), t2 = alignZero(tm + th);
        return (alignZero(t1) > 0 && alignZero(t2) > 0) ?
                List.of(ray.getPoint(t1), ray.getPoint(t2)) :
                (alignZero(t1) > 0) ? List.of(ray.getPoint(t1)) :
                        (alignZero(t2) > 0) ? List.of(ray.getPoint(t2)) :
                                null;
    }
}
