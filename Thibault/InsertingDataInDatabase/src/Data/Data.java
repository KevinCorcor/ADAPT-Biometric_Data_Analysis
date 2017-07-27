package Data;

/* Abstract class, every type of Data inherit from it
 * It defines the basic attribute (ID and creationDate) and functions
 * 
 */
public abstract class Data {
	private static long ID_ACTUEL=0;
	protected long ID=0;
	protected long creationDate;
	

	public Data(){
		ID_ACTUEL++;
		ID=ID_ACTUEL;
	}
	
	/// Give the query that insert the data in the databases
	/// with each data in the object
	public abstract String getInsertQuery(String nomTable);

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getCreationDate() {
		return creationDate;
	}
	/**
	 * simple setter
	 * 
	 * @param creationDate - long in milliseconds
	 */
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	
	/// check if the format of the given file is correct or not
	/// (A picture can't be a CSV file)
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

	protected static String[] getAuthorizedType(){
		String[] retour = {};
		return retour;
	}
	
}
