package FileInteraction;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * used to write to a specified file
 * 
 * @author kevincorcoran 
 * @basedOn http://www.homeandlearn.co.uk/java/write_to_textfile.html
 *
 */
public class WriteFile
{
	private String path;
	private boolean append_to_file = false;
	
	/**
	 * constructor, append is automatically set to false here
	 *  
	 * @param file_path - string - name of the file you want write to 
	 */
	public WriteFile (String file_path)
	{
		path = file_path;
	}
	
	/**
	 * constructor
	 * 
	 * @param file_path - string - name of the file you want write to 
	 * @param append_value - boolean true for append, false for overwrite
	 */
	public WriteFile (String file_path, boolean append_value)
	{
		path = file_path;
		append_to_file = append_value;
	}
	
	/**
	 * writes on the same line
	 * 
	 * @param textLine
	 * @throws IOException
	 */
	public void writeToFile(String textLine) throws IOException
	{
		FileWriter write = new FileWriter( path , append_to_file);
		PrintWriter print_line = new PrintWriter( write );
		print_line.printf( "%s" , textLine);
		print_line.close();
	}
	
	/**
	 * writes to next line
	 * 
	 * @param textLine
	 * @throws IOException
	 */
	public void writeToFile_2(String textLine) throws IOException
	{
		FileWriter write = new FileWriter( path , append_to_file);
		PrintWriter print_line = new PrintWriter( write );
		print_line.printf( "%s"+ "%n" , textLine);
		print_line.close();
	}
}
