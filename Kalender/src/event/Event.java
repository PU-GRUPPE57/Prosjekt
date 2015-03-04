package event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import users.Admin;
import users.User;

public class Event {

	private int id;
	private String name;
	private Room rom;
	private User owner;
	private int priority;
	private Timestamp start;
	private Timestamp end;
	private String description;
	

	public Event(String name, int priority, Room rom, User owner, Timestamp start, Timestamp slutt, String description){
		if (!name.matches("[a-ÂA-≈0-9]+")){
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.priority = priority;
		this.id = -1;
		this.rom=rom;
		this.owner=owner;
		this.start = start;
		this.end = slutt;
		this.description= description;
	}
	
	//Brukes kun til Â hente event ved id:
	private Event(int id,Room rom, String name, Timestamp t, Timestamp t2 , int priority, User owner, String description){
		this.id =id;
		this.name = name;
		this.rom=rom;
		this.owner = owner;
		this.priority=priority;
		this.start = t;
		this.end = t2;
		this.description=description;
	}

	public void save(Connection conn){
		id = generateID(conn);
		String addEventSql = "INSERT INTO AVTALE VALUES " + "(" + id + ",'"  + rom.getId() + "','" + name + "','" + start + "','" + end + "','" + description + "','" + priority + "','" + owner.getId() + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
			addUser(conn,owner);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//legger til bruker i eventet:
	public void addUser(Connection conn, User u){
		u.addEvent(conn, this);
	}
	
	//returnerer et map over brukere som er med, og hvilken tilstand de har (ikke svart (0) , takket ja (1), takket nei (2))
	//TODO test
	public HashMap<User, Integer> checkInviteStatus(Connection conn){
		HashMap<User, Integer> m = new HashMap<User, Integer>();
		String sql = "SELECT BRUKER.BRUKERID, BRUKERIAVTALE.STATUS FROM BRUKER, BRUKERIAVTALE WHERE BRUKER.BRUKERID=BRUKERIAVTALE.BRUKERID AND BRUKERIAVTALE.AVTALEID =" + this.id;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				m.put(User.getUser(conn, rs.getInt("BRUKER.BRUKERID")), rs.getInt("BRUKERIAVTALE.STATUS"));
			}
			stmt.close();
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//TODO
		throw new IllegalStateException("Noe gikk feil ved henting av brukere");
	}

	private int generateID(Connection conn){
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(A.AVTALEID) AS M FROM AVTALE AS A");
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
	
	//Returnerer liste over alle inviterte:
	public List<User> getUsers(Connection conn){
		List<User> l = new ArrayList<User>();
		String sql = "SELECT BRUKER.BRUKERID FROM BRUKER, BRUKERIAVTALE WHERE BRUKER.BRUKERID=BRUKERIAVTALE.BRUKERID AND BRUKERIAVTALE.AVTALEID =" + this.id;
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
		throw new IllegalStateException("Noe gikk feil ved henting av inviterte brukere i event");
	}
	
	
	public int getId() {
		if (id == -1) throw new IllegalStateException("invalid event id, call event.save() first");
		return id;
	}
	
	//henter event ved id:
public static Event getEvent(Connection conn , int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AVTALE WHERE AVTALEID = " + id);
			rs.next();
			Event u = new Event(id, Room.getRoom(conn, rs.getInt("Romid")), rs.getString("Name"), rs.getTimestamp("Start"), rs.getTimestamp("Slutt"), rs.getInt("Prioritet"), User.getUser(conn, rs.getInt("Brukerid")), rs.getString("Beskrivelse")); 
			stmt.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get event by id: " + id );
	}

	@Override
	public String toString() {
		return name + "ID: " + id;
	}
	
	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}
	public Room getRom() {
		return rom;
	}
}
