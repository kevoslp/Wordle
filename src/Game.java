import java.io.*;
import java.util.Scanner;

// written by Kevin Tran (tran1199)
public class Game {
    boolean gameOver = false;
    Wordle wordle;
    int i = 0, j = 0;
    String[] guesses = new String[6];

    public Game(Wordle wordle) {
        this.wordle = wordle;
    }

    /** Method to check if user guess is correct or not
     * also prints out the guess with its correct colors
     * and writes the guess to the file 'userGuesses.txt' */
    public void checkAns(String str) {
        str = str.toUpperCase();
        // Makes sure that the string is at least 5 long and alphabetic
        if (str.length() == 5 && str.matches("[a-zA-Z]+")) {

            // Correct guess
            if (str.equals(wordle.getAnswer())) {
                guesses[i++] = str;
                wordle.regurgitateGuess(guesses);
                System.out.println("\n!!!!!!!!");
                System.out.println("Winner I guess");
                writeGuesses(guesses);
                gameOver = true;
            } else {
                // incorrect guess
                wordle.setAttemptsLeft(wordle.attemptsLeft - 1);
                guesses[i++] = str;
                wordle.regurgitateGuess(guesses);
                System.out.print("\nAttempts Left: " + wordle.getAttemptsLeft() + "\n");
            }

        // Wrong length and/or not alphabetic
        } else {
            System.out.println("\nWord must be 5 letters long and also be alphabetic");
            System.out.println("Attempts Left: " + wordle.getAttemptsLeft());
        }

        // Ran out of attempts
        if (wordle.getAttemptsLeft() == 0 && this.gameOver == false) {
            System.out.println("Out of Attempts! Loser");
            writeGuesses(guesses);
            gameOver = true;
        }

    }

    /** Method to write all of user's guesses to the file */
    public void writeGuesses(String[] guesses) {
        int length = guesses.length;
        try(FileWriter fw = new FileWriter("userGuesses.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (int i = 0; i < length; i++) {
                out.println(guesses[i]);
            }

        } catch (IOException e) {
            System.out.println("File wasn't found");
        }
    }

    public boolean isGameOver() { return gameOver; }

    /** Sets wordle and game objects and play the game until it's over
     * Answer is printed out by default */
    public static void main(String[] args) throws FileNotFoundException {
        Wordle wordle = new Wordle();
        Game game = new Game(wordle);
        System.out.println("The answer: " + wordle.getAnswer());
        Scanner scan = new Scanner(System.in);
        while (!game.isGameOver()) {
            System.out.println("Enter your guess: ");
            String input = scan.nextLine();
            game.checkAns(input);
        }

    }
}

