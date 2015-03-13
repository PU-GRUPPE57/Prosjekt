package users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import event.Event;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserEventModel {
	private Event e;
	private User u;
	private SimpleIntegerProperty status, visibility;
	
	
	private UserEventModel(Event e, User u, SimpleIntegerProperty status, SimpleIntegerProperty visibility){
		this.u=u;
		this.e = e;
		this.status = status;
		this.visibility = visibility;
	}
	
	public static List<UserEventModel> getUserEventModels(Connection conn, Event e){
		List<UserEventModel> l = new ArrayList<>();
		for (User u : e.getUsers(conn)){
			l.add(getUserEventModel(conn, u, e));
		}
		return l;
	}
	
	private static UserEventModel getUserEventModel(Connection conn, User u,Event e){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM BRUKERIAVTALE WHERE BRUKERID = " + u.getId() +" AND AVTALEID = " + e.getId());
			rs.next();
			UserEventModel e1 = new UserEventModel(e,u, new SimpleIntegerProperty(rs.getInt("Status")),new SimpleIntegerProperty(rs.getInt("Synlighet")));
			stmt.close();
			return e1;
		} catch (SQLException err) {
			err.printStackTrace();
		}
		throw new IllegalStateException("UserEventmodel failed to generate");
	}
	
	public SimpleStringProperty userProperty(){
		return new SimpleStringProperty(u.getUsername());
	}
	public SimpleStringProperty eventProperty(){
		return new SimpleStringProperty(e.getName());
	}
	public SimpleStringProperty statusProperty(){
		return User.eventResponse(status.get());
	}
	public SimpleIntegerProperty visibilityProperty(){
		return visibility;
	}
	public User getU() {
		return u;
	}
	public Event getE() {
		return e;
	}

}
