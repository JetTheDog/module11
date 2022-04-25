package application;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Client extends Application {
	int clientNum;
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	Socket socket= null;
	
	@Override
	public void start(Stage primaryStage) {
		
		VBox appVert = new VBox();
		HBox appHor = new HBox();
		Scene appScene = new Scene(appVert,450,300);
		TextField numField = new TextField();
		Label numLabel = new Label("Enter a number to check prime: ");
		Button submit = new Button("   SUBMIT   ");
		TextArea tBox = new TextArea();
		
		primaryStage.setTitle("Client");
		primaryStage.setScene(appScene);
		primaryStage.show();
		appVert.getChildren().add(appHor);
		appHor.getChildren().add(numLabel);
		appHor.getChildren().add(numField);
		numField.setMaxWidth(250);
		appVert.getChildren().add(submit);
		appVert.getChildren().add(tBox);
		
		submit.setOnAction(actionEvent ->  {
			try {
				if (socket == null) {
					socket = new Socket("localhost", 8000);
					fromServer = new DataInputStream(socket.getInputStream());
					toServer = new DataOutputStream(socket.getOutputStream());
				}
				
				if (numField.getText().isEmpty() == false) {
					clientNum = Integer.parseInt(numField.getText());
					tBox.appendText("You entered: " + clientNum + "\n");
					toServer.writeInt(clientNum);
					toServer.flush();
					Boolean tNum = fromServer.readBoolean();
					
					if (tNum == true) {
						tBox.appendText(clientNum + " is prime. \n");
					}
					else {
						tBox.appendText(clientNum + " is not prime. \n");
					}
					
				} else {
					tBox.appendText("You have not entered a number. \n");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}