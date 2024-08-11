package renderer;

import geometries.Geometries;
import geometries.Geometry;
import geometries.Intersectable.GeoPoint;
import geometries.Polygon;
import primitives.*;
import scene.Scene;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Voxel-based ray tracer. performs ray tracing using the technique of ray marching through a voxel-grid.
 * most effective for scenes that contain many objects and with areas that have many objects clumped-up in them
 */
public class VoxelRayTracer extends SimpleRayTracer {
    /**
     * The maximum cubic dimensions allowed for the scene's bounding box
     */
    private static final double MAX_SCENE_DIAMETER = 9000;
    /**
     * The ratio between voxels to geometries in the scene.
     * determines how many voxels should be created for each geometry (in average). default is one
     */
    private static final double VOXEL_TO_GEOMETRY_RATIO = 3d;

    /**
     * The three-dimensional voxels grid
     */
    private Voxel[][][] voxels;

    /**
     * The minimum coordinates of the scene's bounding box
     */
    private Point sceneBoxMin;
    /**
     * The maximum coordinates of the scene's bounding box
     */
    private Point sceneBoxMax;

    /**
     * The minimum x coordinate of the scene's bounding box
     */
    private double sceneBoxMinX;
    /**
     * The minimum y coordinate of the scene's bounding box
     */
    private double sceneBoxMinY;
    /**
     * The minimum z coordinate of the scene's bounding box
     */
    private double sceneBoxMinZ;

    /**
     * The maximum x coordinate of the scene's bounding box
     */
    private double sceneBoxMaxX;
    /**
     * The maximum y coordinate of the scene's bounding box
     */
    private double sceneBoxMaxY;
    /**
     * The maximum z coordinate of the scene's bounding box
     */
    private double sceneBoxMaxZ;

    /**
     * The size of each voxel-cube on the x-axis
     */
    private double voxelSizeX;
    /**
     * The size of each voxel-cube on the y-axis
     */
    private double voxelSizeY;
    /**
     * The size of each voxel-cube on the z-axis
     */
    private double voxelSizeZ;

    /**
     * Geometries container that contains polygons for the scene's bounding box walls
     */
    private Geometries walls;

    /**
     * Constructor that initializes the tracer with the given scene.
     * will perform the division of the scene into a voxel grid. and initialize
     * the tracer's fields accordingly
     *
     * @param scene a scene for the tracer
     */
    public VoxelRayTracer(Scene scene) {
        super(scene);
        divideScene();
    }

