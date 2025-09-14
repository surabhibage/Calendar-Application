import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.Pipe.SourceChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * This class represents consits of a main method and main menu navigation for the user
 * @author Surabhi Bage 
 * @version 1.0
 */
public class MyCalendarTester {
    /**
     * This is a main method to take user input to choose different menu options, and the perform different method calls respectively 
     */
    public static void main(String args[]) throws IOException{
        MyCalendar cal = new MyCalendar();
        
        //pritning the current month and year 
        LocalDate date = LocalDate.now();
        int currentDateInt = date.getDayOfMonth();
        int monthLength = date.getMonth().maxLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        String month = date.format(formatter);
        String monthName = month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
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

        
            if (num < 7){ //stay in the same line until Saturday 
                if(i < 10){ 
                    if(i == currentDateInt){
                        System.out.print("[" + i + "]");
                    }
                    else{
                        System.out.print(" " + i); //if it is a single digit, add an extra space 
                    }
                }
                else{
                    if(i == currentDateInt){
                        System.out.print("[" + i + "]");
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
        try{
            cal.populateCalendar();
        }
        catch(Exception e){
            System.out.println("Error: events.txt not found. Check file location.");
        }

        Scanner scanner = new Scanner(System.in); 

        System.out.println("Select one of the following main menu options:");
        System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");

        String option = scanner.nextLine();
        while(true){
            
            if(option.equals("V")){
                LocalDate d = LocalDate.now();
                System.out.println("[D]ay view or [M]onth view ? ");
                String viewOption = scanner.nextLine();
                if(viewOption.equals("D")){
                    cal.dayView(d);
                    System.out.println("[P]revious or [N]ext or [G]o back to main menu ?");
                    viewOption = scanner.nextLine();
                    while(!viewOption.equals("G")) {
                        if(viewOption.equals("P")){
                            d = d.minusDays(1);
                            cal.dayView(d);
                        }
                        else if (viewOption.equals("N")){
                            d = d.plusDays(1);
                            cal.dayView(d);
                        }
                        else{
                            System.out.println("Invalid option");
                        }
                        System.out.println("[P]revious or [N]ext or [G]o back to main menu ?");
                        viewOption = scanner.nextLine();
                    }
                }
                else if(viewOption.equals("M")){
                    cal.monthView(d);
                    System.out.println("[P]revious or [N]ext or [G]o back to main menu ?");
                    viewOption = scanner.nextLine();
                    while(!viewOption.equals("G")) {
                        if(viewOption.equals("P")){
                            d = d.minusMonths(1);
                            cal.monthView(d);
                        }
                        else if (viewOption.equals("N")){
                            d = d.plusMonths(1);
                            cal.monthView(d);
                        }
                        else{
                            System.out.println("Invalid option");
                        }
                        System.out.println("[P]revious or [N]ext or [G]o back to main menu ?");
                        viewOption = scanner.nextLine();
                    }
                }
                else{
                    System.out.println("Invalid viewing option");
                }
            }
            else if(option.equals("C")){
                cal.add();
            }
            else if(option.equals("G")){
                cal.goTo();
            }
            else if(option.equals("E")){
                cal.printEvents();
            }
            else if(option.equals("D")){
                cal.delete();
            }
            else if(option.equals("Q")){
                System.out.println("Good Bye");
                cal.makeOutputFile();
                break;
            }
            else{
                System.out.println("Invalid option. Please choose from the menu.");
            }
            System.out.println();
            System.out.println("Select one of the following main menu options:");
            System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
            option = scanner.nextLine();
        }
    }
}
