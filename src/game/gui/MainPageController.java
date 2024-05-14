package game.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import game.engine.Battle;
import game.engine.base.Wall;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.WeaponFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainPageController {
	
	private Scene game;	
	private Battle battle;
	public VBox dangerLevel;
	public HBox turns; 
	public HBox HUD;
	public BorderPane layout;
	ObservableList<HBox> lanes;
	public MainPageController() throws Exception{
		
	}
	
	public MainPageController(int numOfLanes, int resources, double width , double height) throws Exception{
		this.battle = new Battle(1, 0, 220, numOfLanes, resources);
	    layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		ObservableList<VBox> weapons = weaponShopBuilder();
		HBox lowBar = (HBox) layout.lookup("#turns");
		ListView<VBox> weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		weaponShop.setItems(weapons);
		ListView<HBox> lanes = (ListView<HBox>) layout.lookup("#lanes");
		ObservableList<HBox> lane = lanesBuilder(battle);
		ObservableList <ImageView> approachingTitans =  updateApproachingTitans();
		HBox titanImages = (HBox)layout.lookup("#titanImages");
		titanImages.getChildren().addAll(updateApproachingTitans());
		titanImages.setSpacing(10);
		addGrass(numOfLanes);
		updateTitansInLanes();
		lanes.setItems(lane);
		game = new Scene(layout ,width , height);	
	}
	
	private ObservableList<VBox> weaponShopBuilder() throws IOException {
		HashMap<Integer,WeaponRegistry> shop = this.battle.getWeaponFactory().getWeaponShop();
		ObservableList<VBox> weapons = FXCollections.observableArrayList();
		for(int i=1;i <= shop.size();i++) {
			VBox temp = new VBox();
			WeaponRegistry weapon = shop.get(i);
			ImageView imageView= null;
			String range;
			switch(i) {
				case 1: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"PiercingCannon.png")));
						range = "Attacks the closest 5 titans";break;
				case 2: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"SniperCannon.png")));
						range = "Attacks the closest titan";break;
				case 3: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"VolleySpreadCannon.png")));
						range = "Attacks any titan within "+weapon.getMinRange()+" to "+weapon.getMaxRange()+" meters";break;
				case 4: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"WallTrap.png")));
						range = "Attacks the closest titan only if it has reached the wall";break;
				default:imageView = null;
						range = "Error while getting the range";
			}
	        imageView.setFitWidth(100);
	        imageView.setFitHeight(100);
			Text name = new Text(weapon.getName());
			Text price = new Text("Price: " + weapon.getPrice());
			temp.getChildren().addAll(imageView,name,price);
			Tooltip tooltip = new Tooltip("Damage: "+weapon.getDamage()+"\nRange: "+range);
	        Tooltip.install(temp, tooltip);
			temp.setAlignment(Pos.CENTER);
			temp.setId("weaponInShop");
			weapons.add(temp);
		}
		 return weapons;
	}
	private ObservableList<HBox> lanesBuilder(Battle battle) throws FileNotFoundException {
	    lanes = FXCollections.observableArrayList();
		ArrayList<Lane> originalLanes = battle.getOriginalLanes();
		HashMap<Integer,WeaponRegistry> shop = this.battle.getWeaponFactory().getWeaponShop();
		for(int i = 0;i<originalLanes.size();i++) {
			HBox wall = new HBox();
			GridPane weapons = new GridPane();
			for(int j=1;j <= shop.size();j++) {
				VBox temp = new VBox();
				WeaponRegistry weapon = shop.get(i);
				String fileName;
				String range;
				switch(j) {
					case 1: fileName = "PiercingCannon";break;
					case 2: fileName = "SniperCannon";;break;
					case 3: fileName = "VolleySpreadCannon";break;
					case 4: fileName = "WallTrap";break;
					default:fileName = "test";
				}
				ImageView image = new ImageView(new Image(new FileInputStream("images"+File.separator+fileName+".png")));
		        Label amount = new Label("X0");
		        amount.setAlignment(Pos.BASELINE_RIGHT);
		        image.setFitWidth(20);
		        image.setFitHeight(20);
		        temp.getChildren().addAll(image,amount);
		        weapons.add(temp, 0, j-1);
			}
			VBox Wall = new VBox();
			ImageView image = new ImageView(new Image(new FileInputStream("images"+File.separator+"Wall.png")));
			image.setFitWidth(30);
	        image.setFitHeight(100);
			GridPane healthBar = new GridPane();
	        int numCols = 10;
	        for (int j = 0; j < numCols; j++) {
	            Rectangle rect = new Rectangle(10, 10);
	            rect.setFill(Color.GREEN);
	            healthBar.add(rect, j, 0); // column , row
	        }
	        Wall.getChildren().addAll(image,healthBar);
	        GridPane lane = new GridPane();
	        wall.getChildren().addAll(Wall,weapons,lane);
	        lanes.add(wall);
			}
		return lanes;
		
	}
	public Scene getGame() {
		return game;
	}
	public ObservableList <ImageView> updateApproachingTitans () throws FileNotFoundException {
		battle.refillApproachingTitans();
		ArrayList<Titan> approachingTitans = battle.getApproachingTitans();
		ObservableList<ImageView> updatedTitansQueue = FXCollections.observableArrayList();
		int turnTitansNum= battle.getNumberOfTitansPerTurn();
		ImageView currentImage;
		Tooltip titanName;
		for(int i=0; i<approachingTitans.size() && i<10; i++) {
		   int currentDangerLevel= approachingTitans.get(i).getDangerLevel();
		   
		   switch(currentDangerLevel) {
		       case 1:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"PureTitan.png")));break;
		       case 2:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"AbnormalTitan.png")));break;
		       case 3:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"ArmoredTitan.png")));break;
		       case 4:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"ColossalTitan.png")));break;
		       default: currentImage = null;break;
		   }
		
		switch(currentDangerLevel) {
		   case 1:  titanName  = new Tooltip("Pure Titan");break;
	       case 2:  titanName  = new Tooltip("Abnormal Titan");break;
	       case 3:  titanName  = new Tooltip("Armored Titan");break;
	       case 4:  titanName = new Tooltip("Colossal Titan");break;
	       default: titanName = null;break;
		}
		currentImage.setFitHeight(40);
		currentImage.setFitWidth(40);
		Tooltip.install(currentImage, titanName);
		updatedTitansQueue.add(currentImage);
		}
		return updatedTitansQueue;
	}
	public void addGrass(int numLanes) throws FileNotFoundException {
		for (int i = 0; i < numLanes; i++) {//passes on every lane
			HBox temp = lanes.get(i);
			GridPane currentLane = (GridPane) temp.getChildren().get(2);
		  for (int j = 0; j < 29; j++) {// adds each cell
			 // System.out.println(j);// enters all
			  ImageView grass = new ImageView(new Image(new FileInputStream("images"+File.separator+"grass.jpg")));
			  StackPane cell = new StackPane();
			  cell.prefHeight(150);
			  cell.setPrefWidth(35);
			  grass.setFitHeight(150);
			  grass.setFitWidth(35);
			  cell.getChildren().add(grass);
			  currentLane.add(cell, j, i);
	        }
		  }
		 //System.out.print(((GridPane)lanes.get(2).getChildren().get(2)).getChildren().get(26)); the stackpane is there but no grass
		}
	public void updateTitansInLanes () throws FileNotFoundException {
		//created a lane with titans
	    Lane trial = new Lane (new Wall(200));
		PriorityQueue <Lane> allLanes = new PriorityQueue<Lane>();
		trial.addTitan(new PureTitan(1,2,3,59,10,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,58,5,6,7));
		allLanes.add(trial);
	
	   //  PriorityQueue <Lane> allLanes = battle.getLanes(); the right way or origianl lanes to maintain order
		Queue <Lane> temp = new LinkedList<Lane>();
		int size1 = allLanes.size();
	    for(int i =0; i < size1 ;i++) {
		  Lane currLane = allLanes.remove();// take one lane
		  temp.add(currLane);
		  PriorityQueue<Titan> currTitans = currLane.getTitans();//take the titans in this lane
		  Queue <Titan> temp2 = new LinkedList<Titan>();
		  ImageView currentImage = null;
		  ObservableList< Node> cellsInLane = ((GridPane)lanes.get(i).getChildren().get(2)).getChildren();// typecast into stackpane
		  int size = cellsInLane.size();
		  for(int j = 0; j < size ;j++) {
			  ImageView grassTemp = (ImageView)((StackPane)cellsInLane.get(j)).getChildren().remove(0);
			  ((StackPane)cellsInLane.get(j)).getChildren().clear();
			  VBox left = new VBox();
			  VBox right = new VBox();
			  StackPane curr = ((StackPane)cellsInLane.get(j));
			  curr.getChildren().addAll(grassTemp, left, right); 
			  StackPane.setAlignment(right, Pos.CENTER_RIGHT);
			  StackPane.setAlignment(left, Pos.CENTER_LEFT);
			  //left.setMaxWidth(curr.getWidth()/2);
			 // right.setMaxWidth(curr.getWidth()/2);
			 // ((StackPane)cellsInLane.get(j)).getChildren().add(new Pane ());  
		  }
		  while(!currTitans.isEmpty()) { // each titan in lane
			  	Titan currTitan = currTitans.remove();
			  	temp2.add(currTitan);
			  	int distance = currTitan.getDistance();
			  	int col = distance/2 - 1;
			  	
			  if(currTitan instanceof PureTitan) 
			      currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"PureTitan.png")));
			  else if (currTitan instanceof AbnormalTitan)
				  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"AbnormalTitan.png")));
			  else if (currTitan instanceof ArmoredTitan)
				  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"ArmoredTitan.png")));
			  else if (currTitan instanceof ColossalTitan)
	              currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"ColossalTitan.png")));
			  
			  currentImage.setFitWidth(25);
			  currentImage.setFitHeight(25);
			  StackPane currCell = (StackPane) cellsInLane.get(col);
			  int parity = distance%2;
			  VBox willAdd =  parity == 0? (VBox)currCell.getChildren().get(1):(VBox)currCell.getChildren().get(2);
			  
		      //((Pane)currCell.getChildren().get(1)).getChildren().add(currentImage);
		 
		      //StackPane.setMargin(button1, new javafx.geometry.Insets(20, 20, 20, 20));
		      willAdd.getChildren().add(currentImage);
		  while(!temp2.isEmpty()) currTitans.add(temp2.remove());
		}
	     while (!temp.isEmpty()) allLanes.add(temp.remove());   
	}
  }
}
	/*public void movingTitansF (int numLanes) {
		for (int i = 0; i < numLanes; i++) {//passes on every lane
			HBox temp = lanes.get(i);
			GridPane currentLane = (GridPane) temp.getChildren().get(2);//got one lane
			for (int j = 0; j < 9; j++) {
				ObservableList<Node> cells = currentLane.getChildren(); // typecast to stack pane
		        StackPane nextCell = (StackPane)cells.get(j+1);
		        StackPane currCell = (StackPane)cells.get(j);
		        if(nextCell.getChildren().size() == 2) {
		        	ImageView willAdd  = (ImageView)nextCell.getChildren().get(1);// will be a vbox
		        	if(j==0) 
		        		currCell.getChildren().add(willAdd);	        	
		        	else {
		        		if(currCell.getChildren().size()==2)
		        	    currCell.getChildren().remove(1);
		        	    currCell.getChildren().add(willAdd);
	        	}
	         }
		        else {
		        	if(currCell.getChildren().size()==2)
		        	    currCell.getChildren().remove(1);
		        }
		        if(j==8) {
		        	if(nextCell.getChildren().size()==2)
		        	    nextCell.getChildren().remove(1);
		        }
	      }
	   }
    }
	public void spawnTitans() throws FileNotFoundException {//for now spawns one titan will just use the numberoftitans per turn to loop and have 
		//an observable list if titans and add all of them instead of one
		// use danger levels on right to know which lane we will spawn to 
		// take one from approaching titans and add it 
		Titan willAdd = battle.getApproachingTitans().remove(0);//should remove from the observable list mariam made since
		//the one we wanna add is already removed backend wise 
		int leastDangerIndex = 0; // will be extracted 
		HBox temp = lanes.get(leastDangerIndex);
		GridPane leastDangerLane = (GridPane) temp.getChildren().get(2);
		StackPane lastCell = (StackPane) leastDangerLane.getChildren().get(9);
		ImageView titan = null;
		 if(willAdd instanceof PureTitan)
			   titan = new ImageView(new Image(new FileInputStream("images"+File.separator+"PureTitan.png")));
			  else if (willAdd instanceof AbnormalTitan)
				  titan = new ImageView(new Image(new FileInputStream("images"+File.separator+"AbnormalTitan.png")));
			  else if (willAdd instanceof ArmoredTitan)
				 titan = new ImageView(new Image(new FileInputStream("images"+File.separator+"ArmoredTitan.png")));
			  else if (willAdd instanceof ColossalTitan)
	           titan = new ImageView(new Image(new FileInputStream("images"+File.separator+"ColossalTitan.png")));
		 titan.setFitWidth(20);
		 titan.setFitHeight(20);
		lastCell.getChildren().add(titan);	
	}*/

	
