package Data;

///Enumerate the type of file you can parse
public enum FileType {
	
	APP(1,"App"),
	CLIPBOARD(2,"Clipboard"),
	KEYLOGGER(3,"KeyLogger"),
	KEYSTROKES(4,"Keystrokes"),
	MOUSE(5,"Mouse"),
	SCREENSHOT(6,"Screenshot"),
	SENSOR(7,"Sensor"),
	PHOTO(8,"Photo"),
	GPS(9,"GPS");
	
	private String name;
	private int id;
	
	FileType(int id, String name){
		this.id=id;
		this.name=name;
	}
	
	public String toString(){
		return this.name();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
