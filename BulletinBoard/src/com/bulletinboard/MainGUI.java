package com.bulletinboard;

import java.util.List;

import com.bulletinboard.posts.Post;
import com.bulletinboard.posts.PostGUI;
import com.bulletinboard.utils.databaseUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainGUI {
	
	private static ScrollPane content;
	private static int ROW_HEIGHT = 24;
	
	public static GridPane load() {	
		Button signUpBtn = new Button("Sign Up");
		Button signInBtn = new Button("Sign In");

		HBox headerBtns = new HBox();
		headerBtns.getStyleClass().add("hbox");
		headerBtns.getChildren().addAll(signInBtn, signUpBtn);
		
		Label mainPageTitle = new Label ("Mind Grapes");
		Label testUserWelcome = new Label ("Welcome, DudeBro18!");
		mainPageTitle.setFont(new Font("Ariel",23));

		GridPane screenHeader = new GridPane();
		screenHeader.getStyleClass().add("gridpane-header");
		screenHeader.add(mainPageTitle, 0, 0);
		screenHeader.add(testUserWelcome, 1, 0);
		screenHeader.add(headerBtns, 2, 0);
		screenHeader.setAlignment(Pos.TOP_CENTER);

		GridPane screen = new GridPane();
		screen.getStyleClass().add("gridpane");		
		screen.add(screenHeader, 0, 0);
		
		List<Post> allPosts = Post.getAllPosts();
		ListView<HBox> postListView = new ListView<>();
		screen.setAlignment(Pos.TOP_CENTER);
		
		if (allPosts.size() == 0) {
			Label noPosts = new Label ("No Posts Yet!");
			screen.add(noPosts, 0, 1);
		}
		else {
			ObservableList<HBox> postList = FXCollections.observableArrayList();
			for (Post post : allPosts) {
				Label postTitle = new Label(post.getID() + " : " + post.getTitle());
				postTitle.setFont(new Font("Arial",15));
				Text postDetails = new Text(" -- Submitted by " + post.getUser() + " on " + post.getSubmitted().toString());
				//postDetails.setWrappingWidth(300);
				HBox postHBox = new HBox();
				postHBox.getChildren().addAll(postTitle, postDetails);
				postList.add(postHBox);
			}
			postListView.setPrefHeight(allPosts.size() * ROW_HEIGHT); //hax, yo.
			postListView.setItems(postList);
			screen.add(postListView, 0, 2);
		}
		
		postListView.setOnMouseClicked(new EventHandler<MouseEvent>(){
	          @Override
	          public void handle(MouseEvent arg0) {
	        	  System.out.println("Selected: " + postListView.getSelectionModel().getSelectedItem().getChildren().get(0));
	        	  int selectedPostID = trimSelectedID(postListView.getSelectionModel().getSelectedItem().getChildren().get(0).toString());
	        	  setContent(PostGUI.loadPostView(selectedPostID));
	          }
	 
	      });
		
		Button newPostButton = new Button("New Post");
		newPostButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainGUI.setContent(PostGUI.loadPostNew());
			}
		});
		
		screen.add(newPostButton, 0, 3);

		return screen;
	}
	
	public static Scene loadScene() {
		GridPane root = new GridPane();
		content = new ScrollPane();
		
		databaseUtils.connect();
		root = load();
		databaseUtils.disconnect();
		setContent(root);
		
		Scene scene = new Scene(content,400,400);
		scene.getStylesheets().add("com/bulletinboard/styles/main.css");
		
		return scene;
	}
	
	public static int trimSelectedID(String selection) {
		String[] tokens = selection.split("'");
		//System.out.println(tokens[1]);
		String[] tokenTokens = tokens[1].split(" ");
		//System.out.println(tokenTokens[0]);

		return Integer.parseInt(tokenTokens[0]);
	}
	
	public static Node getContent() {
		return MainGUI.content.getContent();
	}
	
	public static void setContent(Node content) {
		MainGUI.content.setContent(content);
	}
}







