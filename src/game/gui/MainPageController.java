
package game.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import game.engine.Battle;
import game.engine.base.Wall;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.PiercingCannon;
import game.engine.weapons.SniperCannon;
import game.engine.weapons.VolleySpreadCannon;
import game.engine.weapons.WallTrap;
import game.engine.weapons.Weapon;
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
	private int numOfLanes;
	private ProgressBar turnBar;
	private StackPane popup;
	private Lane chosenLane;
    private Weapon choseWeapon;
	private ListView<HBox> lanesList;
	private Lane selectedLane;
	private ListView<VBox> weaponShop;
	private ObservableList<HBox> lanes;
	private HBox titanImages;
    public HBox turns;
    public VBox dangerLevel;
    public Label dangerLabel;

    
	public MainPageController() throws Exception{
		
	}

	public MainPageController(int numOfLanes, int resources, double width , double height) throws Exception{
		this.numOfLanes = numOfLanes;
		this.battle = new Battle(1, 0, 59, numOfLanes, resources);
		layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		game = new Scene(layout ,width , height);	
		game.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
		weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		ObservableList<VBox> weapons = weaponShopBuilder();
		weaponShop.setItems(weapons);
		lanesList = (ListView<HBox>) layout.lookup("#lanes");
		lanesBuilder();
		lanesList.setItems(lanes);
		titanImages = (HBox)layout.lookup("#titanImages");
		turnBar = (ProgressBar) layout.lookup("#turnBar");
		turnBar.setStyle("-fx-background-color: transparent;");
		turnBar.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		turns = (HBox) layout.lookup("#turns");
		Region beforeBar= (Region) turns.getChildren().remove(0);
		StackPane progressBar= (StackPane) turns.getChildren().remove(0);
		updateApproachingTitans();
        Button purchaseWeapon = new Button("Purchase Weapon");
        purchaseWeapon.setId("purchaseOrPass");
        purchaseWeapon.setPrefHeight(25);
        purchaseWeapon.setPrefWidth(200);
        purchaseWeapon.setOnAction(e -> setLane());

        Button passTurn = new Button("Pass Turn");
        passTurn.setId("purchaseOrPass");
        passTurn.setPrefHeight(25);
        passTurn.setPrefWidth(200);
        turns.getChildren().addAll(beforeBar, purchaseWeapon, passTurn, progressBar);

        turns.setMargin(beforeBar, new Insets(5, 10, 5, 10));
        turns.setMargin(purchaseWeapon, new Insets(5, 10, 5, 10));
        turns.setMargin(passTurn, new Insets(5, 10, 5, 10));
        
        passTurn.setOnAction(e1-> {
	    		battle.passTurn();
	    		try {
	    			update();
	        		// give player 3 seconds to see changes
	    		}
	    		catch(Exception e2) {
	    			e2.printStackTrace();
	    		}
		});
		setCells();
		update();
		//give the player 3 seconds before showing the popup
	}
	
	private ObservableList<VBox> weaponShopBuilder() throws IOException {
		HashMap<Integer,WeaponRegistry> shop = this.battle.getWeaponFactory().getWeaponShop();
		ObservableList<VBox> weapons = FXCollections.observableArrayList();
		for(int i=1;i <= shop.size();i++) {
			VBox temp = new VBox();
			WeaponRegistry weapon = shop.get(i);
			ImageView imageView= null;
			String range;
			String typeS;
			switch(i) {
				case 1: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"PiercingCannon.png")));
						range = "Attacks the closest 5 titans";
						typeS="Piercing Cannon";break;
				case 2: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"SniperCannon.png")));
						range = "Attacks the closest titan";
						typeS="Sniper Cannon";break;
				case 3: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"VolleySpreadCannon.png")));
						range = "Attacks any titan within "+weapon.getMinRange()+" to "+weapon.getMaxRange()+" meters";
						typeS="Volley Spread Cannon";break;
				case 4: imageView = new ImageView(new Image (new FileInputStream("images"+File.separator+"WallTrap.png")));
						range = "Attacks the closest titan only if it has reached the wall";
						typeS="Wall Trap";break;
				default:imageView = null;
						range = "Error while getting the range";
						typeS = "Error while getting the type";
			}
	        imageView.setFitWidth(100);
	        imageView.setFitHeight(100);
			Text name = new Text(weapon.getName());
			Text price = new Text("Price: " + weapon.getPrice());
			Text damagePoints= new Text("Damage: " + weapon.getDamage());
			Text type= new Text("Type: " + typeS);
			temp.getChildren().addAll(imageView,name,price,damagePoints,type);
			Tooltip tooltip = new Tooltip("Range: "+ range);
	        Tooltip.install(temp, tooltip);
			temp.setAlignment(Pos.CENTER);
			temp.setId("weaponInShop");
			weapons.add(temp);
		}
		 return weapons;
	}
	private void lanesBuilder() throws FileNotFoundException {
		lanes = FXCollections.observableArrayList();
		ArrayList<Lane> originalLanes = battle.getOriginalLanes();
		HashMap<Integer,WeaponRegistry> shop = this.battle.getWeaponFactory().getWeaponShop();
		dangerLevel = (VBox) layout.lookup("#dangerLevel");
		int heightPerLane = 0;
		int imageSize = 0;
		int wallLength = 0;
		int wallrecLength = 0;
		if(battle.getOriginalLanes().size()==3) {
			heightPerLane = (int) (700/3);
			imageSize = (int)(heightPerLane/4)-22;
			wallLength = heightPerLane-14;
			wallrecLength = wallLength - 7;
			
			
			//dangerLevel.getChildren().addAll(lane1, lane2, lane3);
			//dangerLevel.setAlignment(Pos.TOP_CENTER);
		}
		else {
			heightPerLane = (int) (700/5);
			imageSize = (int)(heightPerLane/4)-22;
			wallLength = heightPerLane-22;
			wallrecLength = wallLength - 10;
			//dangerLevel.getChildren().addAll(lane1, lane2, lane3,lane4,lane5);
			//dangerLevel.setAlignment(Pos.TOP_CENTER);
		}
	    for(int i=0; i<numOfLanes; i++) {
	        	VBox dangerEach= new VBox();
				dangerEach.setPrefHeight(heightPerLane);
				Text sumEach= new Text("0");
				dangerLabel= new Label("Danger Level: ");
				dangerLevel.getChildren().add(dangerEach);
				dangerEach.getChildren().addAll(dangerLabel,sumEach);
				dangerEach.setAlignment(Pos.CENTER);
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
					default:fileName = "test";break;
				}
				ImageView image = new ImageView(new Image(new FileInputStream("images"+File.separator+fileName+".png")));
		        Text amount = new Text("X0");
		        amount.setFill(Color.WHITE);
		        image.setFitWidth(imageSize);
		        image.setFitHeight(imageSize);
		        temp.setAlignment(Pos.BASELINE_RIGHT);
		        temp.getChildren().addAll(image,amount);
		        weapons.add(temp, 0, j-1);
			}
			VBox wallHealth = new VBox();
			StackPane Wall = new StackPane();
			Rectangle wallShape = new Rectangle(50,wallrecLength);
			wallShape.setFill(Color.GRAY);			
			Wall.getChildren().addAll(wallShape,weapons);
			Wall.setAlignment(Pos.CENTER);
			StackPane laneStack = new StackPane();
			GridPane lane = new GridPane();
	        laneStack.getChildren().add(lane);
			ProgressBar healthBar = new ProgressBar();
			int currentHealth = originalLanes.get(i).getLaneWall().getCurrentHealth();
	        healthBar.setProgress(currentHealth/10000);
	        healthBar.setPrefWidth(50);
	        healthBar.setMaxWidth(50);
	        healthBar.setPrefHeight(14);
	        healthBar.setMaxHeight(14);
	        healthBar.setStyle("-fx-accent: green;");
	        wallHealth.getChildren().addAll(Wall,healthBar);
			wall.getChildren().addAll(wallHealth,laneStack);
	        lanes.add(wall);
			}
	}
	public Scene getGame() {
		return game;
	}
	public ObservableList <ImageView> updateApproachingTitans () throws FileNotFoundException {
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
	public void setCells() throws FileNotFoundException {
		for (int i = 0; i < numOfLanes; i++) {//passes on every lane
			HBox temp = lanes.get(i);
			StackPane cont = (StackPane) temp.getChildren().get(1);
			GridPane currentLane = (GridPane) cont.getChildren().get(0);
		  for (int j = 0; j < 29; j++) {// adds each cell
			  StackPane cell = new StackPane();
			  cell.setPrefWidth(39);
			  cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			  if(numOfLanes == 3) 
			  cell.prefHeight(230);
			  else 
			  cell.prefHeight(130);
			  currentLane.add(cell, j, i);
	        }
		  }
		}
	
	public void updateTitansInLanes () throws FileNotFoundException {
		//created a lane with titans
		/*ArrayList <Lane> allLanes = new ArrayList<Lane>();
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
		allLanes.add(trial2);*/
	    ArrayList<Lane> allLanes = battle.getOriginalLanes(); //what should actually happen
		int size1 = allLanes.size();
	    for(int i = 0; i < size1 ;i++) {
		  Lane currLane = allLanes.get(i);// take one lane backend
		  if(currLane.isLaneLost()) {
			  HBox fLane = (HBox)lanes.get(i);
			  fLane.getChildren().clear();
			  StackPane lostLane = new StackPane();
			  lostLane.setPrefWidth(1230);
			  if(numOfLanes == 3) 
			  lostLane.prefHeight(230);
			  else 
			   lostLane.prefHeight(130);
			  /*Image soilImage = new Image(new FileInputStream("images"+File.separator+"grass.jpg")); // put desired image
			  BackgroundImage backgroundImage = new BackgroundImage(
		                soilImage,
		                BackgroundRepeat.NO_REPEAT,
		                BackgroundRepeat.NO_REPEAT,
		                BackgroundPosition.CENTER,
		                BackgroundSize.DEFAULT);
		                
		      lostLane.setBackground(new Background(backgroundImage));*/
			  Label lost = new Label("This is  a Lost Lane");
			  lostLane.getChildren().addAll(lost);
			  fLane.getChildren().add(lostLane);
		  }
		  else {
		  PriorityQueue <Titan> currTitans = currLane.getTitans();//take the titans in this lane backend
		  Queue <Titan> temp2 = new LinkedList<Titan>();
		  ImageView currentImage = null;
		  StackPane cont = ((StackPane)lanes.get(i).getChildren().get(1));
		  if(currLane.getLaneWall().getCurrentHealth()==4) { //75% 25%
			  //different healths set the background of the stack pane into more soily or more grassy 
		  }
		  ObservableList< Node> cellsInLane = ((GridPane)cont.getChildren().get(0)).getChildren();// typecast into stackpane
		  int size = cellsInLane.size();
		  for(int j = 0; j < size ;j++) {
			  StackPane curr = ((StackPane)cellsInLane.get(j));
			  curr.getChildren().clear();
			  VBox left = new VBox();
			  VBox right = new VBox();
			  curr.getChildren().addAll(left,right); 
			  StackPane.setAlignment(right, Pos.CENTER_RIGHT);
			  StackPane.setAlignment(left, Pos.CENTER_LEFT);
			  left.setMaxWidth(curr.getWidth()/2);
			  right.setMaxWidth(curr.getWidth()/2);
			  } 
		  while(!currTitans.isEmpty()) { // each titan in lane
			  	Titan currTitan = currTitans.remove();
			  	temp2.add(currTitan);
			  	int distance = currTitan.getDistance();
			  	int col = distance==0?0:distance/2 - 1;
			  	int thealth = currTitan.getCurrentHealth();
			  	//Text currDangerText= (Text) currDangerLane.getChildren().get(1);
			  	//int sumSoFar= Integer.parseInt(currDangerText.getText())+ currTitan.getDangerLevel();
			  	//currDangerText.setText(sumSoFar + " ");
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
			  VBox willAdd =  parity == 0? (VBox)currCell.getChildren().get(0):(VBox)currCell.getChildren().get(1);
			  VBox titanCont = new VBox();
			  Label health = new Label (""+ thealth);
			  health.setId("healthOfTitans");
			  titanCont.getChildren().addAll(currentImage, health);
		      willAdd.getChildren().add(titanCont);
		      
		  }
		  while(!temp2.isEmpty()) currTitans.add(temp2.remove());
		  }
		  
	    }	    
	}

	public void setLane() {
		int selectedIndex = lanesList.getSelectionModel().getSelectedIndex();
        selectedLane = battle.getOriginalLanes().get(selectedIndex);
        weaponShop.setOnMouseClicked(e1 -> purchaseWeapon());
	}
	
	/*public void updateWeaponsCounter(int laneIndex) {
		HBox currLaneWall = (HBox) lanes.get(laneIndex);
		
		VBox vBox = (VBox) weapons.getChildren().get(row * originalLanes.size() + col);
	
	}*/
	public void purchaseWeapon() {
		int weaponCode = weaponShop.getSelectionModel().getSelectedIndex()+1;
		try {
			battle.purchaseWeapon(weaponCode, selectedLane);
			selectedLane = null;
			lanesList.setOnMouseClicked(null);
            weaponShop.setOnMouseClicked(null);
            //updateWeaponsCounter(weaponCode -1);
            update();
            // give player 3 seconds to notice changes
		}
		catch(InvalidLaneException e) {
			Stage alertStage = new Stage();
			alertStage.setTitle("Invalid Lane");
		    Label label = new Label(e.getMessage());
		    label.setAlignment(Pos.CENTER);
		    Button closeButton = new Button("Exit");
		    closeButton.setOnAction(event -> alertStage.close());
		    BorderPane pane = new BorderPane();
		    pane.setTop(label);
		    pane.setCenter(closeButton);
		    Scene scene = new Scene(pane, 500, 100);
		    alertStage.setScene(scene);
		    alertStage.initModality(Modality.APPLICATION_MODAL);
		    alertStage.initStyle(StageStyle.UNDECORATED);
		    alertStage.show();
		}
		catch(InsufficientResourcesException e) {
			Stage alertStage = new Stage();
			alertStage.setTitle("Insufficient Resources");
		    Label label = new Label(e.getMessage());
		    label.setAlignment(Pos.CENTER);
		    Button closeButton = new Button("Exit");
		    closeButton.setOnAction(event -> alertStage.close());
		    BorderPane pane = new BorderPane();
		    pane.setTop(label);
		    pane.setCenter(closeButton);
		    Scene scene = new Scene(pane, 500, 100);
		    alertStage.setScene(scene);
		    alertStage.initModality(Modality.APPLICATION_MODAL);
		    alertStage.initStyle(StageStyle.UNDECORATED);
		    alertStage.show();
		    // when they are low we need to see if he wants to buy another weapon or pass turn
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void updateDangerlevels() {
		//System.out.println(numOfLanes);
		for(int i=0; i<numOfLanes; i++) {
			//System.out.println(battle.getOriginalLanes().size());
			Lane currLane= battle.getOriginalLanes().get(i);
			VBox currDangerLane = (VBox) dangerLevel.getChildren().get(i);
			Text currDangerText = (Text) currDangerLane.getChildren().get(1);
			currDangerText.setText(currLane.getDangerLevel()+ " ");
		}
	}
	
	public void update() throws Exception {
		//updateWallHealth();
		updateDangerlevels();
		updateResources();
		updateScore();
		updateTurns();
		updateTitansInLanes();
		titanImages.getChildren().clear();
		titanImages.getChildren().addAll(updateApproachingTitans());
		updateWallHealthandWeaponsnum();
		if(battle.isGameOver()) {
			Stage alertStage = new Stage();
			alertStage.setTitle("Game Over!");
		    Label label = new Label("Game Over!");
		    label.setAlignment(Pos.CENTER);
		    Button closeButton = new Button("Exit");
		    closeButton.setOnAction(event -> alertStage.close());
		    BorderPane pane = new BorderPane();
		    pane.setTop(label);
		    pane.setCenter(closeButton);
		    Scene scene = new Scene(pane, 500, 100);
		    alertStage.setScene(scene);
		    alertStage.initModality(Modality.APPLICATION_MODAL);
		    alertStage.initStyle(StageStyle.UNDECORATED);
		    alertStage.show();
		}
	}
	public void updateWallHealthandWeaponsnum() {
		ObservableList<HBox> lanes = lanesList.getItems();
		for(int i =0 ;i<battle.getOriginalLanes().size();i++) {
			if(!battle.getOriginalLanes().get(i).isLaneLost()) {
				HBox allLane = lanes.get(i);
				VBox allWall = (VBox) allLane.getChildren().get(0);
				StackPane Wall = (StackPane) allWall.getChildren().get(0);
				GridPane Weapons = (GridPane) Wall.getChildren().get(1);
				int piercing = 0;
				int sniper = 0;
				int volley = 0;
				int walltrap = 0;
				Lane lane = battle.getOriginalLanes().get(i);
				ArrayList<Weapon> weapons = lane.getWeapons();
				for(int j=0;j<weapons.size();j++) {
					Weapon temp = weapons.get(j);
					if(temp instanceof PiercingCannon)
						piercing++;
					else if(temp instanceof SniperCannon)
						sniper++;
					else if(temp instanceof VolleySpreadCannon)
						volley++;
					else if(temp instanceof WallTrap)
						walltrap++;
				}
				VBox piercingV = (VBox) Weapons.getChildren().get(0);
				Text piercingL = (Text)piercingV.getChildren().get(1);
				piercingL.setText("X"+piercing);
				VBox sniperV = (VBox) Weapons.getChildren().get(1);
				Text sniperL = (Text)sniperV.getChildren().get(1);
				sniperL.setText("X"+sniper);
				VBox volleyV = (VBox) Weapons.getChildren().get(2);
				Text volleyL = (Text)volleyV.getChildren().get(1);
				volleyL.setText("X"+volley);
				VBox walltrapV = (VBox) Weapons.getChildren().get(3);
				Text walltrapL = (Text)walltrapV.getChildren().get(1);
				walltrapL.setText("X"+walltrap);
				ProgressBar healthBar = (ProgressBar) allWall.getChildren().get(1);
				int currentHealth = battle.getOriginalLanes().get(i).getLaneWall().getCurrentHealth();
		        healthBar.setProgress((double)currentHealth/10000);
		        healthBar.setStyle("-fx-accent: green;");
		        
			}
		}
		
		
	}
	
	
}