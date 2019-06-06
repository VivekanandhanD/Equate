/*
	Author : D.Vivekanandhan
*/
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import com.google.gson.*;

public class Servlet extends HttpServlet
{
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request,response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String uri = request.getRequestURI();
		String strAction = request.getParameter("action");
		String user="",usr="",pass="";
		JsonUtil json = new JsonUtil();
		Extractdata extract = new Extractdata();							//Extractdata.java
		try
		{
			if(session == null)
			{
				if(uri.endsWith("login.do"))
				{
					Login check = new Login();
					user = request.getParameter("uname");
					pass = request.getParameter("pwd");
   					String login= check.Check(user,pass);
			   		if(login == "Success")
			   		{
						session = request.getSession(true);
						session.setAttribute("user", user);
						session.setMaxInactiveInterval(300*60);
						usr= (String)session.getAttribute("user");
						out.println(usr);
					}
					else
						out.println(login);
				}
				else
				{
					RequestDispatcher dispatcher = request.getRequestDispatcher("/web/login.html");
					dispatcher.forward(request,response);
				}
		   	}
		   	else if(uri.endsWith("dbconn.do"))
			{
				Login check = new Login();
   				String login=check.Check("admin","admin!@#");
			   	out.println("<html><head><title>This is a response from Servlet</title></head><body>");
			   	usr= (String)session.getAttribute("user");
			   	out.println("<h1>Hello "+usr+"</h1>");
			   	out.println(login);
			   	out.println("</body></html>");
		   	}
		   	else if(uri.endsWith("logout.do"))
		   	{
		   		session.invalidate();
		   		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/login.html");
				dispatcher.forward(request,response);
		   	}
		   	else if(uri.endsWith("homedashboard.do"))
		   	{
				RequestDispatcher dispatcher = request.getRequestDispatcher("/web/homedashboard.html");
				dispatcher.forward(request,response);
		   	}
		   	else if(uri.endsWith("daybook.do"))
		   	{
		   		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/daybook.html");
				dispatcher.forward(request,response);
		   	}
		   	else if(uri.endsWith("ledger.do"))
		   	{
		   		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/ledger.html");
				dispatcher.forward(request,response);
		   	}
			else if(uri.endsWith("dashboard.do"))
		   	{
		   		String from = request.getParameter("start");
				String to = request.getParameter("end");
				int[] data = extract.dashboardData(from,to);
		   		out.println(Arrays.toString(data));
		   	}
		   	else if(uri.endsWith("dashboardChart.do"))
		   	{
				String from = request.getParameter("from");
				String to = request.getParameter("to");
				List data = extract.dashboardChart(from,to);
		   		out.println(json.getJsonStringFromList(data));
		   	}
		   	else if(uri.endsWith("daybookdata.do"))
		   	{
		   		String from = request.getParameter("from");
				String to = request.getParameter("to");
				List data = extract.daybook(from,to);
		   		out.println(json.getJsonStringFromList(data));
		   	}
		   	else if(uri.endsWith("newentity.do"))
		   	{
		   		Insertdata insert = new Insertdata();							//Insertdata.java
		   		String ent = request.getParameter("entity");
				String ty = request.getParameter("type");
				String result = insert.newEntity(ent,ty);
		   		out.println(result);
		   	}
		   	else if(uri.endsWith("debtorEntity.do"))
		   	{
		   		List data = extract.daybookMenu('d');
		   		out.println(json.getJsonStringFromList(data));
		   	}
		   	else if(uri.endsWith("creditorEntity.do"))
		   	{
		   		List data = extract.daybookMenu('c');
		   		out.println(json.getJsonStringFromList(data));
		   	}
		   	else if(uri.endsWith("deleteEntry.do"))
		   	{
		   		Deletedata delete = new Deletedata();							//Deletedata.java
		   		String id = request.getParameter("id");
		   		String result = delete.deleteRow(id);
		   		out.println(result);
		   	}
		   	else if(uri.endsWith("insertRow.do"))
		   	{
		   		Insertdata insert = new Insertdata();							//Insertdata.java
				String date = request.getParameter("date");
				String entity = request.getParameter("entity");
				int debit = Integer.parseInt(request.getParameter("debit"));
				int credit = Integer.parseInt(request.getParameter("credit"));
   				String description = request.getParameter("description");
				String data = insert.insertRow(date,entity,debit,credit,description);
		   		out.println(data);
		   	}
		   	else
		   	{
		   		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/index.html");
				dispatcher.forward(request,response);
		   	}
   		}
   		catch(Exception err)
   		{
   			out.println(err.getClass().getName()+" : "+ err.getMessage());
   		}
   	}
   	 
}
