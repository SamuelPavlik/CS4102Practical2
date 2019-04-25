import java.io.IOException;

public class FaceData {
    private static final double SH_MULT = 3;
    private static final double TX_MULT = 5;

    public double shWeight;
    public double txWeight;
    public double[][] shGrid;
    public double[][] txGrid;

    public FaceData(String shFile, String txFile, double[] shEV, double[] txEV) throws IOException {
        shWeight = getFileWeight(shFile, shEV) * SH_MULT;
        txWeight = getFileWeight(txFile, txEV) * TX_MULT;
        shGrid = CSVReader.get2DDataDouble(shFile);
        txGrid = CSVReader.get2DDataDouble(txFile);
    }

    private double getFileWeight(String file, double[] ev) {
        String indexText = file.split("_")[1].split(".csv")[0];
        int index = Integer.parseInt(indexText);
        return ev[index - 1];
    }

}
