import java.sql.Connection;
import java.sql.Timestamp;

import notification.Varsel;
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
//		Group g = Group.getGroup(conn, 5);
		u.addEvent(conn, e);
//		g.addUser(conn, u);
//		g.removeUser(conn, u);
		
//		User a = new User("Sindre", "Flood", "aksjf","asjkfa");
//		User b = new User("Simen", "Hendrick", "aksjf","asjkfa");
//		User c = new User("Eivind", "Asne", "aksjf","asjkfa");
//		User d = new User("Ken", "Midtgard", "aksjf","asjkfa");
//		User e1 = new User("Robert", "Normand", "aksjf","asjkfa");
//		a.save(conn); 
//		b.save(conn);
//		c.save(conn);
//		d.save(conn);
//		e1.save(conn);
//		Room r1 = new Room(5);
//		r1.save(conn);
//		Event g5 = new Event("Hey", 1, Room.getRoom(conn,1), User.getUser(conn,1), new Timestamp(100));
//		g5.save(conn);
//		Group g1 = new Group("Debeste");
//		Group g2 = new Group("halla!");
//		g1.save(conn);
//		g2.save(conn);
//		g1.addUser(conn, a);
		Admin.closeConnection(conn);
	}
}
