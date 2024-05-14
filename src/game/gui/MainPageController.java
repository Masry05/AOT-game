package game.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
	private BorderPane layout;
	private ObservableList<HBox> lanes;
	private int numOfLanes;
	private ProgressBar turnBar;
	public MainPageController() throws Exception{
		
	}

	public MainPageController(int numOfLanes, int resources, double width , double height) throws Exception{
		this.numOfLanes = numOfLanes;
		this.battle = new Battle(1, 0, 10, numOfLanes, resources);
		layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		game = new Scene(layout ,width , height);	
		game.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
		ObservableList<VBox> weapons = weaponShopBuilder();
		ListView<VBox> weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		weaponShop.setItems(weapons);
		ListView<HBox> lanes = (ListView<HBox>) layout.lookup("#lanes");
		ObservableList<HBox> lane = lanesBuilder();
		ObservableList <ImageView> approachingTitans =  updateApproachingTitans();
		HBox titanImages = (HBox)layout.lookup("#titanImages");
		titanImages.getChildren().addAll(updateApproachingTitans());
		titanImages.setSpacing(10);
		lanes.setItems(lane);
		this.turnBar = (ProgressBar) layout.lookup("#turnBar");
		turnBar.setStyle("-fx-background-color: transparent;");
		turnBar.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		updateResources();
		updateScore();
		updateTurns();
		addGrass();
		updateTitansInLanes();
		
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
	        imageView.setFitWidth(130);
	        imageView.setFitHeight(130);
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
	private ObservableList<HBox> lanesBuilder() throws FileNotFoundException {
		lanes = FXCollections.observableArrayList();
		ArrayList<Lane> originalLanes = battle.getOriginalLanes();
		HashMap<Integer,WeaponRegistry> shop = this.battle.getWeaponFactory().getWeaponShop();
		int heightPerLane = 0;
		int imageSize = 0;
		int wallLength = 0;
		if(battle.getOriginalLanes().size()==3) {
			heightPerLane = (int) (700/3);
			imageSize = (int)(heightPerLane/4)-22;
			wallLength = heightPerLane-14;
		}
		else {
			heightPerLane = (int) (700/5);
			imageSize = (int)(heightPerLane/4)-22;
			wallLength = heightPerLane-22;
		}
		
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
		        image.setFitWidth(imageSize);
		        image.setFitHeight(imageSize);
		        temp.getChildren().addAll(image,amount);
		        Insets margin = new Insets(0, 0, 10, 5);
		        VBox.setMargin(wall, margin);
		        weapons.add(temp, 0, j-1);
			}
			VBox wallWeaponsHealth = new VBox();
			Rectangle wallShape = new Rectangle(50,wallLength);
			wallShape.setFill(Color.GREY);
			//ImageView image = new ImageView(new Image(new FileInputStream("images"+File.separator+"Wall.png")));
			GridPane healthBar = new GridPane();
	        int numCols = 10;
	        for (int j = 0; j < numCols; j++) {
	            Rectangle rect = new Rectangle(9 ,5);
	            rect.setFill(Color.GREEN);
	            healthBar.add(rect, j, 0);
	        }
	        HBox wallWeapons = new HBox();
	        wallWeapons.setSpacing(5);
	        wallWeapons.getChildren().addAll(wallShape,weapons);
	        wallWeaponsHealth.getChildren().addAll(wallWeapons,healthBar);
	        GridPane lane = new GridPane();
	        wall.getChildren().addAll(wallWeaponsHealth,lane);
	        wall.setSpacing(5);
	        Insets margin = new Insets(5, 0, 0, 0); // top, right, bottom, left
	        VBox.setMargin(healthBar, margin);
	        //Insets margin3 = new Insets(0, 0, 5, 0);
	        //VBox.setMargin(wall, margin3);
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
	
	public void updateTurns() {
		int numOfTurns = battle.getNumberOfTurns();
		if(numOfTurns<15) {
			Circle circle = (Circle)layout.lookup("#turnCircle");
			circle.setFill(Color.GREEN);
			turnBar.setStyle("-fx-accent: green;");
		}
		else if(numOfTurns<30) {
			Circle circle = (Circle)layout.lookup("#turnCircle");
			circle.setFill(Color.YELLOW);
			turnBar.setStyle("-fx-accent: yellow;");
		}
		else if(numOfTurns<35) {
			Circle circle = (Circle)layout.lookup("#turnCircle");
			circle.setFill(Color.ORANGE);
			turnBar.setStyle("-fx-accent: orange;");
		}
		else {
			Circle circle = (Circle)layout.lookup("#turnCircle");
			circle.setFill(Color.DARKRED);
			turnBar.setStyle("-fx-accent: darkred;");
		}
		Text turns = (Text)layout.lookup("#turnsText");
		turns.setText(numOfTurns+"");
		 // Change color to green
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                for (int i = 0; i <= 100; i++) {
                    double progress = (double) numOfTurns / 35; // get turns
                    updateProgress(progress, 1.0); // Update progress
                }
                return null;
            }
        };
		turnBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
	}
	public void updateResources() {
		Label resources = (Label) layout.lookup("#resourcesNum");
		resources.setText(" : "+battle.getResourcesGathered());
	}
	public void updateScore() {
		Label score = (Label) layout.lookup("#scoreNum");
		score.setText(" : "+battle.getScore());
	}
	public void addGrass() throws FileNotFoundException {
		for (int i = 0; i < numOfLanes; i++) {//passes on every lane
			HBox temp = lanes.get(i);
			GridPane currentLane = (GridPane) temp.getChildren().get(1);
		  for (int j = 0; j < 29; j++) {// adds each cell
			  Rectangle tempp = new Rectangle();
			  StackPane cell = new StackPane();
			  cell.setPrefWidth(39);
			  tempp.setWidth(39);
			  /*cell.prefHeight(numOfLanes == 3?230:130);
			  tempp.prefHeight(numOfLanes == 3?230:130);
			   doesnt work */
			  if(numOfLanes == 3) {
			  cell.prefHeight(230);
			  tempp.setHeight(230);
			  }
			  else 
			  { cell.prefHeight(130);
			    tempp.setHeight(130);
			  }
			  tempp.setFill(Color.GREEN);// put transparent and fix css of stackpane or directly css of rectangle
			  cell.getChildren().add(tempp);
			  currentLane.add(cell, j, i);
	        }
		  }
		}
	public void updateTitansInLanes () throws FileNotFoundException {
		//created a lane with titans
		PriorityQueue <Lane> allLanes = new PriorityQueue<Lane>();
	    Lane trial = new Lane (new Wall(200));
		trial.addTitan(new PureTitan(1,2,3,59,10,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,58,5,6,7));
		trial.addTitan(new ColossalTitan(1,2,3,58,5,6,7));
		Lane trial2 = new Lane (new Wall(200));
		trial2.getLaneWall().setCurrentHealth(-5);
		trial2.addTitan(new PureTitan(1,2,3,59,10,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,59,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,58,5,6,7));
		trial2.addTitan(new ColossalTitan(1,2,3,58,5,6,7));
		allLanes.add(trial);
		allLanes.add(trial2);
	   //  PriorityQueue <Lane> allLanes = battle.originalLanes //what should actually happen
		Queue <Lane> temp = new LinkedList<Lane>();
		int size1 = allLanes.size();
	    for(int i = 0; i < size1 ;i++) {
		  Lane currLane = allLanes.remove();// take one lane
		  temp.add(currLane);
		  if(currLane.isLaneLost()) {
			  HBox fLane = (HBox)lanes.get(i);
			  fLane.getChildren().clear();
			  Rectangle tempp = new Rectangle();
			  StackPane lostLane = new StackPane();
			  lostLane.setPrefWidth(1230);
			  tempp.setWidth(1230);
			  if(numOfLanes == 3) {
			  lostLane.prefHeight(230);
			  tempp.setHeight(230);
			  }
			  else 
			  { lostLane.prefHeight(130);
			    tempp.setHeight(130);
			  }
			  tempp.setFill(Color.BROWN);
			  Label lost = new Label("This is  a Lost Lane");
			  lostLane.getChildren().addAll(tempp,lost);
			  fLane.getChildren().add(lostLane);
		  }
		  else {
		  PriorityQueue <Titan> currTitans = currLane.getTitans();//take the titans in this lane
		  Queue <Titan> temp2 = new LinkedList<Titan>();
		  ImageView currentImage = null;
		  ObservableList< Node> cellsInLane = ((GridPane)lanes.get(i).getChildren().get(1)).getChildren();// typecast into stackpane
		  int size = cellsInLane.size();
		  for(int j = 0; j < size ;j++) {
			  StackPane curr = ((StackPane)cellsInLane.get(j));
			  Rectangle Temp = (Rectangle)((StackPane)cellsInLane.get(j)).getChildren().remove(0);
			  curr.getChildren().clear();
			  VBox left = new VBox();
			  VBox right = new VBox();
			  curr.getChildren().addAll(Temp,left,right); 
			  StackPane.setAlignment(right, Pos.CENTER_RIGHT);
			  StackPane.setAlignment(left, Pos.CENTER_LEFT);
			  left.setMaxWidth(curr.getWidth()/2);
			  right.setMaxWidth(curr.getWidth()/2);
			  } 
		  while(!currTitans.isEmpty()) { // each titan in lane
			  	Titan currTitan = currTitans.remove();
			  	temp2.add(currTitan);
			  	int distance = currTitan.getDistance();
			  	int col = distance/2 - 1;
			  	int thealth = currTitan.getCurrentHealth();
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
			  VBox titanCont = new VBox();
			  Label health = new Label (""+ thealth);
			  health.setId("healthOfTitans");
			  titanCont.getChildren().addAll(currentImage, health);
		      willAdd.getChildren().add(titanCont);
		  }
		  while(!temp2.isEmpty()) currTitans.add(temp2.remove());
		  }
	 }
	    while (!temp.isEmpty()) allLanes.add(temp.remove());   
  }
}