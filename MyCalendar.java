import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;
import java.util.Collections;
/**
 * This class consists of all the methods which help manipulate the calendar, as well as aids in display 
 * @author Surabhi Bage 
 * @version 1.0
 */
public class MyCalendar {

    //should have an underlying data structure to hold events 

    //an arraylist can be used to do so 
    private HashMap<LocalDate, ArrayList<Event>> allEvents = new HashMap<>();
    private ArrayList<Event> events;
    
    /**
     * This method populates the calendar with events from events.txt
     * @throws IOException this exception is thrown if events.txt is not found
     */
    public void populateCalendar() throws IOException{
        File f = new File("/Users/surabhibage/Code/CS151/MyFirstCalendar/src/events.txt");
        FileInputStream file = new FileInputStream(f);
        Scanner s = new Scanner(file);
        int count = 0;
        String name = null;
        while(s.hasNextLine()){
            if(count % 2 == 0){
                name = s.nextLine();
            }
            else{
                String[] arrayString = s.nextLine().split(" ");
                if(arrayString.length == 5){
                    String daysOfTheWeek = arrayString[0];
                    //char[] days = new char[daysOfTheWeek.length()];
                    int[] days = new int[daysOfTheWeek.length()];
                    for(int i = 0; i < daysOfTheWeek.length(); i++){
                        if(daysOfTheWeek.charAt(i) == 'S'){
                            days[i] = 7;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'M'){
                            days[i] = 1;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'T'){
                            days[i] = 2;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'W'){
                            days[i] = 3;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'R'){
                            days[i] = 4;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'F'){
                            days[i] = 5;
                        }
                        else if(daysOfTheWeek.charAt(i) == 'A'){
                            days[i] = 6;
                        }
                    }

                    String startTime = arrayString[1];
                    String endTime = arrayString[2];
                    String startDate = arrayString[3];
                    String endDate = arrayString[4];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDate LocalStartDate = LocalDate.parse(startDate, formatter);
                    LocalDate LocalEndDate = LocalDate.parse(endDate, formatter);

                    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                    LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                    TimeInterval t = new TimeInterval(localStartTime, localEndTime);
                    Event e = new Event(name, t, false);

                    for(int i = 0; i < days.length; i++){

                        LocalDate start = LocalStartDate;

                        while(start.getDayOfWeek().getValue() != days[i]){
                            start = start.plusDays(1);
                        }

                        while(start.isBefore(LocalEndDate) || start.isEqual(LocalEndDate)){

                            if(allEvents.containsKey(start)){
                                allEvents.get(start).add(e);
                            }
                            else{
                                events = new ArrayList<>();
                                events.add(e);
                                allEvents.put(start, events);
                            }

                            start = start.plusDays(7);
                        }
                    }
                }
                else{
                    String date = arrayString[0];
                    String startTime = arrayString[1];
                    String endTime = arrayString[2];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDate localDate = LocalDate.parse(date, formatter);

                    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                    LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);
                    TimeInterval t = new TimeInterval(localStartTime, localEndTime);
                    Event e = new Event(name, t, true);

                    if(allEvents.containsKey(localDate)){
                        allEvents.get(localDate).add(e);
                    }
                    else{
                        events = new ArrayList<>();
                        events.add(e);
                        allEvents.put(localDate, events);}
                }
            }
            count++;
        }
    }
    
    /**
     * This method adds an event to the calendar data structure, allEvents
     */
    public void add(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        System.out.print("Enter event date(MM/DD/YYYY): ");
        String date = scanner.nextLine();
        LocalDate starDate = LocalDate.now();

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            starDate = LocalDate.parse(date, formatter);
        }
        catch(DateTimeParseException ex){
            System.out.println("Invalid date or Invalid date format. Please enter a date between 1 - 28/31 or Please use MM/DD/YYYY");
            return;
        }

        System.out.print("Starting time(HH:MM): ");
        String startingTime = scanner.nextLine();
        System.out.print("Ending time(HH:MM): ");
        String endingTime = scanner.nextLine();
        
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm");
        LocalTime startTime = LocalTime.parse(startingTime, formatterTime);
        LocalTime endTime = LocalTime.parse(endingTime, formatterTime);

        TimeInterval t = new TimeInterval(startTime, endTime);
        Event e = new Event(name, t, true);

        if(allEvents.containsKey(starDate)){
            int count = 0;
            ArrayList<Event> eventsTemp = allEvents.get(starDate);

            for(int j = 0; j < eventsTemp.size(); j++){
                if(eventsTemp.get(j).checkOverlap(e)){
                    count++;
                }
            }
            if(count == 0){
                allEvents.get(starDate).add(e);
            }
            else{
                System.out.println("Events overlap, cannot be added");
            }
        }
        else{
            events = new ArrayList<>();
            events.add(e);
            allEvents.put(starDate, events);
        }
    }
    
    
    public void remove(Event e){
        events.remove(e);
    }

    /**
     * This method prints one time events by year, in order of their starting time. It also prints recurring events in order of their starting time
     * @throws FileNotFoundException this excpetion this thrown if event.txt is not found
     */
    public void printEvents() throws FileNotFoundException{

        TreeMap<LocalDate, ArrayList<Event>> sortedEvents = new TreeMap<>(allEvents);

        Set<Integer> yearsList = new HashSet<>();
        for (Map.Entry<LocalDate, ArrayList<Event>> entry : allEvents.entrySet()) {
            yearsList.add(entry.getKey().getYear());
        }

        System.out.println("ONE TIME EVENTS");
        System.out.println();
        

        for(Integer element : yearsList){
            System.out.println(element); //print out the year 
            for(Map.Entry<LocalDate,ArrayList<Event>> entry : sortedEvents.entrySet()) {
                
                if(entry.getKey().getYear() == element){ //print out all the events which are equivalent to the year in the set 
                    Collections.sort(entry.getValue());
                    for(int counter = 0; counter < entry.getValue().size(); counter++){
                        if(entry.getValue().get(counter).getOneTime()){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE"); //EEEE will automatically give the day of the week
                            String dayOfWeek = entry.getKey().format(formatter);
    
                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM");
                            String month = entry.getKey().format(formatter2);
                            
                            System.out.println("  " + dayOfWeek + " " + month + " " + entry.getKey().getDayOfMonth() + " " + entry.getValue().get(counter).getTimeInterval().getStartTime() + " - " + entry.getValue().get(counter).getTimeInterval().getEndTime() + " " + entry.getValue().get(counter).getName());
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("RECURRING EVENTS");
        /*CS151 Lecture
        TR 9:00 10:15 1/23/2025 5/8/2025  */

        File f = new File("/Users/surabhibage/Code/CS151/MyFirstCalendar/src/events.txt");
        FileInputStream file = new FileInputStream(f);
        Scanner s = new Scanner(file);
            TreeMap<LocalDate, ArrayList<Event>> sortedDates = new TreeMap<>();
        while(s.hasNextLine()){
            String name = s.nextLine();
            String details = s.nextLine();
            String[] arrayString = details.split(" ");

            if(arrayString.length == 5){
                String daysOfTheWeek = arrayString[0];
                String startTime = arrayString[1];
                String endTime = arrayString[2];
                String startDate = arrayString[3];
                String endDate = arrayString[4];

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                LocalDate LocalStartDate = LocalDate.parse(startDate, formatter);
                LocalDate LocalEndDate = LocalDate.parse(endDate, formatter);

                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:mm");
                LocalTime localStartTime = LocalTime.parse(startTime, formatterTime);
                LocalTime localEndTime = LocalTime.parse(endTime, formatterTime);

                TimeInterval t = new TimeInterval(localStartTime, localEndTime);
                Event e = new Event(name, t, false);

                if(sortedDates.containsKey(LocalStartDate)){
                    sortedDates.get(LocalStartDate).add(e);
                }
                else{
                    ArrayList<Event> ev = new ArrayList<>();
                    ev.add(e);
                    sortedDates.put(LocalStartDate, ev);
                }
                
            }
            
        }

        for(Map.Entry<LocalDate, ArrayList<Event>> entry : sortedDates.entrySet()){
            for(int counter2 = 0; counter2 < entry.getValue().size(); counter2++){
                System.out.println(entry.getValue().get(counter2).getName() + "\n" + entry.getKey() + " " + entry.getValue().get(counter2).getTimeInterval().getStartTime() + " " + entry.getValue().get(counter2).getTimeInterval().getStartTime());
            }
        }
        
    }

    
    /**
     * This method displys a day view of events, in order of their starting time
     * @param userEnteredDate the date for which the day view needs to be displayed (starts with the current date, but N and P will change the parameter)
     */
    public void dayView(LocalDate userEnteredDate){

        LocalDate todayDate = userEnteredDate;
        //Thu, January 23, 2025 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE");
        String dayOfWeek = todayDate.format(formatter);
        String d = dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM");
        String month = todayDate.format(formatter2);

        System.out.println(d + ", " + month + " " + todayDate.getDayOfMonth() + ", " + todayDate.getYear());
        if(allEvents.containsKey(todayDate)){
            ArrayList<Event> tempEvents = allEvents.get(todayDate);
            TreeSet<Event> sortedEvents = new TreeSet<>(tempEvents);
            for(Event entry : sortedEvents) {
                System.out.println(entry.getName() + " : " + entry.getTimeInterval().getStartTime() + " - " + entry.getTimeInterval().getEndTime());
            }
            /*for(int i = 0; i < sortedEvents.size(); i++){
                System.out.println(sortedEvents.get(i).getName() + " : " + tempEvents.get(i).getTimeInterval().getStartTime() + " - " + tempEvents.get(i).getTimeInterval().getEndTime());
            }*/
        }
    }

    /**
     * This method displays the month view, the days with events highlighted 
     * @param userEnteredDate will give the month for which the month view needs to be displayed (starts with the current date, but N and P will change the parameter)
     */
    public void monthView(LocalDate userEnteredDate){

        //pritning the current month and year 
        LocalDate date = userEnteredDate;
        int monthLength = date.getMonth().maxLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        String month = date.format(formatter);
        String monthName = " " + month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
        System.out.println(monthName + " " + date.getYear());
        System.out.println("Su Mo Tu We Th Fr Sa");

        LocalDate x = LocalDate.of(date.getYear(), date.getMonth(), 1);
        int num = x.getDayOfWeek().getValue();
        
        //this while loop will find the correct starting position 
        int j = 0;
        while(j < num){
            System.out.print("   ");
            j++;
        }
        
        for(int i = 1; i < monthLength; i++){
            int comp = 0;
            LocalDate d1 = LocalDate.of(date.getYear(), date.getMonth(), i);
            if(allEvents.containsKey(d1)){
                comp = i;
            }
        
            if (num < 7){ //stay in the same line until Saturday 
                if(i < 10){ 
                    if(i == comp){
                        System.out.print("{" + i + "}");
                    }
                    else{
                        System.out.print(" " + i); //if it is a single digit, add an extra space 
                    }
                }
                else{
                    
                    if(i == comp){
                        System.out.print("{" + i + "}");
                    }
                    else{
                        System.out.print(i); //if double digit, print as is, no need to add a space 
                    }    
                }
                System.out.print(" ");
                num++;
            }
            else{ //go to the next line after Saturday, and start from Sunday again
                num = 0; //reset num counter 
                System.out.println();
                i--;
            }

        }
        System.out.println();
    }

    /**
     * This method skips to a date mentioned by the user and displays its day view
     */
    public void goTo(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter event date(MM/DD/YYYY): ");
        String date = scanner.nextLine();
        LocalDate nowDate = LocalDate.now();

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            nowDate = LocalDate.parse(date, formatter);
        }
        catch(DateTimeParseException ex){
            System.out.println("Invalid date or Invalid date format. Please enter a date between 1 - 28/31 or Please use MM/DD/YYYY");
            return;
        }
        dayView(nowDate);
    }

    /**
     * This method helps delete an event given by the user, whether it be recurring, one time, or all the events on a given date
     */
    public void delete(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("[S]elected  [A]ll   [R]ecurring ");
        String userOption = scanner.nextLine();

        if(userOption.equals("S")){
            System.out.print("Enter the date of the event to delete: ");
            String d = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate nowDate = LocalDate.parse(d, formatter);

            //Display the all the events on this day, only if that day has events 
            if(allEvents.containsKey(nowDate)){
                ArrayList<Event> tempEvents = allEvents.get(nowDate);
                TreeSet<Event> sortedEvents = new TreeSet<>(tempEvents);
                for(Event entry : sortedEvents){
                    //16:15 - 17:00 Dentist
                    String eventName = entry.getName();
                    System.out.println(entry.getTimeInterval().getStartTime() + " - " + entry.getTimeInterval().getEndTime() + " " + eventName);
                }
            }
            else{
                System.out.println("There are no events scheduled on this day");
                return;
            }
            System.out.print("Enter the name of the event to delete: ");
            String nameOfEvent = scanner.nextLine();
            int count = 0;
            if(allEvents.containsKey(nowDate)){
                
                for(int k = 0; k < allEvents.get(nowDate).size(); k++){
                    if(allEvents.get(nowDate).get(k).getName().equals(nameOfEvent)){
                        allEvents.get(nowDate).remove(k);
                        count++;
                    }
                    
                }
                //remove that key from the hashmap if there are no events that day
                if(allEvents.get(nowDate).size() == 0){
                    allEvents.remove(nowDate);
                }
            }
            if(count == 0){
                System.out.println("Error: No event found with the specified name on this date.");
            }
            else{
                System.out.println("Event deleted");
            }
                
        }
        else if(userOption.equals("A")){

            System.out.print("Enter the date of the event(s) to delete: ");
            String d = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate nowDate = LocalDate.parse(d, formatter);

            //Display the all the events on this day 
            if(allEvents.containsKey(nowDate)){
                ArrayList<Event> tempEvents = allEvents.get(nowDate);
                TreeSet<Event> sortedEvents = new TreeSet<>(tempEvents);
                for(Event entry : sortedEvents){
                    //16:15 - 17:00 Dentist
                    String eventName = entry.getName();
                    System.out.println(entry.getTimeInterval().getStartTime() + " - " + entry.getTimeInterval().getEndTime() + " " + eventName);
                }
            }

            if(allEvents.containsKey(nowDate)){
                allEvents.remove(nowDate);
                System.out.println("All events on given date deleted");
            }
            else{
                System.out.println("Error: No events found on this date.");
            }
        }
        else{
            System.out.print("Enter the name of the recurring event to delete: ");
            String nameOfEvent = scanner.nextLine();
            int count2 = 0;
            for (Map.Entry<LocalDate, ArrayList<Event>> entry : allEvents.entrySet()) {

                for(int i = 0; i < entry.getValue().size(); i++){
                    if(entry.getValue().get(i).getName().equals(nameOfEvent)){
                        entry.getValue().remove(i);
                        count2++;
                    }
                }
            }
            //Remove the date entirely from the HashMap for those days which have no events left for them 
            Iterator<Map.Entry<LocalDate, ArrayList<Event>>> iterator = allEvents.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<LocalDate, ArrayList<Event>> entry = iterator.next();
                if (entry.getValue().isEmpty()) {
                    iterator.remove();
                }
            }
            if(count2 == 0){
                System.out.println("Error: No recurring event found of this name.");
            }
            else{
                System.out.println("Recurring event deleted");
            }
        }
    }

    /**
     * This event helps make an output file with all the events (including those created by the user)
     */
    public void makeOutputFile() throws FileNotFoundException{
        try{
            File outputFile = new File("/Users/surabhibage/Code/CS151/MyFirstCalendar/src/output.txt"); //a new file is not created in the system, this is just a representation 
            outputFile.createNewFile();  //this actually creates the file 
            FileWriter f = new FileWriter(outputFile); //the file will be overwritten every time the loop runs 
            for (Map.Entry<LocalDate, ArrayList<Event>> entry : allEvents.entrySet()) {
                for(int i = 0; i < entry.getValue().size(); i++){
                    f.write(entry.getValue().get(i).getName() + " " + entry.getKey() + " " + entry.getValue().get(i).getTimeInterval().getStartTime() + " " + entry.getValue().get(i).getTimeInterval().getEndTime() + "\n");
                } 
            }
            f.close();
        }
        catch(IOException e){
            System.out.println("FILE COULD NOT BE CREATED");
        }
    } 
}
