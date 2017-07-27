package Database;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;

import com.csvreader.CsvReader;

///Main class for managing the database
///Only use the "executeQuery" function for a simpler interaction
public class InsertionBDD {

	static String DBlocation = System.getProperty("user.home") + File.separator + "Documents/Stage/BDD/DataBase.db"; //2017 comment: the name and location of the database on your computer should be set here
	
	static String pathToQueries = System.getProperty("user.home") + File.separator + "Stage/data qsensor/LOG_02_COM6.csv";  //2017 comment: or in your case a csv file with e.g. biometric data
	
	
	static String pathToQueriesFolder =  System.getProperty("user.home") + File.separator + "Documents/Stage/data qsensor/"; //2017 comment: the location of files that you want to add to database should be set here
	
	static String queriesTableName0 = "Sensor4"; //the name of the table want to write to should be set here
	
		 private static String connectionUrl=""; //this path should be of the form e.g. "C:\\KhresmoiDBforIR\\Khresmoi.db"  //path to the sqlLite database to go here
		 
		// Declare the JDBC objects.
		private static Connection con;
		private static Statement stmt;
		private static ResultSet rs;
		
		ArrayList<String> record = new ArrayList<String>();
		
		String pathToFile = ""; //the csv file containing the 50 queries
		String queriesTableName = "";

		
		public InsertionBDD(String pathToQueries, String q, String dbLocation){
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

		public void createTable(String query){
			con = null;
		      stmt = null;
		      rs = null;
		    
		      try {
			       
		    	  	 // Establish the connection - sqllite server:
		  			 Class.forName("org.sqlite.JDBC");
		  			 con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
		    	  
		  		    
		  			 //sqllite server:
			         if(query!=null){
				         Statement stat=con.createStatement();
				         stat.execute(query);
			         }
		      }catch (Exception e) {
			         e.printStackTrace();
		      }
		      finally {
		    	  if (rs != null) try { rs.close(); } catch(Exception e) {}
			      if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			      if (con != null) try { con.close(); } catch(Exception e) {}
		      }
		}
		
		public void executeQuery(String query){
			  con = null;
		      stmt = null;
		      rs = null;
		    
		      try {
			       
		    	  	 // Establish the connection - sqllite server:
		  			 Class.forName("org.sqlite.JDBC");
		  			 con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
		    	  

			         PreparedStatement pstmt = con.prepareStatement(query);
			         pstmt.executeUpdate(); // execute insert statement			

		             
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
		
		@SuppressWarnings("deprecation")
		public void printTable(String table){
			ResultSet resultSet=null;
			try {

				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
			  
				Statement stmt=con.createStatement();
				resultSet = stmt.executeQuery("SELECT * FROM "+table);
		        while(resultSet.next())
		        {
		        	ResultSetMetaData metadata=resultSet.getMetaData();
		        	int columnCount=metadata.getColumnCount();
		           // iterate & read the result set
		        	for(int i=1;i<=columnCount;i++){
		        		if(i==2){
		        			Date tamere=new Date(resultSet.getLong(metadata.getColumnName(i)));
		        			System.out.print(i+ " - " + metadata.getColumnName(i) + " - " + tamere.toGMTString());
		        		}
		        		else
		        			System.out.print(i+ " - " + metadata.getColumnName(i) + " - " + resultSet.getString(metadata.getColumnName(i)));
		        		System.out.println(' ');
		        	}
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
		
		public static boolean moveFile(String source, String dirDest){
			if(source==null||source.isEmpty()||dirDest==null||dirDest.isEmpty())
				return false;
			
			dirDest=System.getProperty("user.home") + File.separator + "Documents/MyLifeLogging/"+dirDest+"/";
			System.out.println(dirDest);
			File dir=new File(dirDest);
			dir.mkdirs();
			try {
				Files.move(Paths.get(source), Paths.get(dirDest), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error moving file from "+source+" to "+dirDest);
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		public static boolean copyFile(String source, String dirDest, String fileName){
			if(source==null||source.isEmpty()||dirDest==null||dirDest.isEmpty())
				return false;
			

			dirDest=System.getProperty("user.home") + File.separator + "Documents/MyLifeLogging/"+dirDest+"/";
			File dir=new File(dirDest);
			dir.mkdirs();
			try {
				Files.copy(Paths.get(source), Paths.get(dirDest+fileName), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error copying file from "+source+" to "+dirDest);
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		public int getLastIndexOfTable(String tableName){
			int retour=0;
			ResultSet resultSet=null;			
			String query = "SELECT MAX(ID) FROM " + tableName;

			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
			  
				Statement stmt=con.createStatement();
				resultSet = stmt.executeQuery(query);
					
				if(resultSet!=null){
					retour = resultSet.getInt(1);
				}
				else
					retour = 1;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
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
			
			
			return retour;
		}
		
		public Date getLastDateOfTable(String tableName){
			Date retour=null;
			ResultSet resultSet=null;			
			String query = "SELECT MAX(creationDate) FROM " + tableName ;

			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
			  
				Statement stmt=con.createStatement();
				resultSet = stmt.executeQuery(query);
					
				if(resultSet==null)
					retour=new Date(0);
				else{
					while(resultSet.next() && resultSet.getString(1)!=null){
						retour = new Date(resultSet.getLong(1));
					}	
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
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
			
			
			return retour;
		}
		
		public ResultSet getResultSet(String query){
			ResultSet resultSet=null;			

			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:"+connectionUrl);   
			  
				Statement stmt=con.createStatement();
				resultSet = stmt.executeQuery(query);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return resultSet;
		}
		
		public static void main(String args[]){
			InsertionBDD base = new InsertionBDD(pathToQueries, queriesTableName0, DBlocation);
			
			base.executeQuery("DROP TABLE Sensor");
			base.createTable("CREATE TABLE IF NOT EXISTS Sensor (id INTEGER, xMove FLOAT, yMove FLOAT, zMove FLOAT, temperature FLOAT, eda FLOAT, event SMALLINT)");
			base.printTable("Sensor");
			base.executeQuery("INSERT INTO Sensor VALUES('1','1','1','1','1','1','1')");
			base.printTable("Sensor");
		}
}
