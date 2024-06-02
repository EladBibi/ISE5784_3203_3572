package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Interface that represents a geometry object in a three-dimensional space
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public interface Geometry extends Intersectable {

    /**
     * Function that returns a normal vector(orthogonal vector) to the given point
     *
     * @param point the point we wish to get the normal from
     * @return normalized vector that is perpendicular (orthogonal) to the given point
     */
    public Vector getNormal(Point point);
}
