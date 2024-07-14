package geometries;

import primitives.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Geometries class for working with multiple geometries. built on the principle of the Composite design-pattern
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Geometries extends Intersectable {

    /**
     * Geometries container list
     */
    private List<Intersectable> geometries = new LinkedList<>();

    /**
     * Empty default constructor for creating an empty geometries container
     */
    public Geometries() {
    }

    /**
     * Sets the given emission color for all the geometries that are contained in this geometries object
     * @param emission the emission color for the geometries
     * @return the geometries object itself
     */
    public Geometries setEmission(Color emission) {
        for (Intersectable geo : geometries) {
            ((Geometry) geo).setEmission(emission);
        }
        return this;
    }

    /**
     * Sets the given material for all the geometries that are contained in this geometries object
     * @param material the material for the geometries
     * @return the geometries object itself
     */
    public Geometries setMaterial(Material material) {
        for (Intersectable geo : geometries) {
            ((Geometry) geo).setMaterial(material);
        }
        return this;
    }

    /**
     * Constructor that initializes the geometries container with the given collection of geometries
     *
     * @param geometries one or more geometries objects to be put inside the new geometries container
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
        this.setPivot(Point.ZERO);
    }

    /**
     * Constructor that initializes the geometries container with the given collection of geometries
     * and with a pivot position, used for moving and rotating the geometry
     *
     * @param geometries one or more geometries objects to be put inside the new geometries container
     * @param pivot      the pivot position of the geometry. moving and rotating the object will be done
     *                   around the pivot position
     */
    public Geometries(Point pivot, Intersectable... geometries) {
        this(geometries);
        this.setPivot(pivot);
    }

    /**
     * Adding geometries to the container
     *
     * @param geometries one or more geometries objects to be added to the container
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
        this.setPivot(pivot);
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> list = null;
        for (Intersectable geometry : geometries) {
            var intersections = geometry.findGeoIntersections(ray, maxDistance);
            if (intersections != null) {
                if (list == null)
                    list = new LinkedList<>(intersections);
                else
                    list.addAll(intersections);
            }
        }
        return list;
    }

    @Override
    public Intersectable setPivot(Point point) {
        List<Intersectable> newGeometries = new LinkedList<>();
        for (Intersectable geometry : geometries) {
            newGeometries.add(geometry.setPivot(point));
        }
        geometries = newGeometries;
        this.pivot = point;
        return this;
    }

    @Override
    public Intersectable moveCloneTo(Point position) {
        Geometries cloned = new Geometries();
        List<Intersectable> newGeometries = new LinkedList<>();
        for (Intersectable geometry : geometries) {
            newGeometries.add(geometry.moveCloneTo(position));
        }
        cloned.geometries = newGeometries;
        cloned.setPivot(position);
        return cloned;
    }

    @Override
    public Intersectable cloneAndRotate(Vector rotationAxis, double degrees) {
        return this;
    }
}
