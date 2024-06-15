package primitives;

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
     * @return the subtraction result-vector, a vector from this point to the given point
     */
    public final Vector subtract(Point point) {
        return new Vector(this.xyz.subtract(point.xyz));
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "" + this.xyz;
    }
}
