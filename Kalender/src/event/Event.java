package event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import notification.Varsel;
import notification.Varsel.EventMessages;
import notification.Varsel.UserMessages;
import users.Admin;
import users.User;

public class Event {

	private int id, priority;
	private String name, description;
	private Room rom;
	private User owner;
	private Timestamp start, end;


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
		String addEventSql = "INSERT INTO AVTALE VALUES " + "(" + id + ",'" + name + "','" + start + "','" + end + "','" + description + "','" + priority + "','" + owner.getId() + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
			addUser(conn,owner);
			//			rom.romRes(conn, this);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void replyToInvitation(Connection conn,User u, int reply){
		if (reply<1 || reply>2) throw new IllegalArgumentException("Invalid reply to invitation");
		String sql = "UPDATE BRUKERIAVTALE SET STATUS= " + reply + " WHERE AVTALEID= " + id +" AND BRUKERID= " + u.getId();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			if (!(owner.getId()==u.getId())){
				Varsel v = new Varsel(owner, reply==1 ? UserMessages.EVENT_ACCEPTED : UserMessages.EVENT_DECLINED, u , this);
				v.save(conn);				
			}
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
		if (id == 0) return null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AVTALE WHERE AVTALEID = " + id);
			rs.next();
			Event u = new Event(id, null, rs.getString("Name"), rs.getTimestamp("Start"), rs.getTimestamp("Slutt"), rs.getInt("Prioritet"), User.getUser(conn, rs.getInt("Brukerid")), rs.getString("Beskrivelse")); 
			stmt.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get event by id: " + id );
	}

	public void deleteEvent(Connection conn){
		String deleteEventSql = "DELETE FROM AVTALE WHERE AVTALEID=" + id;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	//TODO test
	public void changeTime(Connection conn, User u, Timestamp start, Timestamp slutt){
		if (u == owner){
			this.start = start;
			this.end = slutt;
			List<User> participants = getUsers(conn);
			for (User user : participants) {
				Varsel v = new Varsel(user, EventMessages.EVENT_ENDRET, this);
				v.save(conn);
			}
		}
	}
	public Timestamp getStart() {
		return start;
	}
	
	public Timestamp getStartReduced(){
		return new Timestamp(start.getYear(), start.getMonth(), start.getDate(), 0 ,0 ,0,0);
		
	}
	
	public Timestamp getEnd() {
		return end;
	}
	
	public static Timestamp convertTimestamp(String day, String month, String year){
		DateFormat dateformat = new SimpleDateFormat("dd/MM/YYYY");
		Date date;
		try {
			date = dateformat.parse(day + "/" + month + "/" + year);
			long time = date.getTime();
			return new Timestamp(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
