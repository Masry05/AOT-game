package game.gui;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
public class Typing {
	static int index=0;
	static Timeline timeline ;
	public static void Typing(String text, Label l) {
		  String[] characters = text.split("");
		  StringBuilder temp = new StringBuilder();
		     index = 0;
		     timeline = new Timeline(new KeyFrame(Duration.millis(100),
		     event -> {    
		        if (index < characters.length) {
		            temp.append(characters[index++]);
		            String newText = temp.toString();
		            l.setText(newText);
		        } else 
		            timeline.stop();
		    }));
		    timeline.setCycleCount(characters.length); // Set the number of cycles

		    // Start the Timeline
		    timeline.play();
	  }
	/*
	 *Here's how it works:

Creation of KeyFrames: Each iteration of the loop creates a new KeyFrame. 
At this point, these KeyFrames are just configurations stored in memory.
Scheduling: The Timeline schedules each KeyFrame according to its specified time.
In your example, you have set the duration for each KeyFrame to 100 milliseconds.
So, each KeyFrame is scheduled to execute 100 milliseconds after the previous one.
Execution: When you call the play() method on the Timeline, it starts executing 
the scheduled KeyFrames one by one, at the specified times.
Text Update: Inside each KeyFrame, the label's text is updated with the current
content of the StringBuilder.
Animation Completion: Once all KeyFrames have been executed, the Timeline stops, 
and the animation is complete.
So, the Timeline doesn't create all KeyFrames upfront and play them simultaneously.
 Instead, it schedules and executes each KeyFrame at the specified time during
  the animation. This allows for a smooth and controlled animation flow.
	 */
	/* You could do this
	 * Timeline timeline = new Timeline();

    // Set the cycle count
    timeline.setCycleCount(numberOfCharacters);

    // Add KeyFrames for each character
    for (int i = 0; i < numberOfCharacters; i++) {
        final int index = i;
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100 * (index + 1)), event -> {
            temp.append(characters[index]);
            String newText = temp.toString();
            l.setText(newText);
        });
        timeline.getKeyFrames().add(keyFrame);
    }

    // Start the Timeline
    timeline.play();
	 */
}
