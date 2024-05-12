package primitives;

/**
 * Represents a Vector in a 3-Dimensional space.
 * Various methods are available for basic vector operations
 * @author Pini Goldfraind
 */
public final class Vector extends Point{

    /**
     * Constructor that receives x,y,z coordinates to represent the vector's
     * direction from the axis center.
     * will throw exception if a zero-vector is given
     * @param x movement on the x-axis
     * @param y movement on the y-axis
     * @param z movement on the z-axis
     * @exception IllegalArgumentException thrown if the vector is a zero-vector
     */
    public Vector(double x, double y, double z){
        super(x,y,z);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero Vector");
    }

    /**
     * Constructor that receives a Double3 object that contains the x,y,z direction
     * coordinates of the vector.
     * will throw exception if a zero-vector is given
     * @param coordinates Double3 object containing the coordinates in a 3D space
     * @exception IllegalArgumentException thrown if the vector is a zero-vector
     */
    public Vector(Double3 coordinates){
        super(coordinates);
        if(this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero Vector");
    }

    /**
     * Vector addition operation. performs addition between two vectors
     * @param vec2 the right operand in the addition operation
     * @return a new vector whose coordinates are summed from the two given vectors
     */
    public final Vector add(Vector vec2){
        return new Vector(this.xyz.add(vec2.xyz));
    }

    /**
     * Vector scaling operation. will scale all three coordinates of the vector by
     * the given real number
     * @param scale real number for the scaling operation
     * @return a new vector that is the original vector, scaled by the given number
     */
    public final Vector scale(double scale){
        return new Vector(this.xyz.scale(scale));
    }

    /**
     * Performs Dot-product operation on the two given vectors
     * @param vec2 second vector for the operation
     * @return a double number which is the dot-product of the two given vectors
     */
    public final double dotProduct(Vector vec2){
        return (this.xyz.d1 * vec2.xyz.d1) + (this.xyz.d2 * vec2.xyz.d2) + (this.xyz.d3 * vec2.xyz.d3);
    }

    /**
     * Performs Cross-product operation on the two given vectors.
     * @param vec2 second vector for the operation
     * @return a new vector that is orthogonal(perpendicular) to the two given vectors
     */
    public final Vector crossProduct(Vector vec2){
        return new Vector(this.xyz.d2 * vec2.xyz.d3 - this.xyz.d3 * vec2.xyz.d2,
                            this.xyz.d3 * vec2.xyz.d1 - this.xyz.d1 * vec2.xyz.d3,
                            this.xyz.d1 * vec2.xyz.d2 - this.xyz.d2 * vec2.xyz.d1);}

    /**
     * Gives the squared length of this vector object
     * @return the squared length of this vector object as double
     */
    public final double lengthSquared(){
        return this.dotProduct(this);
    }

    /**
     * Gives the length of this vector object
     * @return the length(not squared) of this vector object as double
     */
    public final double length(){
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Gives a new normalized vector based on this vector object
     * @return a new vector with the same direction of this vector-object, with magnitude of 1
     */
    public final Vector normalize(){
        return this.scale(1f / this.length());
    }

    @Override
    public final boolean equals(Object obj){
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public final String toString(){
        return super.toString();
    }
}
