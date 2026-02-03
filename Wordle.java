import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Wordle {

    // constants to allow colored text and backgrounds
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public static void main(String[] args) throws FileNotFoundException {
        // print welcome message

        ArrayList<String> dictionaryWords = new ArrayList<>();

        FileReader fileReader= null;
        Scanner filescnr = null;

        try{
            fileReader = new FileReader(args[0]);
            filescnr = new Scanner(fileReader);
        }
        catch(FileNotFoundException e){
            System.out.println("File not found!"); //shows invalid file or file not found
        }

        while(filescnr.hasNextLine()){
            String word = filescnr.nextLine(); // reads each word in file
            if (word == null){
                break;
            }
            if (word.length()==5){
                dictionaryWords.add(word);
            }
        }
        filescnr.close();

        System.out.println("Welcome to Wordle(TM)");


        Random rand = new Random();
        int randIndex = rand.nextInt(dictionaryWords.size());
        String secretWord = dictionaryWords.get(randIndex);

        //System.out.print(secretWord); used to test

        // Hello World for colored text and background
        System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Hello ");
        System.out.print(ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "World");
        System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "!");
        System.out.println(ANSI_RESET);

        int guesses = 5;

        for (int i = 1; i<=guesses; i++) {
            System.out.println("Enter word (" + i + "): ");
            //get user guesses
            Scanner scnr = new Scanner(System.in);
            String attempt = scnr.next();

            if (isSorted(dictionaryWords, attempt) != -1) {
                printWordle(attempt, (matchWordle(secretWord, attempt)));
                if (foundMatch(matchWordle(secretWord, attempt))) {
                    System.out.print("Yes! You win!");
                    System.exit(0);
                }
            }

        }
        System.out.println(secretWord);
    }

    public static int isSorted(ArrayList<String> dictionaryWords, String attempt) {

        for (int i = 0; i < dictionaryWords.size() - 1; i++) {
            String word1 = dictionaryWords.get(i);
            String word2 = dictionaryWords.get(i + 1);
            if (word1.compareTo(word2) > 0) {
                //conducts a linear search if the list is not sorted
                return linearSearch(dictionaryWords, dictionaryWords.size(), 0, attempt);
            } else if (!(word1.compareTo(word2) > 0)){
                //conducts a binary seach if the list is sorted
                return binarySearch(dictionaryWords, dictionaryWords.size(), 0, attempt);
            }
        }
        return -1;
    }


    public static int[] matchWordle(String target, String attempt){
        //check to make sure target and string and the same length
        if (target.length() != attempt.length()) {
            throw new IllegalArgumentException("Guess must be 5 letters!");
        }

        int[] match = new int[target.length()];

        for(int i=0; i< target.length();i++){
            //iterates through each character in target
            if (target.charAt(i)==attempt.charAt(i)){
                match[i]=1;
                //characters match
            }
            else if ((target).contains(attempt.substring(i,i+1))){
                match[i]=2;
            }
            else{
                match[i]=0;
                //characters do not match
            }
        }
        return match;
    }

    public static void printWordle(String attempt, int[] match){
        for(int i=0; i<attempt.length();i++) {

            if (match[i] == 1) {
                System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + attempt.charAt(i));
            }
            else if (match[i]==2){
                System.out.print(ANSI_YELLOW_BACKGROUND + ANSI_BLACK + attempt.charAt(i));
            }
            else{
                System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + attempt.charAt(i));
            }
            System.out.print(ANSI_RESET);
        }
        System.out.println();
    }

    public static boolean foundMatch(int[] match){
        for (int i=0; i<match.length;i++) {
            if (match[i] != 1) {
                return false;
                //if any character does not match returns false
            }
        }
        //all character match returns true
        return true;
    }

    public static int binarySearch(ArrayList<String> words, int highVal, int lowVal, String target) {

        int midVal = (lowVal + highVal) / 2;

        if (lowVal>highVal){
            return -1;
        }
        if (words.get(midVal).equals(target)){
            return midVal;
            //target found at midVal
        }
        else if(target.compareToIgnoreCase(words.get(midVal))<0){
            return binarySearch(words, midVal-1,lowVal, target);
            //target must be on left half of list
        }
        else{
            return binarySearch(words,highVal,midVal+1,target);
            //target must be on right half of list
        }
    }

    public static int linearSearch(ArrayList<String> words, int highVal, int lowVal, String target){
        if (lowVal>highVal){
            return -1;
            //base care returns -1 if target is not found
        }
        if (words.get(lowVal).equals(target)){
            return lowVal;
            //target found in the lowVal index
        }
        else if ((words.get(lowVal).equals(target))==false) {
            return highVal;
            //target found in highVal index
        }
        return linearSearch(words, lowVal + 1, highVal-1, target);
    }

}