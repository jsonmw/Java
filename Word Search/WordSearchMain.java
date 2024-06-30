import java.io.*;
import java.util.*;


// This is the entry into the Word Search Program that handles the menu and input validation.

    // REQUIRED METHODS IN THIS CLASS: printIntro(), generate()
    // EXTRA CREDIT IN THIS CLASS: ability to read from file

public class WordSearchMain {

    private static Scanner input = new Scanner(System.in);
    private static WordSearch current;

    public static void main(String[] args)
            throws FileNotFoundException {

        printIntro();
        menu();

    }

//      Displays the menu and handles the logic behind user input, calling appropriate methods.

    private static void menu()
            throws FileNotFoundException {

        boolean exec = true;
        String choice;

        while (exec) {
            System.out.println();
            displayOptions();
            System.out.println();
            System.out.print("Your choice: ");

            choice = input.nextLine().toLowerCase();
            System.out.println();

            if (choice.equals("g")) {

                System.out.print ("Would you like to create Word Search from a file? ");

                boolean file = confirmYesNo();

                if(current == null && !file) {
                    current = generate();
                } else if(current == null && file) {
                    current = generateFromFile();
                } else {
                    confirmGenerate(file);
                }
            } else if (choice.equals("p")) {
                if(current != null) {
                    current.print();
                    System.out.println();
                } else {
                    System.out.print("You have no Word Search to print!\n");
                }

            } else if (choice.equals("s")) {
                if(current != null) {
                    current.showSolution();
                    System.out.println();
                } else {
                    System.out.print("You have no Word Search solutions to print!\n");
                }

            } else if (choice.equals("f")) {
                if(current != null) {
                    current.save();
                } else {
                    System.out.print("You have no Word Search to save!\n");
                }
            } else if (choice.equals("q")) {
                printOutro();
                exec = false;
            } else {
                System.out.println("Please enter a valid option.");
            }
        }
    }

//      Returns a new Word Search object created using the words entered by the user.

    private static WordSearch generate() {
        System.out.print("Enter the number of words you wish to add: ");
        int wordCount = validateInt();

        input = new Scanner(System.in); // creates new Scanner object to accept String tokens
        ArrayList<String> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(validateWord());
        }
        System.out.print("\nYour Word Search has been created!\n");
        return new WordSearch(words);
    }

//      Returns a new WordSearch object created using words from a file (reads .txt files
//      with each valid alphabetic words on their own individual lines).

    private static WordSearch generateFromFile()
            throws FileNotFoundException {
        System.out.print("Enter the name of the word list file: ");
        String fileName = input.nextLine();
        File wordFile = new File(fileName);

        while(!wordFile.canRead()) {
            System.out.println(fileName + " is not valid. Please enter a valid file name: ");
            fileName = input.nextLine();
            wordFile = new File(fileName);
        }

        Scanner fileReader = new Scanner(wordFile);
        ArrayList<String> words = new ArrayList<>();

        while(fileReader.hasNextLine()) {
            words.add(fileReader.nextLine().toUpperCase(Locale.ROOT));
        }

        System.out.print("\n" + fileName + " has been loaded successfully. New Word Search created!\n");
        return new WordSearch(words);
    }

//      Validates a user-entered integer and returns it.

    private static int validateInt() {
        boolean valid = false;
        String trash;   // used to clear bad input from the Scanner line
        int wordCount = 0;

        while(!valid) {
            if (input.hasNextInt()) {
                wordCount = input.nextInt();
                valid = true;
            } else {
                System.out.print("Please enter a valid number: ");
                trash = input.nextLine();
            }
        }
        return wordCount;
    }

//      Validates a user-entered word containing only alphabetic letters, removes any spaces,
//      then returns it.

    private static String validateWord() {
        boolean valid = false;
        boolean badChar;
        String word = null;

        while(!valid) {
            badChar = false;
            System.out.print("Please enter next word: ");
            word = input.nextLine().toUpperCase();
            word = word.replaceAll(" ", "");

            for(int i = 0; i < word.length(); i++) {
                if (!Character.isLetter(word.charAt(i))) {
                    badChar = true;
                }
            }
            if(!badChar) {
                valid = true;
            } else {
                System.out.println("Please use only alphabetic characters for word: ");
            }
        }
        return word;
    }

//      Confirms that a user wishes to replace their current word search by generating a new one.

    private static void confirmGenerate(boolean file)
            throws FileNotFoundException {
        boolean valid = false;
        boolean yes;
        while(!valid) {
            System.out.print("Do you wish to overwrite your current Word Search? (y/n): ");
            yes = confirmYesNo();
            if(yes && !file) {
                System.out.println();
                current = generate();
                valid = true;
            } else if(yes && file) {
                System.out.println();
                current = generateFromFile();
                valid = true;
            }
            else if(!yes) {
                valid = true;
                System.out.println("\nReturning to main menu.");
            } else {
                System.out.print("Please answer with y or n:");
            }
        }
    }

//      Returns a boolean for y/n options once a valid response is gained from user.

    private static boolean confirmYesNo() {
        boolean valid = false;

        String ans = input.nextLine().toLowerCase();
        while(!valid) {
            if (ans.equals("y")) {
                return true;
            } else if (ans.equals("n")) {
                return false;
            } else {
                System.out.println("Please answer with y or n.");
                ans = input.nextLine().toLowerCase();
            }
        }
        return false;
    }

//      Prints the introductory welcome.

    private static void printIntro() {
        System.out.println(

                "------------------------------------\n" +
                "Welcome to my Word Search Generator!\n" +
                "------------------------------------"
        );
    }

//      Prints a message upon exiting the program.

    private static void printOutro() {
        System.out.println(

                "-----------------------------------------\n" +
                "Thank you for playing, see you next time!\n" +
                "-----------------------------------------\n"
        );
    }

//      Displays menu options.

    private static void displayOptions() {
        System.out.println(

                "Please select an option: \n" +
                "Generate new word search (g)\n" +
                "Print your word search (p)\n" +
                "Show solution to your word search (s) \n" +
                "Save your word search to a file (f) \n" +
                "Quit program (q)"
        );
    }
}