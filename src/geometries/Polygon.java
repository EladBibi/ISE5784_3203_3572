package geometries;

import primitives.BoundingBox;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<Point> intersections = plane.findIntersections(ray, maxDistance);
        if (intersections == null)
            return null;

        Point h = ray.getHead();
        Vector dir = ray.getDirection();

        Vector v1, v2;
        boolean positive = false;
        double sign;

        for (int i = 0; i < size; ++i) {
            Point v1Point = vertices.get(i);
            Point v2Point = vertices.get((i + 1) % size);

            //head-to-vertex[i] vector
            v1 = v1Point.subtract(h);
            //head-to-vertex[i+1] vector
            v2 = v2Point.subtract(h);
            //verifying that the sign is identical with all the vertexes
            Vector n1 = v1.crossProduct(v2).normalize();
            sign = alignZero(n1.dotProduct(dir));
            if (i == 0) {
                positive = sign > 0;
            } else if (positive != (sign > 0)) {
                return null; //signs not matching - no intersection!
            }
        }

        return List.of(new GeoPoint(intersections.getFirst(), this));
    }

    @Override
    public Point getMinCoordinates() {
        double x = Double.MAX_VALUE;
        double y = Double.MAX_VALUE;
        double z = Double.MAX_VALUE;
        for (Point point : vertices) {
            x = point.getX() < x ? point.getX() : x;
            y = point.getY() < y ? point.getY() : y;
            z = point.getZ() < z ? point.getZ() : z;
        }
        return new Point(x, y, z);
    }

    @Override
    public Point getMaxCoordinates() {
        double x = Double.MIN_VALUE;
        double y = Double.MIN_VALUE;
        double z = Double.MIN_VALUE;
        for (Point point : vertices) {
            x = point.getX() > x ? point.getX() : x;
            y = point.getY() > y ? point.getY() : y;
            z = point.getZ() > z ? point.getZ() : z;
        }
        return new Point(x, y, z);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(this, getMinCoordinates(), getMaxCoordinates());
    }

    @Override
    public Intersectable moveCloneTo(Point position) {
        Polygon cloned = (Polygon) this.getClone();
        if (position.equals(pivot))
            return cloned;
        Vector movement = position.subtract(pivot);
        cloned.pivot = position;
        List<Point> newVertices = new LinkedList<>();
        for (Point point : vertices) {
            newVertices.add(point.add(movement));
        }
        cloned.vertices = newVertices;
        cloned.plane = (Plane) plane.moveCloneTo(position);
        return cloned;
    }

    @Override
    public Intersectable cloneAndRotate(Vector rotationAxis, double degrees) {
        return this.getClone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references are identical
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure the object is of the same class
        Polygon other = (Polygon) obj;
        return vertices.equals(other.vertices) && plane.equals(other.plane); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, plane);
    }
}
