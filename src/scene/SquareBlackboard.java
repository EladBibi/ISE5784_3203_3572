package scene;

import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SquareBlackboard {

    private int gridSize;

    private double diameter;

    private Point center;
    private Vector up;
    private Vector right;

    public SquareBlackboard(int gridSize, double diameter, Point center, Vector normal) {
        this.gridSize = gridSize;
        this.diameter = diameter;
        this.center = center;

        right = normal.equals(Vector.RIGHT) ? Vector.FORWARDS : Vector.RIGHT;
        right = normal.crossProduct(right).normalize();
        up = right.crossProduct(normal).normalize();
    }

    public List<Point> randomizePoints(int totalCasts) {
        //calculating the final grid size and the point amount for each cell
        gridSize = gridSize % 2 == 0 ? ++gridSize : gridSize;
        int pointsPerCell = totalCasts / (gridSize * gridSize);

        //calculating the size for each cell in the grid
        double cellDiameter = diameter / gridSize;
        double halfCellDiameter = cellDiameter / 2d;
        int halfGridSize = gridSize / 2;

        Random random = new Random();
        List<Point> points = new LinkedList<>();

        //running on columns, i = x
        for (int i = -halfGridSize; i <= halfGridSize; ++i) {
            Point rowCenter = i != 0 ? center.add(right.scale(i * cellDiameter)) : center;

            //running on rows, j = y
            for (int j = -halfGridSize; j <= halfGridSize; ++j) {
                Point currentCenter = j != 0 ? rowCenter.add(up.scale(j * cellDiameter)) : rowCenter;

                //generating the pre-defined amount of points for each cell in the grid
                for (int c = 0; c < pointsPerCell; ++c) {
                    //randomizing up and down movement inside the cell for getting a random point in the cell
                    double horizontalOffset = random.nextDouble() * 2 - 1;
                    double verticalOffset = random.nextDouble() * 2 - 1;

                    points.add(currentCenter.add(right.scale(horizontalOffset * halfCellDiameter))
                            .add(up.scale(verticalOffset * halfCellDiameter)));
                }
            }
        }
        return points;
    }

}
