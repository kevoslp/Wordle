import java.io.*;
import java.util.Random;
import java.util.Scanner;

// written by Kevin Tran (tran1199)
public class Wordle {

    public int attemptsLeft = 6;
    public String answer;
    public final String resetANSI = "\u001B[0m";
    public final String greenANSI = "\u001B[42m";
    public final String redANSI = "\u001B[41m";
    public final String blackANSI = "\u001B[30m";
    public final String grayANSI = "\u001B[100m";
    String[] words = new String[736];

    /** Constructor that gets the words from the text file
    * and picks a random unique word for the answer */
    public Wordle () throws FileNotFoundException {
        String[] words = getWords();
        String tempWord = words[new Random().nextInt(words.length)].toUpperCase();
        while (checkUsed(tempWord)) {
            String newWord = words[new Random().nextInt(words.length)].toUpperCase();
            tempWord = newWord;
        }
        this.answer = tempWord;
    }

    /** Writes unique words to 'usedWords.txt'
    * and returns a boolean */
    public boolean checkUsed(String word) throws FileNotFoundException {
        File f = new File("usedWords.txt");
        Scanner s = new Scanner(f);
        int freq = 0;

        while (s.hasNextLine()) { // if word is in usedWords, increment frequency
            String line = s.nextLine();
            if (word.equals(line)) {
                freq++;
            }
        }
        s.close();

        if (freq > 0) {
            return true;
        } else {
            // if word is unique, write to file
            try(FileWriter fw = new FileWriter("usedWords.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(word);

            } catch (IOException e) {
                System.out.println("File wasn't found");
            }
            return false;
        }
    }

    /** Gets the words from the file and puts it into an array */
    public String[] getWords() throws FileNotFoundException {
        File f = new File("words.txt");
        Scanner s = new Scanner(f);
        int i = 0;

        while (s.hasNextLine()) {
            String line = s.nextLine();
            words[i] = line;
            i++;
        }
        return words;
    }

    /** Helper method made to help me */
    public String replaceChar(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);
    }

    /** Method to print out the user's guesses along with the
     * correct ANSI colors, according to the rules of Wordle
     *
     * NOTE: I used red instead of yellow because I'm colorblind.
     * I couldn't tell the difference between the green and yellow */
    public void regurgitateGuess(String[] guesses) {
        for (int i = 0; i < 6; i++) {

            // Iterates through all current guesses
            String input = guesses[i];
            String ans = getAnswer();

            // If end of guesses, break the loop
            if (input == null) break;

            System.out.print("->" + (i + 1) + ") ");

            // If the guess was the answer, print all green
            if (input.equals(answer)) {
                System.out.print(greenANSI + input + resetANSI);
                break;
            } else {

                // Created userWord so I can alter the word to make duplicate letters easier
                // colorPosition makes it easy to print out the correct ANSI values
                String userWord = input;
                String[] colorPosition = new String[5];

                // Check the word for green (correct) letters
                // If greens are found: replace the letter in both ans and userWord to '0'
                // Also saves colorPosition for green
                for (int j = 0; j < 5; j++) {
                    if (input.charAt(j) == answer.charAt(j)) {
                        ans = replaceChar(ans, '0', j);
                        userWord = replaceChar(userWord, '0', j);
                        colorPosition[j] = greenANSI;
                    }
                }

                // Checks the word for yellow now
                for (int j = 0; j < 5; j++) {
                    if (userWord.charAt(j) == '0') {

                    // If a yellow exists:
                    // Change characters to '0'. Save location for red
                    } else if (ans.indexOf(userWord.charAt(j)) != -1) {
                        ans = replaceChar(ans,'0', ans.indexOf(userWord.charAt(j)));
                        userWord = replaceChar(userWord, '0', j);
                        colorPosition[j] = redANSI;
                    } else {
                        // Otherwise, grayANSI
                        colorPosition[j] = grayANSI;
                    }
                }

                // Print the guess with colors
                for (int j = 0; j < 5; j++) {
                    System.out.print(colorPosition[j] + input.charAt(j) + resetANSI);
                }
                System.out.println();
            }
        }
    }

    /** Vital getters and setters */

    public String getAnswer() { return answer; }
    public int getAttemptsLeft() { return attemptsLeft; }
    public void setAttemptsLeft(int attemptsLeft) { this.attemptsLeft = attemptsLeft; }

}
