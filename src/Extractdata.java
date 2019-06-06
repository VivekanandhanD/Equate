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

public class Extractdata
{

	String schema = "vivek";
	public List daybook(String From, String To) 
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement stmt = null;
		List dataList;
		Map map;
		SimpleDateFormat gridformat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd");
		//Date from = sqlformat.parse(from);
		//Date to = sqlformat.parse(to);
		try {
	        	dataList = new ArrayList();
			String Sql=null;
			stmt = con.createStatement();

               Sql ="SELECT * FROM "+schema+".daybook where date BETWEEN '"+From+"' AND '"+To+"' ORDER BY date DESC, id DESC";

			System.out.println(Sql);
			
			rs = stmt.executeQuery(Sql);
			while (rs.next()){
				map = new LinkedHashMap<String, String>();
				String text = rs.getDate("date").toString();
				Date date = sqlformat.parse(text);
				map.put("Date", gridformat.format(date));
                    map.put("Entity", rs.getString("entity"));
				if(rs.getInt("debit")!= 0)
				 map.put("Debit",  rs.getInt("debit"));
				else
				 map.put("Debit",  "");
				if(rs.getInt("credit")!= 0)
				 map.put("Credit",  rs.getInt("credit"));
				else
				 map.put("Credit",  "");
               	map.put("Description", rs.getString("description"));
               	map.put("Id", rs.getString("id"));
               	
				dataList.add(map);
			}
			return dataList;
		}
		catch (Exception e) 
		{
               List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()));
               e.printStackTrace();
			return error;
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()+" Error in finally Block"));
                    e.printStackTrace();
                    return error;
			}
		}
	}

	public List daybookMenu(char tab)
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement stmt = null;
		List<String> result;
		String table="debtor";
		try
		{
			result = new ArrayList<String>();
			String Sql=null;
			stmt = con.createStatement();
			if(tab=='c')
				table = "creditor";
               Sql ="SELECT entity FROM "+schema+"."+table+" ORDER BY entity";

			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				result.add((String)rs.getString("entity"));
			}
		}
		catch (Exception e)
		{
			List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()));
               return error;
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()));
               	return error;
			}
		}
		return result;
	}

	public List dashboardChart(String from, String to) 
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement stmt = null;
		List dataList;
		Map map;
		try
		{
	        	dataList = new ArrayList();
			String Sql=null;
			stmt = con.createStatement();

               Sql ="SELECT entity, SUM(debit) FROM "+schema+".daybook WHERE credit=0 AND date BETWEEN '"+from+"' AND '"+to+"' GROUP BY entity order by sum desc";

			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				map = new LinkedHashMap<Object, Object>();
				String entity = rs.getString("entity");
				int amount = rs.getInt("sum");
				map.put("entity", entity);
				map.put("amount", amount);
				dataList.add(map);
			}
		}
		catch (Exception e) 
		{
               List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()));
               e.printStackTrace();
			return error;
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				List<String> error = new ArrayList<String>(Arrays.asList(e.getClass().getName()+" : "+ e.getMessage()+" Error in finally Block"));
                    e.printStackTrace();
                    return error;
			}
		}
		return dataList;
	}

	public int[] dashboardData(String from, String to)
	{
		DBConn connection = new DBConn();						//DBConn.java
		Connection con = connection.connect();
	     ResultSet rs = null;
		Statement stmt = null;
		int[] array = new int[5];
		int id,debit =0, credit=0;
		try
		{
			String Sql=null;
			stmt = con.createStatement();

               Sql ="SELECT * FROM "+schema+".creditor";

			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				id = rs.getInt("id");
				if(id==1)
					array[0] = rs.getInt("amount");
				if(id==2)
					array[1] = rs.getInt("amount");
			}
			
			Sql ="SELECT (select SUM(amount) from "+schema+".creditor)-(select SUM(amount) from "+schema+".debtor) as balance;";
			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				array[2] = rs.getInt("balance");
			}

			Sql ="SELECT (select SUM(amount) from "+schema+".creditor)-(select SUM(amount) from "+schema+".debtor) as balance;";
			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				array[3] = rs.getInt("balance");
			}

			Sql ="SELECT SUM(debit) FROM "+schema+".daybook WHERE date BETWEEN '"+from+"' AND '"+to+"';";
			rs = stmt.executeQuery(Sql);
			while (rs.next())
			{
				array[4] = rs.getInt("sum");
			}
		}
		catch (Exception e) 
		{
               System.out.println(e);
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
			}
		}
		return array;
	}
}
