package primitives;

import static primitives.Util.alignZero;
import static primitives.Util.compare;

/**
 * Represents a Vector in a 3-Dimensional space.
 * Various methods are available for basic vector operations
 *
 * @author Pini Goldfraind
 */
public final class Vector extends Point {

    /**
     * Constant for the positive y-axis direction vector(0, 1, 0)
     */
    public static final Vector UP = new Vector(0, 1, 0);
    /**
     * Constant for the negative y-axis direction vector (0, -1, 0)
     */
    public static final Vector DOWN = new Vector(0, -1, 0);
    /**
     * Constant for the positive x-axis direction vector (1, 0, 0)
     */
    public static final Vector RIGHT = new Vector(1, 0, 0);
    /**
     * Constant for the negative x-axis direction vector (-1, 0, 0)
     */
    public static final Vector LEFT = new Vector(-1, 0, 0);
    /**
     * Constant for the positive z-axis direction vector (0, 0, 1)
     */
    public static final Vector FORWARDS = new Vector(0, 0, 1);
    /**
     * Constant for the negative z-axis direction vector (0, 0, -1)
     */
    public static final Vector BACKWARDS = new Vector(0, 0, -1);

    /**
     * Constructor that receives x,y,z coordinates to represent the vector's
     * direction from the axis center.
     * will throw exception if a zero-vector is given
     *
     * @param x movement on the x-axis
     * @param y movement on the y-axis
     * @param z movement on the z-axis
     * @throws IllegalArgumentException thrown if the vector is a zero-vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero Vector");
    }

    /**
     * Constructor that receives a Double3 object that contains the x,y,z direction
     * coordinates of the vector.
     * will throw exception if a zero-vector is given
     *
     * @param coordinates Double3 object containing the coordinates in a 3D space
     * @throws IllegalArgumentException thrown if the vector is a zero-vector
     */
    public Vector(Double3 coordinates) {
        super(coordinates);
        if (this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero Vector");
    }

    /**
     * Vector addition operation. performs addition between two vectors
     *
     * @param vec2 the right operand in the addition operation
     * @return a new vector whose coordinates are summed from the two given vectors
     */
    public final Vector add(Vector vec2) {
        return new Vector(this.xyz.add(vec2.xyz));
    }

    /**
     * Vector scaling operation. will scale all three coordinates of the vector by
     * the given real number
     *
     * @param scale real number for the scaling operation
     * @return a new vector that is the original vector, scaled by the given number
     */
    public final Vector scale(double scale) {
        return new Vector(this.xyz.scale(scale));
    }

    /**
     * Performs Dot-product operation on the two given vectors
     *
     * @param vec2 second vector for the operation
     * @return a double number which is the dot-product of the two given vectors
     */
    public final double dotProduct(Vector vec2) {
        return (this.xyz.d1 * vec2.xyz.d1) + (this.xyz.d2 * vec2.xyz.d2) + (this.xyz.d3 * vec2.xyz.d3);
    }

    /**
     * Performs Cross-product operation on the two given vectors.
     *
     * @param vec2 second vector for the operation
     * @return a new vector that is orthogonal(perpendicular) to the two given vectors
     */
    public final Vector crossProduct(Vector vec2) {
        return new Vector(this.xyz.d2 * vec2.xyz.d3 - this.xyz.d3 * vec2.xyz.d2,
                this.xyz.d3 * vec2.xyz.d1 - this.xyz.d1 * vec2.xyz.d3,
                this.xyz.d1 * vec2.xyz.d2 - this.xyz.d2 * vec2.xyz.d1);
    }

    /**
     * Gives the squared length of this vector object
     *
     * @return the squared length of this vector object as double
     */
    public final double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Gives the length of this vector object
     *
     * @return the length(not squared) of this vector object as double
     */
    public final double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Gives a new normalized vector based on this vector object
     *
     * @return a new vector with the same direction of this vector-object, with magnitude of 1
     */
    public final Vector normalize() {
        return this.scale(1f / this.length());
    }

    /**
     * Inverts the given vector
     *
     * @return a new vector that has the opposite direction of the given vector and the same length
     */
    public final Vector inverted() {
        return this.scale(-1d);
    }

    /**
     * Rotating a vector around a given axis
     *
     * @param axis    the axis vector for rotating the vector on. MUST be orthogonal
     *                to the vector we are trying to rotate
     * @param degrees angle of rotation in degrees
     * @return a new rotated vector that is the current vector rotated around the given
     * axis by the given degrees amount
     * @throws IllegalArgumentException if the given axis vector is not orthogonal with the current vector
     */
    public Vector rotate(Vector axis, double degrees) {
        // Convert degrees to radians
        double radians = Math.toRadians(degrees);

        if (!compare(this.dotProduct(axis), 0d))
            throw new IllegalArgumentException("The vector is not on the given plane");

        // Calculate trigonometric values
        double cosRad = Math.cos(radians);
        double sinRad = Math.sin(radians);
        double oneMinusCos = 1 - cosRad;

        double nx = axis.getX();
        double ny = axis.getY();
        double nz = axis.getZ();

        // Construct the rotation matrix
        Vector c1 = new Vector(
                cosRad + nx * nx * oneMinusCos,
                nx * ny * oneMinusCos - nz * sinRad,
                nx * nz * oneMinusCos + ny * sinRad
        );

        Vector c2 = new Vector(
                ny * nx * oneMinusCos + nz * sinRad,
                cosRad + ny * ny * oneMinusCos,
                ny * nz * oneMinusCos - nx * sinRad
        );

        Vector c3 = new Vector(
                nz * nx * oneMinusCos - ny * sinRad,
                nz * ny * oneMinusCos + nx * sinRad,
                cosRad + nz * nz * oneMinusCos
        );

        // Apply the rotation matrix to the vector
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        double newX = c1.getX() * x + c2.getX() * y + c3.getX() * z;
        double newY = c1.getY() * x + c2.getY() * y + c3.getY() * z;
        double newZ = c1.getZ() * x + c2.getZ() * y + c3.getZ() * z;

        return new Vector(alignZero(newX), alignZero(newY), alignZero(newZ)).normalize();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public final String toString() {
        return "v" + super.xyz;
    }
}
