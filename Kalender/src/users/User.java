package users;

import java.sql.*;
import java.util.Scanner;

import notification.Listener;

public class User implements Listener {

	private String firstname,lastname, username, password;
	private int id;

	private Scanner input;

	private String addUserSql = "INSERT INTO BRUKER VALUES ";

	public User(String firstname,String lastname, String username, String password){
		isValidUser(firstname,lastname, username, password);
		this.firstname=firstname;
		this.lastname = lastname;
		this.username=username;
		this.password=password;
	}


	public void save(){
		Connection conn = Admin.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addUserSql + "(" + generateID() + ",'"  + firstname + "','" + lastname + "','" + username + "','" + password + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void fireMessage() {
		// TODO Auto-generated method stub

	}

	private boolean isValidUser(String firstname,String lastname, String username, String password){
		if (!(firstname.matches("[a-ÂA-≈]+"))){
			throw new IllegalArgumentException("Invalid firstname");
		}
		else if (!(lastname.matches("[a-ÂA-≈]+"))){
			throw new IllegalArgumentException("Invalid lastname");
		}
		else if (!(username.matches("[a-ÂA-≈0-9]{6,20}"))){
			throw new IllegalArgumentException("Invalid username");
		}
		return true;
	}

	private int generateID(){
		Connection conn = Admin.getConnection();
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(B.BRUKERID) AS M FROM BRUKER AS B");
			rs.next();
			if (rs == null) res = 1;
			else res=rs.getInt("M") + 1;
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("ID-generation failed");
	}


	public static void main(String[] args) {
		User user = new User("Eivind", "Midtgarden", "alkkasjfna", "llolol");
		user.save();
	}	
}
