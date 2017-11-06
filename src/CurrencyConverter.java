import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CurrencyConverter
 */
@WebServlet("/CurrencyConverter")
public class CurrencyConverter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String DB_DRIVE_NAME  = "com.mysql.jdbc.Driver";
	String DB_NAME    = "currency";
	String DB_SERVER_NAME = "uml.cs.uga.edu";
	String DB_CONNECTION_URL = "jdbc:mysql://uml.cs.uga.edu:3306/currency";
	String DB_CONNECTION_USERNAME = "demo";
	String DB_CONNECTION_PWD = "demo";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CurrencyConverter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		String title = "Completed Conversion";
		String value = null;
		String origCurr = null;
		String targetCurr = null;
		Connection c1 = this.connect();
		
		response.setContentType("text/html");
		
		try {
			out = response.getWriter();
		}
		catch (IOException ioe){
			System.out.println("HelloServlet: " + ioe);
		}
		
		value = request.getParameter("value");
		origCurr = request.getParameter("currsymbol1");
		targetCurr = request.getParameter("currsymbol2");
		
		try {
			PreparedStatement ps1 = c1.prepareStatement("SELECT dollarvalue FROM currency WHERE currsymbol=?");
			PreparedStatement ps2 = c1.prepareStatement("SELECT dollarvalue FROM currency WHERE currsymbol=?");
			ps1.setString(1, origCurr);
			ps2.setString(1, targetCurr);
			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			
			String result1;
			String result2;
			
			if (rs1.next())
				result1 = rs1.getString(1);
			else
				result1 = null;
			
			if (rs2.next())
				result2 = rs2.getString(1);
			else
				result2 = null;
			
			String convertedValue = convert(result1, result2, value);
			
			out.println("<html><head><title>" + title + "</title></head>");
			out.println("<body>");
			
			out.println("<h3>" + "Result" + "<h3>");
			out.println("<p>" + value + " " + origCurr + " = " + convertedValue + " "  + targetCurr + "</p>");
			
			out.println("</body>");
			out.println("</html>");
			
			out.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String convert(String curr1, String curr2, String value) {
		float c1 = Float.parseFloat(curr1);
		float c2 = Float.parseFloat(curr2);
		float v =  Float.parseFloat(value);
		
		return (Float.toString((v/c1)*c2));
	}
	
	private Connection connect() {
		Properties connectionProps = new Properties();
        connectionProps.put("user", DB_CONNECTION_USERNAME);
        connectionProps.put("password", DB_CONNECTION_PWD);

    	Connection conn = null; 
        try 
        {
            Class.forName(DB_DRIVE_NAME);
        } 
        
        catch (ClassNotFoundException ex) 
        {
        	ex.getMessage(); 
        }
        
        try 
        {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, connectionProps); 
        } 
        catch (SQLException ex) {
        	ex.getMessage(); 
        }
		return conn;
	}

}
