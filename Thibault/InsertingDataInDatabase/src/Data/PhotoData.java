package Data;

import java.io.File;

public class PhotoData  extends Data{
	private String nomFichier;
	private String cheminFichier;
	public final static String cheminDossier=System.getProperty("user.home") + File.separator + "Documents/MyLifeLogging/Photo/";
	
	public PhotoData(long creationDate){
		this.creationDate=creationDate;
		this.nomFichier=Long.toString(creationDate)+".jpg";
		this.cheminFichier=cheminDossier;
	}


	@Override
	public String getInsertQuery(String nomTable) {
		// TODO Auto-generated method stub
		return "INSERT INTO "+nomTable+" VALUES('"+ID
				+"','"+creationDate
				+"','"+nomFichier
				+"','"+cheminFichier+"')";
	}


	public String getNomFichier() {
		return nomFichier;
	}


	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}


	public String getCheminFichier() {
		return cheminFichier;
	}


	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}


	public static String getChemindossier() {
		return cheminDossier;
	}
	

	protected static String[] getAuthorizedType(){
		String[] retour = {"jpg","png","bmp","gif","JPG","PNG","BMP","GIF"};
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
