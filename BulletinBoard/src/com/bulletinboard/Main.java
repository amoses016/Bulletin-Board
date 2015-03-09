package com.bulletinboard;
	
import com.bulletinboard.utils.ScreenUtils;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {		
			ScreenUtils screen = new ScreenUtils();
			screen.initScreen(primaryStage);
			
			primaryStage.setScene(MainGUI.loadScene());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
