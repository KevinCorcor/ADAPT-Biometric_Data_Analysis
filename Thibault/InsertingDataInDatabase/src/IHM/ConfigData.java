package IHM;

import java.io.File;
import java.io.Serializable;

import javax.swing.JOptionPane;

///ConfigData is a class that handle the configuration of the application
///It's far from being complete, but the "loadLoggerManOnStart" feature works
///The Serializable interface allows the ConfigData object to be saved into a file much easier
public class ConfigData implements Serializable{
	/**
	 *  Variables concerning the global information of the application
	 */
	private static final long serialVersionUID = 2L;
	private boolean launchOnComputerStart=false, 
					loadLoggerManOnStart=false;
	private String dataBaseLocation=System.getProperty("user.home") + File.separator + "Documents/MyLifeLogging/";
	private String dataBaseName="Database.db";
	private String loggerManLocation=System.getProperty("user.home") + File.separator + "Documents/LoggerMan/";
	
	public ConfigData(){
		
	}

	
	public boolean isLaunchOnComputerStart() {
		return launchOnComputerStart;
	}

	public void setLaunchOnComputerStart(boolean launchOnComputerStart) {
		this.launchOnComputerStart = launchOnComputerStart;
	}

	public boolean isLoadLoggerManOnStart() {
		return loadLoggerManOnStart;
	}

	public void setLoadLoggerManOnStart(boolean loadLoggerManOnStart) {
		this.loadLoggerManOnStart = loadLoggerManOnStart;
	}

	public String getDataBaseLocation() {
		return dataBaseLocation;
	}

	///Doesn't work
	public boolean setDataBaseLocation(String dataBaseLocation) {
		File dir=new File(dataBaseLocation);
		if(dir==null||!dir.isDirectory()||!dir.getName().equals("MyLifeLogging")){
			JOptionPane.showMessageDialog(null,"Invalid DataBase location given","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		this.dataBaseLocation = dataBaseLocation;
		return true;
	}

	public String getLoggerManLocation() {
		return loggerManLocation;
	}

	///Doesn't work
	public boolean setLoggerManLocation(String loggerManLocation) {
		File dir=new File(loggerManLocation);
		if(dir==null||!dir.isDirectory()||!dir.getName().equals("LoggerMan")){
			JOptionPane.showMessageDialog(null,"Invalid LoggerMan location given","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		this.loggerManLocation = loggerManLocation;
		return true;
	}


	public String getDataBaseName() {
		return dataBaseName;
	}

	public String getDataBaseFullPath(){
		return this.dataBaseLocation+this.dataBaseName;
	}

	///Doesn't work
	public void setDataBaseName(String dataBaseName) {
		File f = new File(getDataBaseFullPath());
		f.renameTo(new File(getDataBaseLocation()+dataBaseName));
		
		this.dataBaseName = dataBaseName;
	}
}
