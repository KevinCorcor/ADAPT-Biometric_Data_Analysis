package Database;

import java.io.File;

///Not useful for the project
public class mainFor2017intern {

	/**
	 * Following variables are the variables which need to be set when running this code:
	 * 
	 */
	
	static String DBlocation = System.getProperty("user.home") + File.separator + "Documents/Stage/BDD/DataBase.db"; //2017 comment: the name and location of the database on your computer should be set here
	
	static String pathToQueries = System.getProperty("user.home") + File.separator + "Documents/Stage/data qsensor/LOG_02_COM6.csv";  //2017 comment: or in your case a csv file with e.g. biometric data
	
	
	static String pathToQueriesFolder =  System.getProperty("user.home") + File.separator + "Documents/Stage/data qsensor/"; //2017 comment: the location of files that you want to add to database should be set here
	
	static String queriesTableName = "Sensor4"; //the name of the table want to write to should be set here
	
	
	
	
	@SuppressWarnings("unused")
	private static void setPathToQueries(String q){
		
		pathToQueries = q;
		
	}

	@SuppressWarnings("unused")
	private static void setQueriesTableName(String q){
		
		queriesTableName = q;
	}

	@SuppressWarnings("unused")
	private static void setPathToQueriesFolder(String q){
		
		pathToQueriesFolder = q;
	}
	

	//main method:
	public static void main(String[] args) {
		
		
		/**
		 * (1) - write queries to DB table
		 * 2017 comment:  sample of how to call the class to read from file and write to database table.
		 * In this example we are reading from 1 csv file and writing to database table
		 */
		//note this piece of code only needs to be run once
		System.out.println("About to write queries to DB table.....");
		WriteQueriesToDBfor2017intern wqdb = new WriteQueriesToDBfor2017intern(pathToQueries, queriesTableName, DBlocation);
		//wqdb.write();
		System.out.println("Finished writing queries to DB table.....");

		
		wqdb.printAll();
	
		
		/**
		 * 5) **write the queries for IR exps (translated and spelling correction+errors) which are in text files to db table
		 */
	/*	//note this piece of code only needs to be run once
		System.out.println("About to write queries to DB table.....");
		writeQueriesToDBfor2017intern wqdb = new writeQueriesToDB(pathToQueries, queriesTableName);
		//wqdb.writeFromText(new File(pathToQueriesFolder), "SpellError");
		//wqdb.writeFromText(new File(pathToQueriesFolder), "SpellErrorCorrected");
		wqdb.writeFromText(new File(pathToQueriesFolder), "");
		System.out.println("Finished writing queries to DB table.....");
	*/
	
		
	System.out.println("Finished......");

	}

}
