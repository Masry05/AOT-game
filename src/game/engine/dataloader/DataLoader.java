package game.engine.dataloader;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import game.engine.titans.TitanRegistry;
import game.engine.weapons.WeaponRegistry;

public class DataLoader {
	
	private static final String TITANS_FILE_NAME="titans.csv";
	private static final String WEAPONS_FILE_NAME="weapons.csv";
	
	public static HashMap<Integer, TitanRegistry> readTitanRegistry() throws IOException{
		String line;
	    HashMap<Integer,TitanRegistry> titans = new HashMap();
		try (BufferedReader br = new BufferedReader(new FileReader(TITANS_FILE_NAME))) {
		    while ((line = br.readLine()) != null) {
		    	String [] temp=line.split(",");
		    	TitanRegistry titan = new TitanRegistry(Integer.parseInt(temp[0])
		    		   ,Integer.parseInt(temp[1]),Integer.parseInt(temp[2])
		    		   ,Integer.parseInt(temp[3]),Integer.parseInt(temp[4])
		    		   ,Integer.parseInt(temp[5]),Integer.parseInt(temp[6]));
		        titans.put(Integer.parseInt(temp[0]), titan);
		        }
		    }
		 return titans;
	}
	public static HashMap<Integer, WeaponRegistry> readWeaponRegistry() throws IOException{
		String line;
	    HashMap<Integer,WeaponRegistry> weapons = new HashMap();
		try (BufferedReader br = new BufferedReader(new FileReader(WEAPONS_FILE_NAME))) {
		    while ((line = br.readLine()) != null) {
		    	String [] temp=line.split(",");
		    	WeaponRegistry weapon=null;
		    	switch(temp.length) {
		    	
		    	case 2: weapon = new WeaponRegistry(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));break;
			        
		    	case 4: weapon = new WeaponRegistry(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])
		    			,Integer.parseInt(temp[2]),temp[3]);break;
		    	case 6: weapon = new WeaponRegistry(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])
		    			,Integer.parseInt(temp[2]),temp[3],Integer.parseInt(temp[4]),Integer.parseInt(temp[5]));break;
			    }
		    	weapons.put(Integer.parseInt(temp[0]), weapon);
		    }
		}
		return weapons;
	}
}