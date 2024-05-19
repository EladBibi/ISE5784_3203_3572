package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A cylinder in a three-dimensional space, represented by a Tube and height
 *
 * @author Pini Goldfraind
 */
public class Cylinder extends Tube {

    private final double height;

    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
