import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the time interval of an event
 * @author Surabhi Bage 
 * @version 1.0
 */
public class TimeInterval implements Comparable<TimeInterval>{

    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * This constructs the time interval of an event given its starting time and ending time
     * @param startTime
     * @param endTime
     */
    public TimeInterval(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gives the string representation of the time interval of an event
     * @return A string containing the start time and end time of an event
     */
    public String getTimeInterval(){
        return this.startTime + " " + this.endTime;
    }

    /**
     * Gives the start time of an event 
     * @return the start time of an event as a LocalTime
     */
    public LocalTime getStartTime(){
        return this.startTime;
    }

    /**
     * Gives the end time of an event
     * @return the end time of an event as a LocalTime
     */
    public LocalTime getEndTime(){
        return this.endTime;
    }

    /**
     * Overriden method which comapares the start time of two events 
     * @param t the time interval of the event we want to compare 
     * @return negative integer if the event's start time is less, 0 if the event's start time is equal, or a positive integer of the event's start time is greater than the start time of the param event 
     */
    @Override
    public int compareTo(TimeInterval t){
        return this.startTime.compareTo(t.startTime);
    }

}
