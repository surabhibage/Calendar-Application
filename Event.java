/**
 * This class represents an event by its name and time interval
 * @author Surabhi Bage 
 * @version 1.0
 */
public class Event implements Comparable<Event>{

    private String name;
    private TimeInterval timeInterval;
    private boolean oneTime;

    /**
     * This constucts an event with a name, time interval, and a flag to check whether the event is recurrinf or one time
     * @param name this is the name of the event 
     * @param timeInterval this represents the duration of the event
     * @param oneTime this is a flag which tells us whether the event is one time or recurring
     */
    public Event(String name, TimeInterval timeInterval, boolean oneTime){
        this.name = name;
        this.timeInterval = timeInterval;
        this.oneTime = oneTime;
    }

    /**
     * Gives a string representation of an event 
     * @return a string with the name and time interval 
     */
    public String getEvent(){
        return this.name + " " + this.timeInterval.getTimeInterval();
    }

    /**
     * Checks whether two events overlap with one another with respect to their time
     * @param e The event in question to check for overlap 
     * @return true if the events overlap, false if they don't 
     */
    public boolean checkOverlap(Event e){
        if(timeInterval.getEndTime().isBefore(e.timeInterval.getEndTime())){
            if(timeInterval.getEndTime().isBefore(e.timeInterval.getStartTime())){
                return false;
            }
        }
        if(timeInterval.getEndTime().isAfter(e.timeInterval.getEndTime())){
            if(e.timeInterval.getEndTime().isBefore(timeInterval.getStartTime())){
                return false;
            }
        }
        return true;
    }

    /**
     * Gives whether the event is one time or recurring with a boolean value 
     * @return true if the event is one time, and false if it isn't 
     */
    public boolean getOneTime(){
        return this.oneTime;
    }

    /**
     * Returns the name of the event 
     * @return String representation of the name of the event
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gives the duration of an event with respect to its TimeInterval 
     * @return returns a timeInterval instance 
     */
    public TimeInterval getTimeInterval(){
        return this.timeInterval;
    }

    /**
     * Overriden method which allows to comapre two events with reagrds to its starting time
     * @param e the event we need to compare 
     * @return negative integer if the event's time interval less, 0 if the event's time interval is equal, or a positive integer if the event's time interval is greater than of the param event 
     */
    @Override
    public int compareTo(Event e){
        return this.timeInterval.compareTo(e.timeInterval);
    }
}
