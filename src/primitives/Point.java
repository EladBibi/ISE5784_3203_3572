package primitives;

import java.util.Objects;

/**
 * A point in a three-dimensional space, represented by three coordinates x,y,z
 *
 * @author Elad Bibi
 */
public class Point {

    /**
     * Constant for the axis-center point
     */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Double3 object that represents the x,y,z coordinates in the 3d space
     */
    protected final Double3 xyz;

    /**
     * Constructor that builds a point from 3 given double coordinates
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param z position on the z-axis
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Getter for the x coordinate of the point
     *
     * @return the x coordinate of the point
     */
    public double getX() {
        return xyz.d1;
    }

    /**
     * Getter for the y coordinate of the point
     *
     * @return the y coordinate of the point
     */
    public double getY() {
        return xyz.d2;
    }

    /**
     * Getter for the z coordinate of the point
     *
     * @return the z coordinate of the point
     */
    public double getZ() {
        return xyz.d3;
    }

    /**
     * Constructor that builds a point from the given double3 object
     *
     * @param xyz double3 that contains the x,y,z coordinates of the point
     */
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Point Subtraction operation
     *
     * @param point second operand in the subtraction operation
     * @return the subtraction result-vector, a vector from the GIVEN point to THIS point
     * (given point -> this point)
     */
    public final Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * Performs unconventional scalar subtraction.
     * subtracts the given scalar from each of the point's x,y,z coordinates
     *
     * @param d the subtraction scalar factor
     * @return the following point: (x - d, y - d, z - d)
     */
    public final Point subtract(double d) {
        return new Point(getX() - d, getY() - d, getZ() - d);
    }

    /**
     * Point addition operation
     *
     * @param vec a vector as the second operand in the addition
     * @return result point of the addition operation
     */
    public Point add(Vector vec) {
        return new Point(this.xyz.add(vec.xyz));
    }

    /**
     * Gives the distance (not squared) to the given point in the space
     *
     * @param pnt a second point we wish to get the distance from
     * @return the distance between this point to the given second point
     */
    public double distance(Point pnt) {
        return Math.sqrt(this.distanceSquared(pnt));
    }

    /**
     * Gives the squared distance to the given point in the space
     *
     * @param pnt a second point we wish to get the distance from
     * @return the squared distance between this point to the given second point
     */
    public double distanceSquared(Point pnt) {
        double dx = pnt.xyz.d1 - this.xyz.d1;
        double dy = pnt.xyz.d2 - this.xyz.d2;
        double dz = pnt.xyz.d3 - this.xyz.d3;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Performs Quadratic Interpolation using bezier-curves with the given points.
     * the method accepts origin and destination points, as well as an interpolation point
     * as well as normalized double (with range 0 to 1) to represent where we are along the curve.
     * the returned point will be positioned along the curve created by the origin, destination and interpolation points.
     * if the given normalized double t equals to 0, we are at the beginning of the curve, which is the origin point.
     * if the given normalized double t equals to 1, we are at the end of the curve, which is the destination point.
     * for each value in between, the returned point will be in the given t % of the route along the curve,
     * respective to the curve's length, of course. if you have no clue what quadratic interpolation is, watching visual
     * demonstrations on YouTube can greatly help you understand and use this feature.
     *
     * @param originPoint        the origin point, where the curve starts from
     * @param interpolationPoint the interpolation point, will be used for creating the bezier-curve between the origin and destination
     * @param endPoint           the ending/destination point, where the curve ends
     * @param t                  normalized double (range: 0 to 1), represents where we are along the curve, percentage wise
     * @return the point which is t percent along the curve from the origin to the destination point
     * @throws IllegalArgumentException if the given double parameter t is not normalized ( t &lt; 0 or t &gt; 1)
     */
    public static Point quadraticInterpolate(Point originPoint, Point interpolationPoint, Point endPoint, double t) {
        if (t < 0 || t > 1) {
            throw new IllegalArgumentException("Interpolation parameter t must be between 0 and 1");
        }
        double x = Math.pow(1 - t, 2) * originPoint.getX() + 2 * (1 - t) * t * interpolationPoint.getX() + Math.pow(t, 2) * endPoint.getX();
        double y = Math.pow(1 - t, 2) * originPoint.getY() + 2 * (1 - t) * t * interpolationPoint.getY() + Math.pow(t, 2) * endPoint.getY();
        double z = Math.pow(1 - t, 2) * originPoint.getZ() + 2 * (1 - t) * t * interpolationPoint.getZ() + Math.pow(t, 2) * endPoint.getZ();
        return new Point(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "" + this.xyz;
    }
}
