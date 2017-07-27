package DataSorting;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

import Database.InsertionBDD;

///This class was used to correct the GPS noise (it's obsolete now)
public class PaintingWindow extends JFrame implements ComponentListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final int WSIZEX=500, WSIZEY=500;

	PaintPanel pnl = new PaintPanel(this);
	public final static String cheminBD="C:/Users/Administrateur/Documents/MyLifeLogging/Database.db";
	
	public PaintingWindow(){
		this.setTitle("Ma première fenêtre Java");
	    this.setSize(WSIZEX, WSIZEY);
	    this.setLocationRelativeTo(null);               
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setContentPane(pnl);

	    pnl.WSIZEX=this.getSize().width;
	    pnl.WSIZEY=this.getSize().height;
	    //pnl.setLimit(-400, 400,-400,400);
	    this.loadPoint();
	    
	    this.addComponentListener(this);
	    this.addKeyListener(pnl);
	    
	    this.setVisible(true);
	}
	
	private void loadPoint(){
		/*pnl.addPoint(-500, -200);
		pnl.addPoint(0, 0);
		pnl.addPoint(500, 700);*/
		
		InsertionBDD dataBase = new InsertionBDD("null", "typeFichier", cheminBD);
		ResultSet rs = dataBase.getResultSet("SELECT latitude, longitude, creationDate FROM GPS ORDER BY creationDate");
		
		try {
			//int cpt=0;
			while(rs.next()){
				float latitude = rs.getFloat("latitude");
				float longitude = rs.getFloat("longitude");
				
				/*latitude-=53.38;
				longitude+=6.25;
				latitude*=1000;
				longitude*=1000;*/
				
				//System.out.println(Float.toString(latitude) +';'+Float.toString(longitude));
				pnl.addPoint(latitude, longitude);
				//cpt++;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pnl.setLimit();
		pnl.init();
		//pnl.analysePoints();
	}
	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		PaintingWindow w = new PaintingWindow();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	    pnl.WSIZEX=this.getSize().width;
	    pnl.WSIZEY=this.getSize().height;

		pnl.origineX=pnl.WSIZEX/2;
		pnl.origineY=pnl.WSIZEY/2;
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
