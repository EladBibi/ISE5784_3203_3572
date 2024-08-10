package primitives;

import geometries.Geometry;

/**
 * Cubic representation of a Bounding box that contains one geometry object. contains the
 * geometry object and a min and max points
 */
public class BoundingBox {

    /**
     * The geometry object contained within the box
     */
    private Geometry geometry;

    /**
     * The minimum coordinates of the bounding box
     */
    private Point minCoordinates;

    /**
     * The maximum coordinates of the bounding box
     */
    private Point maxCoordinates;

    /**
     * Constructor that initializes the bounding box with the given geometry object and
     * the minimum and maximum coordinates of the geometry, which together form a bounding box
     * @param geo the geometry object contained within the box
     * @param min the minimum x,y,z coordinates of the geometry object
     * @param max the maximum x,y,z coordinates of the geometry object
     */
    public BoundingBox(Geometry geo, Point min, Point max){
        geometry = geo;
        minCoordinates = min;
        maxCoordinates = max;
    }

    /**
     * Getter for the geometry object contained within the box
     * @return the geometry object contained within the box
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Getter for the minimum x,y,z coordinates of the geometry object
     * @return the minimum x,y,z coordinates of the geometry object
     */
    public Point getMinCoords() {
        return minCoordinates;
    }

    /**
     * Getter for the maximum x,y,z coordinates of the geometry object
     * @return the maximum x,y,z coordinates of the geometry object
     */
    public Point getMaxCoords() {
        return maxCoordinates;
    }
}
