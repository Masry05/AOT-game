package game.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test extends Application{
	public void start(Stage primaryStage) {
        // Create a button
        Button button = new Button("Click me!");

        // Create a layout pane and add the button to it
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Create a scene with the layout pane as root
        Scene scene = new Scene(root, 300, 250);

        // Set the scene for the stage
        primaryStage.setScene(scene);

        // Set the title of the stage
        primaryStage.setTitle("JavaFX Button Example");

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
