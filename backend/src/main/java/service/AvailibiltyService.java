package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import model.Availability;
import model.AvailabilityBlock;
import model.Database;

public class AvailibiltyService {

	public static boolean updateAvailibilty(String token, boolean[][] avail) throws SQLException {

		LocalDate day = LocalDate.now();
		Availability ava = new Availability();

		int userId = Database.getUserByToken(token).getUserID();

		for (int dayOfWeek = 0; dayOfWeek < avail.length; dayOfWeek++) {

			LocalTime start = LocalTime.of(7, 0);
			LocalTime end = LocalTime.of(19, 30);

			int slot = 0; // 0–23 for 7:00 -> 19:00

			for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(30)) {

				if (avail[dayOfWeek][slot]) {

					LocalTime blockStart = time;
					LocalTime blockEnd = time.plusMinutes(30);

					ava.addBlock(new AvailabilityBlock(blockStart, blockEnd, day));
				}

				slot++;
			}

			day = day.plusDays(1); // must reassign
		}

		return Database.updateAvailablity(userId, ava);
	}

	public static Availability getAvailability(String token) throws SQLException {

		int userId = Database.getUserByToken(token).getUserID();

		return Database.getUserAvailability(userId);
	}
}