    /**
     * Divide the scene into a voxel grid
     */
    private void divideScene() {
        //setting the bounding box of the scene
        sceneBoxMin = scene.geometries.getMinCoordinates();
        sceneBoxMax = scene.geometries.getMaxCoordinates();
        //ensuring the scene's bounding box is not too large
        ensureSceneSizeLimit();
        //caching fields for later uses
        sceneBoxMinX = sceneBoxMin.getX();
        sceneBoxMinY = sceneBoxMin.getY();
        sceneBoxMinZ = sceneBoxMin.getZ();
        sceneBoxMaxX = sceneBoxMax.getX();
        sceneBoxMaxY = sceneBoxMax.getY();
        sceneBoxMaxZ = sceneBoxMax.getZ();

        //build the polygons of the scene's bounding walls
        buildSceneBoxWalls();

        int totalGeometries = scene.geometries.getGeometriesCount();

        //calculating the total voxels to be in the voxels grid
        double totalVoxels = totalGeometries * VOXEL_TO_GEOMETRY_RATIO;

        //calculating the amount of voxels for each axis dimension,
        // which is root 3 of the totalVoxels
        int dimensionVoxelCount = (int) Math.pow(totalVoxels, 1d / 3d) + 1;
        int numVoxelsX = dimensionVoxelCount;
        int numVoxelsY = dimensionVoxelCount;
        int numVoxelsZ = dimensionVoxelCount;

        //initiating the voxels grid array
        voxels = new Voxel[numVoxelsX][numVoxelsY][numVoxelsZ];

        System.out.println("Voxel grid was created with size " + (dimensionVoxelCount)
                + ". total voxels: " + (dimensionVoxelCount) + "^3 = " + (int) Math.pow(dimensionVoxelCount, 3));

        //size in each dimension of each voxel-box
        voxelSizeX = (sceneBoxMax.getX() - sceneBoxMin.getX()) / numVoxelsX;
        voxelSizeY = (sceneBoxMax.getY() - sceneBoxMin.getY()) / numVoxelsY;
        voxelSizeZ = (sceneBoxMax.getZ() - sceneBoxMin.getZ()) / numVoxelsZ;

        //for debugging prints
        int occupiedVoxelsCount = 0;
        int objectsInVoxelsCount = 0;

        //iterating through all the geometries in the scene and sorting them into their voxels
        List<BoundingBox> boundingBoxes = scene.geometries.getAllBoundingBoxes();
        for (BoundingBox boundingBox : boundingBoxes) {
            Point geoMin = boundingBox.getMinCoords();
            Point geoMax = boundingBox.getMaxCoords();

            //the voxel indexes of the current geometry
            int minIndexX = (int) Math.floor((geoMin.getX() - sceneBoxMin.getX()) / voxelSizeX);
            int maxIndexX = (int) Math.ceil((geoMax.getX() - sceneBoxMin.getX()) / voxelSizeX);
            int minIndexY = (int) Math.floor((geoMin.getY() - sceneBoxMin.getY()) / voxelSizeY);
            int maxIndexY = (int) Math.ceil((geoMax.getY() - sceneBoxMin.getY()) / voxelSizeY);
            int minIndexZ = (int) Math.floor((geoMin.getZ() - sceneBoxMin.getZ()) / voxelSizeZ);
            int maxIndexZ = (int) Math.ceil((geoMax.getZ() - sceneBoxMin.getZ()) / voxelSizeZ);

            //adding the current geometry to the proper voxels
            Geometry geometry = boundingBox.getGeometry();
            for (int x = minIndexX; x < maxIndexX && x < numVoxelsX && x >= 0; ++x) {
                for (int y = minIndexY; y < maxIndexY && y < numVoxelsY && y >= 0; ++y) {
                    for (int z = minIndexZ; z < maxIndexZ && z < numVoxelsZ && z >= 0; ++z) {
                        //instantiating a voxel if it's the first time
                        if (voxels[x][y][z] == null) {
                            voxels[x][y][z] = new Voxel();
                            ++occupiedVoxelsCount;
                        }
                        //adding the geometry into the voxel
                        voxels[x][y][z].geometries.add(geometry);
                        ++objectsInVoxelsCount;
                    }
                }
            }
        }

        System.out.println("Distributed " + totalGeometries + " objects into " + occupiedVoxelsCount + " voxels\n"
                + "Average objects in each voxel: " + (double) objectsInVoxelsCount / occupiedVoxelsCount);
    }

    /**
     * Ensures that the scene's bounding box is within the specified max diameter on each dimension.
     * should be called after the initialization of the scene's min and max coordinate point, and before
     * the division of the scene into voxels. if the scene's bounding box is larger than the max diameter value
     * of the tracer, the method will cut the needed portion from the bounding box while keeping it centered
     * around the same center point
     */
    private void ensureSceneSizeLimit() {
        //size of each dimension
        double sizeX = (sceneBoxMax.getX() - sceneBoxMin.getX());
        double sizeY = (sceneBoxMax.getY() - sceneBoxMin.getY());
        double sizeZ = (sceneBoxMax.getZ() - sceneBoxMin.getZ());
        //center point of the scene
        Point center = new Point(sceneBoxMin.getX() + sizeX / 2d, sceneBoxMin.getY() + sizeY / 2d, sceneBoxMin.getZ() + sizeZ / 2d);
        //ensure sizes are within the limit
        sizeX = Math.min(sizeX, MAX_SCENE_DIAMETER);
        sizeY = Math.min(sizeY, MAX_SCENE_DIAMETER);
        sizeZ = Math.min(sizeZ, MAX_SCENE_DIAMETER);
        //recalculate the scene's bounding box
        sceneBoxMin = new Point(center.getX() - sizeX / 2, center.getY() - sizeY / 2, center.getZ() - sizeZ / 2);
        sceneBoxMax = new Point(center.getX() + sizeX / 2, center.getY() + sizeY / 2, center.getZ() + sizeZ / 2);
    }

    @Override
    protected List<GeoPoint> findGeoIntersections(Ray ray) {
        return getIntersections(ray);
    }

