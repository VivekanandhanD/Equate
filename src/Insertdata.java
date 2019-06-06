/*
	Author : D.Vivekanandhan
*/
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class Insertdata
{
	String schema = "vivek";
	public String insertRow(String _date,String entity,int debit,int credit,String description)
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement insertstmt = null;
		Statement selectstmt = null;
		SimpleDateFormat gridformat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		String result=null, table="";
		int amount = 0; 
		boolean flag=false;
		String err = "";
		try
		{
			date = gridformat.parse(_date);
			insertstmt = con.createStatement();
			selectstmt = con.createStatement();
			String query= "SELECT amount FROM "+schema+".debtor WHERE entity='"+entity+"'";
			rs = selectstmt.executeQuery(query);
			while(rs.next())
               {
               	amount = rs.getInt("amount");
               	flag=true;
               	table="debtor";
               	amount = amount + debit;
               }
               if (!flag)
               {
               	query= "SELECT amount FROM "+schema+".creditor WHERE entity='"+entity+"'";
               	rs = selectstmt.executeQuery(query);
               	while(rs.next())
               	{
               		amount = rs.getInt("amount");
               		table="creditor";
               		amount = amount + credit;
               	}
               }
               
			query = "INSERT INTO "+schema+".daybook(date, entity, debit, credit, description)VALUES ('"+date+"', '"+entity+"', "+debit+", "+credit+", '"+description+"');";
			insertstmt.executeUpdate(query);
			
			query = "UPDATE "+schema+"."+table+" SET amount="+amount+" WHERE entity = '"+entity+"';";
			insertstmt.executeUpdate(query);
			/*query="SELECT * FROM daybook WHERE entity='"+entity+"'";
			rs = selectstmt.executeQuery(query);
			while(rs.next())
               {
               	String text = rs.getDate("date").toString();
               	date = sqlformat.parse(text);
               	result = gridformat.format(date);
               }*/
          }
          catch(Exception e)
          {
          	err = e.getMessage()+"\n";
          	StackTraceElement[] elements = e.getStackTrace();  
            	for (int iterator=1; iterator<=elements.length; iterator++)  
                   err += "Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber()+"\n";
        		//return err;
          }
          finally
          {
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				//return e.getClass().getName()+" : "+ e.getMessage();
			}
          }
          return "Success";
	}

	public String newEntity(String entity, String type)
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
		Statement insertstmt = null;
		try
		{
			insertstmt = con.createStatement();
			String query = "INSERT INTO "+schema+"."+type+"(entity,amount) VALUES ('"+entity+"',0);";
			insertstmt.executeUpdate(query);
			return "Success";
		}
          catch(Exception e)
          {
          	String err = e.getMessage()+"\n";
          	StackTraceElement[] elements = e.getStackTrace();  
            	for (int iterator=1; iterator<=elements.length; iterator++)  
                   err += "Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber()+"\n";
               return err;
          }
          finally
          {
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				return e.getClass().getName()+" : "+ e.getMessage();
			}
          }
	}
}
