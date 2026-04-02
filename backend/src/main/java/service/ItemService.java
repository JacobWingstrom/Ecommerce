package service;

import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import model.Account;
import model.Database;

public class ItemService {
    
    	public static boolean AddListing(String title, String description, String minimumPrice, String endDate, MultipartFile image, String token) throws SQLException {
        Account acct = Database.getUserByToken(token);

		if (acct == null) return false;
		
		System.out.println("user exist");

		return true;
	}
}
