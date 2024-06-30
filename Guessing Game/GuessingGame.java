import java.util.*;
public class GuessingGame {

//    This program produces a simple console game in which the user attempts to guess a random
//    number between 1 and a specified limit. If the guess is incorrect, the user will be notified
//    if the number is higher or lower than their attempt. When the correct number is chosen, the
//    game will say how many guesses it took and prompt the user if they wish to play again. When
//    the user is finished, statistics from the session will be displayed, including total games,
//    total guesses, guesses per game and the best game (fewest guesses).

    public static final int MAX_NUM = 100;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Random random = new Random();

        int totalGames = 0;
        int totalGuess = 0;
        int bestGame = 9999;

        intro();
        boolean exec = true;

        while(exec) {
            System.out.println();
            int guesses = newGame(random, input);

            if(guesses < bestGame) {
                bestGame = guesses;
            }

            totalGames++;
            totalGuess += guesses;
            exec = playAgain(input);
        }
        System.out.println();
        showResults(totalGames,totalGuess, bestGame);
    }

//    Introduces the game to the user.

    public static void intro() {
        System.out.println(
                "This program allows you to play a guessing game.\n" +
                        "I will think of a number between 1 and\n" +
                        MAX_NUM + " and will allow you to guess until\n" +
                        "you get it.  For each guess, I will tell you\n" +
                        "whether the right answer is higher or lower\n" +
                        "than your guess."
        );
    }

//    Handles the logic of each individual game played by the user. Using the given Random and Scanner
//    objects, it generates a random number to be guessed and prompts the user to input guesses until
//    the correct number is guessed. It will return the number of guesses that game took to complete.

    public static int newGame(Random random, Scanner input) {

        System.out.println("I'm thinking of a number between 1 and " + MAX_NUM + "...");
        int answer = random.nextInt(MAX_NUM) + 1;
        int guessNum = 0;
        int guess = 0;

        while(guess != answer) {
            System.out.print("Your guess? ");
            guess = input.nextInt();

            if(guess == answer) {
                if(guessNum == 0) {
                    System.out.println("You got it right in 1 guess");
                    return 1;
                }
                guessNum++;
                System.out.println("You got it right in " + guessNum + " guesses");

            } else if (guess < answer) {
                System.out.println("It's higher.");
                guessNum++;

            } else if (guess > answer) {
                System.out.println("It's lower.");
                guessNum++;
            }
        }
        return guessNum;
    }

//    Prompts user if they wish to play again and, once a valid answer is received, returns whether it
//    starts with Y/y or N/n.

    public static boolean playAgain(Scanner input) {
        boolean yesOrNo = false;
        String ans = "";

        while(!yesOrNo) {
            System.out.print("Do you want to play again? ");
            ans = input.next().toUpperCase();

            if (ans.charAt(0) == 'N' || ans.charAt(0) == 'Y') {
                yesOrNo = true;
            }
        }
        return ans.charAt(0)=='Y';
    }

//    Displays the results of the game session to the user, and generates the average guesses per game.

    public static void showResults(int totalGames, int totalGuess, int bestGame) {
        System.out.println("Overall results:");
        System.out.println("    total games   = " + totalGames);
        System.out.println("    total guesses = " + totalGuess);
        System.out.printf(("    guesses/game  = %.1f\n"), ((double) totalGuess / totalGames));
        System.out.println("    best game     = " + bestGame);
    }
}