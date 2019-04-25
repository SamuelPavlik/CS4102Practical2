import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.*;

public class Face {
    private static final int NO_WEIGHT = -1;

    private List<Triangle> triangles;
    private double shWeight = NO_WEIGHT;
    private double txWeight = NO_WEIGHT;

    public Face(double shWeight, double txWeight) {
        triangles = new ArrayList<>();
    }

    public Face(int[][] mesh, String shFile, String txFile) throws IOException {
        triangles = new ArrayList<>();
        double[][] shGrid = CSVReader.get2DDataDouble(shFile);
        double[][] txGrid = CSVReader.get2DDataDouble(txFile);

        for (int i = 0; i < mesh.length; i++) {
            PVector[] points = new PVector[3];
            Weightable[] colors = new Weightable[3];
            for (int col = 0; col < mesh[0].length; col++) {
                int row = mesh[i][col] - 1;
                points[col] = new PVector(((float) shGrid[row][0]), ((float) shGrid[row][1]), ((float) shGrid[row][2]));
                colors[col] = new Weightable(txGrid[row][0], txGrid[row][1], txGrid[row][2]);
            }
            triangles.add(new Triangle(points[0], points[1], points[2], colors[0], colors[1], colors[2]));
        }
    }

    public Face(int[][] mesh, String shFile, String txFile, double shWeight, double txWeight) throws IOException {
        triangles = new ArrayList<>();
        this.shWeight = shWeight;
        this.txWeight = txWeight;
        double[][] shGrid = CSVReader.get2DDataDouble(shFile);
        double[][] txGrid = CSVReader.get2DDataDouble(txFile);

        for (int i = 0; i < mesh.length; i++) {
            PVector[] points = new PVector[3];
            Weightable[] colors = new Weightable[3];
            for (int col = 0; col < mesh[0].length; col++) {
                int row = mesh[i][col] - 1;
                points[col] = new PVector(((float) shGrid[row][0]), ((float) shGrid[row][1]),
                        ((float) shGrid[row][2])).mult((float) shWeight);
                colors[col] = new Weightable(txGrid[row][0], txGrid[row][1], txGrid[row][2]).mult(txWeight);
            }
            triangles.add(new Triangle(points[0], points[1], points[2], colors[0], colors[1], colors[2]));
        }
    }

//    public Face(Face avgFace, int[][] mesh, String shFile, String txFile, double shWeight, double txWeight) throws IOException {
//        List<Triangle> avgTriangles = avgFace.getTriangles();
//        triangles = new ArrayList<>();
//        double[][] shDeltaGrid = CSVReader.get2DDataDouble(shFile);
//        double[][] txDeltaGrid = CSVReader.get2DDataDouble(txFile);
//
//        for (int i = 0; i < mesh.length; i++) {
//            PVector[] points = new PVector[3];
//            Weightable[] colors = new Weightable[3];
//            for (int col = 0; col < mesh[0].length; col++) {
//                int row = mesh[i][col] - 1;
//                points[col] = new PVector(((float) shDeltaGrid[row][0]), ((float) shDeltaGrid[row][1]), ((float) shDeltaGrid[row][2]));
//                points[col] = points[col].mult((float) shWeight);
//                colors[col] = new Weightable(txDeltaGrid[row][0], txDeltaGrid[row][1], txDeltaGrid[row][2]);
//                colors[col] = colors[col].mult(txWeight);
//            }
//            Triangle newTriangle = avgTriangles.get(i).addVals(points[0], points[1], points[2], colors[0], colors[1],
//                    colors[2]);
//            triangles.add(newTriangle);
//        }
//        triangles.sort(null);
//    }

//    public Face(Face avgFace, int[][] mesh, String shFile1, String txFile1, double shWeight1, double txWeight1
//            , String shFile2, String txFile2, double shWeight2, double txWeight2
//            , String shFile3, String txFile3, double shWeight3, double txWeight3) throws IOException {
//        List<Triangle> avgTriangles = avgFace.getTriangles();
//        triangles = new ArrayList<>();
//        double[][] shDeltaGrid1 = CSVReader.get2DDataDouble(shFile1);
//        double[][] txDeltaGrid1 = CSVReader.get2DDataDouble(txFile1);
//        double[][] shDeltaGrid2 = CSVReader.get2DDataDouble(shFile2);
//        double[][] txDeltaGrid2 = CSVReader.get2DDataDouble(txFile2);
//        double[][] shDeltaGrid3 = CSVReader.get2DDataDouble(shFile3);
//        double[][] txDeltaGrid3 = CSVReader.get2DDataDouble(txFile3);
//
//        for (int i = 0; i < mesh.length; i++) {
//            PVector[] points = new PVector[3];
//            Weightable[] colors = new Weightable[3];
//            for (int col = 0; col < mesh[0].length; col++) {
//                int row = mesh[i][col] - 1;
//                points[col] = new PVector(((float) shDeltaGrid1[row][0]), ((float) shDeltaGrid1[row][1]), ((float) shDeltaGrid1[row][2]));
//                points[col] = points[col].mult((float) shWeight1);
//                colors[col] = new Weightable(txDeltaGrid1[row][0], txDeltaGrid1[row][1], txDeltaGrid1[row][2]);
//                colors[col] = colors[col].mult(txWeight1);
//
//                points[col].add(new PVector(((float) shDeltaGrid2[row][0]), ((float) shDeltaGrid2[row][1]),
//                        ((float) shDeltaGrid2[row][2])).mult((float) shWeight2));
//                colors[col].add(new Weightable(txDeltaGrid2[row][0], txDeltaGrid2[row][1], txDeltaGrid2[row][2]).mult(txWeight2));
//
//                points[col].add(new PVector(((float) shDeltaGrid3[row][0]), ((float) shDeltaGrid3[row][1]),
//                        ((float) shDeltaGrid3[row][2])).mult((float) shWeight3));
//                colors[col].add(new Weightable(txDeltaGrid3[row][0], txDeltaGrid3[row][1], txDeltaGrid3[row][2]).mult(txWeight3));
//            }
//            Triangle newTriangle = avgTriangles.get(i).addVals(points[0], points[1], points[2], colors[0], colors[1],
//                    colors[2]);
//            triangles.add(newTriangle);
//        }
//        triangles.sort(null);
//    }

    public Face add(Face other, double shWeightNorm, double txWeightNorm) {
        for (int i = 0; i < triangles.size(); i++) {
            triangles.get(i).add(other.getTriangles().get(i), shWeightNorm, txWeightNorm);
        }

        return this;
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
            Weightable color = tr.getLambertianColor(new PVector(1, 1, 1), 3, 0.3);
            pApplet.fill(((float) color.w1), ((float) color.w2), ((float) color.w3));
            pApplet.stroke(((float) color.w1), ((float) color.w2), ((float) color.w3));
            pApplet.triangle(tr.p1.x, tr.p1.y, tr.p2.x, tr.p2.y, tr.p3.x, tr.p3.y);
        }
    }

    public Face copy() {
        Face copyFace = new Face(this.shWeight, this.txWeight);
        for (Triangle t : this.triangles) {
            copyFace.triangles.add(t.copy());
        }

        return copyFace;
    }
}
