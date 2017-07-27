package detection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.math.*;
//import static java.nio.file.StandardCopyOption.*;

import FileInteraction.ReadFile;
import FileInteraction.WriteFile;

/**
 *	This class runs analysis methods on 
 *
 *
 * @author kevin Corcoran
 *
 */
public class biometricAnalysis extends Detection
{
	public final static boolean experimentComplete = true; 				//set to true if the important files have been selected
	
	//FOLDERNAMES
	public final static String exptRes = "exptResults/";
	public final static String appIn = "appInput/";
	public final static String imp = "important/";
	public final static String RD = "randomData/";
	public final static String AD = "algorithmicData/";
	public final static String MD = "mixedData/";
	
	// FILENAMES
	public final static String MAX_EDA_STD = "MAX_EDA_STD";
	public final static String MIN_EDA_STD = "MIN_EDA_STD";
	public final static String MAX_TEMP_STD = "MAX_TEMP_STD";
	public final static String MIN_TEMP_STD = "MIN_TEMP_STD";
	public final static String MAX_EDA_REG = "MAX_EDA_REG";
	public final static String MIN_EDA_REG = "MIN_EDA_REG";
	public final static String MAX_TEMP_REG = "MAX_TEMP_REG";
	public final static String MIN_TEMP_REG = "MIN_TEMP_REG";
	
	//OTHER
	
	public final static String P = System.getProperty("user.home") + File.separator + "Documents/MyLifeLogging/";

