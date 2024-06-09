package renderer;


import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.compare;
import static primitives.Util.isZero;

/**
 * Camera class that contains positioning in the plane and view plane size
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Camera implements Cloneable {

    /**
     * The camera's position in the 3D space
     */
    private Point position;
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

    @Override
    protected Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed");
        }
    }

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

            camera.vTo = compare(camera.vTo.length(), 1d) ? camera.vTo : camera.vTo.normalize();
            camera.vUp = compare(camera.vUp.length(), 1d) ? camera.vUp : camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            return (Camera) camera.clone();
        }
    }
}
