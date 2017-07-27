package com.csvreader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Data.AppData;
import Data.ClipboardData;
import Data.FileType;
import Data.GPSData;
import Data.KeyStrokeData;
import Data.KeyboardData;
import Data.MouseData;
import Data.PhotoData;
import Data.QSensorData;
import Data.ScreenshotData;
import DataSorting.Range;
import Database.InsertionBDD;
import IHM.MyWindow;

/**
 * This class group many different function used to parse the different files
 */
public class ParseFile
{

	// Static variables used to locate the database (created automatically) in the system
	static String DBlocation = System.getProperty("user.home") + File.separator + "Documents" + File.separator
			+ "MyLifeLogging"; // 2017 comment: the name and location of the database on your computer should be set here
	static String DBName = "Database.db";
	static String pathToQueries = System.getProperty("user.home") + File.separator + "Documents" + File.separator
			+ "Stage" + File.separator + "data qsensor" + File.separator + "LOG_02_COM6.csv"; // 2017 comment: or in your case a csv file with e.g. biometric data
	static String pathToQueriesFolder = System.getProperty("user.home") + File.separator + "Documents" + File.separator
			+ "Stage" + File.separator + "data qsensor" + File.separator; // 2017 comment: the location of files that you want to add to database should be set here

	// static String queriesTableName = "Sensor"; //the name of the table want to write to should be set here

	/// Array that store the last index of the dataBase to avoid identical values
	public static HashMap<String, Integer> indMax = new HashMap<String, Integer>();
	public static HashMap<String, Date> dateMax = new HashMap<String, Date>();

	/// obsolete
	private static String tableName = "Sensor";

