import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    public static double[][] get2DDataDouble(String fileName) throws IOException {

        BufferedReader CSVFile = new BufferedReader(new FileReader(fileName));
        String dataRow = CSVFile.readLine();
        List<String> lines = new ArrayList<>();

        while (dataRow != null) {
            lines.add(dataRow);
            dataRow = CSVFile.readLine();
        }
        CSVFile.close();

        double[][] grid = new double[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = convertToDoubles(lines.get(i));
        }

        return grid;
    }

    public static int[][] get2DDataInteger(String fileName) throws IOException {
        BufferedReader CSVFile = new BufferedReader(new FileReader(fileName));
        String dataRow = CSVFile.readLine();
        List<String> lines = new ArrayList<>();

        while (dataRow != null) {
            lines.add(dataRow);
            dataRow = CSVFile.readLine();
        }
        CSVFile.close();

        int[][] grid = new int[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = convertToIntegers(lines.get(i));
        }

        return grid;
    }


    public static void printGrid(double[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static double[] convertToDoubles(String line) {
        String[] cols = line.split(",");
        double[] vals = new double[cols.length];
        for (int i = 0; i < cols.length; i++) {
            try {
                vals[i] = Double.parseDouble(cols[i]);
            } catch (NumberFormatException e) {
                System.out.println("Non-double value " + cols[i] + " in column " + i + ". 0 put instead.");
                vals[i] = 0;
            }
        }

        return vals;
    }

    private static int[] convertToIntegers(String line) {
        String[] cols = line.split(",");
        int[] vals = new int[cols.length];
        for (int i = 0; i < cols.length; i++) {
            try {
                vals[i] = Integer.parseInt(cols[i]);
            } catch (NumberFormatException e) {
                System.out.println("Non-integer value " + cols[i] + " in column " + i + ". 0 put instead.");
                vals[i] = 0;
            }
        }

        return vals;
    }
}
