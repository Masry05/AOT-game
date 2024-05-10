package game.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
	Stage window;
    @Override
    public void start(Stage primaryStage) {
    	window = primaryStage;
    	Parent root;
    	Scene s;
		try {
			root = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
		    s = new Scene (root,1000,800);
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
