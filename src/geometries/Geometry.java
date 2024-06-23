package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Interface that represents a geometry object in a three-dimensional space
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry object. default is black
     */
    private Color emission = Color.BLACK;

    /**
     * The Material of the geometry object
     */
    private Material material = new Material();

    /**
     * Getter for the geometry object's emission light
     *
     * @return the geometry object's emission light
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Getter for the geometry object's material
     *
     * @return the Material of the geometry object's
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter for the geometry object's emission light
     *
     * @param emission emission color for the geometry object
     * @return the geometry object itself
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Setter for the geometry object's material
     *
     * @param material new material for the geometry object
     * @return the geometry object itself
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Function that returns a normal vector(orthogonal vector) to the given point
     *
     * @param point the point we wish to get the normal from
     * @return normalized vector that is perpendicular (orthogonal) to the given point
     */
    public abstract Vector getNormal(Point point);
}
