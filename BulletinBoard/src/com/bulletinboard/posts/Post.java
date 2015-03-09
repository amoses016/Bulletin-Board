package com.bulletinboard.posts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.bulletinboard.utils.databaseUtils;

import javafx.collections.FXCollections;

public class Post {
	private int ID;
	private String title, message, user;
	private Timestamp submitted;
	
	private static String postDBTable = "post";
	
	public Post() {};
	
	/**
	 * Post constructor
	 * 
	 * @param newTitle
	 *            New title contents
	 * @param newMessage
	 *            New message contents
	 * @param newUser
	 *            Username submitting comment
	 * @param newSubmitted
	 *            Timestamp of when comment was submitted
	 * @return none
	 */	
	public Post(int newID, String newTitle, String newMessage, String newUser, Timestamp newSubmitted) {
		ID = newID;
		title = newTitle;
		message = newMessage;
		user = newUser;
		submitted = newSubmitted;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int newID) {
		ID = newID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String newTitle) {
		title = newTitle;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String newMessage) {
		message = newMessage;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String newUser) {
		user = newUser;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp newSubmitted) {
		submitted = newSubmitted;
	}

	/**
	 * Submits comment at current timestamp
	 * 
	 * @return List<Post> containing all posts
	 */
	public static List<Post> getAllPosts() {
		List<Post> posts = FXCollections.observableArrayList();
		String query = "SELECT * FROM " + postDBTable + " ORDER BY post_id DESC;";
		
		if (!databaseUtils.isConnected()) {
			System.out.println("Database not connected.");
			databaseUtils.connect();
		}
		try {
			Statement stmt = databaseUtils.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				posts.add(new Post(
						rs.getInt("post_id"),
						rs.getString("title"),
						rs.getString("message"),
						rs.getString("user"),
						rs.getTimestamp("submission")
						));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Oh jeez no the getAllPosts query broke :(");
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Gets post with a given post ID
	 * 
	 * @param postID
	 *            Post ID to pull post details
	 * @return Post object containing post details
	 */
	public static Post getPostGivenID(int postID) {
		Post post = new Post();
		String query = "SELECT * FROM " + postDBTable + " WHERE post_id = " + postID + ";";
		post.setID(postID);
		
		if (!databaseUtils.isConnected()) {
			System.out.println("Database not connected.");
			databaseUtils.connect();
		}
		
		try {
			Statement stmt = databaseUtils.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				post.setTitle(rs.getString("title"));
				post.setMessage(rs.getString("message"));
				post.setUser(rs.getString("user"));
				post.setSubmitted(rs.getTimestamp("submission"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Oh jeez no the getPostGivenID query broke :(");
			e.printStackTrace();
		}
		return post;
	}
	
	/**
	 * Submits post at current timestamp
	 * 
	 * @param newMessage
	 *            New message contents
	 * @param newUser
	 *            Username submitting comment
	 * @param newPostID
	 *            Post ID to which the comment is being submitted
	 * @return true if successful
	 */
	public static boolean submitPost(String newTitle, String newMessage, String user) {
		Date date = new Date();
		Timestamp currentTS = new Timestamp(date.getTime());

		String insertStmt = "INSERT INTO " + postDBTable + " (title,message,submission,user) VALUES (?,?,?,?);";
		
		if (!databaseUtils.isConnected()) {
			System.out.println("Database not connected.");
			databaseUtils.connect();
		}
		
		try {
			PreparedStatement stmt = databaseUtils.getConnection().prepareStatement(insertStmt);
			stmt.setString(1, newTitle);
			stmt.setString(2, newMessage);
			stmt.setTimestamp(3, currentTS);
			stmt.setString(4, user);
			
			if (stmt.execute()) {
				stmt.close();
				return true;
			}
			else {
				stmt.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}