	/**
	 * 
	 * 
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException
	{
		if(!experimentComplete)								//data collection
		{
			createFolders();
		
			RunAnalysis();
		}
		else												//if the important files have been selected and moved to the important folder
		{
			checkAnalysis();
		}
	}
	/**
	 * ensure necessary directories exist
	 */
	public static void createFolders()
	{
		File a = new File(P+exptRes);
		a.mkdir();
		
		File b = new File(P+appIn);
		b.mkdir();
		
		File c = new File(P+imp);
		c.mkdir();
		
		File d = new File(P+AD);
		d.mkdir();	
		
		File e = new File(P+RD);
		e.mkdir();	
		
		File f = new File(P+MD);
		f.mkdir();	
	}
	/**
	 * begin the biometric data analysis
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void RunAnalysis() throws SQLException, ClassNotFoundException, IOException
	{
		System.out.println(
				"*********************************************Biometric Analysis started***********************************************");

		// Standard deviation algorithm
		System.out.println("Standard Deviation Analysis");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------");

		List<Peak> edapeaksstd = getstdPeaks("eda");							//retrieve List of specified metric extremities
		saveToPeaks_txt(edapeaksstd, MAX_EDA_STD);								//Creates the sql input file for Alex's application and the checkAnalysis method
		getalgo(MAX_EDA_STD);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Valley> edavalleysstd = getstdValleys("eda");						//retrieve List of specified metric extremities
		saveToValleys_txt(edavalleysstd, MIN_EDA_STD);							//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MIN_EDA_STD);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Peak> temppeakstd = getstdPeaks("temperature");					//retrieve List of specified metric extremities
		saveToPeaks_txt(temppeakstd, MAX_TEMP_STD);								//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MAX_TEMP_STD);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Valley> tempvalleystd = getstdValleys("temperature");				//retrieve List of specified metric extremities
		saveToValleys_txt(tempvalleystd, MIN_TEMP_STD);							//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MIN_TEMP_STD);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		// regular algo
		System.out.println("Regular Analysis");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------");

		List<Peak> edapeaksreg = getregPeaks("eda");							//retrieve List of specified metric extremities
		saveToPeaks_txt(edapeaksreg, MAX_EDA_REG);								//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MAX_EDA_REG);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Valley> edavalleysreg = getregValleys("eda");						//retrieve List of specified metric extremities
		saveToValleys_txt(edavalleysreg, MIN_EDA_REG);							//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MIN_EDA_REG);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Peak> temppeaksreg = getregPeaks("temperature");					//retrieve List of specified metric extremities	
		saveToPeaks_txt(temppeaksreg, MAX_TEMP_REG);							//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MAX_TEMP_REG);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");

		List<Valley> tempvalleysreg = getregValleys("temperature");				//retrieve List of specified metric extremities
		saveToValleys_txt(tempvalleysreg, MIN_TEMP_REG);						//Creates the sql input file for Alex's application and the RunAnalysis method
		getalgo(MIN_TEMP_REG);													//logs in a text file the lifelog items that would be retrieved by Alex's application

		System.out.println("");
		System.out.println(
				"*********************************************Biometric Analysis Completed***********************************************");

		System.out.println("Random Data phase started");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------------------");

		getregrandom();															//retrieve some random data
		
		System.out.println("Done");

	}

	//**************************************************************************PEAK AND VALLEY DETECTION*********************************************************
	
	/**
	 * returns the peaks of specified float biometric data
	 * 
	 * @param metric - String - xMOVE, yMove, zMove, temperature, eda
	 * @return peaks - List<Peak> - moments determined to be interesting
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Peak> getstdPeaks(String metric) throws ClassNotFoundException, SQLException
	{
		System.out.print("\t Detecting " + metric + " peaks - ");
		
		ResultSet rs = dbRetrieve("SELECT " + metric + ", creationDate FROM Sensor"); 		//Retrieve data with creation dates
	
		List<Double> column = new ArrayList<Double>();										
		List<Long> creationDates = new ArrayList<Long>();
		
		while (rs.next())																	 //converts resultset to lists
		{
			double columnElement = rs.getDouble(metric);									//retrieve one metric value at a time
			column.add(columnElement);														//save one metric value at a time
			
			long creationDate = rs.getLong("creationDate");									//retrieve corresponding value one at a time
			creationDates.add(creationDate);												//save corresponding value one at a time
		}
	
		List<Peak> peaks = PeakDetectionStDev(column, creationDates, 1.0);					//run the detection algorithm, 1.0 is default and is overwritten later
		
		System.out.print("Complete    ");
		
		return peaks;
	}

	//**************************************************************************PEAK AND VALLEY DETECTION*********************************************************
	
	/**
	 * runs the detection algorithm.
	 * returns the peaks of specified float biometric data, although i believe double will also work and seem as though would be preferable 
	 * 
	 * @param metric - String - xMOVE, yMove, zMove, temperature, eda
	 * @return valleys - List<Valley> - moments determined to be interesting
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Valley> getstdValleys(String metric) throws ClassNotFoundException, SQLException
	{
		System.out.print("\t Detecting " + metric + " valleys - ");
		ResultSet rs = dbRetrieve("SELECT " + metric + ", creationDate FROM Sensor"); 		//Retrieve data with creation dates
	
		List<Double> column = new ArrayList<Double>();
		List<Long> creationDates = new ArrayList<Long>();
	
		while (rs.next())																	 //converts resultset to lists
		{
			double columnElement = rs.getDouble(metric);									//retrieve one metric value at a time
			column.add(columnElement);														//save one metric value at a time
						
			long creationDate = rs.getLong("creationDate");									//retrieve corresponding value one at a time
			creationDates.add(creationDate);												//save corresponding value one at a time
		}
	
		List<Valley> valleys = ValleyDetectionStDev(column, creationDates, 1);				//run the detection algorithm, 1.0 is default and is overwritten later
	
		System.out.print("Complete    ");
		
		return valleys;
	}
	
	/**
	 * runs the detection algorithm.
	 * returns the peaks of specified float biometric data, although i believe double will also work and seem as though would be preferable 
	 * 
	 * @param metric - String - xMOVE, yMove, zMove, temperature, eda
	 * @return peaks - List<Peak> - moments determined to be interesting
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Peak> getregPeaks(String metric) throws ClassNotFoundException, SQLException
	{
		System.out.print("\tDetecting " + metric + " peaks - ");
		ResultSet rs = dbRetrieve("SELECT " + metric + ", creationDate FROM Sensor"); 					//retrieves data with creation dates
	
		List<Double> column = new ArrayList<Double>();													
		List<Long> creationDates = new ArrayList<Long>();
	
		while (rs.next())																				//converts resultset to lists
		{ 
			double columnElement = rs.getDouble(metric);												//retrieve one metric value at a time
			column.add(columnElement);																	//save one metric value at a time
			
			long creationDate = rs.getLong("creationDate");												//retrieve corresponding value one at a time
			creationDates.add(creationDate);															//save corresponding value one at a time
		}
	
		List<Peak> peaks = PeakDetection(column, creationDates, 1.0);									//run the detection algorithm, 1.0 is default and is overwritten later

		System.out.print("Complete    ");
		
		return peaks;
	}
	
	/**
	 * runs the detection algorithm.
	 * returns the valleys of specified float biometric data, although i believe double will also work and seem as though would be preferable 
	 * 
	 * @param metric - String - xMOVE, yMove, zMove, temperature, eda
	 * @return valleys - List<Valley> - moments determined to be interesting
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Valley> getregValleys(String metric) throws ClassNotFoundException, SQLException
	{
		System.out.print("\tDetecting " + metric + " valleys - ");
		ResultSet rs = dbRetrieve("SELECT " + metric + ", creationDate FROM Sensor"); 					//retrieves data with creation dates
	
		List<Double> column = new ArrayList<Double>();
		List<Long> creationDates = new ArrayList<Long>();
	
		while (rs.next())																				//converts resultset to lists	
		{ 			
			double columnElement = rs.getDouble(metric);												//retrieve one metric value at a time
			column.add(columnElement);																	//save one metric value at a time
			
			long creationDate = rs.getLong("creationDate");												//retrieve corresponding value one at a time
			creationDates.add(creationDate);															//save corresponding value one at a time
		}
	
		List<Valley> valleys = ValleyDetection(column, creationDates, 1);								//run the detection algorithm, 1.0 is default and is overwritten later
		
		System.out.print("Complete    ");
		return valleys;
	}
	
	//***********************************************************SQL FILE GENERATION******************************************************************************************

	/**
	 * saves an sql string of the moments to be extracted from the database. 
	 * this output is later used by alex's application a input and by the get algo method in this class in order to log the actual files retrived
	 * 
	 * @param peaks - List<Peak> - list of all the points on interest determined by the detection algorithm
	 * @param file_name	String - the file to save the sql string to.
	 * @throws IOException
	 */
	public static void saveToPeaks_txt(List<Peak> peaks, String file_name) throws IOException
	{
		System.out.print("Saving to " + "app" + file_name + " - ");														
		
		String file_path_1 = P + appIn + "app" + file_name + ".txt";															
		
		//clear the file if there already exists that file
		WriteFile data_format_1 = new WriteFile(file_path_1, false);											//empty file
		data_format_1.writeToFile("");															
		data_format_1.writeToFile("");
	
		try
		{
			WriteFile data_1 = new WriteFile(file_path_1, true);												//prepare file to append info to															
		
			int counter = 0;
			
			for (Peak peak : peaks)
			{
	
				if (counter != peaks.size() - 1)
				{
					data_1.writeToFile("(creationDate BETWEEN " + (peak.index - 120000L) + " AND "
							+ (peak.index + 120000L) + ") OR "); // 2 mins on either side of creationDate
				} else
				{
					data_1.writeToFile(
							"(creationDate BETWEEN " + (peak.index - 120000L) + " AND " + (peak.index + 120000L) + ")");
				}
				counter++;
			}
	
			System.out.print("Complete    ");
		}
		catch (IOException e)
		{
			System.out.println("error writing to file");
		}
	}

