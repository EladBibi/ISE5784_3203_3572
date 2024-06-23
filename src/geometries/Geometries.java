package geometries;

import primitives.Ray;

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
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Empty default constructor for creating an empty geometries container
     */
    public Geometries() {
    }

    /**
     * Constructor that initializes the geometries container with the given collection of geometries
     *
     * @param geometries one or more geometries objects to be put inside the new geometries container
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adding geometries to the container
     *
     * @param geometries one or more geometries objects to be added to the container
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> list = null;
        for (Intersectable geometry : geometries) {
            var intersections = geometry.findGeoIntersections(ray);
            if (intersections != null) {
                if (list == null)
                    list = new LinkedList<>(intersections);
                else
                    list.addAll(intersections);
            }
        }
        return list;
    }
}
