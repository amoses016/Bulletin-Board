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
		Button back = new Button("Home Page");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainGUI.setContent(MainGUI.load());
			}
		});
		
		Post post = Post.getPostGivenID(postID);
		Label postTitle = new Label(post.getTitle());
		postTitle.setFont(new Font("Ariel",23));
		
		Text postText = new Text(post.getMessage());
		postText.setWrappingWidth(800);
		postText.setFont(new Font("Ariel",15));
				
		GridPane postPane = new GridPane();
		postPane.getStyleClass().add("gridpane");
		
		VBox postViewVBox = new VBox();
		postViewVBox.getStyleClass().add("vbox");
		postViewVBox.getChildren().addAll(postTitle,postText);
				
		Label commentsTitle = new Label("Comments:");
		commentsTitle.setFont(new Font("Ariel",20));
		
		GridPane commentsGP = new GridPane();
		commentsGP.getStyleClass().add("gridpane");
		commentsGP.add(commentsTitle, 0, 0);
		
		databaseUtils.connect();
		
		List<Comment> allPostComments = Comment.getAllCommentsGivenPostID(postID);
		
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
		
		TextArea commentBox = new TextArea();
		Button commentSubmit = new Button("Submit Comment");
		commentBox.setWrapText(true);
		
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
		
		postViewVBox.getChildren().addAll(commentBox,commentSubmit,back);
		postPane.add(postViewVBox,0,0);

		BorderPane postRoot = new BorderPane();
		postRoot.setCenter(postPane);
		
		databaseUtils.disconnect();
		
		return postRoot;
	}
	
	public static BorderPane loadPostNew() {
		BorderPane postRoot = new BorderPane();
		GridPane postPane = new GridPane();
		//postPane.setVgap(10);
		postPane.getStyleClass().add("gridpane");
		
		Label newPostHeader = new Label("Submit a New Post:");
		newPostHeader.setFont(new Font("Ariel",23));
		
		Label newPostTitle = new Label("Title: ");
		newPostTitle.setFont(new Font("Ariel",18));
		TextField newPostTitleText = new TextField();
		
		Label newPostMessage = new Label("Message: ");
		newPostMessage.setFont(new Font("Ariel",18));
		TextArea newPostMessageText = new TextArea();
		newPostMessageText.setWrapText(true);
		
		postPane.add(newPostHeader, 0, 0);
		postPane.add(newPostTitle, 0, 1);
		postPane.add(newPostTitleText, 0, 2);
		postPane.add(newPostMessage, 0, 3);
		postPane.add(newPostMessageText, 0, 4);		
		
		Button postSubmit = new Button("Submit Post");
		
		postSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (newPostTitleText.getText().isEmpty()) {
					Text postError = new Text("Title cannot be blank.");
					postError.setFill(Color.RED);
					postPane.add(postError, 2, 2);
				}
				else if (newPostMessageText.getText().isEmpty()) {
					Text postError = new Text("Message cannot be blank.");
					postError.setFill(Color.RED);
					postPane.add(postError, 2, 4);
				}
				else {
					if(!Post.submitPost(newPostTitleText.getText(), newPostMessageText.getText(), "DudeBro18")) {
						System.out.println("Error submitting post.");
					}
					MainGUI.setContent(MainGUI.load());
				}
			}
		});
		
	
		Button back = new Button("Home Page");
		
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainGUI.setContent(MainGUI.load());
			}
		});
		
		
		postPane.add(postSubmit, 0, 5);	
		postPane.add(back, 0, 6);	
		
		postRoot.setCenter(postPane);
		
		return postRoot;
	}
	
}
