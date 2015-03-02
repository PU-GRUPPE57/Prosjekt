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
		Connection conn = Admin.getConnection("root", "");
		
		User u = User.getUser(conn, 1);
		User u1 = User.getUser(conn, 2);
		User u2 = User.getUser(conn, 3);
		User u3 = User.getUser(conn, 4);
		User u4 = User.getUser(conn, 5);
		User u5 = User.getUser(conn, 6);
		User u6 = User.getUser(conn, 7);
		Event e = Event.getEvent(conn, 1);
		
		
//		Group g1 = Group.getGroup(conn, 1);
//		u.addEvent(conn, e);
//		g.addUser(conn, u);
//		g.removeUser(conn, u);
//		
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
//		Event g5 = new Event("Hey", 1, Room.getRoom(conn,1), User.getUser(conn,1), new Timestamp(100), new Timestamp(150), "DETTE ER EN EVENT");
//		g5.save(conn);
//		Group g1 = new Group("Debeste", a);
//		Group g2 = new Group("halla!", a);
//		g1.save(conn);
//		g2.save(conn);
//		g1.addUser(conn, b);
//		g1.addUser(conn, c);
//		g1.addUser(conn, d);
//		g1.addUser(conn, e1);
//		
		for (User user: e.getUsers(conn)){
			System.out.println(user.getId());
		}
		
		Admin.closeConnection(conn);
	}
}
