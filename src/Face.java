import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.*;

public class Face {
    private static final int NO_WEIGHT = -1;

    private List<Triangle> triangles;
    private double shWeight = NO_WEIGHT;
    private double txWeight = NO_WEIGHT;
    private HashMap<PVector, Set<Triangle>> pointsToTriangles = new HashMap<>();
    private HashMap<PVector, PVector> normMap;
    private List<PVector> indexedPoints = new ArrayList<>();

    public Face(double shWeight, double txWeight) {
        triangles = new ArrayList<>();
        this.shWeight = shWeight;
        this.txWeight = txWeight;
    }

    public Face(int[][] mesh, String shFile, String txFile) throws IOException {
        triangles = new ArrayList<>();
        double[][] shGrid = CSVReader.get2DDataDouble(shFile);
        double[][] txGrid = CSVReader.get2DDataDouble(txFile);
        HashSet<PVector> pointSet = new HashSet<>();

        for (int i = 0; i < mesh.length; i++) {
            PVector[] points = new PVector[3];
            Weightable[] colors = new Weightable[3];
            for (int col = 0; col < mesh[0].length; col++) {
                int row = mesh[i][col] - 1;
                points[col] = new PVector(((float) shGrid[row][0]), ((float) shGrid[row][1]), ((float) shGrid[row][2]));
                colors[col] = new Weightable(txGrid[row][0], txGrid[row][1], txGrid[row][2]);
            }
            Triangle tr = new Triangle(points[0], points[1], points[2], colors[0], colors[1], colors[2]);
            triangles.add(tr);

//            pointSet.add(points[0]);
//            pointSet.add(points[1]);
//            pointSet.add(points[2]);
//
//            addToTriangles(points[0], tr);
//            addToTriangles(points[1], tr);
//            addToTriangles(points[2], tr);
        }

        indexedPoints = new ArrayList<>(pointSet);
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
            Triangle newTr = new Triangle(points[0], points[1], points[2], colors[0], colors[1], colors[2]);
            triangles.add(newTr);
        }
    }

    private void addToTriangles(PVector point, Triangle triangle) {
        if (!pointsToTriangles.containsKey(point))
            pointsToTriangles.put(point, new HashSet<>());
        pointsToTriangles.get(point).add(triangle);
    }

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
            tr.drawGrad(pApplet, new PVector(0, 0, 1), 3, 0.3, normMap);
//            tr.drawGradPart(pApplet, new PVector(0, 0, 1), 3, 0.3);
//            tr.drawFlat(pApplet, new PVector(0, 0, 1), 3, 0.3);
        }
    }

    public void recalc() {
        for (Triangle t : triangles) {
            addToTriangles(t.p1, t);
            addToTriangles(t.p2, t);
            addToTriangles(t.p3, t);
        }

        normMap = generateNormalsMap();
    }

    private HashMap<PVector, PVector> generateNormalsMap() {
        HashMap<PVector, PVector> normMap = new HashMap<>();
        for (PVector point : pointsToTriangles.keySet()) {
            normMap.put(point, Triangle.getTotalNormal(pointsToTriangles.get(point)));
        }

        return normMap;
    }

    public Face copy() {
        Face copyFace = new Face(this.shWeight, this.txWeight);
        for (Triangle t : this.triangles) {
            copyFace.triangles.add(t.copy());
        }
        copyFace.pointsToTriangles = pointsToTriangles;

        return copyFace;
    }
}
