package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * A Tube in a three-dimensional space, represented with a center axis-ray and a radius.
 *
 * @author Elad Bibi
 */
public class Tube extends RadialGeometry {

    /**
     * The center ray at the heart of the tube
     */
    protected final Ray axis;

    /**
     * Constructor that builds a tube from the given ray and radius
     *
     * @param axis   ray that represents the tube's center
     * @param radius the tube's radius
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        Vector v = axis.getDirection();
        Point h = axis.getHead();
        double distance = v.dotProduct(point.subtract(h));

        //handling the case where the point is on the same level as the tube's head-point
        Point pnt = isZero(distance) ? h : h.add(v.scale(distance));

        return point.subtract(pnt).normalize();
    }
}
