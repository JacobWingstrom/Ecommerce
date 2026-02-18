package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

public class AvailabiltyBlock {
	
	private ArrayList<Set<LocalTime>> timeList;
	
	public AvailabiltyBlock() {
		this.timeList = new ArrayList<Set<LocalTime>>();
	}

}
