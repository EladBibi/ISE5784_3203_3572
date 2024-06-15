package renderer;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for intersections through a camera's view-plane
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class CameraIntersectionsTest {

    /**
     * Error message for failed tests
     */
    private final String msg = "Wrong intersection count";

    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(1)
            .setVpSize(3, 3); //vRight = (1,0,0);


    /**
     * Testing intersection with a sphere through the view plane
     */
    @Test
    void testSphereIntersections() {
        Camera camera = cameraBuilder.build(); //vRight = (1,0,0)
        Sphere sphere;

        //TC01: 2 intersections are expected
        sphere = new Sphere(new Point(0, 0, -3), 1d);
        assertEquals(2, getIntersectionCount(camera, sphere, 3, 3), msg);

        //TC02: 18 intersections are expected
        camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5d);
        assertEquals(18, getIntersectionCount(camera, sphere, 3, 3), msg);

        //TC03: 10 intersections are expected
        camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();
        sphere = new Sphere(new Point(0, 0, -2), 2d);
        assertEquals(10, getIntersectionCount(camera, sphere, 3, 3), msg);

        //TC04: 9 intersections are expected
        camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();
        sphere = new Sphere(new Point(0, 0, -2), 4d);
        assertEquals(9, getIntersectionCount(camera, sphere, 3, 3), msg);

        //TC05: No intersections are expected
        camera = cameraBuilder.setLocation(new Point(0, 0, 0)).build();
        sphere = new Sphere(new Point(0, 0, 1), 0.5d);
        assertEquals(0, getIntersectionCount(camera, sphere, 3, 3), msg);
    }

    /**
     * Testing intersection with a plane through the view plane
     */
    @Test
    void testPlaneIntersections() {
        Camera camera = cameraBuilder.build();
        Plane plane;

        //TC01: 9 intersections are expected
        plane = new Plane(new Point(1, 1, -2), new Point(2, 3, -2), new Point(-2, 3, -2));
        assertEquals(9, getIntersectionCount(camera, plane, 3, 3), msg);

        //TC02: 9 intersections are expected
        plane = new Plane(new Point(1, 1, -2), new Point(2, 3, -1), new Point(-2, 3, -1));
        assertEquals(9, getIntersectionCount(camera, plane, 3, 3), msg);

        //TC03: 6 intersections are expected
        plane = new Plane(new Point(1, 1, -5), new Point(2, 3, -2), new Point(-2, 3, -2));
        assertEquals(6, getIntersectionCount(camera, plane, 3, 3), msg);
    }

    /**
     * Testing intersection with a triangle through the view plane
     */
    @Test
    void testTriangleIntersections() {
        Camera camera = cameraBuilder.build();
        Triangle triangle;

        //TC01: 1 intersection is expected
        triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertEquals(1, getIntersectionCount(camera, triangle, 3, 3), msg);

        //TC02: 2 intersections are expected
        triangle = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertEquals(2, getIntersectionCount(camera, triangle, 3, 3), msg);
    }


    /**
     * Helper function for getting intersection count of a given camera through its view-plane
     *
     * @param cam              the camera
     * @param geometry         geometry object (sphere\plane\triangle ect..)
     * @param horizontalPixels amount of horizontal pixels for the view plane (column count)
     * @param verticalPixels   amount of vertical pixels for the view plane (row count)
     * @return the total amount of intersections from all the rays which were cast
     * through the view-plane(a ray for each pixel)
     */
    private int getIntersectionCount(Camera cam, Geometry geometry, int horizontalPixels, int verticalPixels) {
        int count = 0;
        for (int j = 0; j < verticalPixels; ++j) {
            for (int i = 0; i < horizontalPixels; ++i) {
                Ray ray = cam.constructRay(horizontalPixels, verticalPixels, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                count += intersections != null ? intersections.size() : 0;
            }
        }
        return count;
    }
}
