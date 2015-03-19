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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import notification.Varsel;
import notification.Varsel.EventMessages;
import notification.Varsel.UserMessages;
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
		
		if (start.after(slutt)) throw new IllegalArgumentException("Starttid etter sluttid");
		this.name = name;
		this.priority = priority;
		this.id = -1;
		this.rom=rom;
		this.owner=owner;
		start.setSeconds(0);
		start.setNanos(0);		
		slutt.setSeconds(0);
		slutt.setNanos(0);
		
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
		
		String addEventSql = "INSERT INTO AVTALE VALUES " + "(" + id + ",'" + name +  "','" + start + "','" + end + "','" + description + "','" + priority + "','" + owner.getId() + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
			addUser(conn,owner);

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
		try{
			Statement stmt = conn.createStatement();
			List<User> users = getUsers(conn);
			for (User user : users) {
				Varsel v = new Varsel(user, EventMessages.EVENT_DELETED, this);
				v.save(conn);
			}
			stmt.executeUpdate(deleteEventSql);
			this.removeReservation(conn);
			stmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void removeReservation(Connection conn){
		String deleteEventSql = "DELETE FROM ROMRES WHERE AVTALEID=" + id;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deleteEventSql);
			stmt.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.rom = null;
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

	public void changeTime(Connection conn, User u, Timestamp start, Timestamp slutt){
		if (start.after(slutt))throw new IllegalArgumentException("starttid etter sluttid");
		this.start = start;
		this.end = slutt;

		String sql = "UPDATE AVTALE SET START= '" + start + "', SLUTT= '"+ slutt + "' WHERE AVTALEID= " + id;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			List<User> participants = getUsers(conn);
			for (User user : participants) {
				Varsel v = new Varsel(user, EventMessages.EVENT_ENDRET, this);
				v.save(conn);
			}
		}catch(SQLException e){
			e.printStackTrace();
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

	
	public String getRoom(Connection conn){
		String Sql = "SELECT ROM.NAVN FROM AVTALE,ROMRES, ROM WHERE AVTALE.AVTALEID=ROMRES.AVTALEID AND AVTALE.AVTALEID=" + id + " AND ROM.ROMID = ROMRES.ROMID";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(Sql);
			if (!rs.next()) return null;
			String s = rs.getString("NAVN");
			stmt.close();
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}return null;
	}
	
	public void romres(Connection conn, Room r) {
		r.romRes(conn, this);
	}
	public int getPriority() {
		return priority;
	}
}
