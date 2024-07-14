package xml;

import geometries.Geometries;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;
import renderer.ImageWriter;
import scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Contains static utility methods for parsing camera presets from the xml presets file
 */
public class XmlParser {

    /**
     * The name of the xml scenes file
     */
    private final static String FILE_NAME = "presets.xml";
    /**
     * The path to the scenes file
     */
    private final static String FOLDER_PATH = System.getProperty("user.dir") + "/xml/";

    /**
     * Static Function for reading a scene by name from the scenes file
     *
     * @param sceneName the name of the scene
     * @return An initialized scene object from the xml scenes file
     * that matches the given name, null if the scene is not found
     * @throws RuntimeException if there was an error loading or parsing the file
     */
    public static Scene extractPreset(String sceneName) {
        Element root = extractXmlRoot();

        //finding and extracting the scene with the given name
        NodeList sceneList = root.getElementsByTagName("scene");
        Element sceneElement = extractPreset(root, sceneName);

        if (sceneElement != null)
            return parseScene(sceneElement);

        return null;
    }

    /**
     * Static Function for reading a camera position by a scene name from the scenes file
     *
     * @param sceneName the name of the scene
     * @return A point for the camera position in the scene from the xml scenes file, null if the
     * scene is not found
     * @throws RuntimeException if there was an error loading or parsing the file
     */
    public static Point extractCameraPosition(String sceneName) {
        Element root = extractXmlRoot();
        //finding and extracting the scene with the given name
        NodeList sceneList = root.getElementsByTagName("scene");
        Element sceneElement = extractPreset(root, sceneName);

        if (sceneElement != null) {
            NodeList cameraPosition = sceneElement.getElementsByTagName("camera-position");
            if (cameraPosition.getLength() > 0) {
                Element position = (Element) cameraPosition.item(0);
                return parsePoint(position.getAttribute("p0"));
            }
        }
        return null;
    }

    /**
     * Static Function for reading an Image writer by a scene name from the scenes file
     *
     * @param sceneName the name of the scene
     * @param imageName the name to be assigned to the image file upon its creation
     * @return An Image writer for the scene from the xml scenes file, null if the
     * scene is not found
     * @throws RuntimeException if there was an error loading or parsing the file
     */
    public static ImageWriter extractImageWriter(String sceneName, String imageName) {
        Element root = extractXmlRoot();
        //finding and extracting the scene with the given name
        NodeList sceneList = root.getElementsByTagName("scene");
        Element sceneElement = extractPreset(root, sceneName);

        if (sceneElement != null) {
            NodeList node = sceneElement.getElementsByTagName("image-writer");
            if (node.getLength() > 0) {
                Element imageWriter = (Element) node.item(0);
                int nX = Integer.valueOf(imageWriter.getAttribute("nx"));
                int nY = Integer.valueOf(imageWriter.getAttribute("ny"));
                return new ImageWriter(imageName, nX, nY);
            }
        }
        return null;
    }

    /**
     * Extract a preset element by name (the scene's name) from the xml file
     *
     * @param root the root node of the xml file
     * @param name the name of the scene
     * @return the element of the required preset, null if not found
     */
    private static Element extractPreset(Element root, String name) {
        NodeList sceneList = root.getElementsByTagName("preset");
        for (int i = 0; i < sceneList.getLength(); i++) {
            Element sceneElement = (Element) sceneList.item(i);
            if (sceneElement.getAttribute("name").equals(name)) {
                return sceneElement;
            }
        }
        return null;
    }

