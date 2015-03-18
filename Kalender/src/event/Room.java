package event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.sun.accessibility.internal.resources.accessibility;

import notification.Varsel;
import notification.Varsel.EventMessages;
import users.Admin;
import users.User;

public class Room {

	private int id, size;
	private String name;
	private Types type;


	public static enum Types{
		MOTEROM, KONFERANSEROM, GRUPPEROM, DATASAL, AUDITORIUM;
	}

	public Room(int size, String name, Types type){
		this.id= -1;
		this.size = size;
		this.name = name;
		this.type = type;
	}
	//brukes til å hente rom ved id
	private Room(int id, int size, String name, Types type){
		this.id=id;
		this.size=size;
		this.name = name;
		this.type = type;
	}

	public void save(Connection conn){
		id = generateID(conn);
		String addRomSql = "INSERT INTO ROM VALUES " + "(" + id + ",'"  + size +"','" + name + "','" + type + "')";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addRomSql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Room getRoom(Connection conn, int id){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ROM WHERE ROMID = " + id);
			rs.next();
			Room r = new Room(rs.getInt("Romid"), rs.getInt("Size"), rs.getString("Navn"), Types.valueOf(rs.getString("Type")));
			stmt.close();
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to retrieve room by id:" + id);
	}
	
	public static Room getRoom(Connection conn, String name){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ROM WHERE ROM.NAVN = " + "'" +  name + "'");
			rs.next();
			Room r = new Room(rs.getInt("Romid"), rs.getInt("Size"), rs.getString("Navn"), Types.valueOf(rs.getString("Type")));
			stmt.close();
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to retrieve room by name:" + name);
	}

	private List<Room> getRoomsWithCapacity(int cap){
		//TODO
		return new ArrayList<Room>();
	}

	public int getId() {
		if (id == -1) throw new IllegalStateException("Room ID not set, use room.save() first");
		return id;
	}

	private int generateID(Connection conn){
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(R.ROMID) AS M FROM Rom AS R");
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

	public void romRes(Connection conn, Event ev){
		
		Timestamp t1 = ev.getStart();
		Timestamp t2 = ev.getEnd();
		if (t1.getMinutes() != 0){
			t1.setHours(t1.getHours()-1);
			t1.setMinutes(0);
			t1.setSeconds(0);
			t1.setNanos(0);
		}
		if (t2.getMinutes() != 0){
			t2.setHours(t1.getHours()+1);
			t2.setMinutes(0);
			t2.setSeconds(0);
			t2.setNanos(0);
		}

		String addEventSql = "INSERT INTO ROMRES VALUES " + "(" + id + ",'" + t1 + "','" + t2 + "'," + ev.getId() + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql);
			stmt.close();
			List<User> participants = ev.getUsers(conn);
			for (User user : participants) {
				Varsel v = new Varsel(user, EventMessages.ROM_RESERVERT, ev);
				v.save(conn);				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	@Override
	public String toString() {
		return name + " " +String.valueOf(type);
	}
	public String getName() {
		return name;
	}

	private static List<Room> getAllRooms(Connection conn){
		List<Room> l = new ArrayList<Room>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ROM" );
			while(rs.next()){
				l.add(Room.getRoom(conn, rs.getString("navn")));
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get all rooms");

	}


	private static List<Room> getReserved(Connection conn, Timestamp start, Timestamp end){
		List<Room> l = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ROM, ROMRES WHERE ROM.ROMID = ROMRES.ROMID" );
			while(rs.next()){
				Timestamp t1 = rs.getTimestamp("Start");
				Timestamp t2 = rs.getTimestamp("Slutt");
				
				if ((start.compareTo(t1) <= 0) && (end.compareTo(t1) >= 0)){
					l.add(Room.getRoom(conn,rs.getString("navn")));
				}else if ((start.compareTo(t1) >= 0) && (start.compareTo(t2) <=0)){
					l.add(Room.getRoom(conn,rs.getString("navn")));					
				}else if ((end.compareTo(t1) >= 0) && (end.compareTo(t2) <=0)){
					l.add(Room.getRoom(conn,rs.getString("navn")));	
				}
			}
			stmt.close();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to get reserved rooms");

	}

	public static List<Room> getRooms(Connection conn, Timestamp start, Timestamp end){
		List<Room> l = getAllRooms(conn);
		List<Room> l2 = getReserved(conn, start, end);
		for (Room room : l2) {
			for (Room room2 : l) {
				if (room2.id == room.id){
					l.remove(room2);
					break;
				}
			}
		}
		return l;
	}
	
	public SimpleStringProperty nameProperty(){
		return new SimpleStringProperty(name);
	}
	public SimpleStringProperty capProperty(){
		return new SimpleStringProperty(String.valueOf(size));
	}
	public SimpleStringProperty typeProperty(){
		return new SimpleStringProperty(String.valueOf(type));
	}
}