package com.bulletinboard.posts;

import java.util.List;

import com.bulletinboard.MainGUI;
import com.bulletinboard.comments.Comment;
import com.bulletinboard.utils.databaseUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PostGUI {

	public static BorderPane loadPostView(int postID) {
		//Add back button
		Button back = new Button("Home Page");  
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainGUI.setContent(MainGUI.load());
			}
		});
		
		//get Post details
		Post post = Post.getPostGivenID(postID); 
		
		//Set Post title
		Label postTitle = new Label(post.getTitle()); 
		postTitle.setFont(new Font("Ariel",23));
		
		//Set Post message
		Text postMessage = new Text(post.getMessage()); 
		postMessage.setWrappingWidth(800);
		postMessage.setFont(new Font("Ariel",15));
		
		//Initialize GridPane to contain all post details
		GridPane postPane = new GridPane(); 
		postPane.getStyleClass().add("gridpane");
		
		//Initialize Post VBox and add title and message
		VBox postViewVBox = new VBox(); 
		postViewVBox.getStyleClass().add("vbox");
		postViewVBox.getChildren().addAll(back,postTitle,postMessage);
		
		//Initialize comments title
		Label commentsTitle = new Label("Comments:"); 
		commentsTitle.setFont(new Font("Ariel",20));
		
		//Initialize GridPane to hold all comments
		GridPane commentsGP = new GridPane(); 
		commentsGP.getStyleClass().add("gridpane");
		commentsGP.add(commentsTitle, 0, 0);
		
		//connect to database
		databaseUtils.connect(); 
		
		// get all comments
		List<Comment> allPostComments = Comment.getAllCommentsGivenPostID(postID); 
		
		//Populate commentsGP
		if (allPostComments.size() == 0) { 
			Label noComments = new Label ("No Comments Yet!");
			System.out.println("No comments on post " + postID);
			postViewVBox.getChildren().add(noComments);
		}
		else {
			int commentNum = allPostComments.size(), cur = 0;
			
			for (Comment comment : allPostComments) {
				Text commentMessage = new Text(comment.getMessage());
				commentMessage.setWrappingWidth(300);
				Label commentUser = new Label("  -- " + comment.getUser() + " at " + comment.getSubmitted().toString());
				VBox commentVB = new VBox();
				commentVB.getStyleClass().add("vbox-comments");
				commentVB.getChildren().addAll(commentMessage, commentUser);
				commentsGP.add(commentVB, 0, commentNum - cur + 1);
				cur++;
			}
			
			postViewVBox.getChildren().add(commentsGP);
		}
		
		//Add comment text box
		TextArea commentBox = new TextArea();
		commentBox.setWrapText(true);
		
		//Add submit comment button
		Button commentSubmit = new Button("Submit Comment");
		commentSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (commentBox.getText().isEmpty()) {
					Text commentError = new Text("Comments cannot be blank.");
					commentError.setFill(Color.RED);
					postViewVBox.getChildren().add(commentError);
				}
				else {
					if(!Comment.submitComment(commentBox.getText(), "DudeBro18", postID)) {
						System.out.println("Error submitting comment");
					}
					MainGUI.setContent(PostGUI.loadPostView(postID));
				}
			}
		});
		
		//Add comment box, button to vbox
		postViewVBox.getChildren().addAll(commentBox,commentSubmit);
		postPane.add(postViewVBox,0,0);

		//Add GridPane to BorderPane
		BorderPane postRoot = new BorderPane();
		postRoot.setCenter(postPane);
		
		//disconnect from database
		databaseUtils.disconnect();
		
		return postRoot;
	}
	
	public static BorderPane loadPostNew() {
		//Add post GridPane
		GridPane postPane = new GridPane();
		postPane.getStyleClass().add("gridpane");
		
		//Add back button
		Button back = new Button("Home Page");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainGUI.setContent(MainGUI.load());
			}
		});
		postPane.add(back, 0, 0);	
		
		//Add post header
		Label newPostHeader = new Label("Submit a New Post:");
		newPostHeader.setFont(new Font("Ariel",23));
		postPane.add(newPostHeader, 0, 1);
		
		//Add new post title label
		Label newPostTitle = new Label("Title: ");
		newPostTitle.setFont(new Font("Ariel",18));
		postPane.add(newPostTitle, 0, 2);
		
		//Add new post title text field
		TextField newPostTitleText = new TextField();
		postPane.add(newPostTitleText, 0, 3);
		
		//Add new post message label
		Label newPostMessage = new Label("Message: ");
		newPostMessage.setFont(new Font("Ariel",18));
		postPane.add(newPostMessage, 0, 4);

		//Add new post message text field
		TextArea newPostMessageText = new TextArea();
		newPostMessageText.setWrapText(true);
		postPane.add(newPostMessageText, 0, 5);		
		
		//Add post submit button
		Button postSubmit = new Button("Submit Post");		
		postSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (newPostTitleText.getText().isEmpty()) {
					Text postError = new Text("Title cannot be blank.");
					postError.setFill(Color.RED);
					postPane.add(postError, 2, 3);
				}
				else if (newPostMessageText.getText().isEmpty()) {
					Text postError = new Text("Message cannot be blank.");
					postError.setFill(Color.RED);
					postPane.add(postError, 2, 5);
				}
				else {
					if(!Post.submitPost(newPostTitleText.getText(), newPostMessageText.getText(), "DudeBro18")) {
						System.out.println("Error submitting post.");
					}
					MainGUI.setContent(MainGUI.load());
				}
			}
		});		
		postPane.add(postSubmit, 0, 5);	
		
		//Add GridPane to BorderPane
		BorderPane postRoot = new BorderPane();
		postRoot.setCenter(postPane);
		
		return postRoot;
	}
	
}
