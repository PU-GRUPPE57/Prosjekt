package event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import users.Admin;
import users.User;

public class Event {

	private int id;
	private String name;
	private Room rom;
	private User owner;
	private int priority;
	private Timestamp time;
	

	public Event(String name, int priority, Room rom, User owner, Timestamp t){
		if (!name.matches("[a-ÂA-≈0-9]+")){
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.priority = priority;
		this.id = -1;
		this.rom=rom;
		this.owner=owner;
		this.time = t;
	}
	
	//Brukes kun til Â hente event ved id:
	private Event(int id,Room rom, String name, Timestamp t , int priority, User owner){
		this.id =id;
		this.name = name;
		this.rom=rom;
		this.owner = owner;
		this.priority=priority;
	}

	public void save(Connection conn){
		id = generateID(conn);
		String addEventSql = "INSERT INTO AVTALE VALUES " + "(" + id + ",'"  + rom.getId() + "','" + name + "','" + time + "','" + priority + "','" + owner.getId() + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	//Ikke testet enda:
	public List<User> getParticipants(Connection conn){
		List<User> l = new ArrayList();
		// Finner brukerID, navn og avtaleID i alle som er medlem av avtale
		String sql = "select B.fornavn,B.etternavn"
				+ " from bruker as B, BrukerAvtale as BA "
				+ "where B.BrukerID=BA.BrukerID AND BA.AvtaleID= " + id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(User.getUser(conn, rs.getInt("BrukerID")));
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return l;
	}

	public int getId() {
		if (id == -1) throw new IllegalStateException("invalid event id, call event.save() first");
		return id;
	}
	
public static Event getEvent(Connection conn , int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AVTALE WHERE AVTALEID = " + id);
			rs.next();
			Event u = new Event(id, Room.getRoom(conn, rs.getInt("Romid")), rs.getString("Name"), rs.getTimestamp("Tidpunkt"), rs.getInt("Prioritet"), User.getUser(conn, rs.getInt("Brukerid"))); 
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
}
