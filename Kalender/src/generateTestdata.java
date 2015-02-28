import java.sql.Connection;

import event.Event;
import event.Room;
import users.Admin;
import users.Group;
import users.User;


public class generateTestdata {

	public static void main(String[] args) {
		Connection conn = Admin.getConnection();
		User u = User.getUser(conn, 1);
		Event e = Event.getEvent(conn, 1);
		
		u.addEvent(conn, e);
		
//		User a = new User("Sindre", "Flood", "aksjf","asjkfa");
//		User b = new User("Simen", "Hendrick", "aksjf","asjkfa");
//		User c = new User("Eivind", "Asne", "aksjf","asjkfa");
//		User d = new User("Ken", "Midtgard", "aksjf","asjkfa");
//		User e = new User("Robert", "Normand", "aksjf","asjkfa");
//		a.save(conn); 
//		b.save(conn);
//		c.save(conn);
//		d.save(conn);
//		e.save(conn);
//		Room r1 = new Room(5);
//		r1.save(conn);
//		Event g = new Event("Hey", 1, Room.getRoom(conn,1), User.getUser(conn,1));
//		g.save(conn);
//		Group g1 = new Group("Debeste");
//		Group g2 = new Group("halla!");
//		g1.save(conn);
//		g2.save(conn);
//		g1.addUser(conn, a);
		Admin.closeConnection(conn);
	}
}
