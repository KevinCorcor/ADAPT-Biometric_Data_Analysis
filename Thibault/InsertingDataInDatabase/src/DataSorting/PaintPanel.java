package DataSorting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
 
///This class was used to correct the GPS noise (it's obsolete now)
public class PaintPanel extends JPanel implements KeyListener{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame fenetre=null;
	
	private float minX=0, maxX=100, minY=0, maxY=100;
	public float origineX=1, origineY=1;
	public int WSIZEX=500, WSIZEY=500;
	
	private ArrayList<Point> listPoint=new ArrayList<Point>();
	//private ArrayList<Point> listCorrige=new ArrayList<Point>();
	public int ovalSize=10;
	
	///Control affichage
	private final int NB_POINT_AFFICHE=7;
	private int actualFirstPoint=0;
	
	public PaintPanel(JFrame fen){
		this.fenetre=fen;
		this.addKeyListener(this);
	}
	
  public void paintComponent(Graphics g){
    //Vous verrez cette phrase chaque fois que la méthode sera invoquée
    //System.out.println(listPoint.size());
    
    //g.setColor(Color.RED);
    //g.fillOval(origineX-20,origineY-20, 40,40);
    //System.out.println(this.actualFirstPoint);
    int cpt=0;
    float moyenne=0;
    for(int i=0;i<listPoint.size()-1; i++){
    	moyenne+=distance(listPoint.get(i).getX(), listPoint.get(i).getY(), listPoint.get(i+1).getX(), listPoint.get(i+1).getY());
    }
    moyenne/=listPoint.size();
    //System.out.println("Moyenne: "+moyenne);
   
    moyenne=0;
    for(cpt=0;cpt<this.NB_POINT_AFFICHE;cpt++){
    	moyenne+=distance(listPoint.get((this.actualFirstPoint+cpt)%listPoint.size()).getX(), 
    			listPoint.get((this.actualFirstPoint+cpt%listPoint.size())%listPoint.size()).getY(), 
    			listPoint.get((this.actualFirstPoint+cpt+1)%listPoint.size()).getX(),
    			listPoint.get((this.actualFirstPoint+cpt+1%listPoint.size())%listPoint.size()).getY());
        
    	float coord[]=listPoint.get((this.actualFirstPoint+cpt)%listPoint.size()).getCoord();
    	//System.out.println(this.actualFirstPoint+cpt);

    	float coordX=(coord[0]-minX)*(WSIZEX-1)/(maxX-minX), 
    			coordY=(coord[1]-minY)*(WSIZEY-1)/(maxY-minY);
    	
    	String sCoord=Float.toString(coordX);
    	sCoord+=';'+Float.toString(coordY);
    	
    	g.setColor(listPoint.get(this.actualFirstPoint+cpt).color);
    	sCoord=Float.toString(coord[0])+';'+Float.toString(coord[1]);
    	g.fillOval((int)coordX,(int)coordY, 10,10);
    	g.drawString(sCoord, (int)coordX,(int)coordY);
    	
    	System.out.println(listPoint.get(this.actualFirstPoint+cpt).toString() + ' ' + moyenne);   	
    }
   /* for(Point p : this.listCorrige){
    	float coord[]=p.getCoord();

    	float coordX=(coord[0]-minX)*(WSIZEX-1)/(maxX-minX), 
    			coordY=(coord[1]-minY)*(WSIZEY-1)/(maxY-minY);
    	g.fillOval((int)coordX,(int)coordY, 10,10);
    }*/
    moyenne/=this.NB_POINT_AFFICHE;
    //System.out.println("moyenne points: "+moyenne);
  }
  
  public void addPoint(float x, float y){
	  //float coord[]={x,y};
	  listPoint.add(new Point(x,y));
  }
  
  public void init(){
	  //int cpt=0;
	  float moyenne=0;
	  for(int i=0;i<listPoint.size()-1; i++){
		  ///Calcul moyenne
		  moyenne+=distance(listPoint.get(i).getX(), listPoint.get(i).getY(), listPoint.get(i+1).getX(), listPoint.get(i+1).getY());
		  
		  ///Initialisation distance
		  Point prev=null,next=null;
		  if(i>0)
			  prev=listPoint.get(i-1);
		  if(i<listPoint.size()-1)
			  next=listPoint.get(i+1);
		  listPoint.get(i).calculDistances(prev,next);
		  listPoint.get(i).distAnormal();
		  
		  ///Détermine si estBruit
		  if(i>0){
			  prev=next=null;
			  if(i-1>0)
				  prev=listPoint.get(i-2);
			  if(i<listPoint.size()-2)
				  next=listPoint.get(i);
			  if(listPoint.get(i-1).estBruit(prev, next))
				  listPoint.get(i-1).color=Color.RED;
		  }
		  
	  }
	  moyenne/=listPoint.size();
	  Point.moyenneDist=moyenne;
	  System.out.println("Moyenne: "+moyenne);
	  correction();
  }
  
