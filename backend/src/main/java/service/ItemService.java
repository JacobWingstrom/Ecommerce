package service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import model.Account;
import model.Database;
import model.Item;

public class ItemService {

	public static boolean AddListing(String title, String description, String minimumPrice, String endDate,
			MultipartFile image, String token, String location) throws SQLException {
		Account acct = Database.getUserByToken(token);

		if (acct == null)
			return false;

		Item item = null;

		try {
			item = new Item(title,
				description, null,
				new BigDecimal(minimumPrice),
				LocalDate.parse(endDate).atStartOfDay(),
				image.getBytes()
			);
		} catch (IOException err) {
			return false;
		}

		item.setLocation(location);
		Database.addItem(item, acct.getUserID());
		return true;
	}
}
