
import java.util.*;
import java.io.*;

//    This program provides a calendar of the user's
//    choosing for the current year, either specified or current. Once a calendar is
//    displayed, the user can navigate forward or backwards one month, or choose to
//    exit the program.


//    * "fp" option allows printing of currently displayed calendar, if one exists.
//    * added "ae" option for additional event utilities
//      - ability to save events file, including ability to overwrite current calendarEvents.txt
//              file with the eventCal array in program memory
//      - ability to ouput complete list of events in the calendar to console
//      - ability to load a custom file to clear and repopulate the event calendar

public class MyCalendar {

    public static final int SIZE = 15;
    public static final int DAY_SHIFT = 6;
    public static final int WIDTH = 7 * SIZE;

    public static void main(String[] args)
            throws FileNotFoundException {

        String[][] eventCal = constructEventCal();
        int[] workingDate = {0, 0};
        readEventFile(new File("calendarEvents.txt"), eventCal, workingDate);
        menu(eventCal, workingDate);

    }

//    Displays the menu to the user and handles options based on validated user input. Accepts the
//    default event calendar as well as the current working date placeholder array. Additional annotations
//    to clarify menu options.

    public static void menu(String[][] eventCal, int[] workingDate)
            throws FileNotFoundException {
        Calendar calendar = Calendar.getInstance();
        Scanner console = new Scanner(System.in);

        int currentMonth = -1;

        System.out.println();
        System.out.println("Welcome to your calendar");
        System.out.println();

        boolean exec = true;
        while (exec) {

            displayOptions();
            String input = console.nextLine().toLowerCase();

                // Allows viewing the calendar of a specific date.

            if (input.equals("e")) {
                System.out.print("Which date would you like to look at? ");
                String date = console.nextLine();
                boolean valid = false;
                while (!valid) {
                    valid = getInput(date, workingDate);
                    if (valid) {
                        displayMonth(workingDate, buildMonth(workingDate[0], workingDate[1], eventCal));
                        currentMonth = workingDate[0];
                    } else {
                        System.out.print("Please enter a valid date. ");
                        date = console.nextLine();
                    }
                }

                // Calls the Calendar object to view today's date.

            } else if (input.equals("t")) {
                workingDate[0] = calendar.get(Calendar.MONTH) + 1;
                workingDate[1] = calendar.get(Calendar.DATE);

                displayMonth(workingDate, buildMonth(workingDate[0], workingDate[1], eventCal));
                currentMonth = workingDate[0];

                // Iterates forward through calendar by 1 month.

            } else if (input.equals("n")) {
                if (currentMonth == -1) {
                    System.out.println("You must have a calendar displayed.");
                } else {
                    workingDate[0] = currentMonth + 1;
                    if (workingDate[0] > 12) {
                        workingDate[0] -= 12;
                    }
                    workingDate[1] = -1;
                    displayMonth(workingDate, buildMonth(workingDate[0], -1, eventCal));
                    currentMonth = workingDate[0];
                }

                // Iterates backward through calendar by 1 month.

            } else if (input.equals("p")) {
                if (currentMonth == -1) {
                    System.out.println("You must have a calendar displayed. ");
                } else {
                    workingDate[0] = currentMonth - 1;
                    if (workingDate[0] == 0) {
                        workingDate[0] = 12;
                    }
                    workingDate[1] = -1;
                    displayMonth(workingDate, buildMonth(workingDate[0],
                            workingDate[1], eventCal));
                    currentMonth = workingDate[0];
                }

                // Creates new event.

            } else if (input.equals("ev")) {
                System.out.print("Please input new event ('MM/DD <Event Name>') ");
                String event = console.nextLine();
                boolean legit = false;

                while(!legit) {
                    if (event.contains(" ") && getInput(event.substring(0, event.indexOf(' ')), workingDate)) {
                        legit = true;
                        newEvent(event, workingDate, eventCal);
                        System.out.println(eventCal[(workingDate[0] - 1)][(workingDate[1] - 1)] + " has been added to the calendar!");
                    } else {
                        System.out.print("Please input a valid event ('MM/DD <Event Name>') ");
                        event = console.nextLine();
                    }
                }

                // Enters additional event utility menu.

            } else if (input.equals("ae")) {
                eventMenu(eventCal, workingDate, console);

                // Outputs desired calendar to file

            } else if (input.equals("fp")) {
                System.out.print("Which month would you like to print? (MM format). Press enter to print currently displayed month ");
                String fpMonth = console.nextLine();
                boolean valid = false;

                // This if/else if checks for an empty string, and prints the currently
                // displayed month, or solicits a valid month if there isn't one displayed.

                if(fpMonth.equals("") && currentMonth == -1) {
                    System.out.print("You do not have a current month being displayed. ");
                } else if(fpMonth.equals("")) {
                    printMonth(buildMonth(workingDate[0], -1, eventCal), console);
                    System.out.println("Your calendar file has been created!");
                    System.out.println();
                    valid = true;
                }

                while(!valid) {
                    if(validMonth(fpMonth)) {
                        int date = Integer.parseInt(fpMonth);
                        printMonth(buildMonth(date, -1, eventCal), console);
                        System.out.println("Your calendar file has been created!");
                        System.out.println();
                        valid = true;
                    } else {
                        System.out.print("Please enter a valid month. ");
                        fpMonth = console.nextLine();
                    }
                }

                // Exits program.

            }  else if (input.equals("q")) {
                exec = false;
            } else {
                System.out.println("Please enter a valid command.");
            }
        }
    }

//    Displays the event calendar advanced options, having the related arrays passed to it for modification.
//    Additional comments within to clarify menu options.

