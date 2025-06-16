package ie.atu.sw;

import ie.atu.sw.exceptions.InputFolderNotFoundException;
import ie.atu.sw.exceptions.MissingInputCypheredException;
import ie.atu.sw.exceptions.MissingInputDecipheredException;
import ie.atu.sw.exceptions.MissingInputPlainTextException;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class Menu {

    private Scanner s;
    private boolean keepRunning = true;
    String outputFileCypheredDirectory;
    String outputFileDecipheredDirectory;
    String inputFileDirectory;
    String key;

    Cypher cypher = new Cypher();
    Parser parser = new Parser();

    public Menu(){
        s = new Scanner(System.in);
    }

    public void showOptions() {
        // Menu for displaying application options
        System.out.println(ConsoleColour.CYAN_BRIGHT);
        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
        System.out.println("*                                                          *");
        System.out.println("*                   ADFGVX File Encryption                 *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Specify Input Plain Text File Directory");
        System.out.println("(2) Specify Input/Output Encrypted File Directory");
        System.out.println("(3) Specify Output Decrypted File Directory");
        System.out.println("(4) Set Key");
        System.out.println("(5) Encrypt");
        System.out.println("(6) Decrypt");
        System.out.println("(7) Quit");

        System.out.print(ConsoleColour.GREEN_BRIGHT);
        System.out.print("Select Option [1-6]>");
        System.out.println();
    }


    /**
     * Start the application showing the menu options
     */
    public void start() {

        while (keepRunning) {
            showOptions();

            int choice = Integer.parseInt(s.next());

            // The line below is a bug resolver:
            //      Eliminating '\n' left in Scanner buffers after the s.next() call.
            //      Reference: https://stackoverflow.com/questions/23450524/java-scanner-doesnt-wait-for-user-input
            s.nextLine();

            // The switch statement is evaluated as soon as a menu option is chosen
            // It redirects the flow to the corresponding method
            switch (choice) {
                case 1 -> inputFileDirectory();
                case 2 -> outputFileCypheredDirectory();
                case 3 -> outputFileDecipheredDirectory();
                case 4 -> setKey();
                case 5 -> encryptDecryptFiles(true);
                case 6 -> encryptDecryptFiles(false);
                case 7 -> keepRunning = false;
                default -> out.println("[Error] Invalid Selection");
            }
        }
    }

    /**
     * Method used to enter the input plain text file directory
     */
    public void inputFileDirectory() {

        out.println("Enter input file directory for plain text files");
        inputFileDirectory = s.nextLine();
        //set the file directory containing plain text files to be cyphered
        parser.setPlainTextDir(inputFileDirectory);
    }

    /**
     * Method used to enter the input/output cyphered file directory
     */
    public void outputFileCypheredDirectory() {
        out.println("Enter input/output file directory for encrypted files");
        outputFileCypheredDirectory = s.nextLine();
        //set the file directory to save encrypted files.
        parser.setCypheredTextDir(outputFileCypheredDirectory);
    }

    /**
     * Method used to enter the output deciphered file directory
     */
    public void outputFileDecipheredDirectory() {

        out.println("Enter output file directory for deciphered");
        outputFileDecipheredDirectory = s.nextLine();
        //set the file directory to save deciphered files.
        parser.setDeCypheredTextDir(outputFileDecipheredDirectory);
    }

    /**
     * Method used to set key.
     * The key is used during text encryption and decryption.
     * To be more specific, it is used in transposition.
    */
    public void setKey() {

        out.println("Enter Key");
        key = s.next();
        //Try Catch is important while setting key, it handle any exception in case the key is not validated.
        try {
            cypher.setKey(key.trim().toUpperCase());
        } catch (Exception e) {
            out.println(e.getMessage());
            try {
                System.in.read();
            } catch (IOException ioException) {
                // Printing the Exception Stack Trace for information
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Method used to start the process to encrypt and decrypt text. It calls parse to load files and handle any exception that might occur.
     * @param b - It is used to flag the method for encryption (true) or decryption (false)
     */
    public void encryptDecryptFiles(boolean b) {
        // Try to load the files
        try {
            if (b) {
                parser.loadFiles(cypher, true);
            } else {
                parser.loadFiles(cypher, false);
            }
            // Handle the exception and print the corresponding message
        } catch (MissingInputPlainTextException | MissingInputCypheredException | MissingInputDecipheredException | InputFolderNotFoundException e) {
            System.out.print(ConsoleColour.RED_BRIGHT);
            if (e instanceof MissingInputPlainTextException) {
                out.println("Please enter the input file directory.");
            } else if (e instanceof MissingInputCypheredException) {
                out.println("Please enter the output file directory for cyphered process.");
            } else if (e instanceof MissingInputDecipheredException) {
                out.println("Please enter the output file directory for deciphered process.");
            } else if (e instanceof InputFolderNotFoundException) {
                out.println("The input folder containing plain text files to be cyphered was not found.");
            }
            System.out.print(ConsoleColour.GREEN_BRIGHT);
            out.println("Press Enter to load the menu again.");
            // Wait for Enter key pressed to load the menu again
            // In case an exception is throws up an IOException
            try {
                System.in.read();
            } catch (IOException ioException) {
                // Printing the Exception Stack Trace for information
                ioException.printStackTrace();
            }
        }

    }

}

