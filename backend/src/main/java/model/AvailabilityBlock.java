package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityBlock {

	private LocalTime start;
	private LocalTime end;	
	private LocalDate date;
	private Integer block_id;
	private Integer user_id;

	public AvailabilityBlock(LocalTime start, LocalTime end, LocalDate date) {
		this.start = start;
		this.end = end;
		this.date = date;
		this.block_id = null;
		this.user_id = null;
	}

	public AvailabilityBlock(LocalTime start, LocalTime end, LocalDate date, Integer block_id, Integer user_id){
		this.start = start;
		this.end = end;
		this.date = date;
		this.block_id = block_id;
		this.user_id = user_id;
	}
	
	public LocalTime getStart(){
		return this.start;
	}

	public LocalTime getEnd(){
		return this.end;
	}

	public LocalDate getDate(){
		return this.date;
	}

	public Integer getBlockId(){
		return this.block_id;
	}

	public Integer getUserId(){
		return this.user_id;
	}

}