    public static void eventMenu(String[][] eventCal, int[] workingDate, Scanner console)
                throws FileNotFoundException {

        boolean eventExec = true;
        while(eventExec) {
            displayEventOptions();
            String input = console.nextLine();

            // Views current event calendar

            if (input.equals("v")) {
                displayEvents(eventCal);

            // Reads selected event calendar file into memory

            } else if (input.equals("l")) {
                System.out.print("Which event file do you wish to load? ");
                String file = console.nextLine();
                File eventFile = new File(file);
                boolean inputFile = true;

                while(!eventFile.canRead() && inputFile == true) {
                    System.out.print("Please enter a valid events file or press 'q' to return to events menu. ");
                    file = console.nextLine();
                    if(file.toLowerCase().equals("q")) {
                        inputFile = false;
                        System.out.println();
                    }
                    eventFile = new File(file);
                }

                if(eventFile.canRead()) {
                    clearEventCal(eventCal);
                    readEventFile(eventFile, eventCal, workingDate);
                    System.out.println("New event calendar loaded!");
                    displayEvents(eventCal);
                }

            // Saves current event calendar to file

            } else if (input.equals("s")) {
                System.out.print("Please enter the desired file name for the event calendar, or press enter to save as default. ");
                String fileName = console.nextLine();

                if(fileName.equals("") || fileName.equals("calendarEvents.txt")) {
                    System.out.print("WARNING: This action will overwrite current default calendar. Press 'y' if you wish to continue ");
                    String overwrite = console.nextLine();

                    if (overwrite.equals("y")) {
                        saveEventCal("calendarEvents.txt", eventCal);
                        System.out.println("Default event calendar updated!");
                        System.out.println();
                    }
                } else {
                    saveEventCal(fileName, eventCal);
                    System.out.println(fileName + " has been saved!");
                    System.out.println();
                }

            // Exits additional event utility menu

            } else if (input.equals("q")) {
                eventExec = false;

            } else {
                System.out.println("Please enter a valid command.");
            }
        }
    }

//    Displays the given month and selected day (when not previous/next) on the console.

    public static void displayMonth(int[] workingDate, String[] calendarMonth) {
        System.out.println();
        for(int i = 0; i < calendarMonth.length; i++) {
            System.out.println(calendarMonth[i]);
        }
        displayDate(workingDate[0], workingDate[1]);
        System.out.println();
    }

//    Prints the given calendarMonth to the desired file. *NOTE* intentionally does not call
//    displayDate as that looked really redundant in print form, with specific day being
//    irrelevant there.

    public static void printMonth(String[] calendarMonth, Scanner console)
             throws FileNotFoundException {
        System.out.print("Please specify the desired output file name: ");
        PrintStream output = new PrintStream(console.nextLine());
        output.println();
        for(int i = 0; i < calendarMonth.length; i++) {
            output.println(calendarMonth[i]);
        }
    }
//    Builds calendar output from the user input month and day, calling the appropriate
//    methods to assemble the pieces in the correct format, and returns it to be printed
//    to file or to console.

