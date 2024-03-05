 package game.engine.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {
	//accessed at class level ?? static??
	
	private static final String TITANS_FILE_NAME="titans.csv";
	private static final String WEAPONS_FILE_NAME="weapons.csv";

	public DataLoader() {
		
	}

	public String getTITANS_FILE_NAME() {
		return TITANS_FILE_NAME;
	}

	public String getWEAPONS_FILE_NAME() {
		return WEAPONS_FILE_NAME;
	}


    public static String[][] readCSV(String csvFilePath) throws IOException {
	    String line;
	    String[][]full=new String[4][];
	   
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
	        int i=0;
		    while ((line = br.readLine()) != null) {
		        // Split the line by commas
		        full[i]= line.split(",");
		        i++;
		        }
		    }
		return full;
		}
	
	public static void main(String[] args) throws IOException {
	    String csvFile = "/Users/salmatarek/Documents/GitHub/AOT/titans.csv";
		//String csvFile = ""
	    //how is the path universal
	    String [][] titans = readCSV(csvFile);
	    for(int i=0;i<titans.length;i++) {
	    	for(int j=0;j<titans[i].length;j++)
	    		System.out.print(titans[i][j]+" ");
	    	System.out.println();
	    }
	}
}