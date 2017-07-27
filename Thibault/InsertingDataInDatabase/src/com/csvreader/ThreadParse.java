package com.csvreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Data.GPSData;
import Data.PhotoData;
import Data.QSensorData;
import IHM.WindowEvent;

///Class used to manage the parsing of a file or a list of file while not blocking the software
public class ThreadParse extends Thread{

	private String pathType;	///Type of the file (GPS, Sensor, photo, ...)
	private String path;		
	private List<File> files=null;	///List of the files to parse
	private WindowEvent window;
	
	///Insert queries for the database
	private final ArrayList<ArrayList<String>> queries;	
		
	public ThreadParse(String pathType, String path, ArrayList<ArrayList<String>> queries, WindowEvent window){
		this.pathType=pathType;
		this.path=path;
		this.window=window;
		this.queries=queries;
	}
	
	public ThreadParse(String pathType,ArrayList<ArrayList<String>> queries, final List<File> listFile, WindowEvent window){
		this.pathType=pathType;
		this.queries=queries;
		this.files=listFile;
		this.path="";
		this.window=window;
	}
	
	///This thread just identify the type of file given and launch the corresponding function to parse it
	///This allows the software to parse several files at once
	@Override
	public void run() {
		///The list in which the queries will be stored
		ArrayList<String> listQ=null;

		///Parse loggerMan
		if(pathType.equals("LoggerMan")){
			window.window.textArea.append("Loading LoggerMan\n");
			
			listQ=ParseFile.parseAllFiles(path);
			window.window.textArea.append("LoggerMan loaded\n");
			
			window.loggingLoggerMan=false;
		}else{
	
			///If only one file has been transmitted, we put it in an array
			if(files==null){
				files=new ArrayList<File>();
				files.add(new File(path));
			}
			for(File f : files){
				path=f.getAbsolutePath();
				window.window.textArea.append(path+'\n');
				String type=pathType;
				
				if(f.isDirectory()){		///Determine if the file is a file or a directory
					type+=" directory";
				}else{
					type+=" file";
				}
				if(type.equals("Photo file")){
					if(!PhotoData.verifyFormat(path)){
						JOptionPane.showMessageDialog(null,"Wrong type given","Information",JOptionPane.ERROR_MESSAGE);
						WindowEvent.BD_inUse=false;
						return;
					}
					listQ=ParseFile.parseOnePhoto(path);
				}else if(type.equals("GPS file")){
					if(!GPSData.verifyFormat(path)){
						JOptionPane.showMessageDialog(null,"Wrong type given","Information",JOptionPane.ERROR_MESSAGE);
						WindowEvent.BD_inUse=false;
						return;
					}
					
					///Read the CSV file
					File file = CsvFileHelper.getResource(path);
					final CsvFile csvFile = new CsvFile01(file);
					final List<String[]> data = csvFile.getData();
					listQ=ParseFile.parseGPS(data);
				}else if(type.equals("Sensor file")){
					if(!QSensorData.verifyFormat(path)){
						JOptionPane.showMessageDialog(null,"Wrong type given","Information",JOptionPane.ERROR_MESSAGE);
						WindowEvent.BD_inUse=false;
						return;
					}
					File file = CsvFileHelper.getResource(path);
					final CsvFile csvFile = new CsvFile01(file);
					final List<String[]> data = csvFile.getData();
					listQ=ParseFile.parseQSensor(data);
				}else if(type.equals("Photo directory")){
					listQ=ParseFile.parsePhoto(path);	
				}else if(type.equals("GPS directory")){
					ArrayList<String> listPath=ParseFile.getAllPath(path);
					for(String s : listPath){
						if(!GPSData.verifyFormat(s)){
							Thread t = new Thread(new Runnable(){
						        public void run(){
						        	JOptionPane.showMessageDialog(null,"Wrong type given","Information",JOptionPane.ERROR_MESSAGE);
						        }
						    });
							t.start();
							continue;
						}
						File file = CsvFileHelper.getResource(s);
						final CsvFile csvFile = new CsvFile01(file);
						final List<String[]> data = csvFile.getData();
						listQ=ParseFile.parseGPS(data);	
					}
				}else if(type.equals("Sensor directory")){
					ArrayList<String> listPath=ParseFile.getAllPath(path);
					for(String s : listPath){
						if(!QSensorData.verifyFormat(s)){
							Thread t = new Thread(new Runnable(){
						        public void run(){
						        	JOptionPane.showMessageDialog(null,"Wrong type given","Information",JOptionPane.ERROR_MESSAGE);
						        }
						    });
							t.start();
							continue;
						}
						///Read the CSV file
						File file = CsvFileHelper.getResource(s);
						final CsvFile csvFile = new CsvFile01(file);
						final List<String[]> data = csvFile.getData();
						///Parse the readen lines
						listQ=ParseFile.parseQSensor(data);	
					}
				}
				
				window.window.textArea.append("done.\n");
			}
		}
		if(listQ!=null){
			synchronized(queries){
				queries.add(listQ);	
			}	
		}
		
		Thread t = new Thread(new Runnable(){
	        public void run(){
	        	JOptionPane.showMessageDialog(null,"Adding: "+pathType+" at "+path,"Information",JOptionPane.INFORMATION_MESSAGE);
	        }
	    });
		t.start();
		
		Thread t2 = new Thread(new Runnable(){
	        public void run(){
	        	///update the progress bar and launch the insert thread (if it's not started yet)
				window.startInsertThread();
				window.majThreadProgressBar();
	        }
	    });
		t2.start();
		return;
	}

}
