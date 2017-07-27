package Data;
import com.csvreader.CSVParsable;

public class KeyStrokeData extends Data implements CSVParsable{

	private int key;
	
	public KeyStrokeData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub
				
			try{
				key=Integer.parseInt(oneLine[0]);
						
			}catch(Exception e){
				key=-1;
				System.out.println("Erreur lecture fichier");
			}
		
			creationDate=Long.parseLong(oneLine[1]);
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		
		return "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+key+"')";
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
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
