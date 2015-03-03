package notification;

import event.Event;
import event.Room;
import users.Group;
import users.User;

public class Varsel {

	public static enum Messages{
		USER_ADD_GROUP(1), USER_REMOVED_GROUP(2), USER_INVITE_EVENT(3), ROM_RESERVERT(4), EVENT_ENDRET(5), EVENT_DECLINED(6);
		private int value;

		private Messages(int value){
			this.value=value;
		}
	}

	public void fireMessage(Group g, Messages m, Event e, Group g2, User u, Room r){
		String txt="";
		switch (m){
		case USER_ADD_GROUP: txt = "Du har blitt lagt til i gruppe: " + g2.getName() + " av " + u.getUsername();
		case USER_REMOVED_GROUP: txt = "Du har blitt fjernet fra gruppe: " + g2.getName() + " av " + u.getUsername();
		case USER_INVITE_EVENT: txt = "Du har blitt invitert til en avtale: " + e.getName() + " av" + u.getUsername();
		case EVENT_DECLINED: txt =  "Bruker: " + u.getUsername() + " har declined invitasjon til: " + e.getName();
		case EVENT_ENDRET: txt = "Eventet: " + e.getName() + " har blitt endret";
		case ROM_RESERVERT: txt = "Rommet: " + r.getName() + " har blitt reservert for: " + e.getName();
		}
		
		
		if (txt.equals("")) throw new IllegalStateException("Varselet ble ikke generert");
		g.fireMessage(txt);
	}

	public void fireMessage(User u, Messages m, Event e, Group g, User u2){

	}

	public static void main(String[] args) {
		//		for (Messages m : Messages.values()) {
		//		System.out.println(m.name());
		//	}
		//		System.out.println(Messages.EVENT_DECLINED.equals(Messages.EVENT_DECLINED));
	}
}
