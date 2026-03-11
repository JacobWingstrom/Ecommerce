package model;

public class Administrator extends Account {
	
	public Administrator(String username, String password, String area) {
		super(username, password, area);
	}
	public Administrator(String username, String password, String salt, String area, int userId){
		super(username, password, salt, area, userId);
	}


}