    public static String[] buildMonth(int month, int select, String[][] eventCal) {
        int length = getLength(month);
        int day = 1;
        int row = 1;
        int shift = getShift(month, DAY_SHIFT);

        int totalRows = 0;
        if(length == 31 && shift > 5) {
            totalRows = 6;
        } else {
            totalRows = 5;
        }

        // This holds all the Strings that will make up the Calendar output

        int monthArrayLength = (14 + totalRows + (SIZE/2) * totalRows);
        String [] calendarMonth = new String[monthArrayLength];
        drawHeader(month, calendarMonth);

        while(day <= length) {
            day = buildRow(month, row, day, shift, length, select, calendarMonth, eventCal);
            row++;
        }
        drawLine();

        return calendarMonth;
    }

//    Builds an individual 7 day week using a "row" parameter to be sure that the first week
//    the month is shifted to the appropriate starting day. Takes the date the row starts on
//    and returns the date that the row ended on. Additionally, takes the day that was
//    selected in the menu, to pass to the formatDate function to mark appropriately and calls
//    a function to check each date against the event calendar. Formats information using the
//    shift modifier passed to it, and stores its output in the calendarMonth array.

    public static int buildRow(int month, int row, int start, int shift, int length, int select,
                               String[] calendarMonth, String[][] eventCal) {
        drawLine();

        int finish = start;
        int eventCheck = start;
        int numDays = 7 - shift;
        int workingLine = (13 + row + ((row-1) * (SIZE/2)));

        // pads first row date and sets remaining rows at full size

        String nextLine = "";
        if(row != 1) {
            numDays = 6;
        } else {
            nextLine = padFirstRow(shift);
        }

        // iterates through row, passing to formatDate and letting the function know if it's the
        // selected date needing to be marked

        boolean isSelected;
        for(int i = start; i <= start + numDays; i++) {

            isSelected = false;
            if (finish <= length) {
                if (i == select) {
                    isSelected = true;
                }

                nextLine += formatDate(i, isSelected);
                finish++;

            } else {
                nextLine += "|";
                for(int j = 1; j < SIZE; j++) {
                    nextLine += " ";
                }
            }
        }
        nextLine += "|";
        calendarMonth[workingLine] = nextLine;

        // checks for matches in event calendar within the row and adds any found to calendar output

        nextLine = "";

        if(row ==1 ){
            nextLine = padFirstRow(shift);
        }

        boolean isEvent = false;
        workingLine++;

        for(int i = eventCheck -1; i < (eventCheck+numDays); i++) {
            if (i < length && eventCal[month-1][i] != null) {
                nextLine += formatEvent(eventCal[month-1][i]);
            } else {
                nextLine += "|";
                for (int j = 1; j < SIZE; j++) {
                    nextLine += " ";
                }
            }
        }

        nextLine += "|";
        calendarMonth[workingLine] = nextLine;

        // outputs the remaining white space to complete the row

        for(int i = 1; i < SIZE/2-1; i++) {
            workingLine++;
            nextLine = "";
            for (int j = 1; j <= 7; j++) {
                String s = "|";
                for (int k = s.length(); k < SIZE; k++) {
                    s += " ";
                }
                nextLine += s;
            }
            nextLine += "|";
            calendarMonth[workingLine] = nextLine;
        }
        calendarMonth[workingLine+1] = drawLine();
        return finish;
    }

//       Draws the ASCII header, drawing the vaguely freemason pyramidy drawing and message from the
//       TIME LORD at the beginning of the chosen month's calendar, centering it according to SIZE.

    public static void drawHeader(int month, String[] calendarMonth) {
        String s = "";
        for(int i = s.length(); i < WIDTH/2 -13; i++) {
            s += " ";
        }

        calendarMonth[0] = s + "      /\\****(<>)****/\\";

        for(int i = 1; i<=6; i++) {
            String newLine = "";
            for(int j = 0; j < 6-i; j++) {
                newLine += " ";
            }
            newLine += (s + "/");
            for(int j = 1; j <= 7; j++) {
                newLine += "*";
            }
            for(int j = 1; j <= (2*i); j++) {
                newLine += "=";
            }
            for(int j = 1; j <= 7; j++) {
                newLine+="*";
            }
            newLine+= "\\";
            calendarMonth[i] = newLine;
        }

        calendarMonth[7] = (s + "\\*BENEVOLENT TIMELORD SAYS*/");
        calendarMonth[8] = (s + "    ~~~~~~ |   |  ~~~~~~");
        calendarMonth[9] = "";

	// centers the month numeral

        s = "";
        for(int i = s.length(); i < WIDTH/2 -1; i++) {
            s = s + " ";
        }
        s += month;
        for(int i = s.length(); i < WIDTH/2; i++) {
            s += " ";
        }
        calendarMonth[10] = s;
        calendarMonth[11] = "";
        displayDayNames(calendarMonth);
        calendarMonth[13] = drawLine();
    }

//      Creates the line that tops each row of days in a week and closes the bottom of the calendar.    

