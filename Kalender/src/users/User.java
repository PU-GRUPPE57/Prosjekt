package users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import users.Group;
import event.Event;
import notification.Varsel;
import notification.Varsel.EventMessages;
import notification.Varsel.UserMessages;

public class User {

	private String firstname,lastname, username, password;
	private int id, admin;

	//Konstuktører
	public User(String firstname,String lastname, String username, String password, boolean admin){
		//isValidUser(firstname,lastname, username, password);
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.id = -1;
		if (admin) this.admin = 1;
		else this.admin = 0; 
	}

	//BRUKES TIL Å HENTE BRUKER VED ID
	private User(int id,String firstname,String lastname, String username, String password, boolean admin){
		//isValidUser(firstname,lastname, username, password);
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.id = id;
		if (admin) this.admin = 1;
		else this.admin = 0; 
	}

	//Sjekker om innloggingsinformasjon er riktig:
	public static User login(Connection conn, String username, String password){
		String sql= "SELECT BRUKERNAVN,PASSORD FROM BRUKER WHERE BRUKERNAVN = '" + username + "'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			boolean log = rs.getString("passord").equals(password);
			stmt.close();
			if (log) return User.getUser(conn, username);
			else return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Login failed in class user for some reason");
	}



	//legger til ev i user:
	public void addEvent(Connection conn, Event ev){
		if (id ==-1) this.save(conn);
		String addEventSql = "INSERT INTO BRUKERIAVTALE VALUES " + "(" + ev.getId() + "," + id + "," + 0 + "," + 1 + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
			if(!(id == ev.getOwner().getId())){
				Varsel v = new Varsel(this, EventMessages.USER_INVITE_EVENT, ev);
				v.save(conn);				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//legger til this i gruppe g:
	public void addToGroup(Connection conn, Group g){
		g.addUser(conn, this);
	}

	//fjerner ev fra brukeriavtale:
	public void removeEvent(Connection conn, Event ev, User u){
		if (id == -1) this.save(conn);
		if (u.getId() == id) throw new IllegalArgumentException("you cannot remove yourself, you are admin");
		String deleteEventSql = "DELETE FROM BRUKERIAVTALE WHERE BRUKERID=" + id + " AND AVTALEID=" + ev.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//fjerner g fra brukerigruppe:
	public void removeGroup(Connection conn, Group g, User removedby){
		if (id == -1) this.save(conn);
		if (removedby.getId() == id) throw new IllegalArgumentException("you cannot remove yourself, you are admin");
		String deleteEventSql = "DELETE FROM BRUKERIGRUPPE WHERE BRUKERID=" + id + " AND GRUPPEID=" + g.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Lagrer:
	public void save(Connection conn){
		id = generateID(conn);
		String addUserSql = "INSERT INTO BRUKER VALUES " + " (" + id + ",'"  + firstname + "','" + lastname + "'," + admin + ",'" + username + "','" + password + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addUserSql);

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Henter bruker med id-en som gis:
	public static User getUser(Connection conn , int id){
		if (id==0) return null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BRUKER WHERE BRUKERID = " + id);
			rs.next();
			User u = new User(rs.getInt("BrukerID"), rs.getString("Fornavn"), rs.getString("Etternavn"), rs.getString("Brukernavn"), rs.getString("passord"), rs.getInt("isAdmin") == 1);
			stmt.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get user by id: " + id);
	}

	//Henter bruker ved brukernavn
	public static User getUser(Connection conn, String username){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BRUKER WHERE BRUKERNAVN = '" + username + "'");
			rs.next();
			User u = new User(rs.getInt("BrukerID"), rs.getString("Fornavn"), rs.getString("Etternavn"), rs.getString("Brukernavn"), rs.getString("passord"), rs.getInt("isAdmin") == 1);
			stmt.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get user by username: " + username);
	}

	//henter alle som ikke er invitert fra før:
	public static List<User> getInviteList(Connection conn, Group g){
		List<User> l = getUsers(conn);
		List<User> l2 = g.getUsers(conn);
		int i = 0;
		while (i < l.size()){
			for (int j = 0; j < l2.size(); j++) {
				if (l.get(i).getId() == l2.get(j).getId()){
					l.remove(l.get(i));
					i--;
					break;
				}
			}
			i++;
		}
		return l;
	}
	
	
	//henter alle bruker:
	public static List<User> getUsers(Connection conn){
		List<User> l = new ArrayList<User>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BRUKER");
			while(rs.next()){
				l.add(new User(rs.getInt("BrukerID"), rs.getString("Fornavn"), rs.getString("Etternavn"), rs.getString("Brukernavn"), rs.getString("passord"), rs.getInt("isAdmin") == 1));				
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get user by users");

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

	//Returnerer alle events tilknyttet denne user:
	public List<Event> getAllEvents(Connection conn){
		List<Event> l = new ArrayList<Event>();
		String sql = "SELECT AVTALE.AVTALEID FROM AVTALE, BRUKERIAVTALE WHERE AVTALE.AVTALEID=BRUKERIAVTALE.AVTALEID AND BRUKERIAVTALE.BRUKERID =" + this.id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(Event.getEvent(conn, rs.getInt("AVTALE.AVTALEID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Noe gikk feil ved henting av inviterte brukere i event");
	}
	//Returnerer alle events tilknyttet denne user:
	public List<Event> getHiddenEvents(Connection conn){
		List<Event> l = new ArrayList<Event>();
		String sql = "SELECT AVTALE.AVTALEID FROM AVTALE, BRUKERIAVTALE WHERE AVTALE.AVTALEID=BRUKERIAVTALE.AVTALEID AND BRUKERIAVTALE.SYNLIGHET = 0 AND BRUKERIAVTALE.BRUKERID =" + this.id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(Event.getEvent(conn, rs.getInt("AVTALE.AVTALEID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//TODO
		throw new IllegalStateException("Noe gikk feil ved henting av inviterte brukere i event");
	}
	//Returnerer alle events tilknyttet denne user:
	public List<Event> getVisibleEvents(Connection conn){
		List<Event> l = new ArrayList<Event>();
		String sql = "SELECT AVTALE.AVTALEID FROM AVTALE, BRUKERIAVTALE WHERE AVTALE.AVTALEID=BRUKERIAVTALE.AVTALEID AND BRUKERIAVTALE.SYNLIGHET = 1 AND BRUKERIAVTALE.BRUKERID =" + this.id ;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(Event.getEvent(conn, rs.getInt("AVTALE.AVTALEID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Noe gikk feil ved henting av inviterte brukere i event");
	}
	//returnerer en liste som inneholder alle varslene brukeren har:
	public List<Varsel> getNotifications(Connection conn){
		List<Varsel> l = new ArrayList<Varsel>();
		String sql = "SELECT VARSEL.VARSELID FROM VARSEL, BRUKERHARVARSEL WHERE VARSEL.VARSELID=BRUKERHARVARSEL.VARSELID AND BRUKERHARVARSEL.BRUKERID =" + this.id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(Varsel.getVarsel(conn, rs.getInt("VARSEL.VARSELID")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Noe gikk feil ved henting av varsler for bruker " + username);
	}
	//setter synlighet i brukeriavtale til 0, gjemmer event:
	public void hideEvent(Connection conn, Event ev){
		String sql = "UPDATE BRUKERIAVTALE SET SYNLIGHET= " + 0 + " WHERE AVTALEID= " + ev.getId() +" AND BRUKERID= " + id;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//setter synlighet i brukeriavtale til 1, gjør event synlig igjen:
	public void unhideEvent(Connection conn, Event ev){
		String sql = "UPDATE BRUKERIAVTALE SET SYNLIGHET= " + 1 + " WHERE AVTALEID= " + ev.getId() +" AND BRUKERID= " + id;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Henter alle grupper brukeren er med i:
		public List<Group> getGroups(Connection conn){
			List<Group> l = new ArrayList<Group>();
			String sql = "SELECT GRUPPE.GRUPPEID FROM GRUPPE, BRUKERIGRUPPE WHERE GRUPPE.GRUPPEID=BRUKERIGRUPPE.GRUPPEID AND BRUKERIGRUPPE.BRUKERID =" + this.id;
			
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					l.add(Group.getGroup(conn, rs.getInt("GRUPPE.GRUPPEID")));
				}
				stmt.close();
				return l;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new IllegalStateException("Noe gikk feil ved henting av brukere");
		}
		
	public void removeVarsel(Connection conn, Varsel v){
		String deleteEventSql = "DELETE FROM VARSEL WHERE VARSELID=" + v.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public SimpleStringProperty usernameProperty(){
		return new SimpleStringProperty(username);
	}
	
	//endrer for svar i til renderEvent:
	public static SimpleStringProperty eventResponse(int i){
		if (i>2 || i<0 ) throw new IllegalArgumentException("wrong parameters: 0, 1 or 2");
		switch (i){
		case 0 : return new SimpleStringProperty("NOT RESPONDED");
		case 1 : return new SimpleStringProperty("ACCEPTED");
		case 2 : return new SimpleStringProperty("DECLINED");
		}
		return null;
	}

	public static List<User> getInviteList(Connection conn, Event event) {
		List<User> l = getUsers(conn);
		List<User> l2 = event.getUsers(conn);
		int i = 0;
		while (i < l.size()){
			for (int j = 0; j < l2.size(); j++) {
				if (l.get(i).getId() == l2.get(j).getId()){
					l.remove(l.get(i));
					i--;
					break;
				}
			}
			i++;
		}
		return l;
	}

}
