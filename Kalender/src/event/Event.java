package event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	


	public Event(String name, int priority){
		if (!name.matches("[a-ÂA-≈0-9]+")){
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.priority = priority;
		this.id = generateID();
	}

	public void save(){
		String addEventSql = "INSERT INTO AVTALE VALUES ";
		Connection conn = Admin.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(addEventSql + "(" + id + ",'"  + name + "','" + rom.getId() + "','" + owner.getId() + "','" + priority + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int generateID(){
		Connection conn = Admin.getConnection();
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(A.AVTALEID) AS M FROM AVTALE AS A");
			rs.next();
			if (rs == null) res = 1;
			else res=rs.getInt("M") + 1;
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("ID-generation failed");
	}

	public List<User> getParticipants(){
		List<User> l = new ArrayList();
		Connection conn = Admin.getConnection();
		// Finner brukerID, navn og avtaleID i alle som er medlem av avtale
		String sql = "select B.fornavn,B.etternavn"
				+ " from bruker as B, BrukerAvtale as BA "
				+ "where B.BrukerID=BA.BrukerID AND BA.AvtaleID= " + id;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				l.add(new User(rs.getString("FIRSTNAME"),rs.getString("LASTNAME")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return l;
	}

	public int getId() {
		return id;
	}
}
