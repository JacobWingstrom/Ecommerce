package model;

public class Administrator extends Account {
	
	public Administrator(String username, String password) {
		super(username, password);
	}
	public Administrator(String username, String password, String salt){
		super(username, password, salt);
	}


}
