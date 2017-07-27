package Data;
import com.csvreader.CSVParsable;

public class KeyboardData extends Data implements CSVParsable {

	private String word;
	
	public KeyboardData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub

		word=oneLine[0];
		creationDate=Long.parseLong(oneLine[1]);
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		return "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+word+"')";
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
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
