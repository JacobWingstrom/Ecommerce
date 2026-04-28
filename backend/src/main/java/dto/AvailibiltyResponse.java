package dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import model.Availability;
import model.AvailabilityBlock;

public class AvailibiltyResponse {

	private String token;
	private boolean[][] availability;

	public AvailibiltyResponse(String token, Availability availability) {
		this.token = token;
		this.availability = createBinaryAvailability(availability);
	}

	public String getToken() {
		return this.token;
	}

	public boolean[][] getAvailability() {
		return this.availability;
	}

	private static boolean[][] createBinaryAvailability(Availability availability) {

		LocalDate today = LocalDate.now();
		boolean[][] binary = new boolean[7][24];

		Map<LocalDate, ArrayList<AvailabilityBlock>> blocksByDate = availability.getBlocksByDate();

		for (Map.Entry<LocalDate, ArrayList<AvailabilityBlock>> entry : blocksByDate.entrySet()) {
			LocalDate date = entry.getKey();
			int dayIndex = (int) ChronoUnit.DAYS.between(today, date);
			if (dayIndex < 0 || dayIndex >= 7) continue;

			for (AvailabilityBlock block : entry.getValue()) {
				int startSlot = timeToSlot(block.getStart());
				int endSlot = timeToSlot(block.getEnd());
				for (int slot = startSlot; slot < endSlot; slot++) {
					if (slot >= 0 && slot < 24) {
						binary[dayIndex][slot] = true;
					}
				}
			}
		}

		return binary;
	}

	private static int timeToSlot(LocalTime time) {

		int hour = time.getHour();
		int minute = time.getMinute();

		// Validate range
		if (hour < 7 || hour > 19 || (hour == 19 && minute > 0)) {
			return -1; // outside 7am–7pm
		}

		int slot = 0;

		// Count full hours past 7am
		for (int h = 7; h < hour; h++) {
			slot += 2; // each hour = 2 half-hour slots
		}

		// Add the half-hour if needed
		if (minute >= 30) {
			slot += 1;
		}

		return slot;
	}

}
