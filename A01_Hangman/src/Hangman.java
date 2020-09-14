import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Hangman {

    private String word;
    private char[] hidden;
    private char[] wordArray;
    private int lives;
    private boolean playerWins;
    private Scanner input = new Scanner(System.in);
    private String guess;
    private ArrayList<Character> guesses = new ArrayList<>();

    public Hangman(){
        lives = 6;
        playerWins = false;
        word = randWord();
        wordArray = word.toCharArray();
        hidden = hideTheWord(word.length());
    }

    public static void main(String[] args) {
        Hangman game = new Hangman();
        game.start();
    }

    // Reads a .txt file containing possible words, puts them in an arrayList, and returns one at random
    private String randWord() {
        List<String> wordList = new ArrayList<>();

        try {
            InputStream input = getClass().getResourceAsStream("res/hangmanWords.txt");
            DataInputStream data_input = new DataInputStream(input);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(data_input));
            String str_line;

            while ((str_line = buffer.readLine()) != null) {
                str_line = str_line.trim();
                if ((str_line.length() != 0)) {
                    wordList.add(str_line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        int rand = new Random().nextInt(wordList.size());
        return wordList.get(rand);
    }

    // Fills the hidden array with _'s
    private char[] hideTheWord(int x){
        char[] c = new char[x];
        for (int i = 0; i < x; i++){
            c[i]= '_';
        }
        return c;
    }

    // Starts the game and handles all the game logic until the player either wins or loses
    private void start(){
        while (!playerWins && lives != 0){
            System.out.println("(Lives: " + lives + ")");
            printCharArray(hidden);
            System.out.println("\nGuesses: " + Arrays.toString(guesses.toArray()));
            if(!checkGuess(playerGuess())){
                lives -= 1;
            }
            if (checkVictory()){
                System.out.println("You win!");
                printCharArray(hidden);
                System.out.println();
                replay();
            }
        }
        System.out.println("You lose! The word was: " + word);
        replay();
    }

    // retrieve player guess and make sure the input is valid
    private char playerGuess(){
        System.out.println("\nEnter your guess: ");
        guess = input.next();
        if (guess.matches("[a-zA-z]*$") && guess.length() == 1){
            if(guesses.contains(guess.charAt(0))){
                System.out.println("You already guessed this character!");
                playerGuess();
            }
            return Character.toLowerCase(guess.charAt(0));
        } else {
            System.out.println("Invalid guess! Try again!");
            playerGuess();
        }
        return Character.toLowerCase(guess.charAt(0));
    }

    // Prints a char[]...
    private void printCharArray(char[] chArr){
        for (char c : chArr){
            System.out.print(c + " ");
        }
    }

    // Checks if the guessed character is in the word and updates the array accordingly
    private boolean checkGuess(char c){
        boolean contains = false;
        for (int i = 0; i < wordArray.length; i++) {
            if (wordArray[i] == c) {
                contains = true;
                hidden[i] = c;
            }
        }
        if (!contains){
            guesses.add(c);
        }
        return contains;
    }

    // Checks to see if the array contains any remaining hidden characters
    private boolean checkVictory(){
        boolean wins = true;
        for (int i = 0; i < hidden.length; i++){
            if (hidden[i] == '_'){
                wins = false;
            }
        }
        return wins;
    }

    // Asks the player if they want to play again
    private void replay(){
        System.out.println("Do you want to play again? (Y/N)");
        guess = input.next();
        if (Character.toLowerCase(guess.charAt(0)) == 'y'){
            reset();
            start();
        } else if(Character.toLowerCase(guess.charAt(0)) == 'n'){
            System.out.println("Thanks for playing!");
            System.exit(0);
        } else {
            System.out.println("Invalid input!");
            replay();
        }
    }

    // Resets all the class variables to prepare for a new game
    private void reset(){
        lives = 6;
        playerWins = false;
        word = randWord();
        wordArray = word.toCharArray();
        hidden = hideTheWord(word.length());
        guesses.clear();
    }
}