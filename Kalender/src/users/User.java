package users;

import java.sql.Connection;
import java.util.Scanner;

import notification.Listener;

public class User implements Listener {

	private String name, email, username, password;
	private int id;
	
	private Scanner input;
	
	private String addUserSql = "INSERT INTO USER VALUES";
	
	public User(String name, String email, String username, String password){
		checkUser(name, email, username, password);
		this.name=name;
		this.email=email;
		this.username=username;
		this.password=password;
	}
	
	public void fireMessage() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean checkUser(String name, String email, String username, String password){
		if (!(name.matches("[a-ÂA-≈]+[ ][a-ÂA-≈]+"))){
			throw new IllegalArgumentException("Invalid name");
		}
		else if (!(username.matches("[a-ÂA-≈0-9]{6,20}"))){
			throw new IllegalArgumentException("Invalid username");
		}
		return true;
	}
	
	private Connection getConnection(){
		
	}
	
	@Override
	public String toString() {
		return name +" " + username;
	}
public static void main(String[] args) {
	User user = new User("Sindre Flood", "dkfalk", "alk", "llolol");
	System.out.println(user);
}	
}
