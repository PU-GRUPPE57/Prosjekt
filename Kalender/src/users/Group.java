package users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import event.Event;
import notification.Varsel;
import notification.Varsel.EventMessages;
import notification.Varsel.GroupMessages;

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
			if (!(admin.getId() == u.getId())){
				Varsel v = new Varsel(u, GroupMessages.USER_ADDED, this);
				v.save(conn);						
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addEvent(Connection conn, Event event){
		String addEventSql = "INSERT INTO GRUPPEIAVTALE VALUES " + "(" + event.getId() + "," + id + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Event> getGroupEvents(Connection conn){
		List<Event> l = new ArrayList<>();
		String addEventSql = "SELECT * FROM GRUPPEIAVTALE WHERE GRUPPEID =" + id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(addEventSql);
			while(rs.next()){
				l.add(Event.getEvent(conn, rs.getInt("AvtaleID")));
			}
			
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}return null;
	}
	
	//fjerner bruker u:
	public void removeUser(Connection conn, User u){
		if (id == -1) save(conn);
		String deleteUserSql = "DELETE FROM BRUKERIGRUPPE WHERE GRUPPEID=" + id + " AND BRUKERID=" + u.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteUserSql);
			stmt.close();
			Varsel v = new Varsel(u, GroupMessages.USER_REMOVED, this);
			v.save(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteGroup(Connection conn){
		String deleteEventSql = "DELETE FROM GRUPPE WHERE GRUPPEID=" + id;
		try {
			Statement stmt = conn.createStatement();
			for (User user : getUsers(conn)) {
				Varsel v = new Varsel(user, GroupMessages.GROUP_DELETED, this);
				v.save(conn);
			}
			List<Event> l = getGroupEvents(conn);
			for (Event event : l) {
				event.deleteEvent(conn);
			}
			stmt.executeUpdate(deleteEventSql);
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
		if (id==0) return null;
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

	public SimpleStringProperty textProperty() {
		return new SimpleStringProperty(name);
	}
	public List<Event> getEvents(Connection conn) {
		List<Event> l = new ArrayList<Event>();
		String sql = "SELECT AVTALEID FROM GRUPPEIAVTALE WHERE GRUPPEID =" + this.id;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(Event.getEvent(conn, rs.getInt("AvtaleID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}return null;
	}
}