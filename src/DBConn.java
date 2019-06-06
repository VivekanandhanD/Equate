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

public class DBConn
{
	public String URL,DATABASE,USERNAME,PASSWORD, PROPERTY = "DBConn.properties";
	public Connection connect() 
	{
		try
		{
			InputStream is = this.getClass().getResourceAsStream(PROPERTY);
			Properties props = new Properties();
			props.load(is);
			URL = (String) props.get("URL");
			DATABASE = (String) props.get("DBNAME");
			USERNAME = (String) props.get("USERNAME");
			PASSWORD = (String) props.get("PASSWORD");
			is.close();
		}
		catch(Exception err)
		{
			System.err.println(err.toString());
		}
		Connection c = null;
	      	try {
	        	Class.forName("org.postgresql.Driver");
	         	c = DriverManager.getConnection(URL+DATABASE,USERNAME,PASSWORD);
	         	System.out.println("Opened database successfully");
	      	}
	      	catch ( Exception e )
	      	{
	         	System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        }
	        return c;
	
	}
	public Connection connect(String classname,String url, String user, String pass)
	{
		Connection c = null;
		try {
	        	Class.forName(classname);
	         	c = DriverManager.getConnection(url,user,pass);
	         	System.out.println("Opened database successfully");
	      	}
	      	catch ( Exception e )
	      	{
	        	System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	        }
	        return c;
	}
}
