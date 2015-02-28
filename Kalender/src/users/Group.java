package users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import notification.Listener;

public class Group implements Listener {
	
	private int id;
	private String name;
	
	public Group(String name){
		this.id = generateID();
		this.name=name;
	}
	//BRUKES TIL Å HENTE GRUPPE VED ID
	private Group(int id, String name){
		this.id=id;
		this.name= name;
	}
	
	public void addUser(Connection conn, User u){
		String addEventSql = "INSERT INTO BRUKERIGRUPPE VALUES ";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql + "(" + id + "," + u.getId() + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeUser(Connection conn, User u){
		//TODO
	}
	
	public void save(Connection conn){
		String addGroupSql = "INSERT INTO GRUPPE VALUES ";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addGroupSql + " (" + id + ",'"  + name +"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void fireMessage() {
		// TODO Auto-generated method stub
		
	}
	
	
	public List<User> getUsers(){
		//TODO
		return null;
	}
	//Henter gruppe med gitt id:
	public static Group getGroup(Connection conn, int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM GROUP WHERE GRUPPEID = " + id);
			rs.next();
			Group g = new Group(rs.getInt("GruppeID"), rs.getString("Navn"));
			stmt.close();
			return g;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Failed to get group by id: " + id);
	}
	
	private int generateID(){
		Connection conn = Admin.getConnection();
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(G.GRUPPEID) AS M FROM GRUPPE AS G");
			rs.next();
			if (rs == null) res = 1;
			else res=rs.getInt("M") + 1;
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("ID-generation failed");
	}
	
	

}
