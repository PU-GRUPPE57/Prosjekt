package users;

import java.sql.Connection;

public class Connectable {
	
	protected Connection conn;
	
	public Connectable(Connection conn){
		this.conn = conn;
	}

}
