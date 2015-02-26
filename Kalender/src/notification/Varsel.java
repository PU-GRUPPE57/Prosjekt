package notification;

import users.Group;
import users.User;

public class Varsel {
	
	
	public void fireGroupMessage(Group g){
		g.fireMessage();
	}
	
	public void fireIndividualMessage(User u){
		u.fireMessage();
	}
	
	
	public void addListener(Listener list){
		
	}
	
	public void removeListener(Listener list){
		
	}
	
	
}
