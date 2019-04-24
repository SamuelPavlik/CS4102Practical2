import java.io.IOException;

public class FaceFactory {
    private static final double SH_MULT = 3;
    private static final double TX_MULT = 5;

    private Face avgFace;
    private int[][] mesh;
    private double[] shEV;
    private double[] txEV;

    public FaceFactory(String meshFile, String avgShadeFile, String avgTextFile, String shEVFile, String txEVFile) throws IOException {
        this.shEV = get1DArray(CSVReader.get2DDataDouble(shEVFile));
        this.txEV = get1DArray(CSVReader.get2DDataDouble(txEVFile));
        this.mesh = CSVReader.get2DDataInteger(meshFile);
        this.avgFace = new Face(mesh, avgShadeFile, avgTextFile);
    }

    public double[] get1DArray(double[][] ev) {
        double[] array = new double[ev.length];
        for (int i = 0; i < ev.length; i++) {
            array[i] = ev[i][0];
        }

        return array;
    }

    public Face createFace(String shFile, String txFile) throws IOException {
        double shWeight = getFileWeight(shFile, shEV) * 3;
        double txWeight = getFileWeight(txFile, txEV) * 5;
        Face face = new Face(avgFace, mesh, shFile, txFile, shWeight, txWeight);
        return new Face(avgFace, mesh, shFile, txFile, shWeight, txWeight);
    }

//    public Face createSyntheticFace(String shFile1, String txFile1, String shFile2, String txFile2, String shFile3,
//                                    String txFile3) throws IOException {
//        double shWeight = getFileWeight(shFile1, shEV) * SH_MULT;
//        double txWeight = getFileWeight(txFile2, txEV) * TX_MULT;
//        Face face = new Face(avgFace, mesh, shFile1, txFile1, shWeight, txWeight);
//    }

    private double getFileWeight(String file, double[] ev) {
        String indexText = file.split("_")[1].split(".csv")[0];
        int index = Integer.parseInt(indexText);
        return ev[index - 1];
    }
}
