package game.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class Start extends Application {
	Stage window;
    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
    	Scene s;
		try {
		    AnchorPane homepage = (AnchorPane) FXMLLoader.load(getClass().getResource("Homepage.fxml"));
			Image backgroundImage = new Image(new FileInputStream("images"+File.separator+"home.jpg")); // Replace with your image path
		        BackgroundImage background = new BackgroundImage(
		            backgroundImage,
		            BackgroundRepeat.NO_REPEAT, // or BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT_X, BackgroundRepeat.REPEAT_Y
		            BackgroundRepeat.NO_REPEAT,
		            BackgroundPosition.CENTER,
		            new BackgroundSize(
		                100, 100, true, true, true, false));
		    Background bg = new Background(background);
		    homepage.setBackground(bg);
	        s = new Scene (homepage,1500,790);
		    window.setScene(s);
	    	window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
  
	public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

}