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
import javafx.scene.text.Text;
import game.engine.Battle;
import game.engine.lanes.Lane;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.WeaponFactory;
import java.io.*;
import java.util.ArrayList;

public class MainPageController {
	
	private Scene game;	
	private Battle battle;
	//public ListView<VBox> weaponShop;
	//public ListView<HBox> lanes;
	public VBox dangerLevel;
	public HBox turns; 
	public HBox HUD;
	@FXML
	public HBox upcomingTitans;
	public MainPageController() throws Exception{
		
	}

	public MainPageController(int numOfLanes, int resources, double height , double width) throws Exception{
		this.battle = new Battle(1, 0, 10, numOfLanes, resources);
		BorderPane layout = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		ObservableList<VBox> weapons = weaponShopBuilder();
		//weaponShop.setItems(weapons);
		ListView<VBox> weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		ListView<HBox> lanes = (ListView<HBox>) layout.lookup("#lanes");
		ObservableList<HBox> lane = lanesBuilder(battle);
		ObservableList <ImageView> approachingTitans =  updateApproachingTitans();
		HBox titanImages = (HBox)layout.lookup("#titanImages");
		titanImages.getChildren().addAll(updateApproachingTitans());
		titanImages.setSpacing(10);
			
		lanes.setItems(lane);
		game = new Scene(layout ,height , width);	
	}
	
	private ObservableList<VBox> weaponShopBuilder() throws IOException {
		WeaponFactory shop = this.battle.getWeaponFactory();
		ObservableList<VBox> weapons = FXCollections.observableArrayList();
		for(int i=0;i< shop.getWeaponShop().size();i++) {
			VBox temp = new VBox();
			WeaponRegistry weapon = shop.getWeaponShop().get(i+1);
			String fileName;
			switch(i+1) {
			case 1: fileName = "PiercingCannon";break;
			case 2: fileName = "SniperCannon";break;
			case 3: fileName = "VolleySpreadCannon";break;
			case 4: fileName = "WallTrap";break;
			default: fileName = "test"; 
			}
			Image image = new Image(new FileInputStream("images"+File.separator+fileName+".png"));
	        ImageView imageView = new ImageView(image);
	        imageView.setFitWidth(100);
	        imageView.setFitHeight(100);
			Text name = new Text(weapon.getName());
			Text price = new Text(weapon.getPrice()+"");
			temp.getChildren().addAll(imageView,name,price);
			String range;
			switch(i+1) {
				case 1: range = "Attacks the closest 5 titans";break;
				case 2: range = "Attacks the closest titan";break;
				case 3: range = "Attacks any titan within "+weapon.getMinRange()+" to "+weapon.getMaxRange()+" meters";break;
				case 4: range = "Attacks the closest titan only if it has reached the wall";break;
				default: range = "Error while getting the range"; 
			}
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
		for(int i = 0;i<originalLanes.size();i++) {
			HBox wall = new HBox();
			GridPane weapons = new GridPane();
	        ImageView image1 = new ImageView(new Image(new FileInputStream("images"+File.separator+"PiercingCannon.png")));
	        Label amount1 = new Label("X0");
	        image1.setFitHeight(100);
			image1.setFitWidth(100);
	        VBox piercingCannon = new VBox();
	        piercingCannon.getChildren().addAll(image1,amount1);
	        ImageView image2 = new ImageView(new Image(new FileInputStream("images"+File.separator+"SniperCannon.png")));
	        image2.setFitHeight(100);
			image2.setFitWidth(100);
	        Label amount2 = new Label("X0");
	        VBox sniperCannon = new VBox();
	        sniperCannon.getChildren().addAll(image2,amount2);
	        ImageView image3 = new ImageView(new Image(new FileInputStream("images"+File.separator+"VolleySpreadCannon.png")));
	        image3.setFitHeight(100);
			image3.setFitWidth(100);
	        Label amount3 = new Label("X0");
	        VBox volleySpreadCannon =  new VBox();
	        volleySpreadCannon.getChildren().addAll(image3,amount3);
	        ImageView image4 = new ImageView(new Image(new FileInputStream("images"+File.separator+"WallTrap.png")));
	        image4.setFitHeight(100);
			image4.setFitWidth(100);
	        Label amount4 = new Label("X0");
	        VBox wallTrap = new VBox();
	        wallTrap.getChildren().addAll(image4,amount4);
	        weapons.add(piercingCannon, 0, 0);
	        weapons.add(sniperCannon, 0, 1);
	        weapons.add(volleySpreadCannon, 0, 2);
	        weapons.add(wallTrap, 0, 3);
	        GridPane lane = new GridPane();
	        wall.getChildren().addAll(weapons,lane);
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
		       case 1:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"Pure Titan.jpg")));break;
		       case 2:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"Abnormal Titan.jpg")));break;
		       case 3:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"ArmoredTitan.jpeg")));break;
		       case 4:  currentImage = new ImageView(new Image(new FileInputStream("images"+File.separator+"CollosalTitan.jpeg")));break;
		       default: currentImage = null;break;
		   }
		
		switch(currentDangerLevel) {
		   case 1:  titanName  = new Tooltip("Pure Titan");break;
	       case 2:  titanName  = new Tooltip("Abnormal Titan");break;
	       case 3:  titanName  = new Tooltip("Armored Titan");break;
	       case 4:  titanName = new Tooltip("Colossal Titan");break;
	       default: titanName = null;break;
		}
		currentImage.setFitHeight(52);
		currentImage.setFitWidth(27);
		Tooltip.install(currentImage, titanName);
		updatedTitansQueue.add(currentImage);
		}
		return updatedTitansQueue; 
	
		
	}
}