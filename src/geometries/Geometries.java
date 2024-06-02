package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Geometries class for working with multiple geometries. built on the principle of the Composite design-pattern
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Geometries implements Intersectable {

    private List<Intersectable> geometries = new LinkedList<Intersectable>();

    /**
     * Empty default constructor for creating an empty geometries container
     */
    public Geometries() {

    }

    /**
     * Constructor that initializews the geometries container with the given, one or more geometries
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
        for (Intersectable geometry : geometries)
            this.geometries.add(geometry);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> list = null;
        for (Intersectable geometry : geometries) {
            List<Point> intersections = geometry.findIntersections(ray);
            if (list == null && intersections != null) {
                list = new LinkedList<Point>(intersections);
            } else if (intersections != null) {
                list.addAll(intersections);
            }
        }
        return list != null
                ? list.stream().sorted(Comparator.comparingDouble((p) -> p.distance(ray.getHead()))).toList()
                : list;
    }
}
