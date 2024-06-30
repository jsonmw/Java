import java.util.*;
import java.io.*;

public class WordSearch {

//      This class is used to produce new Word Searches.

    // REQUIRED METHODS IN THIS CLASS: print(), showSolution()
    // EXTRA CREDIT IN THIS CLASS: save() method allows for saving Word Search to file.

    private static final int MAX_TRIES = 100; // # of times attempted to place a word before giving up
    private static final int DEFAULT_SIZE = 20; // default size of word search
    private static final int ALPHABET = 26;

    private final int size; // actual size of word search
    private final ArrayList<String> wordList; // list of words contained in word search
    private final int[] workingPos; // holds the values of the current working position for several methods to access efficiently
    private final char[][] solutions; // holds the word search's solution
    private final char[][] wordSearch; // holds the word search with randomized filler letters
    private final Random random; // random object called throughout class to generate integers

//      Constructs a new Word Search object using a list of words. Instantiates the state to initial values
//      based on the given word.

    public WordSearch(ArrayList<String> words) {
        this.wordList = words;
        this.random = new Random();
        this.workingPos = new int[3];  // [y, x, direction (0 horizontal, 1 vertical, 2 diagonal)]
        this.size = setSize(getLongest(), words.size());

        wordSearch = new char[size][size];
        solutions = new char[size][size];
        generateWordSearch();
    }

//      Prints the Word Search to the console.

    public void print() {
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                System.out.print(wordSearch[i][j]);
            }
            System.out.println();
        }
    }

//      Outputs the Word Search to a file.

    public void save()
            throws FileNotFoundException  {

        Scanner input = new Scanner(System.in);
        System.out.print("Enter a name for your Word Search file: ");
        String fileName = input.nextLine();
        PrintStream output = new PrintStream(fileName);

        output.println("WORD SEARCH");
        output.println("------------\n");

        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                output.print(wordSearch[i][j]);
            }
            output.println();
        }

        output.println();
        output.println("SOLUTION");
        output.println("------------\n");

        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                output.print(solutions[i][j]);
            }
            output.println();
        }
        System.out.println("\nYour word search has been saved to " + fileName + "!");
    }

//      Prints the Word Search's solution to the console.

    public void showSolution() {
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                System.out.print(solutions[i][j]);
            }
            System.out.println();
        }
    }

//      Returns an integer containing the longest word in the list's length.

    private int getLongest() {
        int longest = 0;

        for(String s : wordList) {
            if (s.length() > longest) {
                longest = s.length();
            }
        }
        return longest;
    }

//      Places the given word onto both the wordSearch and solutions array, or informs user it
//      was not able to after the maximum number of tries.

    private void placeWord(String word){
        boolean wordPosition = getPosition(word);
        if(wordPosition && workingPos[2] == 0) {
            for(int i = 0; i < word.length(); i++ ) {
                solutions[workingPos[0]][workingPos[1]+i] = word.charAt(i);
                wordSearch[workingPos[0]][workingPos[1]+i] = word.charAt(i);
            }
        } else if(wordPosition && workingPos[2] == 1) {
            for(int i = 0; i < word.length(); i++ ) {
                solutions[workingPos[0]+i][workingPos[1]] = word.charAt(i);
                wordSearch[workingPos[0]+i][workingPos[1]] = word.charAt(i);
            }
        } else if(wordPosition&& workingPos[2]== 2) {
            for(int i = 0; i < word.length(); i++ ) {
                solutions[workingPos[0]+i][workingPos[1]+i] = word.charAt(i);
                wordSearch[workingPos[0]+i][workingPos[1]+i] = word.charAt(i);
            }
        } else {
            System.out.println("Could not place " + word + " after " + MAX_TRIES + " tries.");
        }
    }

//      Attempts the maximum amount of tries to assign the given word a random set of coordinates
//      and a direction. Will return a boolean if it's successful or not.

    private boolean getPosition (String word){
        int tries = MAX_TRIES;

        while(tries > 0) {
            workingPos[0]= random.nextInt(size - word.length() + 1);  // y
            workingPos[1] = random.nextInt(size - word.length() + 1); // x
            workingPos[2] = random.nextInt(3);                        // direction (0: horizontal, 1: vertical, 2: diagonal)

            if(checkSpaces(word)) {
                return true;
            }
            tries--;
        }
        return false;
    }

//      Given a word, verifies that the current working coordinates and direction have enough spaces
//      that are either unused or already contain the letter needed. Returns boolean of success.

    private boolean checkSpaces (String word) {
        int usableSpaces = 0;

        if(workingPos[2] == 0 && (workingPos[1] + word.length()) <= (workingPos[1] + size)) {
            for(int i = 0; i < word.length(); i++) {
                if(solutions[workingPos[0]][workingPos[1]+i] == '-' || solutions[workingPos[0]][workingPos[1]+i] == word.charAt(i)) {
                    usableSpaces++;
                }
            }
        } else if(workingPos[2] == 1 && (workingPos[0] + word.length()) <= (workingPos[0] + size)) {
            for(int i = 0; i < word.length(); i++) {
                if(solutions[workingPos[0]+i][workingPos[1]] == '-' || solutions[workingPos[0]+i][workingPos[1]] == word.charAt(i)) {
                    usableSpaces++;
                }
            }
        } else if(workingPos[2] == 2 &&
                (workingPos[1] + word.length()) <= (workingPos[1] + size) &&
                (workingPos[0] + word.length()) <= (workingPos[0] + size)) {

            for(int i = 0; i < word.length(); i++) {
                if(solutions[workingPos[0]+i][workingPos[1]+i] == '-' || solutions[workingPos[0]+i][workingPos[1]+i] == word.charAt(i) ) {
                    usableSpaces++;
                }
            }
        }
        return (usableSpaces == word.length());
    }

//      Fills the empty solutions array with -'s, and then iterates through the list of words attempting to place them.

    private void generateSolution() {

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                solutions[i][j] = '-';
            }
        }
        for(int i = 0; i < wordList.size(); i++) {
            placeWord(wordList.get(i));
        }
    }

//      Calls the generateSolution to produce fill the arrays with the word list and filler, then replaces all the
//      hyphens on the WordSearch array with randomized letters.

    private void generateWordSearch() {
        generateSolution();
        int randomLetter;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(solutions[i][j] == '-') {
                    randomLetter = (char) random.nextInt(ALPHABET);
                    wordSearch[i][j] = (char) ('A' + randomLetter);
                }
            }
        }
    }

//      Dynamically grows the Word Search depending on longest word or number of words.

    private int setSize(int longestWord, int wordCount) {

        if(longestWord >= DEFAULT_SIZE || wordCount >= DEFAULT_SIZE) {
            if ((longestWord  >= wordCount)) {
                return (longestWord * 4)/3;
            } else {
                return (wordCount * 4)/3;
            }
        } else {
            return DEFAULT_SIZE;
        }
    }
}