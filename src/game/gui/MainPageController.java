package game.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import game.engine.Battle;
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
	public VBox dangerLevel;
	public HBox turns; 
	public HBox HUD;
	public MainPageController() throws Exception{
		
	}

	public MainPageController(int numOfLanes, int resources, double width , double height) throws Exception{
		this.battle = new Battle(1, 0, 10, numOfLanes, resources);
		BorderPane layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		ObservableList<VBox> weapons = weaponShopBuilder();
		ListView<VBox> weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		weaponShop.setItems(weapons);
		ListView<HBox> lanes = (ListView<HBox>) layout.lookup("#lanes");
		ObservableList<HBox> lane = lanesBuilder(battle);
		ObservableList <ImageView> approachingTitans =  updateApproachingTitans();
		HBox titanImages = (HBox)layout.lookup("#titanImages");
		titanImages.getChildren().addAll(updateApproachingTitans());
		titanImages.setSpacing(10);
			
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
		ObservableList<HBox> lanes = FXCollections.observableArrayList();
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
	            healthBar.add(rect, j, 0);
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
}