package ui;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class UI extends Application {
    
	
	public void createUI() {
		System.out.println("c11");
		launch();
		System.out.println("c22");
	}
	
	
	@Override
	public void start(Stage stage) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("UI.fxml"));
			
			Scene scene = new Scene(root);
	        stage.setTitle("TaskBuddy v0.2");
	        stage.getIcons().add(new Image(getClass().getResourceAsStream("bal.png")));
	        stage.setScene(scene);
	        stage.setResizable(false);
	        stage.show();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
