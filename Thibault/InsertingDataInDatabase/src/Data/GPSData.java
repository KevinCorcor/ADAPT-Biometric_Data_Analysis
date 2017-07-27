package Data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import com.csvreader.CSVParsable;
import com.csvreader.ParseFile;

import DataSorting.Point;

public class GPSData extends Data implements CSVParsable  {

	private float latitude=0, longitude=0, altitude=0;
	private Date creationDate=null;
	private String device="", OS="";
	private float version=0;
	
	///Variable used to correct the noise in the gps data
	public static float moyenneDist=0;
	public static final float gapDistance=0.006f;
	
	private float distPrev=-1, distNext=-1;
	private boolean distPrevAnormal=false, distNextAnormal=false;
	private boolean estBruit=false;
	public Color color=Color.black;
	
	public GPSData(GPSData g){
		this.latitude=g.latitude;
		this.longitude=g.longitude;
		this.altitude=g.altitude;
		this.creationDate=new Date(g.creationDate.getTime());
		this.device=g.device;
		this.OS=g.OS;
		this.version=g.version;
		this.estBruit=true;
		this.distPrev=g.distPrev;
		this.distNext=g.distNext;
		this.distPrevAnormal=this.distNextAnormal=false;
		this.color=Color.RED;
	}
	
	public GPSData(float x, float y){
		latitude=x;
		longitude=y;
	}
	
