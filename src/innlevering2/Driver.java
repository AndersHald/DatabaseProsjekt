package innlevering2;

import java.sql.*;

public class Driver {

	public static void main(String[] args) {
		try{
			// 1. Get a connection to database
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/anderoha_database", "anderoha", " ");
			
			// 2. Get a statement
			Statement myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			ResultSet myRs = myStmt.executeQuery("SELECT * FROM bruker");
			
			// 4. Process the result set
			while(myRs.next()){
				System.out.println(myRs.getString("navn"));
			}
			
		}
		catch (Exception exc ) {
			exc.printStackTrace();
		}

	}

}