	//*******************************************************************************************************SAVESTO**********************************************************
	
	/**
	 * saves an sql string of the moments to be extracted from the database. 
	 * this output is later used by alex's application a input and by the get algo method in this class in order to log the actual files retrived
	 * 
	 * @param valleys - List<Valley> - list of all the points on interest determined by the detection algorithm
	 * @param file_name	String - the file to save the sql string to.
	 * @throws IOException
	 */
	public static void saveToValleys_txt(List<Valley> valleys, String file_name) throws IOException
	{
		System.out.print("Saving to " + "app" + file_name + " - ");
		
		String file_path_1 = P + appIn + "app" + file_name + ".txt";
	
		WriteFile data_format_1 = new WriteFile(file_path_1, false);											//empty file
		data_format_1.writeToFile("");
	
		try
		{
			WriteFile data_1 = new WriteFile(file_path_1, true);												//prepare file to append info to
		
			int counter = 0;
			
			for (Valley valley : valleys)
			{
				if (counter != valleys.size() - 1)
				{
					data_1.writeToFile("(creationDate BETWEEN " + (valley.index - 120000L) + " AND "
							+ (valley.index + 120000L) + ") OR "); 												// 2 mins on either side of creationDate
				} else
				{
					data_1.writeToFile("(creationDate BETWEEN " + (valley.index - 120000L) + " AND "
							+ (valley.index + 120000L) + ")");
				}
				counter++;
			}
			
			System.out.print("Complete    ");
		}
		catch (IOException e)
		{
			System.out.println("error writing to file");
		}
	}
	//*****************************************************************COPY PHOTOS TO RESPECTIVE FOLDERS*********************************************************8
	
