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

public class Deletedata
{
	String schema = "vivek";
	public String deleteRow(String id)
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement deletestmt = null;
		Statement updatestmt = null;
		Statement selectstmt = null;
		String result="",entity="", table="";
		//int ID = Integer.parseInt(id);
		int amount = 0, debit = 0, credit = 0; 
		boolean flag=false;
		String err = "";
		try
		{
			deletestmt = con.createStatement();
			selectstmt = con.createStatement();
			updatestmt = con.createStatement();
			String query= "SELECT entity,debit,credit FROM "+schema+".daybook WHERE id="+id;
			rs = selectstmt.executeQuery(query);
			while(rs.next())
               {
				entity = rs.getString("entity");
				debit = rs.getInt("debit");
				credit = rs.getInt("credit");
               }

			if(debit==0)
			{
				query= "SELECT amount FROM "+schema+".creditor WHERE entity='"+entity+"'";
               	rs = selectstmt.executeQuery(query);
               	while(rs.next())
               	{
               		amount = rs.getInt("amount");
               		table="creditor";
               		amount = amount - credit;
               	}
			}
               else if (credit == 0)
               {
               	query= "SELECT amount FROM "+schema+".debtor WHERE entity='"+entity+"'";
               	rs = selectstmt.executeQuery(query);
               	while(rs.next())
               	{
               		amount = rs.getInt("amount");
               		table="debtor";
               		amount = amount - debit;
               	}
               }
               query = "UPDATE "+schema+"."+table+" SET amount="+amount+" WHERE entity='"+entity+"';";
               updatestmt.executeUpdate(query);
			query = "DELETE FROM "+schema+".daybook WHERE id="+id+";";
			deletestmt.executeUpdate(query);
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
               System.out.println(err);
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
				System.out.println(e);
				return e.getClass().getName()+" : "+ e.getMessage();
			}
          }
          return "Success";
	}
}
