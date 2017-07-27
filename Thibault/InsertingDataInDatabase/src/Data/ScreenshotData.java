package Data;
import com.csvreader.CSVParsable;

/// Data corresponding to the screenshot taken by LoggerMan
public class ScreenshotData extends Data implements CSVParsable {

	private String appName="";
	private String windowTitle="";
	private String imagePath="";
	private String originalFilePath="C:/Users/Administrateur/Documents/LoggerMan/Screen/";
	
	public ScreenshotData(String[] oneLine){
		fillWithCSV(oneLine);
	}
	
	///Fill the data of the object with the line from the CSV file
	@Override
	public void fillWithCSV(String[] oneLine) {
		// TODO Auto-generated method stub
		appName=oneLine[0];
		for(int i=1;i<oneLine.length-2;i++)
			if(oneLine[i]!=null)
				windowTitle+=oneLine[i];

		imagePath=oneLine[oneLine.length-2];
		
		this.originalFilePath+=imagePath.substring(0,10)+"/"+Integer.parseInt(imagePath.subSequence(11,13).toString())+"/"+imagePath;
		
		creationDate=Long.parseLong(oneLine[oneLine.length-1]);
		
		appName=appName.replace('\'', ' ');
		appName=appName.replace('\"', ' ');
		windowTitle=windowTitle.replace('\'', ' ');
		windowTitle=windowTitle.replace('\"', ' ');
	}

	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		String query="INSERT INTO "+nomTable+" VALUES("+ID
				+",'"+creationDate
				+"','"+appName
				+"','"+windowTitle
				+"','"+imagePath+"')";
		return query;
	}


	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public void setOriginalFilePath(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}	

	protected static String[] getAuthorizedType(){
		String[] retour = {"jpg","JPG"};
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
