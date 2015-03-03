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

public class Room {
	
	private int id;
	private int size;
	private String name;
	private String type;
	
	
	public Room(int size){
		this.id= -1;
		this.size=size;
	}
	
	//brukes til å hente rom ved id
	private Room(int id, int size){
		this.id=id;
		this.size=size;
	}
	
	public void save(Connection conn){
		id = generateID(conn);
		
		String addRomSql = "INSERT INTO ROM VALUES " + "(" + id + ",'"  + size + "')";
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
			Room r = new Room(rs.getInt("Romid"), rs.getInt("Size"));
			stmt.close();
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("failed to retrieve room by id:" + id);
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
	
	@Override
	public String toString() {
		return id + " " +size;
	}
	public String getName() {
		return name;
	}
	
	
}