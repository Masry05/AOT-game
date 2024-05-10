package game.gui;

import java.io.IOException;

import game.engine.Battle;
import javafx.application.Application;
import javafx.stage.Stage;
	public class Controller extends Application {
		
		private Battle battle;
		private int numberOfLanes;
		
		public void start(Stage primaryStage) {
			numberOfLanes = 3;
			try {
				battle = new Battle(1,0,10,3,250);
			}
			catch (IOException e){	
			}
			try{
				MainPageController game = new MainPageController(battle);
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

