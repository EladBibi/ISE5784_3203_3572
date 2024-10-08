package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.compare;
import static primitives.Util.isZero;

/**
 * A cylinder in a three-dimensional space - finite tube. represented by a Tube and height
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Cylinder extends Tube {

    /**
     * The cylinder's height
     */
    private final double height;

    /**
     * Constructor that initiates a cylinder from the given Axis-ray, radius and height
     *
     * @param axis   Ray for the cylinder's center
     * @param radius Cylinder radius
     * @param height Height of the cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {

        Vector v = axis.getDirection();
        Point h = axis.getHead();
        double distance;
        try {
            distance = v.dotProduct(point.subtract(h));
        } catch (Exception ex) { //will get here if a zero-vector exception was thrown
            //handling the case where the point is in the middle of the cylinder's base
            return v.inverted();
        }

        //handling the case where the point is on the cylinder's base
        if (isZero(distance))
            return v.inverted();
            //handling the case where the point is on the cylinder's ceiling
        else if (compare(distance, this.height))
            return v;

        return point.subtract(axis.getPoint(distance)).normalize();
    }

}
