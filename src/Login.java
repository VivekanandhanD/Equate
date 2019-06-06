/*
	Author : D.Vivekanandhan
*/
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Login
{
	public String Check(String user, String pass) 
	{
		String usr="",pwd="";
		DBConn connection = new DBConn();					//DBConn.java
		Connection c = connection.connect();
	      	Statement stmt = null;
	      	boolean flag = false;
	      	try {
	        	stmt = c.createStatement();
	         	String sql = "select * from Login_Details.details";
	         	ResultSet rs = stmt.executeQuery(sql);
	         	while ( rs.next() ) 
	         	{
	            		usr = rs.getString("user");
	            		pwd = rs.getString("pass");
	            		if((user.equals(usr)&&(pass.equals(pwd))))
	            		{
	            			flag=true;
	            		}
	         	}
	         	rs.close();
	         	stmt.close();
	         	c.close();
	         	if(flag==false)
	         		return "Failed";
	         	return "Success";
	      	} catch ( Exception e ) {
	         	System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         	return "Error "+e.toString();
	         }
	
	    }
}
