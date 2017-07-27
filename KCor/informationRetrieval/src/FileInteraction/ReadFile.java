package FileInteraction;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;

/**
 * used to read a specified file
 * 
 * @author KevinCorcoran
 * @basedOn http://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
 */
public class ReadFile
{
	private String path;
	
	/**
	 * 
	 * @param file_path
	 */
	public ReadFile(String file_path)
	{
		path = file_path;
	}
	
	/**
	 * saves the contents to a String list
	 * 
	 * @return textData - String list
	 * @throws IOException
	 */
	public List<String> OpenFile() throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		
		List<String> textData = new ArrayList<String>();
		
		String str;
		while ((str = textReader.readLine()) != null) 					//keep reading in until  nothing left
		{
			textData.add(str);											//add the string to the list
		}
		textReader.close();												
		
		return textData;
	}
	
	/**
	 * saves the contents to a single String
	 * 
	 * @return textData - String
	 * @throws IOException
	 */
	public String OpenFile2() throws IOException
	{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		String textData = new String();

		textData = textReader.readLine();
		
		textReader.close();
		
		return textData;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int readLines() throws IOException 
	{
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		int numberOfLines = 0;
		
		while(new String(bf.readLine()) != null)
		{
			numberOfLines++;
		}
		bf.close();
		
		return numberOfLines;
	}
}
