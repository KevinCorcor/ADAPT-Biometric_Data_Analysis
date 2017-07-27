package Database;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import IHM.WindowEvent;

public class ThreadInsert implements Runnable{

	public InsertionBDD dataBase;
	
	///The array of array of string allow the thread to get one array
	///Then letting the other thread use the array of array
	private final ArrayList<ArrayList<String>> queries;
	private final WindowEvent windowE;
	
	public ThreadInsert(InsertionBDD bd, ArrayList<ArrayList<String>> list, WindowEvent w){
		this.dataBase=bd;
		this.queries=list;
		this.windowE=w;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		WindowEvent.BD_inUse=true;
		while(WindowEvent.BD_inUse){
			ArrayList<String> actualQueries=null;
			///Get the first array in the list
			synchronized(queries){
				if(queries.size()<1)
					break;
				actualQueries= queries.remove(0);
			}
			if(actualQueries==null)
				continue;
			int cpt=0;
			///Insert the queries one by one
			for(String quer : actualQueries){
				if(quer!=null){
					this.dataBase.executeQuery(quer);
					cpt++;
				}
				String textProgress=cpt+" / "+actualQueries.size()+" queries completed";
				this.windowE.window.progressBar.setString(textProgress);
				this.windowE.window.progressBar.setValue(cpt*100/actualQueries.size());
			}
			actualQueries=null;

			final int cpt2=cpt;
			Thread t = new Thread(new Runnable(){
		        public void run(){
		        	JOptionPane.showMessageDialog(null,"Approximatly " + cpt2 + " lines added","File added",JOptionPane.INFORMATION_MESSAGE);
		        	windowE.cleanParseThread(); ///Delete the thread that are done
		        }
		    });
			t.start();
			
			///If there are still queries, the database is still being used
			WindowEvent.BD_inUse=(queries.size()>0);
		}
	}

}
