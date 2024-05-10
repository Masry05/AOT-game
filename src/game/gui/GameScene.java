package game.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import game.engine.weapons.WeaponRegistry;
import game.engine.weapons.factory.WeaponFactory;
import java.io.*;
import java.util.ArrayList;

public class GameScene {
	
	private Scene game;	
	public GameScene() throws Exception{
		
	}
	
	public GameScene(Battle battle) throws Exception{
		BorderPane layout = FXMLLoader.load(getClass().getResource("GameScene.fxml"));
		ListView<VBox> weaponShop = (ListView<VBox>) layout.lookup("#weaponShop");
		ObservableList<VBox> weapons = weaponShopBuilder(battle);
		weaponShop.setItems(weapons);
		ListView<HBox> lanes = (ListView<HBox>) layout.lookup("#lanes");
		ObservableList<HBox> lane = lanesBuilder(battle);
		lanes.setItems(lane);
		game = new Scene(layout ,300, 300);
	}
	
	private ObservableList<VBox> weaponShopBuilder(Battle battle) throws IOException {
		WeaponFactory shop = battle.getWeaponFactory();
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
	        VBox piercingCannon = new VBox();
	        piercingCannon.getChildren().addAll(image1,amount1);
	        ImageView image2 = new ImageView(new Image(new FileInputStream("images"+File.separator+"SniperCannon.png")));
	        Label amount2 = new Label("X0");
	        VBox sniperCannon = new VBox();
	        sniperCannon.getChildren().addAll(image2,amount2);
	        ImageView image3 = new ImageView(new Image(new FileInputStream("images"+File.separator+"VolleySpreadCannon.png")));
	        Label amount3 = new Label("X0");
	        VBox volleySpreadCannon =  new VBox();
	        volleySpreadCannon.getChildren().addAll(image3,amount3);
	        ImageView image4 = new ImageView(new Image(new FileInputStream("images"+File.separator+"WallTrap.png")));
	        Label amount4 = new Label("X0");
	        VBox wallTrap = new VBox();
	        wallTrap.getChildren().addAll(image4,amount4);
	        weapons.add(piercingCannon, 0, 0);
	        weapons.add(sniperCannon, 0, 1);
	        weapons.add(volleySpreadCannon, 0, 2);
	        weapons.add(wallTrap, 0, 3);
	        GridPane lane = new GridPane();
	        lanes.add(wall);
			}
		return lanes;
		
	}
	public Scene getGame() {
		return game;
	}
}
