package notification;

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
	
//	public void fireGroupMessage(Group g){
//		g.fireMessage();
//	}
//	
//	public void fireIndividualMessage(User u){
//		u.fireMessage();
//	}
//	
	
	public void addListener(Listener list){
		
	}
	
	public void removeListener(Listener list){
		
	}
	
	
}
