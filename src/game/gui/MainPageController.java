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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import game.engine.Battle;
import game.engine.BattlePhase;
import game.engine.lanes.Lane;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.WeaponFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MainPageController {
	
	private Scene game;	
	private Battle battle;
	private BorderPane layout;
	private ProgressBar turnBar;
	public MainPageController() throws Exception{
		
	}

	public MainPageController(int numOfLanes, int resources, double width , double height) throws Exception{
		this.battle = new Battle(1, 0, 10, numOfLanes, resources);
		layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
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
		ObservableList<HBox> lanes = FXCollections.observableArrayList();
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
}