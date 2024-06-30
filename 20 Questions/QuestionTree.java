import java.io.*;
import java.util.*;

public class QuestionTree {

//    This program holds the information and behaviors needed to play 20 questions with
//    a set of questions and answers.

    private QuestionNode root;
    private int gamesPlayed;
    private int gamesWon;
    private final UserInterface ui;

//    Initializes a new QuestionTree associated with the given UserInterface.

    public QuestionTree (UserInterface ui) {
        if(ui == null) {
            throw new IllegalArgumentException();
        }

        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.ui = ui;
        this.root = new QuestionNode("computer");

    }

//    Executes one complete game of 20 questions.

    public void play() {
        root = play(root);
        gamesPlayed++;
    }

//    Supplies the correct string to the user interface according to the given QuestionNode,
//    provided it is not null.

    private QuestionNode play(QuestionNode current) {
        if (current == null) {
            throw new IllegalArgumentException();
        }

        if (!isAnswer(current)) {
            ui.print(current.nodeData);
            if (ui.nextBoolean()) {
                current.yes = play(current.yes);
            } else {
                current.no = play(current.no);
            }
        } else {
            ui.print("Would your object happen to be " + current.nodeData + "?");
            if (ui.nextBoolean()) {
                ui.println("I win!");
                gamesWon++;
            } else {
                current = loserCityUSAPopulationThisGuy(current);
            }
        }

        return current;
    }

//    Outputs the current state of the QuestionTree to the given PrintStream object.

    public void save (PrintStream output) {
        if(output == null) {
            throw new IllegalArgumentException();
        }
        save(output, root);
    }

//    Formats the given QuestionTree file correctly via preorder traversal and saves it
//    to the given PrintStream object.

    private void save(PrintStream output, QuestionNode current) {
        if(current == null) {
            throw new IllegalArgumentException();
        }

        if(!isAnswer(current)) {
            output.println("Q: " + current.nodeData);
            save(output, current.yes);
            save(output, current.no);
        } else {
            output.println("A: " + current.nodeData);
        }
    }

//    Assigns the root of the Question tree given a non-null Scanner object containing
//    a list of questions and answers.

    public void load(Scanner input) {
        if(input == null) {
            throw new IllegalArgumentException();
        }
        root = buildTree(input);
    }

//    Returns the root of a new QuestionTree containing the Questions and Answers from
//    the given Scanner object.

    private QuestionNode buildTree(Scanner QAList) {
        QuestionNode currentNode = null;
        if(QAList.hasNextLine()) {
            String[] currentData = QAList.nextLine().split("[:]");
            if(currentData[0].equals("Q")) {
                currentNode = new QuestionNode((buildTree(QAList)), buildTree(QAList), currentData[1].trim());
            } else {
                currentNode = new QuestionNode(currentData[1].trim());
            }
        }
        return currentNode;
    }

//    Returns the number of games played.

    public int totalGames() {
        return gamesPlayed;
    }

//    Returns the number of games won.

    public int gamesWon() {
        return gamesWon;
    }

//    Returns whether the given node represents an answer.

    private boolean isAnswer(QuestionNode node) {
        if(node == null) {
            throw new IllegalArgumentException();
        }
        return (node.no == null && node.yes == null);
    }

//    Given the QuestionNode holding an incorrect answer, creates a new QuestionNode
//    holding the correct answer, and places it after a new clarifying question which
//    is returned to the client.

    private QuestionNode loserCityUSAPopulationThisGuy(QuestionNode wrongObject) {
        ui.print("I lose. What is your object? ");
        QuestionNode correctObject = new QuestionNode(ui.nextLine().trim());
        ui.print("Type a yes/no question to distinguish your item from " + wrongObject.nodeData + ": ");
        String question = ui.nextLine().trim();
        ui.print("And what is the answer for your object? ");
        if(ui.nextBoolean()) {
            return new QuestionNode(correctObject, wrongObject, question);
        }
        return new QuestionNode(wrongObject, correctObject, question);
    }
}
