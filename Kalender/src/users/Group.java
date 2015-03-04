package users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import event.Event;
import notification.Varsel;
import notification.Varsel.EventMessages;

public class Group {
	
	private int id;
	private String name;
	private User admin;
	
	public Group(String name, User admin){
		this.id = -1;
		this.name=name;
		this.admin = admin;
	}
	//BRUKES TIL Å HENTE GRUPPE VED ID
	private Group(int id, String name, User admin){
		this.id=id;
		this.name= name;
		this.admin=admin;
	}
	
	//legger til bruker u:
	public void addUser(Connection conn, User u){
		if (id == -1) save(conn);
		String addEventSql = "INSERT INTO BRUKERIGRUPPE VALUES " + "(" + id + "," + u.getId() + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//fjerner bruker u:
	public void removeUser(Connection conn, User u){
		if (id == -1) save(conn);
		String deleteUserSql = "DELETE FROM BRUKERIGRUPPE WHERE GRUPPEID=" + id + " AND BRUKERID=" + u.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteUserSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//lagrer:
	public void save(Connection conn){
		id = generateID(conn);
		String addGroupSql = "INSERT INTO GRUPPE VALUES " + " (" + id + ",'"  + name + "',"+ admin.getId() +")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addGroupSql);
			stmt.close();
			addUser(conn, admin);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//Fjerner ev fra alle medlemmene i gruppa:
	//TODO test
	public void removeEvent(Connection conn, Event ev){
		List<User> users = ev.getUsers(conn);
		for (User u : users){
			u.removeEvent(conn, ev);
		}
	}
	
	
	public void fireMessage(Connection conn, Varsel v) {
		List<User> users = getUsers(conn);
		for (User u : users){
			u.fireMessage(conn,v);
		}
	}
	
	//Henter alle brukere som er med i gruppen:
	public List<User> getUsers(Connection conn){
		List<User> l = new ArrayList<User>();
		String sql = "SELECT BRUKER.BRUKERID FROM BRUKER, BRUKERIGRUPPE WHERE BRUKER.BRUKERID=BRUKERIGRUPPE.BRUKERID AND BRUKERIGRUPPE.GRUPPEID =" + this.id;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(User.getUser(conn, rs.getInt("BRUKER.BRUKERID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//TODO
		throw new IllegalStateException("Noe gikk feil ved henting av brukere");
	}
	//Henter gruppe med gitt id:
	public static Group getGroup(Connection conn, int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM GRUPPE WHERE GRUPPEID = " + id);
			rs.next();
			Group g = new Group(rs.getInt("GruppeID"), rs.getString("Navn"), User.getUser(conn,Integer.valueOf(rs.getString("Admin"))));
			stmt.close();
			return g;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Failed to get group by id: " + id);
	}
	
	private int generateID(Connection conn){
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
	public String getName() {
		return name;
	}
	public User getAdmin() {
		return admin;
	}
	
	public int getId() {
		if (id == -1) throw new IllegalStateException("Group id not set");
		return id;
	}
	

}
