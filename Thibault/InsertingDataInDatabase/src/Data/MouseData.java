package Data;
import com.csvreader.CSVParsable;

public class MouseData extends Data implements CSVParsable  {

	private int cursorX, cursorY;
	private int type;
	private int nbOfClicks;
	
	public MouseData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub
		try{
			cursorX=Integer.parseInt(oneLine[0]);				
		}catch(Exception e){
			cursorX=-1;
			System.out.println("Erreur lecture fichier");				
		}
		try{
			cursorY=Integer.parseInt(oneLine[1]);				
		}catch(Exception e){
			cursorY=-1;
			System.out.println("Erreur lecture fichier");				
		}
		try{
			type=Integer.parseInt(oneLine[2]);				
		}catch(Exception e){
			type=-1;
			System.out.println("Erreur lecture fichier");				
		}
		try{
			nbOfClicks=Integer.parseInt(oneLine[3]);				
		}catch(Exception e){
			nbOfClicks=-1;
			System.out.println("Erreur lecture fichier");
		}
		
		creationDate=Long.parseLong(oneLine[4]);
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		return "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+cursorX
				+"','"+cursorY
				+"','"+type
				+"','"+nbOfClicks+"')";
	}

	
	public int getCursorX() {
		return cursorX;
	}

	public void setCursorX(int cursorX) {
		this.cursorX = cursorX;
	}

	public int getCursorY() {
		return cursorY;
	}

	public void setCursorY(int cursorY) {
		this.cursorY = cursorY;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNbOfClicks() {
		return nbOfClicks;
	}

	public void setNbOfClicks(int nbOfClicks) {
		this.nbOfClicks = nbOfClicks;
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
}
