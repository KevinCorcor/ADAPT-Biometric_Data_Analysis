package DataSorting;

import java.awt.Color;

///This class was being used to correct the GPS noise
public class Point {
	public static float moyenneDist;
	public static final float gapDistance=0.006f;
	
	private float x=0,y=0;
	private float distPrev=-1, distNext=-1;
	private boolean distPrevAnormal=false, distNextAnormal=false;
	private boolean estBruit=false;
	public Color color=Color.BLACK;
	
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void calculDistances(Point prev, Point next){
		if(prev!=null)
			this.distPrev=this.distance(prev);
		if(next!=null)
			this.distNext=this.distance(next);
		
	}
	
	public boolean estBruit(Point prev, Point next){
		if(estBruit)
			return estBruit;
		if(prev==null||next==null)
			return false;
		return (estBruit=(prev.distNextAnormal && next.distPrevAnormal));
			
	}
	
	public void distAnormal(){
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
	
	public void setAtMiddle(Point a, Point b){
		this.x = (a.x+b.x)/2;
		this.y = (a.y+b.y)/2;
		this.distPrev=this.distance(a);
		this.distNext=this.distance(b);
		distAnormal();
		this.estBruit=false;
		if(this.distPrevAnormal||this.distNextAnormal)
			System.out.println("Erreur correction");
	}
	
	public float distance(Point b){
		return (float)Math.sqrt((this.x-b.x)*(this.x-b.x)+(this.y-b.y)*(this.y-b.y));
	}
	
	public String toString(){
		String retour="";
		
		retour += "Pos: "+this.x+';'+this.y+'\n';
		retour += "Distance: "+this.distPrev+';'+this.distNext+'\n';
		retour += "Anormale? "+this.distPrevAnormal+';'+this.distNextAnormal+'\n';
		retour += "Est bruit: "+this.estBruit+'\n'+'\n';
		
		retour += "moyenneDistance: "+Point.moyenneDist+'\n';
		retour += "gap: "+Point.gapDistance+'\n'+'\n';
		
		return retour;
	}

	public static float getMoyenneDist() {
		return moyenneDist;
	}

	public static void setMoyenneDist(float moyenneDist) {
		Point.moyenneDist = moyenneDist;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
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
		float[] retour={x,y};
		return retour;
	}
}
