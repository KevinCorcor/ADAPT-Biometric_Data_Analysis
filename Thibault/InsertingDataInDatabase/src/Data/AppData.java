package Data;
import com.csvreader.CSVParsable;

public class AppData extends Data implements CSVParsable {

	private String appName;

	
	public AppData(String[] oneLine){
		fillWithCSV(oneLine);
	}

	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub

		appName=oneLine[0];
		creationDate=Long.parseLong(oneLine[1]);
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		return "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+appName+"')";
	}

	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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
