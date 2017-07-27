package Database;
/**
 * Add the 50 general public or general practitioner queries to DB table, 
 * i.e. the ones in the required MIMIR query format
 * , so that they can be used in the retrieval functions
 * 
 * @author: liadh
 * 
 * 2017 comment:  this code acts as a sample of how to read from csv files & text files &
 * write to sqlLite database
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.csvreader.CsvReader;

///I didn't use this file but left it anyway
public class WriteQueriesToDBfor2017intern {


	//for sqllite:
	 private static String connectionUrl=""; //this path should be of the form e.g. "C:\\KhresmoiDBforIR\\Khresmoi.db"  //path to the sqlLite database to go here
	 
	// Declare the JDBC objects.
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
	
	ArrayList<String> record = new ArrayList<String>();
	
	String pathToFile = ""; //the csv file containing the 50 queries
	String queriesTableName = "";

	
	public WriteQueriesToDBfor2017intern(String pathToQueries, String q, String dbLocation){
		pathToFile = pathToQueries;
		queriesTableName = q;
		connectionUrl = dbLocation;
	}
	
	
	/**
	 * This is the write method to use for writing the queries from csv file to DB table
	 * when have the possibility of more than one lookup for a query (meaning don't always 
	 * have a new query on each row of the csv file.
	 */
	public void write() {
		
		// Declare the JDBC objects.
	      con = null;
	      stmt = null;
	      rs = null;
	    
	      try {
		       
	    	  	 // Establish the connection - sqllite server:
	  			 Class.forName("org.sqlite.JDBC");
	  			 con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
	    	  
	  		    
	  			 //sqllite server:
		         String sql="CREATE TABLE "+queriesTableName+"([ID] INT, raw_query TEXT, query_in_Mimir_format TEXT, inst_lookupFromQuery TEXT) ";	
		         
		         Statement stat=con.createStatement();
		         stat.execute(sql);

		         try 
		         {
		        	 FileReader read = new FileReader(pathToFile);
			
		        	 CsvReader reader = new CsvReader(read); 
						
		        	 try 
		        	 {
		        		 reader.readHeaders();

		        		 while (reader.readRecord())
		        		 {
		        			 
		        			 //for sqllite server:
		        			 String SQL = "INSERT INTO "+queriesTableName+"(ID, raw_query, query_in_Mimir_format, inst_lookupFromQuery) VALUES(?, ?, ?, ?);";
		        			 
		        			 PreparedStatement pstmt = con.prepareStatement(SQL); // create a statement
        			  
		        			 String id = reader.get("Query_number"); //System.out.println("id: "+id);
		        			 pstmt.setString(1, id); 
		        			 String query = reader.get("Text_query_in_English_with_correct_spelling"); //System.out.println("query: "+query);
		        			 pstmt.setString(2, query); 
		        			 String mimirquery = reader.get("Queries_converted_to_MIMIR_format"); //System.out.println("mimirquery: "+mimirquery);
		        			 pstmt.setString(3, mimirquery); 
		        			 String instlookup = reader.get("inst"); 
		        			 pstmt.setString(4, instlookup); 
		        			 
		        			 pstmt.executeUpdate(); // execute insert statement
		        		 }

		        		 reader.close();
				
		        	 } 
		        	 catch (IOException e) 
		        	 {
		        		 e.printStackTrace();
		        	 }

		         } 
		         catch (FileNotFoundException e) 
		         {
		        	 e.printStackTrace();
		         }
	             
	      }
	   // Handle any errors that may have occurred.
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	      finally {
	         if (rs != null) try { rs.close(); } catch(Exception e) {}
	         if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	         if (con != null) try { con.close(); } catch(Exception e) {}
	      }
	}
	
	/**
	 * This and subsequent method is used to write the queries which are in text file to DB table
	 */
	public void writeFromText(File dir, String queryType){ //dir is the folder on your computer which holds the files you want to add to the database
		
		for (File child : dir.listFiles()) {

			if (".".equals(child.getName()) || "..".equals(child.getName()))
				continue; // Ignore the self and parent aliases.
			if (child.isFile()){
		
				writeATextFile(child.toString(), queryType); // do something
				//	System.out.println(child);
			}
			else if (child.isDirectory()){
				System.out.println(child);
				writeFromText(child, queryType);
			}
		}
	}
	
	public void writeATextFile(String pathtofile, String queryType){
		
		// Declare the JDBC objects.
	      con = null;
	      stmt = null;
	      rs = null;
	    
	      try {
		       
	    	  	 // Establish the connection - sqllite server:
	  			 Class.forName("org.sqlite.JDBC");
	  			 con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
	    	  
	  		//	 System.out.println(pathtofile);
	  			 String tableName = queryType+pathtofile.substring(pathtofile.lastIndexOf("\\")+1, pathtofile.lastIndexOf("."));
	  			 tableName = tableName.replaceAll("-", "");
	  		
	  			 //
	  		    String sql0 = "DELETE FROM "+tableName+";";  
			    Statement stat0=con.createStatement();
		        stat0.execute(sql0);
		        System.out.println("**tablenam:: "+tableName);  
		        //
		        
	  			 //sqllite server:
		         String sql="CREATE TABLE if not exists "+tableName+"([ID] INT, raw_query TEXT, query_in_Mimir_format TEXT, inst_lookupFromQuery TEXT) ";	
		         
		         Statement stat=con.createStatement();
		         stat.execute(sql);

		         try 
		         {
		        	 BufferedReader in = new BufferedReader(
		        			   new InputStreamReader(
		        	                      new FileInputStream(pathtofile), "UTF8"));
			
		        	 
		        		 int id = 0;
		        		// while (reader.hasNextLine())
		        		 String query;
		        		 while ((query = in.readLine()) != null) 
		        		 {
		        			 id++;
		        		
		        			// String query = reader.nextLine();
		        			 String mimirquery = query.replaceAll(" ", " OR ");
		        	System.out.println(mimirquery);		 
		        			 //for sqllite server:
		        			 String SQL = "INSERT INTO "+tableName+"(ID, raw_query, query_in_Mimir_format) VALUES(?, ?, ?);";
		        			 
		        			 PreparedStatement pstmt = con.prepareStatement(SQL); // create a statement
      			  
		        			 pstmt.setInt(1, id); 
		        			 pstmt.setString(2, query); 
		        			 pstmt.setString(3, mimirquery); 
		       		        			 
		        			 pstmt.executeUpdate(); // execute insert statement
		        		 }

		        		// reader.close();
		        		 in.close();
		        	

		         } 
		         catch (FileNotFoundException e) 
		         {
		        	 e.printStackTrace();
		         }
	             
	      }
	   // Handle any errors that may have occurred.
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	      finally {
	         if (rs != null) try { rs.close(); } catch(Exception e) {}
	         if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	         if (con != null) try { con.close(); } catch(Exception e) {}
	      }
	}
	
	
	/**
	 * This write method can only be used in situation where have one lookup for 
	 * each query (i.e. csv file which has a new query in each row of the file).
	*/
	public void writeArchived() {
		
		// Declare the JDBC objects.
	      con = null;
	      stmt = null;
	      rs = null;
	    
	      try {
		        
	    	  	 // Establish the connection - sqllite server:
	  			 Class.forName("org.sqlite.JDBC");
	  			 con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
	    	  
	  			 
	  			 //sqllite server:
		         String sql="CREATE TABLE if not exists "+queriesTableName+"([ID] INT, raw_query TEXT, query_in_Mimir_format TEXT, inst_lookupFromQuery TEXT) ";	
		         
		         Statement stat=con.createStatement();
		         stat.execute(sql);

		         try 
		         {
		        	 FileReader read = new FileReader(pathToFile);
			
		        	 CsvReader reader = new CsvReader(read); 
						
		        	 try 
		        	 {
		        		 reader.readHeaders();

		        		 while (reader.readRecord())
		        		 {
		        			 //for microsoft sql server:
		        			 // String SQL = "INSERT INTO dbo."+queriesTableName+"(ID, raw_query, query_in_Mimir_format, inst_lookupFromQuery) VALUES(?, ?, ?, ?);";
		        			 
		        			 //for sqllite server:
		        			 String SQL = "INSERT INTO "+queriesTableName+"(ID, raw_query, query_in_Mimir_format, inst_lookupFromQuery) VALUES(?, ?, ?, ?);";
		        			 
		        			 PreparedStatement pstmt = con.prepareStatement(SQL); // create a statement
        			  
		        			 String id = reader.get("Query_number"); //System.out.println("id: "+id);
		        			 pstmt.setString(1, id); 
		        			 String query = reader.get("Text_query_in_English_with_correct_spelling"); //System.out.println("query: "+query);
		        			 pstmt.setString(2, query); 
		        			 String mimirquery = reader.get("Queries_converted_to_MIMIR_format"); //System.out.println("mimirquery: "+mimirquery);
		        			 pstmt.setString(3, mimirquery); 
		        			 String instlookup = reader.get("inst"); 
		        			 pstmt.setString(4, instlookup); 
		        			 
		        			 pstmt.executeUpdate(); // execute insert statement
		        		 }

		        		 reader.close();
				
		        	 } 
		        	 catch (IOException e) 
		        	 {
		        		 e.printStackTrace();
		        	 }

		         } 
		         catch (FileNotFoundException e) 
		         {
		        	 e.printStackTrace();
		         }
	             
	      }
	   // Handle any errors that may have occurred.
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	      finally {
	         if (rs != null) try { rs.close(); } catch(Exception e) {}
	         if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	         if (con != null) try { con.close(); } catch(Exception e) {}
	      }
	}

	public void printAll(){
		ResultSet resultSet=null;
		try {

			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
		  
			Statement stmt=con.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM Sensor4");
	        while(resultSet.next())
	        {
	        	ResultSetMetaData metadata=resultSet.getMetaData();
	        	int columnCount=metadata.getColumnCount();
	           // iterate & read the result set
	        	for(int i=1;i<=columnCount;i++)
	        		System.out.print(i+ "-_-" + metadata.getColumnName(i) + " _-_ " + resultSet.getString(metadata.getColumnName(i)));
	        	System.out.println(' ');
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(resultSet!=null)
					resultSet.close();
				if(stmt!=null)
					stmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
