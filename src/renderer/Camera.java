package renderer;


import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.compare;
import static primitives.Util.isZero;
import static xml.XmlParser.*;

/**
 * Camera class that contains positioning in the plane and view plane size
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Camera implements Cloneable {

    /**
     * The camera's position in the 3D space
     */
    private Point position = Point.ZERO;
    /**
     * Up vector from the camera
     */
    private Vector vUp;
    /**
     * The forward vector to the center of the view-plane
     */
    private Vector vTo;
    /**
     * The orthogonal vector to vTo, vUp
     */
    private Vector vRight;

    /**
     * Distance of the camera from the view-plane
     */
    private double vpDistance = 0f;
    /**
     * The view-plane's width
     */
    private double vpWidth = 0;
    /**
     * The view-plane's height
     */
    private double vpHeight = 0;

    /**
     * the camera's image-writer
     */
    private ImageWriter imageWriter;
    /**
     * the camera's ray-tracer
     */
    private RayTracerBase rayTracer;

    /**
     * Empty constructor
     */
    private Camera() {

    }

    /**
     * Getter for the camera's position
     *
     * @return the camera's position point
     */
    public Point getPosition() {
        return this.position;
    }

    /**
     * +
     * Getter for the vUp vector
     *
     * @return the camera's vUp vector
     */
    public Vector getVup() {
        return this.vUp;
    }

    /**
     * +
     * Getter for the vRight vector
     *
     * @return the camera's vRight vector
     */
    public Vector getVright() {
        return this.vRight;
    }

    /**
     * +
     * Getter for the vTo vector
     *
     * @return the camera's vTo vector
     */
    public Vector getVto() {
        return this.vTo;
    }

    /**
     * Getter for the camera's distance from the view plane
     *
     * @return the camera's distance from the view plane
     */
    public double getVpDistance() {
        return this.vpDistance;
    }

    /**
     * Getter for the view plane's width
     *
     * @return the view plane's width
     */
    public double getVpWidth() {
        return this.vpWidth;
    }

    /**
     * Getter for the view plane's height
     *
     * @return the view plane's height
     */
    public double getVpHeight() {
        return this.vpHeight;
    }

    /**
     * Static function for accessing the camera's builder which can be used to customize its values
     *
     * @return a camera-builder object which allow custom camera configuration
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Function for ray construction through the view plane with a specified number of horizontal &amp; vertical pixels
     *
     * @param nX the amount of horizontal pixels in each row of the view plane
     * @param nY the amount of vertical pixels in each column of the view plane
     * @param j  the required column index in the view plane
     * @param i  the required row index in the view plane
     * @return a ray constructed through the pixel at column j and row i at the view-plane
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        //center point of the view plane
        Point pc = position.add(vTo.scale(vpDistance));

        //pixel size
        double rY = (double) vpHeight / nY;
        double rX = (double) vpWidth / nX;

        //movement in each direction
        double yI = (i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;

        Point pIJ = pc;
        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(-yI));
        }

        return new Ray(position, pIJ.subtract(position).normalize());
    }

    /**
     * Renders the image based on the camera's scene and position.
     * after executing this method, the image will be rendered inside the image-writer
     * and the image file can be constructed
     *
     * @return the camera itself
     */
    public Camera renderImage() {
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        //running on columns, i = y
        for (int i = 0; i < nY; ++i) {
            //running on the row, j = x
            for (int j = 0; j < nX; ++j) {
                castRay(nX, nY, j, i);
            }
        }
        return this;
    }

    /**
     * Helper method for casting a ray through the given pixel and coloring it per calculation
     *
     * @param nX     the amount of horizontal pixels
     * @param nY     the amount of vertical pixels
     * @param column the column's index (x pixel) for casting the ray through
     * @param row    the row's index (y pixel) for casting the ray through
     */
    private void castRay(int nX, int nY, int column, int row) {
        Ray ray = constructRay(nX, nY, column, row);
        imageWriter.writePixel(column, row, rayTracer.traceRay(ray));
    }

    /**
     * Renders a grid (lines and columns) with the given interval and color.
     * should be called AFTER rendering the image in order for the grid to be visible
     *
     * @param interval the pixels count between each line &amp; column of the grid
     * @param color    the color for the grid
     * @return the camera itself
     */
    public Camera printGrid(int interval, Color color) {
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        //running on columns, i = y
        for (int i = 0; i < nY; ++i) {
            //running on the row, j = x
            for (int j = 0; j < nX; ++j) {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
            }
        }
        return this;
    }

    /**
     * Method for constructing a PNG file of our rendered image
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    //----------------Internal class Builder----------------

    /**
     * Static Camera-builder class utilizing the builder design-pattern for
     * dynamically configuring and instantiating the camera
     */
    public static class Builder {

        /**
         * camera object for the builder to build
         */
        private final Camera camera;

        /**
         * Constructor for initializing the camera with default values
         */
        public Builder() {
            this.camera = new Camera();
        }

        /**
         * Constructor for initializing the camera with the given camera's instance
         *
         * @param camera a camera object for initializing our current camera with
         */
        public Builder(Camera camera) {
            this.camera = camera;
        }

        /**
         * Setter for the camera's location
         *
         * @param point the camera's position in the 3D space
         * @return the builder object used in the construction
         */
        public Builder setLocation(Point point) {
            camera.position = point;
            return this;
        }

        /**
         * Loading the camera position in the scene from xml file from the given scene name
         *
         * @param sceneName the name of the scene listed in the scene file
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the given scene name does not exist
         * @throws RuntimeException         if there was an error loading or parsing the scenes file
         */
        public Builder setLocationFromFile(String sceneName) {
            camera.position = extractCameraPosition(sceneName);
            return this;
        }

        /**
         * Setter for the camera's direction in the space
         *
         * @param vTo the forward vector towards the view-plane
         * @param vUp the up vector from the camera. must be orthogonal to vTo
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if either vTo, vUp vectors is a zero-vectors
         *                                  or if vUp, vTo vectors are not orthogonal to each other
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vUp.equals(Vector.ZERO) || vTo.equals(Vector.ZERO)) {
                throw new IllegalArgumentException("Camera must not contain a zero-vector as direction");
            }
            if (!isZero(vUp.dotProduct(vTo))) {
                throw new IllegalArgumentException("Camera 'Up' and 'To' vectors must be orthogonal to each other");
            }
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            return this;
        }

        /**
         * Setter for the camera's position and direction in the space.
         * this method sets both the camera's position and direction
         *
         * @param lookAtPoint a point in the space we wish the camera to look at
         * @param position    the position for the camera in the space
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the two given points, camera position and
         *                                  look-at point, are identical
         */
        public Builder setLocationAndDirection(Point position, Point lookAtPoint) {
            if (lookAtPoint.equals(position)) {
                throw new IllegalArgumentException("The look-at point cannot be the same as the camera position");
            }
            camera.position = position;
            camera.vTo = lookAtPoint.subtract(camera.position).normalize();
            camera.vUp = Vector.RIGHT.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Setter for the view plane's size
         *
         * @param height the actual height of the view plane (not pixel count)
         * @param width  the actual width of the view plane (not pixel count)
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the given height or width are not positive
         */
        public Builder setVpSize(double height, double width) {
            if (height <= 0 || width <= 0)
                throw new IllegalArgumentException("The View plane's width and height must be positive integers");
            camera.vpWidth = width;
            camera.vpHeight = height;
            return this;
        }

        /**
         * Setter for the view-plane's distance from the camera
         *
         * @param distance the distance of the camera from the view plane
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the given distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("The camera's distance from the view plane must be positive double");
            camera.vpDistance = distance;
            return this;
        }

        /**
         * Setter for the camera's image-writer
         *
         * @param imageWriter the image writer that will be used be the camera
         * @return the builder object used in the construction
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Loading an Image writer from xml file
         *
         * @param sceneName the name of the scene listed in the scene file
         * @param imageName the name for the image
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the given scene name does not exist
         * @throws RuntimeException         if there was an error loading or parsing the scenes file
         */
        public Builder setImageWriterFromFile(String sceneName, String imageName) {
            camera.imageWriter = extractImageWriter(sceneName, imageName);
            return this;
        }

        /**
         * Setter for the camera's Ray-tracer
         *
         * @param rayTracer a ray tracer for the camera
         * @return the builder object used in the construction
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Loading a ray tracer from an xml file. all data related to the scene will be loaded from the file
         *
         * @param sceneName the name of the scene listed in the scene file
         * @return the builder object used in the construction
         * @throws IllegalArgumentException if the given scene name does not exist
         * @throws RuntimeException         if there was an error loading or parsing the scenes file
         */
        public Builder setRayTracerFromFile(String sceneName) {
            Scene scene = extractPreset(sceneName);
            if (scene == null)
                throw new IllegalArgumentException("A scene with name: " + sceneName + " does not exist");
            camera.rayTracer = new SimpleRayTracer(scene);
            return this;
        }

        /**
         * Extracting a pre-made setting preset of the camera
         *
         * @param presetName the name of the preset in the file (will also be the scene name)
         * @param imageName  name for the image
         * @return the builder object used in the construction
         */
        public Builder extractPresetFromFile(String presetName, String imageName) {
            return setImageWriterFromFile(presetName, imageName)
                    .setLocationFromFile(presetName)
                    .setRayTracerFromFile(presetName);
        }

        /**
         * Build method for building and extracting the camera object from the builder
         *
         * @return a new camera object based on the values contained in the camera-builder object
         * @throws MissingResourceException if the values provided to the camera-builder are invalid
         *                                  or not sufficient for the instantiation
         */
        public Camera build() {
            String missingResourceMsg = "Missing camera resource";
            String camClassName = Camera.class.getName();

            if (camera.position == null)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "Camera position in the plane is uninitialized");
            if (camera.vTo == null)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "Camera forward vector is uninitialized");
            if (camera.vUp == null)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "Camera up vector is uninitialized");
            if (camera.vpDistance == 0)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "Camera distance from the view plane is uninitialized");
            if (camera.vpHeight == 0)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "View plane height is uninitialized");
            if (camera.vpWidth == 0)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "View plane width is uninitialized");
            if (camera.imageWriter == null)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "The Image writer is uninitialized");
            if (camera.rayTracer == null)
                throw new MissingResourceException(missingResourceMsg, camClassName,
                        "The Ray tracer is uninitialized");

            camera.vTo = compare(camera.vTo.length(), 1d) ? camera.vTo : camera.vTo.normalize();
            camera.vUp = compare(camera.vUp.length(), 1d) ? camera.vUp : camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError("Cloning failed");
            }
        }
    }
}

