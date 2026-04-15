package model;
import java.time.LocalDateTime;

public class Timeslot{
    LocalDateTime start;
    LocalDateTime end;
    public Timeslot(LocalDateTime start, LocalDateTime end){
        this.start = start;
        this.end = end;
    }
    public LocalDateTime getStart(){
        return this.start;
    }
    public LocalDateTime getEnd(){
        return this.end;
    }
}