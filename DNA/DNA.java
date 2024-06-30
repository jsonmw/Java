import java.util.*;
import java.io.*;

//This program allows a user to input a file containing genetic sequences and
//processes the information, collecting notable statistics about the nucleotides-
//how often they appear, mass statistics, a list of the codons that make it up,
//and whether or not it represents a protein - It then formats the data into
//a report and outputs it to the selected file.

public class DNA {
                                    //      Adenine  Cytosine Guanine  Thymine  Junk
    public static final double[] MASS = {135.128, 111.103, 151.128, 125.107, 100.000};
    public static final int MINIMUM_CODONS = 5;
    public static final int MINIMUM_C_G_PERCENT = 30;
    public static final int UNIQUE_NUCLEOTIDES = 4;
    public static final int PER_CODON = 3;


    public static void main(String[] args)
                throws FileNotFoundException {

        Scanner console = new Scanner(System.in);

        System.out.println(
        "This program reports information about DNA\n" +
        "nucleotide sequences that may encode proteins\n"
        );

        System.out.print("Input file name? ");
        File file = new File(console.next());
        Scanner input = new Scanner(file);
        System.out.print("Output file name? ");
        PrintStream output = new PrintStream(console.next());
        System.out.println();

        while(input.hasNextLine()) {

            analyzeSequence(input, output);

        }
    }

//    Takes the Scanner containing the file and reads the next sequence, this initializes
//    and holds the arrays to store the data of each sequence. Calls the appropriate methods
//    for each array, passing them and the PrintStream object it is given to an output method.

    public static void analyzeSequence(Scanner input, PrintStream output) {
        String[] current = readFile(input);
        String name = current[0];
        String sequence = current[1];

        int[] nucleoCount = new int[UNIQUE_NUCLEOTIDES];
        int junk= countNucleotides(sequence, nucleoCount);
        sequence = noJunk(sequence);

        double [] mass = new double[UNIQUE_NUCLEOTIDES];
        double totalMass = getMass(mass, nucleoCount, junk);
        double[] massStats = getMassPercents(mass, totalMass);

        String[] codons = getCodons(sequence);

        boolean proteinFlag = isProtein(codons, massStats);

        outputResults(output, name, sequence, nucleoCount, massStats, totalMass,
                codons, proteinFlag);
    }

//    Given the scanner containing the input file, this returns an array holding the
//    name and sequence to be tested.

    public static String[] readFile(Scanner input) {

        String name = input.nextLine();
        String dna = input.nextLine().toUpperCase();
        String[] pair = {name, dna};

        return pair;
    }

//    Given a sequence, this counts the number of each nucleotide appearing in the given sequence,
//    and increments elements of a counting array that tracks each nucleotide. It returns
//    the number of junk molecules found as a separate integer.

    public static int countNucleotides (String DNA, int[] nucleotides) {

        int junk = 0;

        for(int i = 0; i < DNA.length(); i++) {
            if(DNA.charAt(i) == 'A') {
                nucleotides[0]++;
            } else if(DNA.charAt(i) == 'C') {
                nucleotides[1]++;
            } else if(DNA.charAt(i) == 'G') {
                nucleotides[2]++;
            } else if(DNA.charAt(i) == 'T') {
                nucleotides[3]++;
            } else if(DNA.charAt(i) == '-') {
                junk++;
            }
        }

        return junk;
    }

//    Takes the number of each nucleotide and junk, calculates and stores the
//    total mass of each, then returns the sum mass of all nucleotides/junk.

    public static double getMass (double[] mass, int[] counts, int junk) {
        for(int i = 0; i < mass.length; i++) {
            mass[i] = (counts[i] * MASS[i]);
        }

        double total = 0;

        for(int i = 0;i < mass.length; i++) {

            total += mass[i];
        }
        return total + (junk * MASS[4]);
    }


//    Takes the mass of all the molecules and the total mass of the sequence,
//    and returns the calculated percent each nucleotide contributes to the total

    public static double[] getMassPercents(double[] mass, double total) {
        double[] percents = new double[4];

        for(int i = 0; i < percents.length; i++) {
            percents[i] = Math.round((mass[i] / total) * 1000) /10.0;
        }
        return percents;
    }

//    Takes the given sequence and returns a String with no junk characters.

    public static String noJunk (String dna) {
        dna.replaceAll("-",  "");
        return dna;
    }

//    Validates whether or not the given sequence is a protein and returns
//    what it finds.

    public static boolean isProtein(String[] codons,  double[] percent) {

        boolean protein = false;

        if(codons.length >= MINIMUM_CODONS && codons[0].equals("ATG") &&
                ((codons[codons.length-1].equals("TAA") ||
                codons[codons.length-1].equals("TAG") ||
                codons[codons.length-1].equals("TGA") ))) {
            protein = true;
        }

        if(protein && (percent[1] + percent[2]) > MINIMUM_C_G_PERCENT) {
            protein = true;
        } else {
            protein = false;
        }
        return protein;
    }

//    Isolates and creates an array of Strings representing all the individual codons

    public static String[] getCodons(String sequence){
        String[] codons = new String[sequence.length()/PER_CODON];
        String next = "";

        for(int i = 0; i < codons.length; i++) {
            next = sequence.substring((i * PER_CODON), (i * PER_CODON) + PER_CODON);
            codons[i] = next;
        }

        return codons;
    }

//    Takes the results of the calculations for one sequence and formats them into a
//    report that is printed to the output file.

    public static void outputResults(PrintStream output, String name, String sequence,
                     int[] nucleoCount, double [] mass, double totalMass,
                             String[] codons, boolean proteinFlag) {

        String protein = "";
        if(proteinFlag) {
            protein = "YES";
        } else {
            protein = "NO";
        }

        output.println("Region Name: " + name);
        output.println("Nucleotides: " + sequence);
        output.println("Nuc. Counts: " + Arrays.toString(nucleoCount));
        output.println("Total Mass%: " + Arrays.toString(mass) + " of " +
                Math.round(totalMass * 10)/10.0);
        output.println("Codons List: " + Arrays.toString(codons));
        output.println("Is Protein?: " + protein);
        output.println();
    }
}