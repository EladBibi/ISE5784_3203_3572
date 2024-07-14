package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class that introduce moving and rotating functionality
 * for geometries in the 3D space
 */
public abstract class Movable {

    /**
     * The pivot point for the geometry around which movement and rotation will be done
     * default is the zero point
     */
    protected Point pivot = Point.ZERO;

    /**
     * Setter for the pivot point
     *
     * @param point the new pivot point
     * @return the geometry object itself as Intersectable
     */
    public Intersectable setPivot(Point point) {
        this.pivot = point;
        return (Intersectable) this;
    }

    /**
     * Getter for the pivot position
     *
     * @return the pivot point of this object
     */
    public Point getPivot() {
        return pivot;
    }

    /**
     * Place a clone of this object at the specified position (movement will be
     * performed around the pivot point - make sure it is set properly for expected results).
     * the current geometry will NOT be effected
     *
     * @param position the new position
     * @return the geometry object itself as Intersectable
     */
    public abstract Intersectable moveCloneTo(Point position);

    /**
     * Move a clone of this object by the given x,y,z offset coords (movement will be
     * performed around the pivot point - make sure it is set properly for expected results).
     * the current geometry will NOT be effected
     *
     * @param x movement in the x-axis
     * @param y movement in the y-axis
     * @param z movement in the z-axis
     * @return the geometry object itself as Intersectable
     */
    public abstract Intersectable moveCloneBy(double x, double y, double z);

    /**
     * Rotate a clone of this object on the given axis, with the given degree (rotation will be
     * performed around the pivot point - make sure it is set properly for expected results).
     * the current geometry will NOT be effected
     *
     * @param rotationAxis the axis around which we want to rotate
     * @param degrees      the degree of the rotation
     * @return the geometry object itself as Intersectable
     */
    public abstract Intersectable cloneAndRotate(Vector rotationAxis, double degrees);
}