	/**
	 * logs the files that are extracted from the data base givein the "app" file
	 * 
	 * @param textFile - the "app" output filet o work on
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void getalgo(String textFile) throws ClassNotFoundException, SQLException, IOException
	{
		System.out.print("Copying Files - ");
		
		ReadFile al = new ReadFile(P+appIn + "app" + textFile + ".txt");
		String text = al.OpenFile2();
		
		ResultSet rs = dbRetrieve("SELECT fileName FROM Photo WHERE  " + text); 		// kcor retrieves data with creation dates

		String file_path_2 = P+exptRes+ "exp" + textFile + ".txt";
		
		WriteFile data_format_2 = new WriteFile(file_path_2, false);
		data_format_2.writeToFile("");
		
		WriteFile data_2 = new WriteFile(file_path_2, true);
		
		while (rs.next())																// kcor - converts resultset to lists
		{ 
			String fileName = rs.getString("fileName");

			data_2.writeToFile_2(fileName);
			
			copyFile(P + "Photo/", P+ MD, fileName);
			copyFile(P + "Photo/", P + AD, fileName);
		}
		
		System.out.println("Complete");
	}

	/**
	 * selects some random data to test against 
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void getregrandom() throws ClassNotFoundException, SQLException, IOException
	{
		System.out.print("\tCopying Random Data - ");
		File dirAD = new File(P+AD);
		int avgDataSet = (dirAD.list().length) / 8;												//the average number of images retrieved by each algorithm(8 of them)
		ResultSet rs = dbRetrieve(
				"SELECT fileName FROM (SELECT fileName FROM Photo ORDER BY RANDOM()) LIMIT " + avgDataSet); // kcor retrieves data with creation dates

		String file_path_2 = P+exptRes + "exprandomData.txt";
		
		WriteFile data_format_2 = new WriteFile(file_path_2, false);
		data_format_2.writeToFile("");
		
		WriteFile data_2 = new WriteFile(file_path_2, true);

		while (rs.next())																		// kcor - converts resultset to lists
		{ 
			String fileName = rs.getString("fileName");
						
			data_2.writeToFile_2(fileName);
			
			copyFile(P + "Photo/", P+ MD, fileName);
			copyFile(P + "Photo/", P+ RD, fileName);
		}
		
		System.out.println("Complete");
	}

	/**
	 * *taken from Thibaults InsertionBDD
	 * 
	 * @author Thibault - InsertionBDD
	 * @param source -
	 * @param dirDest -
	 * @param fileName -
	 * @return
	 */
	public static boolean copyFile(String source, String dirDest, String fileName)
	{
		if (source == null || source.isEmpty() || dirDest == null || dirDest.isEmpty())
			return false;

		try
		{
			Files.copy(Paths.get(source + fileName), Paths.get(dirDest + fileName),
					StandardCopyOption.REPLACE_EXISTING);

		}
		catch (IOException e)
		{
			System.out.println("Error copying file - " + fileName + " from " + source + " to " + dirDest);
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	//*******************************************************************STATISTICAL ANALYSIS*****************************************************************************
	
	/**
	 * calculates the number of correctly identified photos for each extremity of each metric 
	 * 
	 * @throws IOException
	 */
	public static void checkAnalysis() throws IOException
	{
		System.out.println(
				"*********************************************Analysis Results***********************************************");
	
		File files = new File(P+exptRes);													
		String[] filesAr = files.list();													//string array of the names of the log files
		
		double[] runavg = new double[9];
		
		for (int i = 0; i < filesAr.length; i++)											//cycle through each of the log files
		{
			ReadFile al = new ReadFile(P+ exptRes + filesAr[i]);							
			List<String> text = al.OpenFile();												//string list of the names of every image retrieved by the respective algorithm 
	
			int common = 0;																	//keep
			
			runavg[i] = (double) text.size();												//number of lines in or images for that log file
			for (int j = 0; j < text.size(); j++)
			{
				File img = new File(P+imp + text.get(j));
				if (img.exists())															//if a log entry is in the important folder
				{
					common++;																
				}
			}
	
			System.out.println(filesAr[i] + "\n\t" +"Important lifelog items detected - "+ common +" \n\tsuccess rate - " +Math.round(common/runavg[i]*100)+"%");
			System.out.println("");
		}
	}

	/**
	 * Queries the database for a given String command
	 * 
	 * @param metric - String - sql command
	 * @return ResultSet - results
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ResultSet dbRetrieve(String cmd) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager
				.getConnection("jdbc:sqlite:"+P+"DataBase.db");							//location of database
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(cmd);
		return rs;

	}
}
