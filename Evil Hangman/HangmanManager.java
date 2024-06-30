import java.util.*;

public class HangmanManager {

//    This program creates and handles a new game of Evil Hangman. Given a dictionary of words,
//    a word length, and a maximum number of incorrect guesses, it will attempt to make the
//    most difficult game of hangman possible for the player based on each guess they make.

    private String currentPattern; // the pattern displayed to user
    private int strikes; // the remaining wrong guesses
    private Set<String> currentSetOfWords; // the total list of all possible words
    private SortedSet<Character> guesses; // the currently guessed letters

//    Creates a new Hangman game with the given dictionary, word length, and maximum number
//    of incorrect guesses. Throws an IllegalArgumentException if a length of less than 1
//    or a maximum incorrect guesses of less than 0 is passed to it.

    public HangmanManager(List<String> dictionary, int length, int max) {
        if(length < 1 || max < 0 ) {
            throw new IllegalArgumentException();
        }

        this.guesses = new TreeSet<>();
        this.strikes = max;
        this.currentSetOfWords = new TreeSet<>();
        this.currentPattern = "-";

        for(int i = 0; i < length-1; i++) {                 // builds empty starting pattern
            currentPattern += " -";
        }

        for(String w : dictionary) {                        // removes all words of incorrect length
            if(w.length() == length) {
                currentSetOfWords.add(w);
            }
        }
    }

//    Takes the given character guess and compares it to each key in the patterns map
//    selecting the key associated family of patterns that has the most possible words.
//    If the letter occurs in any existing pattern, it will choose the one with the largest
//    pool of words. If the guessed letter does not occur, it will choose the largest pool
//    of all remaining patterns. Returns the number of guesses left. If the set of words
//    is empty or the player has no strikes remaining, will throw an IllegalStateException
//    and if the player repeats an already guessed character, will throw an IllegalArgumentException.


    public int record(char guess) {
        if(currentSetOfWords.isEmpty() || strikes < 1) {
            throw new IllegalStateException();
        } else if (guesses.contains(guess)) {
            throw new IllegalArgumentException();
        }

        String oldPattern = currentPattern;                  // saves currentPattern for comparison at end
        Map<String, Set<String>> patterns = new TreeMap<>(); // a map to hold possible patterns/families

        for(String s : currentSetOfWords) {
            String testPattern = updatePatterns(currentPattern, s, guess);
            if(patterns.containsKey(testPattern)) {
                patterns.get(testPattern).add(s);            // adds word to existing pattern if already exists
            } else {
                patterns.put(testPattern, new TreeSet<>(Set.of(s)));  // associates new pattern entry with new family
            }
        }

        guesses.add(guess);
        selectBestPattern(patterns);

        if(oldPattern.equals(currentPattern)) {               // checks if guess ended up being correct or not
            strikes--;
        }
        return getOccurrences(currentPattern, guess);
    }

//    Returns an adjusted pattern with the given pattern and guessed character.

    private String updatePatterns(String pattern, String word, char guess) {
        boolean[] containsLetter = new boolean[pattern.length()];
        int booleanIterator = 0; // allows for the boolean to increment separately and update correct values with spaces

        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == guess) {
                containsLetter[booleanIterator] = true;      // puts a "true" in checking array if letter found at index
            }
            booleanIterator +=2;
        }

        String newPattern = "";
        for(int i = 0; i < containsLetter.length; i++) {
            if(i % 2 == 1 ) {                                // properly spaces characters out
                newPattern += " ";
            } else if(containsLetter[i]) {                   // builds a new String with correct char based on
                newPattern += guess;                         // current pattern and array
            } else if(currentPattern.charAt(i) != '-') {
                newPattern += currentPattern.charAt(i);
            } else {
                newPattern+= "-";
            }
        }
        return newPattern;
    }

//    Compares patterns within the given map and sets the potential word list to the pattern
//    to the one with the most options, and updates the potential word list to those values.
//    Breaks ties by choosing the first pattern that occurs in the set.

    private void selectBestPattern(Map<String, Set<String>> patterns) {
        currentSetOfWords.clear();
        int wordChoices = 0;

        for(String s : patterns.keySet()) {
            if(patterns.get(s).size() >= wordChoices) {
                currentPattern = s;
                wordChoices = patterns.get(s).size();
            }
        }
        currentSetOfWords.addAll(patterns.get(currentPattern));
    }

//    Returns the number of times the given character appears in the given pattern.

    private int getOccurrences (String pattern, char guess) {
        int occurrences = 0;
        for(int i = 0; i <pattern.length(); i++) {
            if(pattern.charAt(i) == guess) {
                occurrences++;
            }
        }
        return occurrences;
    }

            // PUBLIC ACCESSOR METHODS \\

//    Returns the list of potential words with respect to the currently made guesses.

    public Set<String> words() {
        return currentSetOfWords;
    }

//    Returns the number of remaining incorrect guesses.

    public int guessesLeft() {
        return strikes;
    }

//    Returns the currently guessed letters in alphabetic order.

    public SortedSet<Character> guesses() {
        return guesses;
    }

//    Returns the current pattern for the user to make their next guess. If the potential
//    words list pool is empty, throws IllegalStateException.

    public String pattern() {
        if(currentSetOfWords.isEmpty()) {
            throw new IllegalStateException();
        }
        return currentPattern;
    }
}