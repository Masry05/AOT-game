package game.gui;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController implements Initializable{
	   @FXML
	    public Button startButton;
	    public Button instructionButton;
	    public Button titanArchiveButton;
	    public Button instructionExit;
	    public MediaView media;
	    public MediaPlayer mediaPlayer;
	    public VBox optionsVbox;
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
		
		}
	    @FXML
	    public void handleStartButtonClick(ActionEvent event) {
	        // Get the button that was clicked
	            ObservableList<Node> children=optionsVbox.getChildren();
	            if(children.size() == 3) {
	            Button easy = new Button("Easy");
	            easy.setId("mode");
	            easy.setPrefHeight(30);
	            easy.setPrefWidth(100);
                easy.setOnAction(e -> {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("EasyMode.fxml"));
					Button easyClicked = (Button) event.getSource();
				    double height = easyClicked.getScene().getHeight();
				    double width = easyClicked.getScene().getWidth();
					Scene easyMode = new Scene (root, width, height);
					Stage window = (Stage) easyClicked.getScene().getWindow();
					window.setScene(easyMode);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            });
	            Button hard = new Button("Hard");
	            hard.setId("mode");
	            hard.setPrefHeight(30);
	            hard.setPrefWidth(100);
	            hard.setOnAction(e -> {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("HardMode.fxml"));
						Button hardClicked = (Button) event.getSource();
					    double height = hardClicked.getScene().getHeight();
					    double width = hardClicked.getScene().getWidth();
						Scene hardMode = new Scene (root, width, height);
						Stage window = (Stage) hardClicked.getScene().getWindow();
						window.setScene(hardMode);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            });
                HBox modes = new HBox();
                modes.getChildren().addAll(easy,hard);
                modes.setPadding(new Insets(5,5,5,5));
                modes.setSpacing(25);
                modes.setAlignment(Pos.CENTER);
                Button instructions = (Button)optionsVbox.getChildren().remove(1);
                Button titanArchive = (Button)optionsVbox.getChildren().remove(1);
                optionsVbox.getChildren().addAll(modes, instructions, titanArchive);
	            }
	            else {
	            	optionsVbox.getChildren().remove(1);
	            }
	        }
	    public void handleInstructionExit(ActionEvent event) {
	    	System.out.println("reached");
	    	Button clickedButton = (Button) event.getSource();//this is not working
	    	 if (clickedButton == instructionExit) {
	    		 Stage currStage = (Stage) instructionExit.getScene().getWindow();
	    		 currStage.close();
	    	 }
	    }
	    public void handleInstructionButtonClick() {
	            Stage popupStage = new Stage();
	            popupStage.initModality(Modality.APPLICATION_MODAL);
	            Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("InstructionPopup.fxml"));
					Scene instructionPop = new Scene (root, 300, 500);
		            // Add event handler to close popup when clicking outside of it
					 /*instructionPop.getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent-> {
		                if (!popupStage.isShowing()) return; // Popup already closed
		                double mouseX = mouseEvent.getSceneX();
		                double mouseY = mouseEvent.getSceneY();
		                double popupX = popupStage.getX();
		                double popupY = popupStage.getY();
		                double popupWidth = popupStage.getWidth();
		                double popupHeight = popupStage.getHeight();
		                if (mouseX < popupX || mouseX > popupX + popupWidth || mouseY < popupY || mouseY > popupY + popupHeight) 
		                    return;
		                
		                popupStage.close();
		            });*/
		            popupStage.setScene(instructionPop);
	                popupStage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		
	    }