	/**
	 * All these functions are meant to parse a csv file of a given format (app, clipboard, etc)
	 *
	 * @param data - String Array
	 * @return listQuery - String Array
	 */
	public static ArrayList<String> parseApp(List<String[]> data)/// DONE
	{
		/// Name of the database table used here
		final String TABLE_NAME = "App";
		/// List of the data to insert in the database (see ThreadInsert)
		ArrayList<String> listQuery = new ArrayList<String>();

		/// Get the max ind and date from the database to add the new data after
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		Date date = null;
		int cpt = 1;
		for (String[] oneLine : data)
		{
			/// Parse the date (originally in millisecond to formated date)
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);

			/// If there were no data in the table or the current data was taken before the last data added in the table, we do not add it
			if (dateMax != null && date.before(dateMax))
				continue;

			AppData appData = new AppData(oneLine); /// Parse the line of the csv file
			appData.setID(indMax + cpt);
			cpt++;

			listQuery.add(appData.getInsertQuery(TABLE_NAME));
		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseClipboard(List<String[]> data)/// DONE
	{ 
		/// Manque la récupération du contenu du fichier
		final String TABLE_NAME = "Clipboard";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		Date date = null;
		int cpt = 1;
		for (String[] oneLine : data)
		{
			if (cpt == 1)
			{
				cpt++;
				continue;
			}
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);
			if (dateMax != null && date.before(dateMax))
				continue;

			if (!ParseFile.saveLoggerManData(oneLine[0], "Clipboard"))
			{
				System.out.println("Error while moving Clipboard");
				continue;
			}
			ClipboardData clipData = new ClipboardData(oneLine);
			clipData.setID(indMax + cpt);
			cpt++;

			listQuery.add(clipData.getInsertQuery(TABLE_NAME));
		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseKeyLogger(List<String[]> data)/// DONE
	{ 
		final String TABLE_NAME = "KeyLogger";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		Date date = null;
		int cpt = 1;
		for (String[] oneLine : data)
		{
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);
			if (dateMax != null && date.before(dateMax))
				continue;

			KeyboardData KeyLogData = new KeyboardData(oneLine);
			KeyLogData.setID(indMax + cpt);
			cpt++;
			listQuery.add(KeyLogData.getInsertQuery(TABLE_NAME));

		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseKeystrokes(List<String[]> data)/// DONE
	{ 
		final String TABLE_NAME = "Keystrokes";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		int cpt = 1;

		Date date = null;
		for (String[] oneLine : data)
		{
			if (cpt <= 2)
			{ /// les premières lignes posent des problèmes de parse (metadata?)
				cpt++;
				continue;
			}
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);
			if (dateMax != null && date.before(dateMax))
				continue;
			KeyStrokeData KeyData = new KeyStrokeData(oneLine);
			KeyData.setID(indMax + cpt - 2);
			cpt++;
			listQuery.add(KeyData.getInsertQuery(TABLE_NAME));

		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseMouse(List<String[]> data)/// DONE
	{ 
		final String TABLE_NAME = "Mouse";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		int cpt = 1;

		Date date = null;
		for (String[] oneLine : data)
		{
			if (cpt <= 2)
			{ /// A CHANGER
				cpt++;
				continue;
			}
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);
			if (dateMax != null && date.before(dateMax))
				continue;
			MouseData mouseData = new MouseData(oneLine);
			mouseData.setID(indMax + cpt - 2);
			cpt++;
			listQuery.add(mouseData.getInsertQuery(TABLE_NAME));

		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseScreen(List<String[]> data) /// DONE - problème avec les '
	{
		/// Changer les images de place
		final String TABLE_NAME = "Screenshot";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		Date date = null;
		int cpt = 1;
		for (String[] oneLine : data)
		{
			long dateMillisecond = Long.parseLong(oneLine[oneLine.length - 1]);
			date = new Date(dateMillisecond);
			if (dateMax != null && date.before(dateMax))
				continue;

			ScreenshotData screenData = new ScreenshotData(oneLine);
			screenData.setID(indMax + cpt);
			cpt++;

			/// move the picture file near the database
			/// If the copy fail, we don't insert the query
			if (!InsertionBDD.copyFile(screenData.getOriginalFilePath(), "Screenshot", screenData.getImagePath()))
			{
				System.out.println("Erreur while copying photo");
				continue;
			}
			listQuery.add(screenData.getInsertQuery(TABLE_NAME));
		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	/**
	 * 
	 * @param data - String array
	 * @return listQuery a string of SQL insert commands
	 */
	public static ArrayList<String> parseQSensor(List<String[]> data)/// Done
	{ 
		final String TABLE_NAME = "Sensor";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME); 										// kcor - last index in the table
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);									// kcor - max date indexed

		Date date = null, startTime = null; 												// kcor - can store milliseconds

		int samplRateMs = 0; 																// kcor - in milliseconds
		int cpt = 0; 																		// kcor - essentially the line of the csv file

		/// List of the data for correcting the noise
		ArrayList<QSensorData> listSD = new ArrayList<QSensorData>(); 						// kcor - array of csv lines

		for (String[] oneLine : data)
		{																					// kcor - oneline is a single line of the csv file.
			/// The first 9 lines are headers, not actual data (see CSV file)
			cpt++;
			if (cpt == 5)
			{/// Sampling rate
				samplRateMs = Integer.parseInt(oneLine[0].substring(15)); 					// kcor - calculating sample rate in ms
				samplRateMs = 1000 / samplRateMs;
				continue;
			} else if (cpt == 6)
			{/// startTime
				/// parser the date
				startTime = new Date(0); 													// kcor - sets date to 1970
				String sDate = oneLine[0].substring(12, 22); 								// kcor - extract DD/MM/YYYY from csv
				parseDay(sDate, startTime); 												// kcor - initialise date day info
				sDate = oneLine[0].substring(23, 31);										// kcor - extract time information
				parseHours(sDate, startTime); 												// kcor - initialise date time info

				if (dateMax != null && !dateMax.before(startTime))
				{
					System.out.println("fichier déjà parsé");
					return null;
				}

				continue;
			} else if (cpt <= 9)
			{ /*
				 * kcor - it is possible here that i could increase 9 to
				 *  an amount which would correspond to an agreed period 
				 *  of time as to which the QSensor is fully calibrated,
				 * note this would also need to be changed below
				 */
				continue;
			}

			QSensorData qData = new QSensorData(oneLine); 									// kcor - creates a new QSensorData every iteration ie per line
			qData.setCreationDate(startTime.getTime() + (cpt - 9) * samplRateMs); 			// kcor - uses the sample rate multiplies by row number to get time from initial time
			qData.setID(indMax + cpt - 9); 													// kcor - increment by row number from the last table index(-header)
			listSD.add(qData); 																// kcor - append this line to the QSensorData array listSD

			/// Uncomment to insert directly and not correcting the noise
			// listQuery.add(qData.getInsertQuery(TABLE_NAME));

		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt); 									// kcor -- indmax is the last used index in the array
		ParseFile.dateMax.put(TABLE_NAME, date); 											// kcor - date is null here - NOT SURE WHY

		/**
		 * a "Plage"(kcor- this was the initial name of the range class) in french is a "range" of data (sorry about that) 
		 * The correction of the noise should be put here since all the
		 *  data are parsed and stored in the List. This
		 * "Range" system however should be strongly reviewed
		 */
		ArrayList<Range> listRange = Range.getRangesEDA(listSD); 							// kcor - group the data into ranges corresponding to eda then calculate other vars

		Range bestRange = Range.getBestRange(listRange); 									// kcor - calculates the best range based on several things
		bestRange.eraseNoise(listRange); 													// kcor - uses the bestRange as a baseline to erase noise

		/// Add all the remaining data (not noise) into de database
		listQuery.addAll(Range.getAllQueries(listRange)); 									// kcor - generates the sql queries for the database
		return listQuery; 																	// kcor -
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<String> parseGPS(List<String[]> data)
	{/// DONE
		final String TABLE_NAME = "GPS";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		ArrayList<GPSData> listGPS = new ArrayList<GPSData>();

		Date date = null;
		int cpt = 0;
		for (String[] oneLine : data)
		{
			if (cpt == 0)
			{
				cpt++;
				continue;
			}
			date = new Date(0);
			ParseFile.parseDay(oneLine[0].substring(0, 10), date);						//kcor tried changing oneline[3] to 0
			ParseFile.parseHours(oneLine[0].substring(11, 19), date);					//kcor - not sure if these lines are neccesary here since they are called in teh gps constructor
			if (dateMax != null && date.before(dateMax))
				continue;

			GPSData gpsData = new GPSData(oneLine);
			gpsData.setID(indMax + cpt);
			listGPS.add(gpsData);
			cpt++;
		}
		/// Once we parsed all the data, we correct the noise
		GPSData.correctionBruit(listGPS);
		for (GPSData gps : listGPS)
		{
			listQuery.add(gps.getInsertQuery(TABLE_NAME));
		}

		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static ArrayList<String> parseOnePhoto(String photoPath)
	{
		final String TABLE_NAME = "Photo";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		Date date = null;

		Path filePath = Paths.get(photoPath);
		File file = new File(photoPath);
		BasicFileAttributes attr = null;
		try
		{
			/// Get the time the photo was taken
			attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (dateMax != null && dateMax.getTime() > attr.lastModifiedTime().toMillis())
		{
			System.out.println("Already done");
			return null;
		}

		PhotoData photoData = new PhotoData(attr.lastModifiedTime().toMillis());
		photoData.setID(indMax + 1);
		date = new Date(attr.lastModifiedTime().toMillis());

		/// Move the photo
		if (!InsertionBDD.copyFile(file.getAbsolutePath(), "Photo", photoData.getNomFichier()))
		{
			System.out.println("Erreur while copying photo");
			return null;
		}
		listQuery.add(photoData.getInsertQuery(TABLE_NAME));
		ParseFile.indMax.put(TABLE_NAME, indMax + 1);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	/// Same as "parseOnePhoto" but for several
	public static ArrayList<String> parsePhoto(String dirPath)
	{
		final String TABLE_NAME = "Photo";
		ArrayList<String> listQuery = new ArrayList<String>();
		int indMax = ParseFile.indMax.get(TABLE_NAME);
		Date dateMax = ParseFile.dateMax.get(TABLE_NAME);

		/*
		 * String query= "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (id INTEGER, creationDate BIGINT, nomFichier VARCHAR2, cheminFichier VARCHAR2)"; listQuery.add(query);
		 */

		// indMax = BD.getLastIndexOfTable(TABLE_NAME);
		// dateMax = BD.getLastDateOfTable(TABLE_NAME);
		Date date = null;
		int cpt = 1;

		File dir = new File(dirPath);

		File[] fList = dir.listFiles();
		for (File file : fList)
		{
			if (!file.isFile())
				continue;

			Path filePath = Paths.get(file.getAbsolutePath());
			BasicFileAttributes attr = null;
			try
			{
				attr = Files.readAttributes(filePath, BasicFileAttributes.class);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dateMax != null && dateMax.getTime() > attr.lastModifiedTime().toMillis())
			{
				System.out.println("Already done");
				continue;
			}

			PhotoData photoData = new PhotoData(attr.lastModifiedTime().toMillis());
			photoData.setID(indMax + cpt);
			cpt++;

			if (!InsertionBDD.copyFile(file.getAbsolutePath(), "Photo", photoData.getNomFichier()))
			{
				System.out.println("Erreur déplacement photo");
				continue;
			}
			date = new Date(attr.lastModifiedTime().toMillis());
			listQuery.add(photoData.getInsertQuery(TABLE_NAME));

		}
		ParseFile.indMax.put(TABLE_NAME, indMax + cpt);
		ParseFile.dateMax.put(TABLE_NAME, date);
		return listQuery;
	}

	public static void eraseDataBase()
	{
		InsertionBDD dataBase = new InsertionBDD(pathToQueries, tableName, DBlocation + "/" + DBName);

		ArrayList<String> erasingQueries = getErasingQueries();
		for (String s : erasingQueries)
		{
			dataBase.executeQuery(s);
		}
	}

	/// Return all the queries that erase the table in the database
	public static ArrayList<String> getErasingQueries()
	{
		ArrayList<String> retour = new ArrayList<String>();
		retour.add("DROP TABLE IF EXISTS App");
		retour.add("DROP TABLE IF EXISTS Clipboard");
		retour.add("DROP TABLE IF EXISTS KeyLogger");
		retour.add("DROP TABLE IF EXISTS Keystrokes");
		retour.add("DROP TABLE IF EXISTS Mouse");
		retour.add("DROP TABLE IF EXISTS Screenshot");
		retour.add("DROP TABLE IF EXISTS GPS");
		retour.add("DROP TABLE IF EXISTS Sensor");
		retour.add("DROP TABLE IF EXISTS Photo");
		return retour;
	}

	/// Return all the queries that erase all the data in the table in the database
	public static ArrayList<String> getDeletingQueries()
	{
		ArrayList<String> retour = new ArrayList<String>();
		retour.add("DELETE FROM App");
		retour.add("DELETE FROM Clipboard");
		retour.add("DELETE FROM KeyLogger");
		retour.add("DELETE FROM Keystrokes");
		retour.add("DELETE FROM Mouse");
		retour.add("DELETE FROM Screenshot");
		retour.add("DELETE FROM GPS");
		retour.add("DELETE FROM Sensor");
		retour.add("DELETE FROM Photo");
		return retour;
	}

	/// Move a loggermanFile (Screenshot or Clipboard)
	private static boolean saveLoggerManData(String fileName, String dirDest)
	{

		String fullPath = "C:/Users/Administrateur/Documents/LoggerMan/" + dirDest + "/";

		fullPath += fileName.subSequence(0, 10) + "/";
		fullPath += Integer.parseInt(fileName.subSequence(11, 13).toString()) + "/";
		fullPath += fileName;

		return InsertionBDD.copyFile(fullPath, dirDest, fileName);
	}

	/// Parse the hours in a file (HH-mm-ss) into a date
	@SuppressWarnings("deprecation")
	public static void parseHours(String s, Date d)
	{
		int val = 0;
		String sDate;
		if(s.charAt(0)=='T')
		{
			sDate = s.substring(1, 3);
			val = Integer.parseInt(sDate);
			val = (val == 23)? 00:val+1;		//kcor - irish timezone, important for gps logger csv data
			d.setHours(val);
			
			sDate = s.substring(4, 6);
			val = Integer.parseInt(sDate);
			d.setMinutes(val);

			sDate = s.substring(7, 9);
			val = Integer.parseInt(sDate);
			d.setSeconds(val);
			
		}else{
			sDate = s.substring(0, 2);
			val = Integer.parseInt(sDate);
			d.setHours(val);
			
			sDate = s.substring(3, 5);
			val = Integer.parseInt(sDate);
			d.setMinutes(val);

			sDate = s.substring(6, 8);
			val = Integer.parseInt(sDate);
			d.setSeconds(val);
		}
		
	}

	@SuppressWarnings("deprecation")
	public static void parseDay(String s, Date d)
	{
		int val = 0;
		String sDate;
		sDate = s.substring(0, 4); // kcor - extract year
		val = Integer.parseInt(sDate); // kcor - parse year into an int
		d.setYear(val-1900); // kcor - initialise the date year field

		sDate = s.substring(5, 7); // kcor - extract month
		val = Integer.parseInt(sDate); // kcor - parse month to int
		d.setMonth(val - 1); // kcor - initialise the date month field

		sDate = s.substring(8, 10); // kcor - extract day of month
		val = Integer.parseInt(sDate); // kcor - parse to int
		d.setDate(val); // kcor - iniitialise to day field
	}

	/// Parse all LoggerMan files directory by directory
	public static ArrayList<String> parseAllFiles(String directory)
	{
		File dir = new File(directory);

		ArrayList<String> queries = new ArrayList<String>();

		// get all the files from a directory
		File[] fList = dir.listFiles();
		for (File file : fList)
		{
			if (file.isDirectory())
			{
				if (file.getName().equals("App"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.APP));
				else if (file.getName().equals("Clipboard"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.CLIPBOARD));
				else if (file.getName().equals("KeyLogger"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.KEYLOGGER));
				else if (file.getName().equals("Keystrokes"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.KEYSTROKES));
				else if (file.getName().equals("Mouse"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.MOUSE));
				else if (file.getName().equals("Screen"))
					queries.addAll(parseAllLoggerMan(file.getAbsolutePath(), FileType.SCREENSHOT));
			}
		}
		return queries;
	}

	public static ArrayList<String> parseAllLoggerMan(String directory, FileType ft)
	{
		File dir = new File(directory);

		ArrayList<String> queries = null;

		// get all the files from a directory
		File[] fList = dir.listFiles();
		for (File file : fList)
		{

			if (file.isFile())
			{
				File fileToParse = CsvFileHelper.getResource(file.getAbsolutePath());

				final CsvFile csvFile = new CsvFile01(fileToParse);
				final List<String[]> data = csvFile.getData();
				// new Thread(new ThreadParse(ft, data, dataBase)).start();

				switch (ft.getId())
				{
					case 1:
						queries = parseApp(data);
						break;
					case 2:
						queries = parseClipboard(data);
						break;
					case 3:
						queries = parseKeyLogger(data);
						break;
					case 4:
						queries = parseKeystrokes(data);
						break;
					case 5:
						queries = parseMouse(data);
						break;
					case 6:
						queries = parseScreen(data);
						break;
				}
			}
		}
		return queries;
	}

	/// Get all the file path from a folder into an array of string
	public static ArrayList<String> getAllPath(String path)
	{
		ArrayList<String> list = new ArrayList<String>();

		File dir = new File(path);
		if (!dir.isDirectory())
		{
			list.add(dir.getAbsolutePath());
		} else
		{
			File[] fList = dir.listFiles();
			for (File f : fList)
			{
				if (f.isDirectory())
					list.addAll(0, getAllPath(f.getAbsolutePath()));
				else
					list.add(f.getAbsolutePath());
			}
		}

		return list;
	}

	public static void initMaxInd(InsertionBDD db)
	{
		for (String TABLE_NAME : MyWindow.NOM_TABLE)
		{
			ParseFile.indMax.put(TABLE_NAME, db.getLastIndexOfTable(TABLE_NAME));
			if (ParseFile.indMax.get(TABLE_NAME) == 0)
				ParseFile.indMax.put(TABLE_NAME, 1);
		}
	}

	/// Initiate the max ID in the database when all the data are erased after the start of the software
	public static void resetMaxInd()
	{
		for (String TABLE_NAME : MyWindow.NOM_TABLE)
		{
			ParseFile.indMax.put(TABLE_NAME, 1);
		}
	}

	public static void initMaxDate(InsertionBDD db)
	{
		for (String TABLE_NAME : MyWindow.NOM_TABLE)
		{
			ParseFile.dateMax.put(TABLE_NAME, db.getLastDateOfTable(TABLE_NAME));
			if (ParseFile.dateMax.get(TABLE_NAME) == null)
				ParseFile.dateMax.put(TABLE_NAME, new Date(0));
		}
	}

	public static void resetMaxDate()
	{
		for (String TABLE_NAME : MyWindow.NOM_TABLE)
		{
			ParseFile.dateMax.put(TABLE_NAME, new Date(0));
		}
	}
}