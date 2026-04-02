package service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import model.Account;
import model.Database;
import model.Item;

public class ItemService {
    
    	public static boolean AddListing(String title, String description, String minimumPrice, String endDate, MultipartFile image, String token) throws SQLException {
        Account acct = Database.getUserByToken(token);

		if (acct == null) return false;
		
		Item item = null;

		try {
			item = new Item(title, description, null, BigDecimal.valueOf(Double.parseDouble(minimumPrice)), LocalDate.parse(endDate).atStartOfDay(), image.getBytes());
		} catch(IOException err) {
			return false;
		}

		Database.addItem(item, acct.getUserID());

		return true;
	}
}
