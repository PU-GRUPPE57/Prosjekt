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

	private String eventId;
	private String name;
	
	public void save(){
		
	}
	
	public List<User> getParticipants(){
		List<User> l = new ArrayList();
		Connection conn = Admin.getConnection();	
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(B.BRUKERID) AS M FROM BRUKER AS B");
			while(rs.next()){
				l.add(new User())
				fname, lname, username
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return l;
	}
	
	
}
