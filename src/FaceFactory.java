import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TransferQueue;

public class FaceFactory {
    private static final double SH_MULT = 3;
    private static final double TX_MULT = 5;

    private Face avgFace;
    private int[][] mesh;
    private double[] shEV;
    private double[] txEV;
    private List<Face> faces;

    public FaceFactory(String meshFile, String avgShadeFile, String avgTextFile, String shEVFile, String txEVFile) throws IOException {
        this.shEV = get1DArray(CSVReader.get2DDataDouble(shEVFile));
        this.txEV = get1DArray(CSVReader.get2DDataDouble(txEVFile));
        this.mesh = CSVReader.get2DDataInteger(meshFile);
        this.faces = new ArrayList<>();
        this.avgFace = new Face(mesh, avgShadeFile, avgTextFile);
    }

    public double[] get1DArray(double[][] ev) {
        double[] array = new double[ev.length];
        for (int i = 0; i < ev.length; i++) {
            array[i] = ev[i][0];
        }

        return array;
    }

    public void addFace(String shFile, String txFile) throws IOException {
        double shWeight = getFileWeight(shFile, shEV) * SH_MULT;
        double txWeight = getFileWeight(txFile, txEV) * TX_MULT;

        this.faces.add(new Face(mesh, shFile, txFile, shWeight, txWeight));
    }

    public Face createSyntheticFace(Weightable shWeights, Weightable txWeights) {
        Face syntFace = avgFace.copy();
        syntFace.add(faces.get(0), shWeights.w1, txWeights.w1);
        syntFace.add(faces.get(1), shWeights.w2, txWeights.w2);
        syntFace.add(faces.get(2), shWeights.w3, txWeights.w3);
        syntFace.getTriangles().sort(null);

        return syntFace;
    }

    private double getFileWeight(String file, double[] ev) {
        String indexText = file.split("_")[1].split(".csv")[0];
        int index = Integer.parseInt(indexText);
        return ev[index - 1];
    }
}
