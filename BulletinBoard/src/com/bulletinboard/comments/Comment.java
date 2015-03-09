package com.bulletinboard.comments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.bulletinboard.utils.databaseUtils;

import javafx.collections.FXCollections;

public class Comment {
	private String message, user;
	private Timestamp submitted;
	private int postID;
	
	private static String commentDBTable = "comment";
	
	/**
	 * Comment constructor
	 * 
	 * @param newMessage
	 *            New message contents
	 * @param newUser
	 *            Username submitting comment
	 * @param newSubmitted
	 *            Timestamp of when comment was submitted
	 * @param newPostID
	 *            Post ID to which the comment is being submitted
	 * @return none
	 */
	public Comment(String newMessage, String newUser, Timestamp newSubmitted, int newPostID) {
		message = newMessage;
		user = newUser;
		submitted = newSubmitted;
		setPostID(newPostID);
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public Timestamp getSubmitted() {
		return this.submitted;
	}
	
	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public static List<Comment> getAllCommentsGivenPostID(int postID) {
		List<Comment> comments = FXCollections.observableArrayList();
		String query = "SELECT * FROM " + commentDBTable + " WHERE post_id = " + postID + " ORDER BY comment_id DESC;";
		
		if (!databaseUtils.isConnected()) {
			System.out.println("Database not connected.");
			databaseUtils.connect();
		}
		
		try {
			Statement stmt = databaseUtils.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				comments.add(new Comment(
						rs.getString("message"),
						rs.getString("user"),
						rs.getTimestamp("submission"),
						rs.getInt("post_id")
						));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Oh jeez no the getAllPosts query broke :(");
			e.printStackTrace();
		}
		return comments;
	}
	
	/**
	 * Submits comment at current timestamp
	 * 
	 * @param newMessage
	 *            New message contents
	 * @param newUser
	 *            Username submitting comment
	 * @param newPostID
	 *            Post ID to which the comment is being submitted
	 * @return true if successful
	 * @throws SQLException 
	 */
	public static boolean submitComment (String newMessage, String user, int postID) {
		Date date = new Date();
		Timestamp currentTS = new Timestamp(date.getTime());

		String insertStmt = "INSERT INTO " + commentDBTable + " (message,submission,user,post_id) VALUES (?,?,?,?);";
		
		if (!databaseUtils.isConnected()) {
			System.out.println("Database not connected.");
			databaseUtils.connect();
		}
		
		try {
			PreparedStatement stmt = databaseUtils.getConnection().prepareStatement(insertStmt);
			stmt.setString(1, newMessage);
			stmt.setTimestamp(2, currentTS);
			stmt.setString(3, user);
			stmt.setInt(4, postID);
			
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