  public void correction(){
	  for(int i=0;i<listPoint.size()-1; i++){
		  if(listPoint.get(i).isEstBruit() && i>0){
			  Point correction = new Point(0,0);
			  correction.setAtMiddle(listPoint.get(i-1), listPoint.get(i+1));
			  correction.color=Color.BLUE;
			  //this.listCorrige.add(correction);
			  this.listPoint.add(i, correction);
			  i++;
		  }
	  }
  }
  
  public void analysePoints(){
	  /*for(int i=0;i<listPoint.size();i++){
		  if(pointCorrect(listPoint, i))
		  {
			  listColor.remove(i);
			  listColor.add(i, Color.RED);
		  }
	  }*/
	  //pointCorrect(listPoint,0);
  }
  
  public boolean pointCorrect(ArrayList<float[]> list, int indPoint){
	  /*float moy1=moyennePoint(list,indPoint-5,indPoint-1),
			  moy2=moyennePoint(list,indPoint+1,indPoint+5),
			  moy=moyennePoint(list,indPoint-1,indPoint+1);
	  float gap=0.5f;*/
	  
	  float moyTot=moyennePoint(list,0,list.size());
	  for(int i=1;i<list.size()-1;i++){
		  if(distance(list.get(i)[0],list.get(i)[1],list.get(i+1)[0],list.get(i+1)[1])>moyTot
				  && distance(list.get(i)[0],list.get(i)[1],list.get(i-1)[0],list.get(i-1)[1])>moyTot){
		  }
	  }
	  
	 return true;
  }
  
  public float moyennePoint(ArrayList<float[]> list, int min, int max){
	  float moyenne=0;
	  //int deplacement;
	  for(int i=min;i<max-1;i++){
		  if(i<0){
			  min=i+1;
			  continue;
		  }
		  if(i>=list.size()-1){
			  max=i;
			  break;
		  }
		  moyenne+=distance(list.get(i)[0],list.get(i)[1],list.get(i+1)[0],list.get(i+1)[1]);
	  }
	  moyenne/=(max-min);
	  return moyenne;
  }
  
  public float distance(float x1, float y1, float x2, float y2){
	  return (float)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
  }
  
  public void setLimit(int winSizeX, int winSizeY){
	  maxX=winSizeX/2;
	  maxY=winSizeY/2;
	  minX=-maxX;
	  minY=-maxY;
	  
	  origineX=this.WSIZEX/2+20;
	  origineY=this.WSIZEY/2+20;
  }
  
  public void setLimit(int mx,int max,int my,int may){
	  this.minX=mx;
	  this.maxX=max;
	  this.minY=my;
	  this.maxY=may;

	  origineX=this.WSIZEX/2+20;
	  origineY=this.WSIZEY/2+20;
  }
  
  public void setLimit(){
	  this.minX=this.maxX=this.listPoint.get(0).getX();
	  this.minY=this.maxY=this.listPoint.get(0).getY();
	  
	  for(Point point : this.listPoint){
		  float[] coord = point.getCoord();
		  if(coord[0]<this.minX)
			  this.minX=coord[0];
		  else if(coord[0]>this.maxX)
			  this.maxX=coord[0];
		  
		  if(coord[1]<this.minY)
			  this.minY=coord[1];
		  else if(coord[1]>this.maxY)
			  this.maxY=coord[1];
	  }
	  
	  float gap=(maxX-minX)/6;
	  maxX+=gap;
	  minX-=gap;
	  gap=(maxY-minY)/6;
	  maxY+=gap;
	  minY-=gap/2;

	  origineX=this.WSIZEX/2+20;
	  origineY=this.WSIZEY/2+20;
  }
  
  

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	
		this.actualFirstPoint++;
		this.actualFirstPoint%=this.listPoint.size();
		this.fenetre.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	
	}
}