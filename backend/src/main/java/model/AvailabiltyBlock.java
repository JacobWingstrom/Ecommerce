package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AvailabiltyBlock {

	private ArrayList<Set<LocalTime>> timeList;

	public AvailabiltyBlock() {
		this.timeList = new ArrayList<Set<LocalTime>>();
	}

	public List<Set<LocalTime>> getTimeList() {
		return Collections.unmodifiableList(this.timeList);
	}

	public boolean insertTimeBlock(LocalTime start, LocalTime finish) {
		
		// ADD CHECKS TO SEE IF THIS IS LEGAL
		if (start.isAfter(finish)) {
			return false;
		}
		
		Set<LocalTime> set = new HashSet<>();
		set.add(start);
		set.add(finish);
		
		this.timeList.add(set);
		
		return true;
	}

}
