package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Scene class. contains the scene's name, background &amp; ambient color, geometries package
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class Scene {

    /**
     * The name of the scene
     */
    public String name;
    /**
     * Background color of the scene. default is black
     */
    public Color background = Color.BLACK;
    /**
     * The ambient-light color of the scene. default is NO ambient-light
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * Geometries package of the scene. contains all the geometries that are in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructor that initializes the scene with the given name
     *
     * @param name a name for the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Setter for the scene's background color
     *
     * @param color a color for the scene's background
     * @return the scene object itself
     */
    public Scene setBackground(Color color) {
        this.background = color;
        return this;
    }

    /**
     * Setter for the scene's ambient light
     *
     * @param ambientLight ambient light object for the scene's ambient light field
     * @return the scene object itself
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for the geometries package of the scene
     *
     * @param geometries a geometries package which contains the geometries we wish our scene to contain
     * @return the scene object itself
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }


}
