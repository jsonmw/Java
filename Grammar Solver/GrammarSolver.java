import java.util.*;

public class GrammarSolver {

//    This program utilizes a given grammar to generate as many random grammatically correct constructs
//    (such as sentences or mathematical expressions) as the user wishes.

    private SortedMap<String, List<String[]>> grammar;
    private final Random random; // used as a constant so each recursive call on the stack isn't instantiating
                                 // a new Random object, creating potentially hundreds of them in memory

//    Creates a new GrammarSolver object from the given grammar.

    public GrammarSolver(List<String> grammar) {

        this.grammar = buildGrammar(grammar);
        this.random = new Random();

    }

//    Returns whether the given symbol exists in the grammar.

    public boolean grammarContains(String symbol) {
        return grammar.containsKey(symbol);
    }

//    Returns a string containing all the symbols in the grammar, separated by
//    commas alphabetically, and enclosed in brackets.

    public String getSymbols() {
        return grammar.keySet().toString();
    }

//    Returns grammatically correct text structures, based on the requirements of
//    the given symbol, the given number of times.

    public String[] generate(String symbol, int times) {
        if(!grammarContains(symbol) || times < 0) {
            throw new IllegalArgumentException();
        }
        String[] output = new String[times];

        for(int i = 0 ; i < output.length; i++) {
            output[i] = grammarCrawler(symbol);
        }
        return output;
    }

//    Explores the grammar looking for the given symbol, searching until it discovers
//    a terminal symbol, and returns the String produced by this process.

    private String grammarCrawler(String symbol) {
        String output = "";
        if(!grammarContains(symbol)) {
            return symbol;
        } else {
            int randomSelector = random.nextInt(grammar.get(symbol).size());
            for(String current : grammar.get(symbol).get(randomSelector)) {
                output += grammarCrawler(current) + " ";
            }
        }
        return output.trim();
    }

//    Given the list of words, returns a grammar Map containing the non-terminal and
//    a list of all the contained rules or words.

    private SortedMap<String, List<String[]>> buildGrammar(List<String> list) {
        SortedMap<String, List<String[]>> grammar = new TreeMap<>();
        for(String word: list) {
            String[] splitNonTerminal = word.trim().split(":");
            grammar.put(splitNonTerminal[0].trim(), formatTerminals(splitNonTerminal[1]));
        }
        return grammar;
    }

//    Given a line of text, returns a list of individual words (separated by pipes) with
//    any white space removed.

    private List<String[]> formatTerminals(String strings) {
        String[] noPipes = strings.split("[|]");
        List<String[]> terminals = new ArrayList<>();

        for (String s : noPipes) {
            String[] noSpaces = s.trim().split("[ \t]+");
            terminals.add(noSpaces);
        }
        return terminals;
    }
}