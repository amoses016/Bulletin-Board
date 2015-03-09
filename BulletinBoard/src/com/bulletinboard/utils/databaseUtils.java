package com.bulletinboard.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseUtils {
	private static Connection connection = null;
	
	public static boolean connect() {
		return connect("sql3.freemysqlhosting.net","sql368992","uV9!hG5%","sql368992",3306);
	}
	
	public static boolean connect(String hostname, String username, String password, String database, int port) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean disconnect() {
		try {
			connection.close();
			connection = null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	public static boolean isConnected() {
		if (connection == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
	}
}
