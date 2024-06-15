package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Elementary tests for the camera's image writer
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class ImageWriterTest {

    private int horizontalRes = 800;
    private int verticalRes = 500;
    private ImageWriter imageWriter = new ImageWriter("first test", horizontalRes, verticalRes);

    /**
     * Testing the image-writer with a basic image-render &amp; construction of a grid in an empty scene
     */
    @Test
    void testImageWriter() {
        int cubeWidth = horizontalRes / 16;
        int cubeHeight = verticalRes / 10;
        //running on columns, i = y
        for (int i = 0; i < verticalRes; ++i) {
            //running on the row, j = x
            for (int j = 0; j < horizontalRes; ++j) {
                Color color = i % cubeHeight == 0 || j % cubeWidth == 0 ?
                        new Color(3, 20, 252) : new Color(252, 140, 3);
                imageWriter.writePixel(j, i, color);
            }
        }
        imageWriter.writeToImage();
    }
}
