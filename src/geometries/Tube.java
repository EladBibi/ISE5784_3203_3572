package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A Tube in a three-dimensional space, represented with a center axis-ray and a radius.
 *
 * @author Elad Bibi
 */
public class Tube extends RadialGeometry {

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
        return null;
    }
}
