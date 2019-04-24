import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.*;

public class Face {
    private List<Triangle> triangles;

    public Face(int[][] mesh, String shFile, String txFile) throws IOException {
        triangles = new ArrayList<>();
        double[][] shGrid = CSVReader.get2DDataDouble(shFile);
        double[][] txGrid = CSVReader.get2DDataDouble(txFile);

        for (int i = 0; i < mesh.length; i++) {
            PVector[] points = new PVector[3];
            Color[] colors = new Color[3];
            for (int col = 0; col < mesh[0].length; col++) {
                int row = mesh[i][col] - 1;
                points[col] = new PVector(((float) shGrid[row][0]), ((float) shGrid[row][1]), ((float) shGrid[row][2]));
                colors[col] = new Color(txGrid[row][0], txGrid[row][1], txGrid[row][2]);
            }
            triangles.add(new Triangle(points[0], points[1], points[2], colors[0], colors[1], colors[2]));
        }
    }

    public Face(Face avgFace, int[][] mesh, String shFile, String txFile, double[] shEV, double[] txEV) throws IOException {
        List<Triangle> avgTriangles = avgFace.getTriangles();
        triangles = new ArrayList<>();
        Set<Triangle> tr = new TreeSet<>();
        double[][] shDeltaGrid = CSVReader.get2DDataDouble(shFile);
        double[][] txDeltaGrid = CSVReader.get2DDataDouble(txFile);
        double shWeight = getFileWeight(shFile, shEV) * 3;
        double txWeight = getFileWeight(txFile, txEV) * 5;

        for (int i = 0; i < mesh.length; i++) {
            PVector[] points = new PVector[3];
            Color[] colors = new Color[3];
            for (int col = 0; col < mesh[0].length; col++) {
                int row = mesh[i][col] - 1;
                points[col] = new PVector(((float) shDeltaGrid[row][0]), ((float) shDeltaGrid[row][1]), ((float) shDeltaGrid[row][2]));
                points[col] = points[col].mult((float) shWeight);
                colors[col] = new Color(txDeltaGrid[row][0], txDeltaGrid[row][1], txDeltaGrid[row][2]);
                colors[col] = colors[col].mult(txWeight);
            }
            Triangle newTriangle = avgTriangles.get(i).addVals(points[0], points[1], points[2], colors[0], colors[1],
                    colors[2]);
            triangles.add(newTriangle);
        }
        triangles.sort(null);
    }

    public void scale(float val) {
        for (Triangle t : triangles) {
            t.scale(val);
        }
    }

    public void reverse() {
        for (Triangle t : triangles) {
            t.reverse();
        }
    }

    public void moveBy(PVector vector) {
        for (Triangle t : triangles) {
            t.moveBy(vector);
        }
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void draw(PApplet pApplet) {
        for (Triangle tr : triangles) {
            Color color = (Color) tr.getAverageColor();
            pApplet.fill(((float) color.r), ((float) color.g), ((float) color.b));
            pApplet.stroke(((float) color.r), ((float) color.g), ((float) color.b));
            pApplet.triangle(tr.p1.x, tr.p1.y, tr.p2.x, tr.p2.y, tr.p3.x, tr.p3.y);
        }
    }

    private double getFileWeight(String file, double[] ev) {
        String indexText = file.split("_")[1].split(".csv")[0];
        int index = Integer.parseInt(indexText);
        return ev[index - 1];
    }
}