    @Override
    protected List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return getIntersections(ray, maxDistance);
    }

    @Override
    public GeoPoint findClosestIntersection(Ray ray) {
        var intersections = getIntersections(ray);
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);

    }

    /**
     * Get all the intersection points of the given ray with the scene using the
     * voxel-grid traversal algorithm
     *
     * @param ray a ray to trace
     * @return a collection of all the intersection points of the given ray with the scene
     */
    private List<GeoPoint> getIntersections(Ray ray) {
        return getIntersections(ray, Double.MAX_VALUE);
    }

    /**
     * Get all the intersection points of the given ray with the scene, which are within
     * the given distance range, using the voxel-grid traversal algorithm
     *
     * @param ray         a ray to trace
     * @param maxDistance the maximum distance from the ray's head to look for intersections in
     * @return a collection of all the intersection points of the given ray with the scene
     * within the given distance
     */
    private List<GeoPoint> getIntersections(Ray ray, double maxDistance) {
        Point head = ray.getHead();
        Vector dir = ray.getDirection();
        Vector invDir = new Vector(1.0 / dir.getX(), 1.0 / dir.getY(), 1.0 / dir.getZ());

        //stepping intervals in each axis dimension
        int stepX = dir.getX() > 0 ? 1 : (dir.getX() < 0 ? -1 : 0);
        int stepY = dir.getY() > 0 ? 1 : (dir.getY() < 0 ? -1 : 0);
        int stepZ = dir.getZ() > 0 ? 1 : (dir.getZ() < 0 ? -1 : 0);

        //finding the entry point of the ray into the scene's box
        Point startingVoxelPoint;
        if (isInsideBoundingBox(head)) {
            //ray starts within the scene's box
            startingVoxelPoint = head;
        } else {
            //ray starts outside the scene's box
            var edgeIntersections = walls.findIntersections(ray);
            if (edgeIntersections == null) {
                //ray does not intersect the scene's bounding box
                return null;
            }
            startingVoxelPoint = ray.findClosestPoint(edgeIntersections);
            double d = head.distance(startingVoxelPoint);
            startingVoxelPoint = ray.getPoint(d + 0.1);
        }

        //calculate the indexes of the first voxel
        Point currentVoxelIndex = getVoxelIndex(startingVoxelPoint);
        int voxelX = (int) currentVoxelIndex.getX();
        int voxelY = (int) currentVoxelIndex.getY();
        int voxelZ = (int) currentVoxelIndex.getZ();

        //tDelta for each axis dimension
        double tDeltaX = stepX != 0 ? Math.abs(voxelSizeX * invDir.getX()) : Double.POSITIVE_INFINITY;
        double tDeltaY = stepY != 0 ? Math.abs(voxelSizeY * invDir.getY()) : Double.POSITIVE_INFINITY;
        double tDeltaZ = stepZ != 0 ? Math.abs(voxelSizeZ * invDir.getZ()) : Double.POSITIVE_INFINITY;

        //tMax is the jumping interval for the ray's traversal in the grid
        double tMaxX = getTMax(head.getX(), dir.getX(), sceneBoxMinX, voxelSizeX, voxelX, stepX);
        double tMaxY = getTMax(head.getY(), dir.getY(), sceneBoxMinY, voxelSizeY, voxelY, stepY);
        double tMaxZ = getTMax(head.getZ(), dir.getZ(), sceneBoxMinZ, voxelSizeZ, voxelZ, stepZ);

        Set<GeoPoint> uniqueIntersections = new HashSet<>();
        //traversing the ray through the voxel grid
        while (voxelX >= 0 && voxelX < voxels.length &&
                voxelY >= 0 && voxelY < voxels[0].length &&
                voxelZ >= 0 && voxelZ < voxels[0][0].length) {

            //checking if the ray has traveled beyond the maxDistance
            if (tMaxX > maxDistance && tMaxY > maxDistance && tMaxZ > maxDistance) {
                break;
            }

            //checking if there are intersections inside the current voxel
            if (voxels[voxelX][voxelY][voxelZ] != null) {
                var innerIntersections = voxels[voxelX][voxelY][voxelZ].findGeoIntersections(ray, maxDistance);
                if (innerIntersections != null) {
                    uniqueIntersections.addAll(innerIntersections);
                }
            }

            //moving to the next voxel
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    voxelX += stepX;
                    tMaxX += tDeltaX;
                } else {
                    voxelZ += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if (tMaxY < tMaxZ) {
                    voxelY += stepY;
                    tMaxY += tDeltaY;
                } else {
                    voxelZ += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
        }

        //returning the found intersections as a list
        return uniqueIntersections.isEmpty() ? null : new LinkedList<>(uniqueIntersections);
    }

    /**
     * Checks if the given point is inside the bounding box of the scene
     *
     * @param p a point to test
     * @return true if the given point is inside the bounding box of the scene, false otherwise
     */
    private boolean isInsideBoundingBox(Point p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        return x >= sceneBoxMinX && x <= sceneBoxMaxX &&
                y >= sceneBoxMinY && y <= sceneBoxMaxY &&
                z >= sceneBoxMinZ && z <= sceneBoxMaxZ;
    }

    /**
     * Computes the `tMax` value for voxel traversal along a specific axis.
     * The `tMax` value determines when the ray will cross from the current voxel to the next voxel
     * along the specified axis. It is calculated based on the ray's origin, direction, and the
     * dimensions of the voxels. This method is essential for the 3D Digital Differential Analyzer (3DDDA)
     * algorithm, which marches the ray through the voxel grid.
     *
     * @param originCoord  The coordinate of the ray's origin along the specific axis (x, y, or z).
     * @param direction    The direction of the ray along the specific axis. A positive value indicates
     *                     the ray is moving in the positive direction along the axis, and a negative
     *                     value indicates it is moving in the negative direction.
     * @param boxMin       The minimum coordinate of the bounding box along the specific axis.
     * @param voxelSize    The size of each voxel along the specific axis.
     * @param currentVoxel The index of the current voxel along the specific axis.
     * @param step         The step value along the specific axis, which can be -1, 0, or 1, indicating
     *                     the direction of traversal. A step of 0 means the ray is parallel to the axis
     *                     and does not move across voxels along that axis.
     * @return The computed `tMax` value, which represents the parameter `t` at which the ray will cross
     * from the current voxel to the next voxel along the specified axis. If the ray does not move along
     * the axis (step is 0), the method returns `Double.POSITIVE_INFINITY`.
     */
    private double getTMax(double originCoord, double direction, double boxMin, double voxelSize, int currentVoxel, int step) {
        if (step == 0) return Double.POSITIVE_INFINITY;
        return step > 0 ?
                ((currentVoxel + 1) * voxelSize + boxMin - originCoord) / direction :
                (currentVoxel * voxelSize + boxMin - originCoord) / direction;
    }

    /**
     * Get voxel indexes of the voxel that contains the given point inside the 3-dimensional voxels-grid.
     *
     * @param point represents a point inside the voxels-grid
     * @return a point representing the x,y,z indexes of the voxel which contains the given point
     */
    private Point getVoxelIndex(Point point) {
        int xIndex = (int) Math.floor((point.getX() - sceneBoxMinX) / voxelSizeX);
        int yIndex = (int) Math.floor((point.getY() - sceneBoxMinY) / voxelSizeY);
        int zIndex = (int) Math.floor((point.getZ() - sceneBoxMinZ) / voxelSizeZ);
        return new Point(xIndex, yIndex, zIndex);
    }

    /**
     * Build polygons for each of the 6 walls of the scene's bounding box, and initiate
     * the walls object with them
     */
    private void buildSceneBoxWalls() {
        // Calculate the corner points of the bounding box
        Point p1 = new Point(sceneBoxMin.getX(), sceneBoxMin.getY(), sceneBoxMin.getZ());
        Point p2 = new Point(sceneBoxMax.getX(), sceneBoxMin.getY(), sceneBoxMin.getZ());
        Point p3 = new Point(sceneBoxMax.getX(), sceneBoxMax.getY(), sceneBoxMin.getZ());
        Point p4 = new Point(sceneBoxMin.getX(), sceneBoxMax.getY(), sceneBoxMin.getZ());

        Point p5 = new Point(sceneBoxMin.getX(), sceneBoxMin.getY(), sceneBoxMax.getZ());
        Point p6 = new Point(sceneBoxMax.getX(), sceneBoxMin.getY(), sceneBoxMax.getZ());
        Point p7 = new Point(sceneBoxMax.getX(), sceneBoxMax.getY(), sceneBoxMax.getZ());
        Point p8 = new Point(sceneBoxMin.getX(), sceneBoxMax.getY(), sceneBoxMax.getZ());

        // Define the bottom wall (p1, p2, p3, p4)
        Polygon bottomWall = new Polygon(p1, p2, p3, p4);

        // Define the top wall (p5, p6, p7, p8)
        Polygon topWall = new Polygon(p5, p6, p7, p8);

        // Define the wall1 (p1, p2, p6, p5)
        Polygon wall1 = new Polygon(p1, p2, p6, p5);

        // Define the wall2 (p2, p3, p7, p6)
        Polygon wall2 = new Polygon(p2, p3, p7, p6);

        // Define the wall3 (p3, p4, p8, p7)
        Polygon wall3 = new Polygon(p3, p4, p8, p7);

        // Define the wall4 (p4, p1, p5, p8)
        Polygon wall4 = new Polygon(p4, p1, p5, p8);

        walls = new Geometries(bottomWall, topWall, wall1, wall2, wall3, wall4);
    }
}
