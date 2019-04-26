import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory to generate Face objects
 */
public class FaceFactory {
    private static final double SH_MULT = 3;
    private static final double TX_MULT = 7;

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

    /**
     * Flatten a 2D array of doubles
     * @param ev 2D array of doubles
     * @return 1D array of doubles
     */
    public double[] get1DArray(double[][] ev) {
        double[] array = new double[ev.length];
        for (int i = 0; i < ev.length; i++) {
            array[i] = ev[i][0];
        }

        return array;
    }

    /**
     * Add Face object to list of all face objects
     * @param shFile sh file of face to add
     * @param txFile tx file of face to add
     * @throws IOException
     */
    public void addFace(String shFile, String txFile) throws IOException {
        double shWeight = getFileWeight(shFile, shEV) * SH_MULT;
        double txWeight = getFileWeight(txFile, txEV) * TX_MULT;

        this.faces.add(new Face(mesh, shFile, txFile, shWeight, txWeight));
    }

    /**
     * Generated synthetic face as a result of faces in the factory's list and provided weights
     * @param shWeights shader weights for each of faces
     * @param txWeights color weights for each of faces
     * @return generated synthetic face
     */
    public Face createSyntheticFace(Weightable shWeights, Weightable txWeights) {
        Face syntFace = avgFace.copy();
        syntFace.add(faces.get(0), shWeights.w1, txWeights.w1);
        syntFace.add(faces.get(1), shWeights.w2, txWeights.w2);
        syntFace.add(faces.get(2), shWeights.w3, txWeights.w3);
        syntFace.getTriangles().sort(null);

        return syntFace;
    }

    /**
     * @param file file path of face shaders or textures
     * @param ev 1D array of weights for each face in the given directory
     * @return weight of this face's shaders or colors
     */
    private double getFileWeight(String file, double[] ev) {
        String indexText = file.split("_")[1].split(".csv")[0];
        int index = Integer.parseInt(indexText);
        return ev[index - 1];
    }

    /**
     * @return list of all Face objects
     */
    public List<Face> getFaces() {
        return faces;
    }
}
