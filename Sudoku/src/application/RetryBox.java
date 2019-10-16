package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class RetryBox {

	public static void display(String title, String message) {
		Main main = new Main();
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		//Defines a modal window that blocks events from being delivered to any other application window.
		window.setTitle(title);
		VBox vb = new VBox();
		Text txt = new Text(message);
		txt.setFont(new Font(15));
		HBox hbtxt = new HBox();
		hbtxt.getChildren().add(txt);
		HBox.setMargin(hbtxt, new Insets(20,20,20,20));
		hbtxt.setAlignment(Pos.CENTER);
		HBox hbox = new HBox();
		Button retry = new Button("Retry");
		Button quit = new Button("Quit");
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		HBox.setMargin(quit,new Insets(20,20,20,20));
		HBox.setMargin(retry,new Insets(20,20,20,20));
		hbox.getChildren().addAll(retry,quit);		
		vb.getChildren().addAll(hbtxt,hbox);
		window.setScene(new Scene(vb, 390, 150));
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(20));
		window.show();
		final EventHandler<ActionEvent> backToMenu = new EventHandler<ActionEvent>() {			
			@Override
			public void handle(final ActionEvent event) {
				window.close();
				Main.pausethegame = true;
				//when you get an incorrect answer, you will go back the start page just as clicked the pause button
				Main.sudokuWindow.close();
				//back to menu
				try {
					main.start(main.initalWindow);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		};
		
		final EventHandler<ActionEvent> restartTheGame = new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				window.close();
				window.setScene(main.sudokuScene);
			}
		};
		retry.setOnAction(restartTheGame);
		quit.setOnAction(backToMenu);
	}

}
