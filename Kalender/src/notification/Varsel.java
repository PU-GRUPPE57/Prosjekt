package notification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import event.Event;
import event.Room;
import users.Group;
import users.User;

public class Varsel {

	private int id;
	private Event event;
	private Group group;
	private User repliedUser;
	private String text;
	private int type;
	private User user;

	public Varsel(User user, EventMessages m, Event e){
		this.event = e;
		this.user = user;
		this.group = null;
		type = 1;
		generateText(null, m,null);
	}


	public Varsel(User user, GroupMessages m, Group g){
		this.group = g;
		this.user = user;
		this.event = null;
		type = 2;
		generateText(m,null,null);
	}

	public Varsel(User user, UserMessages m, User u, Event e){
		this.user = user;
		this.repliedUser = u;
		this.event = e;
		this.group=null;
		type = 3;
		generateText(null,null,m);
	}

	//Lagrer:
	public void save(Connection conn){
		id = generateID(conn);
		String sql1;
		if (repliedUser != null) sql1 = "INSERT INTO VARSEL VALUES " + " (" + id +"," + event.getId() + "," + null + "," + repliedUser.getId() + ",'" + text + "'," + type + ")"; 
		else if (event!=null) sql1 = "INSERT INTO VARSEL VALUES " + " (" + id + "," + event.getId() + "," + null + "," + null +",'" + text + "'," + type + ")";			
		else if (group != null) sql1 = "INSERT INTO VARSEL VALUES " + " (" + id + "," + null + "," + group.getId() + "," + null + ",'" + text + "'," + type + ")";
		else throw new IllegalStateException("Varsel ikke laget");
		String sql2 = "INSERT INTO BRUKERHARVARSEL VALUES " + " (" + user.getId() + "," + this.id + ")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql1);
			stmt.executeUpdate(sql2);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int generateID(Connection conn){
		int res;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(V.VARSELID) AS M FROM VARSEL AS V");
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

	public static enum EventMessages{
		USER_INVITE_EVENT, ROM_RESERVERT, EVENT_ENDRET;
	}

	public static enum GroupMessages{
		USER_ADDED, USER_REMOVED;
	}

	public static enum UserMessages{
		EVENT_ACCEPTED, EVENT_DECLINED;
	}

	private void generateText(GroupMessages g, EventMessages e, UserMessages u){
		if (e != null){
			switch (e){
			case USER_INVITE_EVENT: text = "Du har blitt invitert til en avtale: " + event.getName() + " av: " + event.getOwner().getUsername();
			break;
			//TODO
			case EVENT_ENDRET: text = "Eventet: " + event.getName() + " har blitt endret";
			break;
			//TODO
			case ROM_RESERVERT: text = "Rommet: " + event.getRom().getName() + " har blitt reservert for: " + event.getName();
			break;
			}
		}
		else if (g!=null){
			switch (g){
			case USER_ADDED: text = "Du har blitt lagt til i gruppe: " + group.getName() + " av: " + group.getAdmin().getUsername();
			break;
			case USER_REMOVED: text = "Du har blitt fjernet fra gruppe: " + group.getName() + " av: " + group.getAdmin().getUsername();
			break;
			}

		}
		else if (u!=null){
			switch (u){
			case EVENT_ACCEPTED: text = "Bruker: " + repliedUser.getUsername() + " har akseptert invitasjon til: " + event.getName();
			break;
			case EVENT_DECLINED: text =  "Bruker: " + repliedUser.getUsername() + " har avvist invitasjon til: " + event.getName();
			break;
			}
		}
	}
}