	public GPSData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub
		latitude=Float.parseFloat(oneLine[1]);											//kcor -  for new app changed from 0 to 1
		longitude=Float.parseFloat(oneLine[2]);											//kcor -  for new app changed from 1 to 2
		altitude=Float.parseFloat(oneLine[3]);											//kcor -  for new app changed from 2 to 3
		creationDate = new Date(0);									
		ParseFile.parseDay(oneLine[0].substring(0, 10), creationDate);
		ParseFile.parseHours(oneLine[0].substring(10, 19), creationDate);
		/*device=oneLine[4];
		OS=oneLine[5];
		version=Float.parseFloat(oneLine[6]);*/
		device=null;																	//kcor - these where temperarily changed to suit new app
		OS=null;
		version=0;
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		String retour="";
		//System.out.println(creationDate.toGMTString());
		retour = "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate.getTime()
				+"','"+latitude
				+"','"+longitude
				+"','"+altitude
				+"',NULL,NULL,NULL,NULL,NULL,NULL"
				+",'"+device
				+"','"+OS
				+"','"+version+"')";
		return retour;
	}
	

	protected static String[] getAuthorizedType(){
		String[] retour = {"csv","CSV"};
		return retour;
	}
	
	public static boolean verifyFormat(String pathFile){
		String[] authorizedType = getAuthorizedType();
		System.out.println(authorizedType[0]);
		System.out.println(pathFile.substring(pathFile.length()-3).toString());
		for(String s : authorizedType){
			if(s.equals(pathFile.substring(pathFile.length()-3).toString()))
				return true;
		}

		return false;
	}
	
	///NOISE CORRECTION PART
	public static ArrayList<GPSData> correctionBruit(ArrayList<GPSData> list){
		  float moyenne=0;
		  
		  for(int i=0;i<list.size()-1; i++){
			  ///Calcul of the average distance
			  moyenne+=list.get(i).distance(list.get(i+1));
			  
			  ///Initiate distance
			  GPSData prev=null,next=null;
			  if(i>0)
				  prev=list.get(i-1);
			  if(i<list.size()-1)
				  next=list.get(i+1);
			  list.get(i).calculDistances(prev,next);
			  list.get(i).distAnormal();
			  
			  ///Determine if the actual point is noise
			  if(i>0){
				  prev=next=null;
				  if(i-1>0)
					  prev=list.get(i-2);
				  if(i<list.size()-2)
					  next=list.get(i);
				  if(list.get(i-1).isNoise(prev, next))
					  list.get(i-1).color=Color.BLUE;
			  }
			  
		  }
		  moyenne/=list.size();
		  Point.moyenneDist=moyenne;
		  System.out.println("Moyenne: "+moyenne);
		  return correction(list);
	}
	
	private static ArrayList<GPSData> correction(ArrayList<GPSData> listPoint){
		  ArrayList<GPSData> retour=new ArrayList<GPSData>();
		  
		  for(int i=0;i<listPoint.size()-1; i++){
			  if(listPoint.get(i).isEstBruit() && i>0){
				  retour.add(new GPSData(listPoint.get(i)));
				  listPoint.get(i).setAtMiddle(listPoint.get(i-1), listPoint.get(i+1));

				  i++;
			  }
		  }
		  
		  return retour;
	  }
	
	///Calculate the distance between two points
	public float distance(GPSData b){
		return (float)Math.sqrt((this.latitude-b.latitude)*(this.latitude-b.latitude)+
							(this.longitude-b.longitude)*(this.longitude-b.longitude));
	}
	
	private void distAnormal(){
		///Check if the distance between the previous and the next
		///point is equivalent or abnormal (or inexistant)
		if(distPrev==-1 && distNext==-1){ 
			distPrev=distNext=0;
			distPrevAnormal=distNextAnormal=true;
			estBruit=true;
			return;
		}
		if(distPrev==-1)
			distPrev=distNext;
		if(distNext==-1)
			distNext=distPrev;
		
		if(distPrev+gapDistance < distNext){
			distNextAnormal=true;
		}
		if(distNext+gapDistance < distPrev){
			distPrevAnormal=true;
		}
	}
	
	private void calculDistances(GPSData prev, GPSData next){
		if(prev!=null)
			this.distPrev=this.distance(prev);
		if(next!=null)
			this.distNext=this.distance(next);
		
	}
	
	public boolean isNoise(GPSData prev, GPSData next){
		if(estBruit)
			return estBruit;
		if(prev==null||next==null)
			return false;
		return (estBruit=(prev.distNextAnormal && next.distPrevAnormal));
			
	}
	
	///Put the point identified as noise between the next and the previous point
	private void setAtMiddle(GPSData a, GPSData b){
		this.latitude = (a.latitude+b.latitude)/2;
		this.longitude = (a.longitude+b.longitude)/2;
		this.distPrev=this.distance(a);
		this.distNext=this.distance(b);
		distAnormal();
		this.estBruit=false;
		if(this.distPrevAnormal||this.distNextAnormal){
			System.out.println("Erreur correction");
			if(this.distPrevAnormal)
				System.out.println("\tdist prev anormale");
			if(this.distNextAnormal)
				System.out.println("\tdist next anormale");
		}
	}
	
	
	///Getter & Setter
	public static float getMoyenneDist() {
		return moyenneDist;
	}

	public static void setMoyenneDist(float moyenneDist) {
		Point.moyenneDist = moyenneDist;
	}

	public float getX() {
		return latitude;
	}

	public void setX(float x) {
		this.latitude = x;
	}

	public float getY() {
		return longitude;
	}

	public void setY(float y) {
		this.longitude = y;
	}

	public float getDistPrev() {
		return distPrev;
	}

	public void setDistPrev(float distPrev) {
		this.distPrev = distPrev;
	}

	public float getDistNext() {
		return distNext;
	}

	public void setDistNext(float distNext) {
		this.distNext = distNext;
	}

	public boolean isDistPrevAnormal() {
		return distPrevAnormal;
	}

	public void setDistPrevAnormal(boolean distPrevAnormal) {
		this.distPrevAnormal = distPrevAnormal;
	}

	public boolean isDistNextAnormal() {
		return distNextAnormal;
	}

	public void setDistNextAnormal(boolean distNextAnormal) {
		this.distNextAnormal = distNextAnormal;
	}

	public boolean isEstBruit() {
		return estBruit;
	}

	public void setEstBruit(boolean estBruit) {
		this.estBruit = estBruit;
	}

	public static float getGapdistance() {
		return gapDistance;
	}
	
	public float[] getCoord(){
		float[] retour={latitude,longitude};
		return retour;
	}
}