    /**
     * Extracts the root element from the xml file
     *
     * @return the root element of the presets file, containing all the presets
     */
    private static Element extractXmlRoot() {
        try {
            String fullPath = FOLDER_PATH + FILE_NAME;
            File xmlFile = new File(fullPath);

            //checking if the file exists
            if (!xmlFile.exists()) {
                throw new RuntimeException("File: " + FILE_NAME + " not found in: " + FOLDER_PATH);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            //getting the root element (presets)
            return document.getDocumentElement();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing file " + FILE_NAME, e);
        }
    }

    /**
     * Parses a scene from the given xml node-element
     *
     * @param sceneElement the scene's root element
     * @return a newly initialized scene object from the given scene node
     */
    private static Scene parseScene(Element sceneElement) {
        String sceneName = sceneElement.getAttribute("name");
        Scene scene = new Scene(sceneName);

        //extracting the background light
        Color backgroundColor = extractBackgroundLight(sceneElement);
        if (backgroundColor != null)
            scene.setBackground(backgroundColor);

        //extracting the ambient light
        AmbientLight ambientLight = extractAmbientLight(sceneElement);
        if (ambientLight != null)
            scene.setAmbientLight(ambientLight);

        //extracting the scene's geometries
        NodeList geometriesList = sceneElement.getElementsByTagName("geometries");
        Geometries geometries = new Geometries();
        if (geometriesList.getLength() > 0) {
            Element geometriesElement = (Element) geometriesList.item(0);
            NodeList geometryList = geometriesElement.getChildNodes();
            for (int i = 0; i < geometryList.getLength(); i++) {
                if (geometryList.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element geometryElement = (Element) geometryList.item(i);
                    String tagName = geometryElement.getTagName();

                    //calling the proper parsing function for the geometry
                    switch (tagName) {
                        case "sphere":
                            Sphere sphere = parseSphere(geometryElement);
                            geometries.add(sphere);
                            break;
                        case "triangle":
                            Triangle triangle = parseTriangle(geometryElement);
                            geometries.add(triangle);
                            break;
                        case "plane":
                            Plane plane = parsePlane(geometryElement);
                            geometries.add(plane);
                            break;
                        case "polygon":
                            break;
                        case "tube":
                            break;
                        case "cylinder":
                            break;
                    }
                }
            }
        }
        scene.setGeometries(geometries);

        return scene;
    }

    /**
     * Extract and build the background light color from the given scene element
     *
     * @param element the scene element containing the background light
     * @return the parsed background color
     */
    private static Color extractBackgroundLight(Element element) {
        NodeList backgroundColorList = element.getElementsByTagName("background-color");
        if (backgroundColorList.getLength() > 0) {
            Element backgroundColorElement = (Element) backgroundColorList.item(0);
            return parseColor(backgroundColorElement.getAttribute("color"));
        }
        return null;
    }

    /**
     * Extract and build the Ambient Light object from the given scene element
     *
     * @param element the scene element containing the background light
     * @return the parsed Ambient Light object
     */
    private static AmbientLight extractAmbientLight(Element element) {
        NodeList ambientLightList = element.getElementsByTagName("ambient-light");
        if (ambientLightList.getLength() > 0) {
            Element ambientLightElement = (Element) ambientLightList.item(0);
            Color ambientLightColor = parseColor(ambientLightElement.getAttribute("color"));
            Double3 kScalar = parseDouble3(ambientLightElement.getAttribute("scalar"));
            return new AmbientLight(ambientLightColor, kScalar);
        }
        return null;
    }

    /**
     * Parsing a sphere object from teh given xml element
     *
     * @param element the sphere element
     * @return the newly parsed sphere
     */
    private static Sphere parseSphere(Element element) {
        Point center = parsePoint(element.getAttribute("center"));
        double radius = Double.parseDouble(element.getAttribute("radius"));
        return new Sphere(center, radius);
    }

    /**
     * Parsing a Triangle object from the given xml element
     *
     * @param element the Triangle element
     * @return the newly parsed Triangle object
     */
    private static Triangle parseTriangle(Element element) {
        Point p0 = parsePoint(element.getAttribute("p0"));
        Point p1 = parsePoint(element.getAttribute("p1"));
        Point p2 = parsePoint(element.getAttribute("p2"));
        return new Triangle(p0, p1, p2);
    }

    /**
     * Parsing a plane object from the given xml element
     *
     * @param element the plane element
     * @return the newly parsed plane object
     */
    private static Plane parsePlane(Element element) {
        Point p0 = parsePoint(element.getAttribute("p0"));
        Vector n = parseVector("n");
        return new Plane(p0, n);
    }

    /**
     * Parsing a point from a string containing its 3D coords
     *
     * @param pointStr the point string
     * @return the newly parsed point object
     */
    private static Point parsePoint(String pointStr) {
        String[] coords = pointStr.split(" ");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        return new Point(x, y, z);
    }

    /**
     * Parsing a vector from a string containing its 3D coords
     *
     * @param pointStr the vector string
     * @return the newly parsed vector object
     */
    private static Vector parseVector(String pointStr) {
        Point tmp = parsePoint(pointStr);
        return new Vector(tmp.getX(), tmp.getY(), tmp.getZ());
    }

    /**
     * Parsing a double3 object from a string containing its 3 coords
     *
     * @param pointStr the double3 string
     * @return the newly parsed double3 object
     */
    private static Double3 parseDouble3(String pointStr) {
        String[] coords = pointStr.split(" ");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        return new Double3(x, y, z);
    }

    /**
     * Parsing a color object from a string containing its RGB values
     *
     * @param colorStr the color string
     * @return the newly parsed color object
     */
    private static Color parseColor(String colorStr) {
        String[] rgb = colorStr.split(" ");
        int r = Integer.parseInt(rgb[0]);
        int g = Integer.parseInt(rgb[1]);
        int b = Integer.parseInt(rgb[2]);
        return new Color(r, g, b);
    }
}