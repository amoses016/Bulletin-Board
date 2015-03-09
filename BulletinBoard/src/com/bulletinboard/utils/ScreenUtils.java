package com.bulletinboard.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ScreenUtils {
	private Rectangle2D bounds;
	//private int offsetX = 890, offsetY = 300;
	private int MAX_WIDTH = 1030, MAX_HEIGHT = 900;
	
	public ScreenUtils() {
		bounds = Screen.getPrimary().getVisualBounds();
	};
	
	public Stage initScreen(Stage stage) {
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		//stage.setWidth(bounds.getWidth()-offsetX);
		//stage.setHeight(bounds.getHeight()-offsetY);
		
		/**Test for x-axis screen limitations*/
		if (bounds.getWidth() < MAX_WIDTH) 
			stage.setWidth(bounds.getWidth());
		else
			stage.setWidth(MAX_WIDTH);
		
		/**Test for y-axis screen limitations*/
		if (bounds.getHeight() < MAX_HEIGHT) 
			stage.setHeight(bounds.getHeight());
		else
			stage.setHeight(MAX_HEIGHT);
		
		stage.setTitle("Allison's Bulletin Board");
		
		return stage;
	}
}
