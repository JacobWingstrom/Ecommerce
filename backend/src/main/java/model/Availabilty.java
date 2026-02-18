package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Availabilty {
	
	private HashMap <LocalDate, AvailabiltyBlock> availableTimes;
	
	public Availabilty() {
		this.availableTimes = new HashMap<LocalDate, AvailabiltyBlock>();
	}

	public Map<LocalDate, AvailabiltyBlock> getAvailableTimes() {
		return Collections.unmodifiableMap(availableTimes);
	}

	
	public boolean setAvailableTimes(LocalDate date, ArrayList<AvailabiltyBlock> times) {
		return false;
	}

}
