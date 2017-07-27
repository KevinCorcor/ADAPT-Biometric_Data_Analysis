package IHM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.csvreader.ParseFile;
import com.csvreader.ThreadParse;

import Database.InsertionBDD;
import Database.ThreadInsert;

public class WindowEvent implements ActionListener {

	public MyWindow window;
	public static boolean BD_inUse=false;
	public boolean loggingLoggerMan=false;
	
	private Thread insertThread;	///only thread interacting with the database
	private ArrayList<ThreadParse> parseThread=new ArrayList<ThreadParse>();	///threads that parse the files
	
	public static ArrayList<ArrayList<String>> queries=new ArrayList<ArrayList<String>>(); ///queries to insert the data
		
	public WindowEvent(MyWindow window){
		this.window=window;
	}
	
	///this function check which action was performed
	/// to know which type of files has to be parsed
	/// the drop panel is handled separatly
	@Override
	public void actionPerformed(ActionEvent e) {
		if(insertThread==null || insertThread.getState().equals("TERMINATED") || BD_inUse==false){
			insertThread=null;
			WindowEvent.BD_inUse=false;

		}
		
		
		JButton btn=(JButton)e.getSource();
		if(btn.getText().equals("Load LoggerMan")){
			if(!loadLoggerMan())
				JOptionPane.showMessageDialog(null,"LoggerMan is already being loaded","Information",JOptionPane.INFORMATION_MESSAGE);
			
		}
		else if(btn.getText().equals("Change Logger Man location")){
			this.changeLoggerManLocation();
		}
		else if(btn.getText().equals("Change data base location")){
			this.changeDataBaseLocation();
		}
		else if(btn.getText().equals("Save changes")){
			window.getConfig().setLaunchOnComputerStart(window.getLaunchOnComputerStart().isSelected());
			window.getConfig().setLoadLoggerManOnStart(window.getLoadLoggerManOnStart().isSelected());
			this.changeDataBaseLocation();
			this.changeLoggerManLocation();
			
			FileOutputStream fichier;
			try {
				fichier = new FileOutputStream("Config.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fichier);
				oos.writeObject(this.window.getConfig());
				
				oos.flush();
				oos.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null,"Changes saved","Information",JOptionPane.INFORMATION_MESSAGE);
		}else if(btn.getText().equals("Change Database name")){
			changeDataBaseName();
		}else if(btn.getText().equals("Erase Data")){

			synchronized(queries){
				queries.add(ParseFile.getDeletingQueries());
			}
			window.textArea.append("Erasing all data\n");
			ParseFile.resetMaxInd();
			ParseFile.resetMaxDate();
			Thread t2 = new Thread(new Runnable(){
		        public void run(){
					startInsertThread();
					majThreadProgressBar();
		        }
		    });
			t2.start();
		}
		
		cleanParseThread();
		return;
	}
	
	public boolean loadLoggerMan(){
		if(this.loggingLoggerMan)
			return false;
		this.loggingLoggerMan=true;
		this.parseThread.add(new ThreadParse("LoggerMan", window.getCHEMIN_LOGGERMAN(),queries,this));
		this.parseThread.get(this.parseThread.size()-1).start();
		
		return true;
	}
	
	public void changeDataBaseName(){
		String name=(String)JOptionPane.showInputDialog(null, 
			      "Enter new name for the DataBase",
			      "Change Database name",
			      JOptionPane.QUESTION_MESSAGE);
		if(name==null||name.isEmpty()){
			JOptionPane.showMessageDialog(null,"Empty name not allowed","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.window.getConfig().setDataBaseName(name);
	}
	
	public void changeLoggerManLocation(){
		String text = window.getLoggerManLocation().getText();
		if(text==null||text.isEmpty()){
			JOptionPane.showMessageDialog(null,"Empty path not allowed","Error",JOptionPane.ERROR_MESSAGE);
		}
		else if(this.window.getConfig().setLoggerManLocation(text))
			this.window.setCHEMIN_LOGGERMAN(text);
	}
	
	public void changeDataBaseLocation(){
		String text = window.getDataBaseLocation().getText();
		if(text==null||text.isEmpty()){
			JOptionPane.showMessageDialog(null,"Empty path not allowed","Error",JOptionPane.ERROR_MESSAGE);
		}
		else if(this.window.getConfig().setDataBaseLocation(text))
			this.window.setCHEMIN_BD(text);			
	}
	
	///Start the parsing of the dropped files
	public void dropFiles(final List<File> files){
		String typeFichier=WindowEvent.fileTypeDiagBox();
		
		this.parseThread.add(new ThreadParse(typeFichier,queries, files, this));
		this.parseThread.get(this.parseThread.size()-1).start();
		
		cleanParseThread();
	}
	
	///get the type of the file that has to be parsed
	public static String fileTypeDiagBox(){
		String fileType[]={"Photo","GPS","Sensor"};
		
	    String type = (String)JOptionPane.showInputDialog(null, 
	      "Please give the type of file you are importing",
	      "File type",
	      JOptionPane.QUESTION_MESSAGE,null,
	      fileType,fileType[0]);
	    
	    return type;
	}
	
	///Start the thread that insert the data if there are queries waiting
	public void startInsertThread(){
		if(this.insertThread==null||this.insertThread.getState().equals(Thread.State.TERMINATED)){
			InsertionBDD dataBase = new InsertionBDD("null", "typeFichier", window.getCHEMIN_BD()+window.getDB_NAME());
			insertThread = new Thread(new ThreadInsert(dataBase,WindowEvent.queries,this));
			insertThread.start();
		}
	}
	
	///Erase the Parsing Thread that are over
	public void cleanParseThread(){
		for(int i=0;i<this.parseThread.size();i++){
			if(this.parseThread.get(i)==null || this.parseThread.get(i).getState().equals(Thread.State.TERMINATED))
				this.parseThread.remove(i);
		}
		majThreadProgressBar();
	}
	
	///Update the progress bar
	public void majThreadProgressBar(){
		float completion=0.0f;
		if(this.parseThread.size()==0)
		{
			this.window.threadProgressBar.setString("Waiting for parsing thread");
			this.window.threadProgressBar.setValue(0);
		}
		else{
			completion=completion/this.parseThread.size()*100;
			this.window.threadProgressBar.setString(this.parseThread.size()+" threads running");
			this.window.threadProgressBar.setValue((int)completion);
		}
	}
}
