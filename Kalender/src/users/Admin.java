package users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {

	public static Connection getConnection(){
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		final String DB_URL = "jdbc:mysql://localhost:3306/test2";

		//  Database credentials
		final String USER = "root";
		final String PASS = "";

		Connection conn = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");


			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConnection(Connection conn, Statement stmt){
		try{
			conn.close();
			stmt.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
				try{
					if(conn!=null)
						conn.close();
				}catch(SQLException se1){
					se1.printStackTrace();
				}//end finally try
		}// end try
	}

}