    public static String drawLine() {
        String line = "";
        for(int i = 0; i <= WIDTH; i++) {
            line+= "=";
        }
        return line;
    }

                    //    UTILITY METHODS \\

//      Takes user's input, validates it, and returns whether or not it represents a valid date and format.
//      Stores the given date in the array passed to it.

    public static boolean  getInput (String date, int[] workingDate) {
        boolean valid = false;
        if(date.length() > 0) {
            workingDate[0] = monthFromDate(date);
            workingDate[1] = dayFromDate(date);
        }

        if ((workingDate[0] >= 1 && workingDate[0] <= 12) && (workingDate[1] <= getLength(workingDate[0])
                && workingDate[1] >= 1)) {
            valid = true;
        }
        return valid;
    }

//      Takes the input date, isolates the month and, if it is an integer, returns the parsed value.

    public static int monthFromDate(String date) {

        if(date.contains("/") && !date.startsWith("/")) {
            int delim = date.indexOf("/");
            String month = date.substring(0, delim);

            boolean valid = true;
            int i = 0;
            while(i < month.length() && valid) {
                valid = Character.isDigit(month.charAt(i));
                i++;
            }
            if(valid) {
                return Integer.parseInt(month);
            }
        }
        return -1;
    }

//       Takes the input date, isolates the day and, if it is an integer, returns the parsed value.

    public static int dayFromDate(String date) {

        if(date.contains("/") && !date.startsWith("/")) {
            int delim = date.indexOf("/");
            String day = date.substring(delim + 1);

            boolean valid = true;
            int i = 0;
            while(i < day.length() && valid) {
                valid = Character.isDigit(day.charAt(i));
                i++;
            }
            if(valid) {
                return Integer.parseInt(day);
            }
        }
        return -1;
    }

//      Tests if the string passed to it contains a valid month numeral and returns results.

    public static boolean validMonth(String fpMonth) {
        if (fpMonth.length() > 0 && ((Character.isDigit(fpMonth.charAt(0)) && fpMonth.length() < 2) ||
                (Character.isDigit(fpMonth.charAt(0)) && Character.isDigit(fpMonth.charAt(1)) &&
                        fpMonth.length() < 3))) {
            int date = Integer.parseInt(fpMonth);
            if (date > 0 && date <= 12) {
                return true;
            }
        }
        return false;
    }

//       Given the month passed to it, this returns the number of days in that month.

    public static int getLength(int month) {
        int length;

        if(month == 2) {
            length = 28;
        } else if ((month <=7 && month % 2 == 0) || (month > 7 && month % 2 == 1)) {
            length = 30;
        } else {
            length = 31;
        }
        return length;
    }

//       Takes a month and initial shifted first day of the year as input and returns the total amount
//       amount of cumulative shift for the first day of the given month.

    public static int getShift(int month, int shift) {
        if(month != 1) {
            for (int i = 1; i < month; i++) {
                shift += (getLength(i));
            }
            shift %=7;
            if(shift == 0) {
                shift = 7;
            }
        }
        return shift;
    }

//       Prints the month/day given to it.  Does not print day in situations such as viewing next or
//       previous month, to avoid situations such as "February 31st".

    public static void displayDate(int month, int day) {
        System.out.println("Month: " + month);
        if(day > 0) {
            System.out.println("Day:   " + day);
        }
    }

//       Displays the menu options to the console.

    public static void displayOptions() {
        System.out.println(

            "Please type a command\n" +
                    "\t\"e\" to enter a date and display the corresponding calendar\n" +
                    "\t\"t\" to get today's date and display today's calendar\n" +
                    "\t\"n\" to display the next month\n" +
                    "\t\"p\" to display the previous month\n" +
                    "\t\"ev\" to add a new event to the calendar\n" +
                    "\t\"ae\" to view additional event calendar utilities\n" +
                    "\t\"fp\" to print a calendar to file\n" +
                    "\t\"q\" to quit the program"
        );
    }

//       Displays event utility menu options to the console.

    public static void displayEventOptions() {
        System.out.println(

                "Please type a command\n" +
                        "\t\"v\" to view current event calendar\n" +
                        "\t\"l\" to load a new event calendar file\n" +
                        "\t\"s\" to set current event calendar as default\n" +
                        "\t\"q\" to return to main menu"
        );
    }

//       Adds the horizontal banner of the day names to the calendar.

