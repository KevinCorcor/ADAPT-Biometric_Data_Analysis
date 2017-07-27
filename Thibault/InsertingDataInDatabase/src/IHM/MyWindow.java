package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.csvreader.ParseFile;

import Database.InsertionBDD;

///This Human-Machine Interface follows a View-Controller pattern
///Class that takes care of the appearance of the software (View)
public class MyWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean enCours=true;
	
	///COMPONENT
	private JPanel mainPan=new JPanel(), optionPan=new JPanel();
	private JTabbedPane tabbedPane=new JTabbedPane();
	
	///Load file component
	private JButton btn_loadLoggerMan=new JButton("Load LoggerMan");
	public JProgressBar progressBar=new JProgressBar(),
			threadProgressBar = new JProgressBar();
	public JTextArea textArea = new JTextArea(5, 20);
	private JScrollPane scrollPane = new JScrollPane(textArea); 
	DropPane dropPane = new DropPane();
	
	///Option pan components
	JTextField dataBaseLocation=new JTextField(), loggerManLocation=new JTextField();
	JCheckBox launchOnComputerStart=new JCheckBox("Launch on computer start");
	JCheckBox loadLoggerManOnStart=new JCheckBox("Load LoggerMan on start");
	JButton changeDataBase=new JButton("Change data base location"), 
			changeLoggerMan=new JButton("Change Logger Man location"),
			changeDataBaseName = new JButton("Change Database name"),
			saveData=new JButton("Save changes"),
			eraseData=new JButton("Erase Data");	
	
	///FIXE DATA (aim to a specific folder on the harddisk)
	public final static int WINDOW_SIZE_X=500, WINDOW_SIZE_Y=500;
	private String CHEMIN_BD=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"MyLifeLogging"+File.separator;
	private String CHEMIN_LOGGERMAN=System.getProperty("user.home")+File.separator+"Documents"+File.separator+"LoggerMan"+File.separator;
	private String DB_NAME="Database.db";
	
	private ConfigData config;
	private WindowEvent eventHandler;	///The event Handler of the software (Controller)
	
	
	public static final int NB_TABLE = 9;
	public static final String[] NOM_TABLE = {"App", "Clipboard", "GPS", "KeyLogger", "Keystrokes", "Mouse", "Photo", "Screenshot", "Sensor"};

	public MyWindow(String arg){
		super(arg);
		this.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		FileInputStream fichier;	///Try to read the ConfigData file
		ObjectInputStream ois=null;	///Is there is none, a new one is created
		try {
			fichier = new FileInputStream("Config.ser");
			ois = new ObjectInputStream(fichier);
			this.config=new ConfigData();
			this.config=(ConfigData)ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.config==null){
			config=new ConfigData();
			System.out.println("New config created");
			textArea.append("New config created\n");
		}
		
		eventHandler=new WindowEvent(this);
		
		loadAddFilePane();	///Load the component of the two different tab
		loadOptionPane();
		
		tabbedPane.addTab("Add file", null, mainPan,"Add a file");
		tabbedPane.addTab("Options", optionPan);
		this.setContentPane(this.tabbedPane);
		
		InsertionBDD db = new InsertionBDD("null", "typeFichier", this.getCHEMIN_BD()+this.getDB_NAME());

		createAllTable();
		ParseFile.initMaxInd(db);	///Initiate the max ID and Date of each table in the database (to insert the next data after)
		ParseFile.initMaxDate(db);
		
		if(this.config.isLoadLoggerManOnStart()){	///Check if we have to load loggerman on start (regarding the ConfigData object)
			this.eventHandler.loadLoggerMan();
		}
	}
	
	///The loadXXXPane are just initializing the component (appearance and events)
	private void loadOptionPane(){
		
		dataBaseLocation.setPreferredSize(new Dimension(250, 30));
		loggerManLocation.setPreferredSize(new Dimension(250, 30));
		
		changeDataBase.addActionListener(eventHandler);
		changeLoggerMan.addActionListener(eventHandler);
		changeDataBaseName.addActionListener(eventHandler);
		saveData.addActionListener(eventHandler);
		
		this.loadLoggerManOnStart.setSelected(this.config.isLoadLoggerManOnStart());
		this.launchOnComputerStart.setSelected(this.config.isLaunchOnComputerStart());
		this.dataBaseLocation.setText(this.config.getDataBaseLocation());
		this.loggerManLocation.setText(this.config.getLoggerManLocation());
		
		JPanel pnlChampDB=new JPanel();
		pnlChampDB.setLayout(new BoxLayout(pnlChampDB, BoxLayout.LINE_AXIS));
		pnlChampDB.add(dataBaseLocation);
		changeDataBase.setEnabled(false);
		pnlChampDB.add(changeDataBase);
		JPanel pnlChampLM=new JPanel();
		pnlChampLM.setLayout(new BoxLayout(pnlChampLM, BoxLayout.LINE_AXIS));
		pnlChampLM.add(loggerManLocation);
		changeLoggerMan.setEnabled(false);
		pnlChampLM.add(changeLoggerMan);
		JPanel pnlChamp=new JPanel();
		pnlChamp.setLayout(new BoxLayout(pnlChamp,BoxLayout.PAGE_AXIS));
		pnlChamp.add(pnlChampDB);
		pnlChamp.add(Box.createVerticalStrut(30),BorderLayout.CENTER);
		pnlChamp.add(pnlChampLM);
		pnlChamp.add(Box.createVerticalStrut(30),BorderLayout.CENTER);
		changeDataBaseName.setEnabled(false);
		pnlChamp.add(changeDataBaseName);
		pnlChamp.add(Box.createVerticalStrut(30),BorderLayout.CENTER);
		launchOnComputerStart.setEnabled(false);
		pnlChamp.add(launchOnComputerStart);
		pnlChamp.add(loadLoggerManOnStart);
		pnlChamp.add(Box.createVerticalStrut(30),BorderLayout.CENTER);
		pnlChamp.add(saveData);
		eraseData.addActionListener(this.eventHandler);
		pnlChamp.add(eraseData);
		optionPan.add(pnlChamp);
	}
	
	private void loadAddFilePane(){
		textArea.setEditable(false);	///Avoir the user to write lines in the log panel
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel pnlLoggerMan = new JPanel();
		pnlLoggerMan.setLayout(new BorderLayout());
		btn_loadLoggerMan.addActionListener(eventHandler);
		btn_loadLoggerMan.setPreferredSize(new Dimension(220,60));
		pnlLoggerMan.add(btn_loadLoggerMan,BorderLayout.SOUTH);
		
		
		JPanel pnlChoice = new JPanel();
		pnlChoice.setLayout(new BorderLayout());
		pnlChoice.add(scrollPane,BorderLayout.WEST);
		pnlChoice.add(pnlLoggerMan,BorderLayout.SOUTH);
		
		this.mainPan.setLayout(new BorderLayout());
		this.mainPan.add(pnlChoice,BorderLayout.WEST);
		
		
		///Adding drag&drop zone
		
		dropPane.setSize(new Dimension(200,400));
		dropPane.setMinimumSize(new Dimension(200,400));
		dropPane.setBackground(new Color(240,240,240));
		dropPane.setBorder(BorderFactory.createLineBorder(Color.black));
		dropPane.setMessage("Drop your file here");
		dropPane.setEventHandler(eventHandler);
		this.mainPan.add(dropPane,BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(480,350));
		
		
		///Adding progress bar
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);
		progressBar.setString("Waiting for queries");
		
		threadProgressBar.setMaximum(100);
		threadProgressBar.setMinimum(0);
		threadProgressBar.setStringPainted(true);
		threadProgressBar.setString("Waiting for parsing thread");
		
		JPanel pnlProgressBar = new JPanel();
		pnlProgressBar.setLayout(new BoxLayout(pnlProgressBar,BoxLayout.PAGE_AXIS));
		pnlProgressBar.add(threadProgressBar);
		pnlProgressBar.add(progressBar);
		this.mainPan.add(pnlProgressBar,BorderLayout.SOUTH);
	}
	
	public static void main(String[] args){
		MyWindow window = new MyWindow("MyLifeLogging");
		window.setVisible(true);
	}
	
	///the name is explicit
	public void createAllTable(){
		InsertionBDD dataBase = new InsertionBDD("null", "typeFichier", getCHEMIN_BD()+getDB_NAME());
			ArrayList<String> creatingTableQueries = getCreatingTableQueries();
			for(String s : creatingTableQueries){
				dataBase.executeQuery(s);
			}
		}
	
	public static ArrayList<String> getCreatingTableQueries(){
		ArrayList<String> retour = new ArrayList<String>();
		retour.add("CREATE TABLE IF NOT EXISTS "+"App"+" (id INTEGER, creationDate BIGINT, appName VARCHAR2)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Clipboard"+" (id INTEGER, creationDate BIGINT, filePath VARCHAR2)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"KeyLogger"+" (id INTEGER, creationDate BIGINT, word VARCHAR2)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Keystrokes"+" (id INTEGER, creationDate BIGINT, key INTEGER)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Mouse"+" (id INTEGER, creationDate BIGINT, cursorX INTEGER, cursorY INTEGER, type INTEGER, nbOfClicks INTEGER)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Screenshot"+" (id INTEGER, creationDate BIGINT, appName VARCHAR2, windowTitle VARCHAR2, imagePath VARCHAR2)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"GPS"+" (id INTEGER, creationDate BIGINT, latitude FLOAT, longitude FLOAT, altitude FLOAT, "
				+ "street VARCHAR2, neighborhood VARCHAR2,city VARCHAR2, district VARCHAR2, postCode VARCHAR2, country VARCHAR2,"
				+ " device VARCHAR2, OS VARCHAR2, version FLOAT)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Photo"+" (id INTEGER, creationDate BIGINT, fileName VARCHAR2, filePath VARCHAR2)");
		retour.add("CREATE TABLE IF NOT EXISTS "+"Sensor"+" (id INTEGER, creationDate BIGINT, xMove FLOAT, yMove FLOAT, zMove FLOAT, battery FLOAT, temperature FLOAT, eda FLOAT, event SMALLINT)");
		return retour;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public JPanel getMainPan() {
		return mainPan;
	}

	public void setMainPan(JPanel mainPan) {
		this.mainPan = mainPan;
	}

	public String getCHEMIN_BD() {
		return CHEMIN_BD;
	}
	
	public void setCHEMIN_BD(String cHEMIN_BD) {
		CHEMIN_BD = cHEMIN_BD;
	}
	
	public String getCHEMIN_LOGGERMAN() {
		return CHEMIN_LOGGERMAN;
	}
	
	public void setCHEMIN_LOGGERMAN(String CHEMIN_LOGGERMAN) {
		this.CHEMIN_LOGGERMAN = CHEMIN_LOGGERMAN;
	}
	
	public String getDB_NAME(){
		return DB_NAME;
	}

	public JPanel getOptionPan() {
		return optionPan;
	}

	public void setOptionPan(JPanel optionPan) {
		this.optionPan = optionPan;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public JButton getBtn_loadLoggerMan() {
		return btn_loadLoggerMan;
	}

	public void setBtn_loadLoggerMan(JButton btn_loadLoggerMan) {
		this.btn_loadLoggerMan = btn_loadLoggerMan;
	}

	public JTextField getDataBaseLocation() {
		return dataBaseLocation;
	}

	public void setDataBaseLocation(JTextField dataBaseLocation) {
		this.dataBaseLocation = dataBaseLocation;
	}

	public JTextField getLoggerManLocation() {
		return loggerManLocation;
	}

	public void setLoggerManLocation(JTextField loggerManLocation) {
		this.loggerManLocation = loggerManLocation;
	}

	public JCheckBox getLaunchOnComputerStart() {
		return launchOnComputerStart;
	}

	public void setLaunchOnComputerStart(JCheckBox launchOnComputerStart) {
		this.launchOnComputerStart = launchOnComputerStart;
	}

	public JCheckBox getLoadLoggerManOnStart() {
		return loadLoggerManOnStart;
	}

	public void setLoadLoggerManOnStart(JCheckBox loadLoggerManOnStart) {
		this.loadLoggerManOnStart = loadLoggerManOnStart;
	}

	public ConfigData getConfig() {
		return config;
	}

	public void setConfig(ConfigData config) {
		this.config = config;
	}

	public WindowEvent getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(WindowEvent eventHandler) {
		this.eventHandler = eventHandler;
	}

	public void setDB_NAME(String dB_NAME) {
		DB_NAME = dB_NAME;
	}

}
