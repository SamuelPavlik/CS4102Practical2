import java.io.IOException;

public class FaceFactory {
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
            Face face = new Face(avgFace, mesh, shFile, txFile, shEV, txEV);
            return new Face(avgFace, mesh, shFile, txFile, shEV, txEV);
    }
}
