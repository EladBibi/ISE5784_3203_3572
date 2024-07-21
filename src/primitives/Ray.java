package primitives;

import geometries.Intersectable.GeoPoint;
import scene.BlackboardBase;
import scene.SquareBlackboard;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a Ray in a 3-Dimensional space.
 * contains head-point for the ray's starting position and a normalized direction vector
 *
 * @author Pini Goldfraind
 */
public class Ray {

    /**
     * Delta constant used for shifting a point upwards or downwards from the original position
     */
    private static final double DELTA = 0.01;

    /**
     * The ray's starting position in the 3D space
     */
    private final Point head;


    /**
     * The ray's direction vector normalized
     */
    private final Vector direction;

    /**
     * Constructor that accepts a head point and direction vector.
     * will only initialize the ray with a normalized direction vector.
     * will normalize the given direction vector if it's not already
     *
     * @param head      starting position for the ray
     * @param direction direction of the ray. does not have to be normalized
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        if (direction.length() != 1f)
            direction = direction.normalize();
        this.direction = direction;
    }

    /**
     * Constructor that initiates a ray with a shifted position forwards/backwards
     * based on the angle of the given direction and the normal at the head-point
     *
     * @param head      starting position for the ray
     * @param direction direction vector of the ray which must be normalized ahead
     * @param normal    normal vector at the ray's starting position
     */
    public Ray(Point head, Vector direction, Vector normal) {
        double nv = normal.dotProduct(direction);
        Vector epsVector = normal.scale(nv < 0 ? -DELTA : DELTA);
        this.head = head.add(epsVector);
        this.direction = direction;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public final String toString() {
        return "h" + this.head + " " + this.direction;
    }

    /**
     * Generates a beam of rays from the head point of this ray through a squared blackboard with
     * the given diameter, which is located at the given distance from the head point.
     *
     * @param gridSize           the size of the grid to create on the black board, rows and columns (total
     *                           cells: gridSize * gridSize)
     * @param blackBoardDiameter the diameter for the square blackboard (the width and height)
     * @param distance           the distance of the blackboard from beam's origin
     * @param minTotalRayCasts   total rays we should cast through the blackboard, heavily impacts performance.
     * @return a collection of rays which are all generated from the current ray's head point and pass
     * through a specified blackboard in the 3D space
     */
    public List<Ray> generateBeam(int gridSize, double blackBoardDiameter, double distance, int minTotalRayCasts) {
        List<Ray> rays = new LinkedList<>();
        rays.add(this);

        //if there is 1 ray in the beam OR the blackboard size is 0, there is
        //no need to construct a beam
        if (minTotalRayCasts == 1 || isZero(blackBoardDiameter)) {
            return rays;
        }

        //calculating the center point of the blackboard and forming it
        Point center = this.getPoint(distance);
        BlackboardBase BlackboardBase = new SquareBlackboard(blackBoardDiameter, center, direction, gridSize);

        //creating points on the blackboard and forming rays through them
        List<Point> points = BlackboardBase.randomizePoints(minTotalRayCasts);
        for (Point point : points) {
            Vector dir = point.subtract(head).normalize();
            rays.add(new Ray(head, dir));
        }
        return rays;
    }

    /**
     * Getter for the ray's head point
     *
     * @return the head point of the ray
     */
    public Point getHead() {
        return this.head;
    }

    /**
     * Getter for the ray's direction vector
     *
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return this.direction;
    }

    /**
     * Gives a point on the ray
     *
     * @param t scalar for scaling the direction vector
     * @return head point + t * direction vector
     */
    public Point getPoint(double t) {
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException ignore) {
            return head;
        }
    }

    /**
     * Returns the point from the given list that is the closest to the ray's starting point
     *
     * @param points container of points in the space
     * @return the closest point to the ray's starting position (head point)
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(p, null)).toList()).point;
    }

    /**
     * Returns the geo-point from the given list that is the closest to the ray's starting point
     *
     * @param geoPoints container of geo-points in the space
     * @return the closest geo-point to the ray's starting position (head point).
     * the comparison will be with the point field of each geo-point in the list
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints) {
        if (geoPoints == null || geoPoints.isEmpty())
            return null;
        double distance = Double.POSITIVE_INFINITY;
        GeoPoint myGeoPoint = null;
        for (GeoPoint geoPoint : geoPoints) {
            double currentDistance = this.head.distanceSquared(geoPoint.point);
            if (currentDistance < distance) {
                distance = currentDistance;
                myGeoPoint = geoPoint;
            }
        }
        return myGeoPoint;
    }
}
