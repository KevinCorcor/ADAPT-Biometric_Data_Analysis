package Data;
import java.util.Date;

import com.csvreader.CSVParsable;

/**
 * The data corresponding to those captured by the QSensor
 *
 * private float xAxis, yAxis, zAxis;
 * private float battery;
 * private float temperature;
 * private float EDA;
 * private int event;
 * private Date startDate=new Date(0); 
 * 
 * @author kevin
 *
 */
public class QSensorData extends Data implements CSVParsable{

	private float xAxis, yAxis, zAxis;
	private float battery;
	private float temperature;
	private float EDA;
	private int event;
	
	private Date startDate=new Date(0);
	
	/**
	 * 
	 * @param oneLine
	 */
	public QSensorData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	/**
	 * @param oneLine - String array
	 */
	public void fillWithCSV(String[] oneLine){
		
		creationDate=0;
		if(oneLine.length!=6)
		{
			System.out.println("Donnees corrompues");
			xAxis=yAxis=zAxis=battery=temperature=EDA=0;
			event=0;
			return;
		}
		zAxis = Float.parseFloat(oneLine[0]);
		yAxis = Float.parseFloat(oneLine[1]);
		xAxis = Float.parseFloat(oneLine[2]);
		battery = Float.parseFloat(oneLine[3]);
		temperature = Float.parseFloat(oneLine[4]);
		EDA = Float.parseFloat(oneLine[5]);
		event = 0;
	}
	
	/**
	 *	Initiate the start date in the file's header 
	 *
	 * @param oneLine
	 * @param startTime
	 */
	@SuppressWarnings("deprecation")
	public void fillWithCSVDate(String[] oneLine, Date startTime){
		this.startDate=new Date(startTime.getTime());
		this.startDate.setHours(1);
		this.startDate.setMinutes(0);
		this.startDate.setSeconds(0);
		fillWithCSV(oneLine);
	}
	
	/**
	 * @param nomTable String - name of the table to insert into 
	 * @return retour String - single SQL command
	 */
	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		String retour="INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+xAxis
				+"','"+yAxis
				+"','"+zAxis
				+"','"+battery
				+"','"+temperature
				+"','"+EDA
				+"','"+event+"')";
		/*if(temperature < 32 || EDA<0.1)
		{
			System.out.println("Noise detected : "+retour);
			return null;
		}*/
		
		return retour;
	}
	
	/**
	 * 
	 * @param date
	 */
	@SuppressWarnings("deprecation")
	public void setStartDate(Date date){

		this.startDate=new Date(date.getTime());
		this.startDate.setHours(1);
		this.startDate.setMinutes(0);
		this.startDate.setSeconds(0);
		this.creationDate = creationDate+this.startDate.getTime();
	}
	
	/**
	 * 
	 * @param date
	 */
	public void setCreationDate(Long date){
		this.creationDate=date;
	}
	
	/**
	 * 
	 * @return xAxix - float
	 */
	public float getxAxis() {
		return xAxis;
	}
	/**
	 * 
	 * @param xAxis - flaot
	 */
	public void setxAxis(float xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * 
	 * @return yAxis - float
	 */
	public float getyAxis() {
		return yAxis;
	}

	/**
	 * 
	 * @param yAxis
	 */
	public void setyAxis(float yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * 
	 * @return zAxis
	 */
	public float getzAxis() {
		return zAxis;
	}

	/**
	 * 
	 * @param zAxis
	 */
	public void setzAxis(float zAxis) {
		this.zAxis = zAxis;
	}

	/**
	 * 
	 * @return battery float
	 */
	public float getBattery() {
		return battery;
	}

	/**
	 * 
	 * @param battery
	 */
	public void setBattery(float battery) {
		this.battery = battery;
	}

	/**
	 * 
	 * @return temperature - float
	 */
	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getEDA() {
		return EDA;
	}

	public void setEDA(float eDA) {
		EDA = eDA;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	protected static String[] getAuthorizedType(){
		String[] retour = {"csv","eda","CSV","EDA"};
		return retour;
	}
	public static boolean verifyFormat(String pathFile){
		String[] authorizedType = getAuthorizedType();
		//System.out.println(authorizedType[0]);
		//System.out.println(pathFile.substring(pathFile.length()-3).toString());
		for(String s : authorizedType){
			if(s.equals(pathFile.substring(pathFile.length()-3).toString()))
				return true;
		}

		return false;
	}
}
