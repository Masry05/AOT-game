import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public static void main(String[] args) {
        String csvFile = "D:/Projects/AOT/titans.csv";
        String[] csvData = new String[0];
        try {
            csvData = readCSV(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<csvData.length;i++)
        	System.out.println(csvData[i]);
        //System.out.println(csvData[0]);
    }

    public static String[] readCSV(String csvFilePath) throws IOException {
        String line;
        String cvsSplitBy = ",";
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                // Split the line by commas
                String[] data = line.split(cvsSplitBy);

                // Append each column of the CSV file to a StringBuilder
                for (String column : data) {
                    stringBuilder.append(column).append(",");
                }
                stringBuilder.append("\n"); // Move to the next line after appending each row
            }
        }

        // Convert the StringBuilder content to a string array
        String[] csvRows = stringBuilder.toString().split("\n");
        return csvRows;
    }
}
