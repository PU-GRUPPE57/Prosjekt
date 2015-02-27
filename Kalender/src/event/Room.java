package event;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	private String Id;
	private int size;
	private String type;
	
	private String sqlgetRoom;
	
	
	private List<Room> getRoomsWithCapacity(int cap){
		//TODO
		return new ArrayList<Room>();
	}
	
	public String getId() {
		return Id;
	}

}