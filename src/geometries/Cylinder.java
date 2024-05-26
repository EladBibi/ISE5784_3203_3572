package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * A cylinder in a three-dimensional space, represented by a Tube and height
 *
 * @author Pini Goldfraind
 */
public class Cylinder extends Tube {

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

        double distance;
        try{
            distance = axis.direction.dotProduct(point.subtract(axis.head));
        }catch (Exception ex){ //will get here if a zero-vector exception was thrown
            //handling the case where the point is in the middle of the cylinder's base
            return axis.direction.scale(-1d);
        }

        //handling the case where the point is on the cylinder's base
        if(Util.isZero(distance))
            return axis.direction.scale(-1d);
        //handling the case where the point is on the cylinder's ceiling
        if(distance == this.height)
            return axis.direction;

        return point.subtract(axis.head.add(axis.direction.scale(distance))).normalize();
    }
}
