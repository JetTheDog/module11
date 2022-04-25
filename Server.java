package application;

import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Server extends Application {
	static ServerSocket serverSocket;
	
	@Override
	public void start(Stage primaryStage) {
		
		VBox appVert = new VBox();
		Scene appScene = new Scene(appVert,450,300);
		TextArea tBox = new TextArea();
		
		primaryStage.setTitle("Server");
		primaryStage.setScene(appScene);
		primaryStage.show();
		appVert.getChildren().add(tBox);
		
		new Thread( () -> {
		      try {
		        serverSocket = new ServerSocket(8000);
		        Socket socket = serverSocket.accept();
		        DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
		        DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
		  
		        while (true) {
					int number = inputFromClient.readInt();
					Boolean tNum = isPrime(number);
					outputToClient.writeBoolean(tNum);
					Platform.runLater(() -> {
						tBox.appendText("Number received: " + number + '\n');
					});
		        }
		      }
		      catch(IOException ex) {
		        ex.printStackTrace();
		      }
		    }).start();
	}
	
	// Code from: https://www.geeksforgeeks.org/java-program-to-check-if-a-number-is-prime-or-not/
	static boolean isPrime(int n) {
		if (n <= 1)
			return false;
		for (int i = 2; i < n; i++)
			if (n % i == 0)
				return false;
		return true;
	}
	
	public static void main(String[] args) {
		launch(args);
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}