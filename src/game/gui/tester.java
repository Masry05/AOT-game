package game.gui;

import java.io.IOException;

import game.engine.Battle;
import javafx.application.Application;
import javafx.stage.Stage;
	public class tester extends Application {
		
		private Battle battle;
		private int numberOfLanes;
		
		public void start(Stage primaryStage) {

			try {
				 MainPageController game = new MainPageController(3,250, 1000, 1000,new Stage());
				 primaryStage.setScene(game.getGame());
				 primaryStage.show();
			}
			   catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
			
		}
	    public static void main(String[] args) {
	        launch(args);
	    }
	}