    public static void displayDayNames(String[] calendarMonth) {
        calendarMonth[12] = (

                formatDayName("SUN") +
                formatDayName("MON") +
                formatDayName("TUE") +
                formatDayName("WED") +
                formatDayName("THU") +
                formatDayName("FRI") +
                formatDayName("SAT")
        );
    }

//       Outputs the given event calendar to the console.

    public static void displayEvents(String[][] eventCal) {
        System.out.println();
        System.out.println("Current event calendar: ");
        System.out.println();
        for(int i = 0; i < eventCal.length; i++) {
            for(int j = 0; j < eventCal[i].length; j++) {
                if(eventCal[i][j] != null) {
                    System.out.println((i+1) + "/" + (j+1) + " " + eventCal[i][j]);
                }
            }
        }
        System.out.println();
    }

//       Takes the numeric date and handles the positioning of that number within each day's
//       cell, marking the selected day if appropriate as per the boolean flag passed to it.
//       It returns the formatted String.

    public static String formatDate(int date, boolean mark) {
        String s = "| " + date;
        if(mark) {
            s += " <";
        }
        for(int i = s.length(); i < SIZE; i++) {
            s += " ";
        }
        return s;
    }

//      Produces a cell line with the given event name padded within it, shortening it if necessary.
//      according to SIZE. Returns the formatted String.

    public static String formatEvent(String eventName) {
        if(eventName.length() > SIZE-3){
            eventName = eventName.substring(0, SIZE-3);
        }
        String s = "| " + eventName;
        for(int i = s.length(); i < SIZE; i++) {
            s += " ";
        }
        return s;
    }

//       Pads the day names according to SIZE constant to align with columns, adjusted for
//       even/odd SIZE variance. Returns formatted String.

    public static String formatDayName(String day) {
        String s = "";

        for(int i = 1; i < (SIZE/2); i++) {
            s += " ";
        }
        s += day;

        if( SIZE % 2 == 0 ) {
            for (int i = 1; i < ((SIZE / 2) - 1); i++) {
                s += " ";
            }
        } else {
            for (int i = 1; i < (SIZE / 2); i++) {
                s += " ";
            }
        }
        return s;
    }

//    Spaces the first row for iterating through to print day numbers and event names.

    public static String padFirstRow(int shift) {
        String s = "";
        for(int i = 1; i < shift; i++){
            s += "|";
            for(int j = 1; j < SIZE; j++) {
                s += " ";
            }
        }
        return s;
    }

//    Builds and returns a correctly-sized array to hold events.

    public static String[][] constructEventCal() {

        String[][] events = new String[12][];

        for(int i = 0; i < events.length; i++) {
                events[i] = new String[getLength(i + 1)];
        }
        return events;
    }

//    Given event information, this creates a new event and adds it to the given event calendar.

    public static void newEvent(String event, int[] workingDate, String[][] eventCal) {

        String date = event.substring(0, event.indexOf(' '));
        String name = event.substring((event.indexOf(' ') + 1));
        name = name.replaceAll(" ", "_");

        workingDate[0] = monthFromDate(date);
        workingDate[1] = dayFromDate(date);

        eventCal[workingDate[0] - 1][workingDate[1] - 1] = name;  // -1 for zero-based indexing
    }

//    Reads the given events file into the given event calendar -- validates it is readable but
//    does NOT validate it is formatted correctly. Incorrectly formatted files will crash the
//    program. I don't know how to do that.

    public static void readEventFile(File eventFile, String[][] eventCal, int[] workingDate)
            throws FileNotFoundException {

        if(eventFile.canRead()) {
            Scanner file = new Scanner(eventFile);
            while(file.hasNextLine()) {
                newEvent(file.nextLine(), workingDate, eventCal);
            }
        }
    }

//    Clears all events from the given event calendar.

    public static void clearEventCal(String[][] eventCal) {
        for(int i = 0; i < eventCal.length; i++) {
            for(int j = 0; j < eventCal[i].length; j++) {
                eventCal[i][j] = null;
            }
        }
    }

//    Saves the event calendar currently in memory to the given file name. Creates file if it does
//    not exist yet.

    public static void saveEventCal(String fileName, String[][] eventCal)
            throws FileNotFoundException  {
        PrintStream output = new PrintStream(fileName);

        for(int i = 0; i < eventCal.length; i++) {
            for(int j = 0; j < eventCal[i].length; j++) {
                if(eventCal[i][j] != null) {
                    output.println((i+1) + "/" + (j+1) + " " + eventCal[i][j]);
                }
            }
        }
    }
}