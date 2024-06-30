import java.util.*;
import java.io.*;

//    This program allows the user to play a game of Mad Libs. To create a mad lib,
//    they will select a template file and select or create a file to save their
//    Mad Lib to. They may also select an existing file to view.

public class MadLibs{

   public static void main(String[] args)
            throws FileNotFoundException {
   
      Scanner console = new Scanner(System.in);
      boolean exec = true;
      
      System.out.println(

            "Welcome to the game of Mad Libs.\n" +
            "I will ask you to provide various words\n" +
            "and phrases to fill in a story.\n" +
            "The result will be written to an output file.\n"

      );

//      Produces the menu for the program and validates selections.

      while(exec) {
         
         System.out.print("(C)reate mad-lib, (V)iew mad-lib, (Q)uit? ");
         String answer = console.nextLine().toUpperCase();
         if(answer.equals("C")) {

            newMadLib(getInput(console), console);

         } else if(answer.equals("V")) {

            viewMadLib(getInput(console));

         } else if(answer.equals("Q")) {
           exec = false;
         }
      }
   }
   
//   Takes the Scanner passed to it and prompts user to enter a file name they wish
//   to access. Verifies the file is readable and, if so, reads it into a new Scanner
//   object that it returns. Throws new exception for the returned new Scanner object.

   public static Scanner getInput(Scanner console)
            throws FileNotFoundException {

      System.out.print("Input file name: ");
      File input = new File(console.nextLine());

      while(!input.canRead()) {
         System.out.print("File not found. Try again: ");
         input = new File(console.nextLine());
      }
      return new Scanner(input);
   }

//   Uses the console Scanner to ask the user for the target output file name and builds
//   the MadLib line-by-line using the other passed-in input Scanner holding the template
//   file, then outputs the completed lines to the user-targeted file. Throws new exception
//   for the instantiated PrintStream object.

   public static void newMadLib(Scanner input, Scanner console)
         throws FileNotFoundException {

      System.out.print("Output file name: ");
      PrintStream output = new PrintStream(console.nextLine());

      System.out.println();

      while(input.hasNextLine()) {

         String line = input.nextLine();
         Scanner text = new Scanner(line);
         output.println(handleLine(text, console));

      }

      System.out.println("Your mad-lib has been created!");
      System.out.println();

   }

//   Takes the Scanner holding the template line and accepts the user's input
//   to create a new line with the placeholders replaced. Returns the new line
//   back to the newMadLib method.

   public static String handleLine(Scanner text, Scanner console) {

      String newLine = "";

      while(text.hasNext()) {
         String word = text.next();

   //    Finds <placeholder> words and ensures that it reads them until the final ">"
   //    in the event there are extra ">", for example if a user created text file has
   //    placeholder with a ">" within, as well as ignoring "<>"s, then formats the
   //    resulting prompt based on if the first letter is a vowel or not.

         if(word.charAt(0) == '<' && word.charAt(word.length()-1) == '>'
                 && word.length() > 2) {

            String swaparoo = word.substring(1, (word.length()-1));
            swaparoo = swaparoo.replace('-',' ');

            char first = swaparoo.toLowerCase().charAt(0);
            if(first == 'a' || first == 'e' || first == 'i' ||
               first == 'o' || first == 'u') {

               System.out.print("Please type an " + swaparoo + ": ");
            } else {
               System.out.print("Please type a " + swaparoo + ": ");
            }
            word = console.nextLine();
         }
         newLine += (word + " ");
      }
      return newLine;
   }

//   Reads the passed-in scanner holding a validated file, then reads and outputs
//   the text onto the console.

   public static void viewMadLib(Scanner input) {
      System.out.println();
      while(input.hasNextLine()) {
         System.out.println(input.nextLine());
      }
      System.out.println();
   }
}