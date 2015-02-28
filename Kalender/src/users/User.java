package users;

import java.sql.*;

import event.Event;
import notification.Listener;

public class User implements Listener {

	private String firstname,lastname, username, password;
	private int id;

	//Konstukt�rer
	public User(String firstname,String lastname, String username, String password){
		//isValidUser(firstname,lastname, username, password);
		this.firstname=firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.id = -1;
	}
	
	//BRUKES TIL � HENTE BRUKER VED ID
	private User(int id,String firstname,String lastname, String username, String password){
		//isValidUser(firstname,lastname, username, password);
		this.firstname=firstname;
		this.lastname = lastname;
		this.username=username;
		this.password=password;
		this.id = id;
	}
	//legger til ev i user:
	public void addEvent(Connection conn, Event ev){
		if (id ==-1) this.save(conn);
		String addEventSql = "INSERT INTO BRUKERIAVTALE VALUES " + "(" + id + "," + ev.getId() + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeEvent(Connection conn, Event ev){
		if (id == -1) this.save(conn);
		String deleteEventSql = "DELETE FROM BRUKERIAVTALE WHERE BRUKERID=" + id + " AND AVTALEID=" + ev.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void save(Connection conn){
		id = generateID(conn);
		String addUserSql = "INSERT INTO BRUKER VALUES " + " (" + id + ",'"  + firstname + "','" + lastname + "','" + username + "','" + password + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addUserSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//Henter bruker med id-en som gis
	public static User getUser(Connection conn , int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BRUKER WHERE BRUKERID = " + id);
			rs.next();
			User u = new User(rs.getInt("BrukerID"), rs.getString("Fornavn"), rs.getString("Etternavn"), rs.getString("Brukernavn"), rs.getString("passord"));
			stmt.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get user by id: " + id);
	}

	public void fireMessage() {
		// TODO Auto-generated method stub

	}
	//Test for restriksjoner i brukernavn, og navn //TODO passord
	private boolean isValidUser(String firstname,String lastname, String username, String password){
		if (!(firstname.matches("[a-�A-�]+"))){
			throw new IllegalArgumentException("Invalid firstname");
		}
		else if (!(lastname.matches("[a-�A-�]+"))){
			throw new IllegalArgumentException("Invalid lastname");
		}
		else if (!(username.matches("[a-�A-�0-9]{6,20}"))){
			throw new IllegalArgumentException("Invalid username");
		}
		return true;
	}

	private int generateID(Connection conn){
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(B.BRUKERID) AS M FROM BRUKER AS B");
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
	
	public String getName(){
		return firstname + " " + lastname;
	}
	public int getId() {
		if (id == -1) throw new IllegalStateException("User ID not set, use user.save() first");
		return id;
	}
	
	@Override
	public String toString() {
		return "Bruker: " + id + firstname + lastname;
	}
	
}
