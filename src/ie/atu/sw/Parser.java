package ie.atu.sw;

import ie.atu.sw.exceptions.InputFolderNotFoundException;
import ie.atu.sw.exceptions.MissingInputCypheredException;
import ie.atu.sw.exceptions.MissingInputDecipheredException;
import ie.atu.sw.exceptions.MissingInputPlainTextException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Parser {
    //Define variable to retrieve its values.
    private String plainTextDir;
    private String cypheredTextDir;
    private String deCypheredTextDir;

    // Get input file directory
    public String getPlainTextDir() {
        return plainTextDir;
    }
    // Set input file directory
    public void setPlainTextDir(String plainTextDir){ this.plainTextDir = plainTextDir; }
    // Get output cyphered file directory
    public String getCypheredTextDir() {
        return cypheredTextDir;
    }
    // Set output cyphered file directory
    public void setCypheredTextDir(String cypheredTextDir) {
        this.cypheredTextDir = cypheredTextDir;
    }

    // Get output deciphered file directory
    public String getDeCypheredTextDir() {
        return deCypheredTextDir;
    }
    // Set output deciphered file directory
    public void setDeCypheredTextDir(String deCypheredTextDir) {
        this.deCypheredTextDir = deCypheredTextDir;
    }

    /**
     * Load files to be encrypted or decrypted and throws custom exceptions to the caller
     * @param cypher - object used to perform encryption/decryption
     * @param encrypt - flag the process to encryption (true) or decryption (false)
     * @throws MissingInputCypheredException
     * @throws MissingInputDecipheredException
     * @throws MissingInputPlainTextException
     * @throws InputFolderNotFoundException
     */
    public void loadFiles(Cypher cypher, boolean encrypt) throws MissingInputCypheredException, MissingInputDecipheredException, MissingInputPlainTextException, InputFolderNotFoundException {

//      Helping the development:
//      These values can be entered on program menu options 1,2 and 3
//        setPlainTextDir("C:\\Users\\renata\\Desktop\\ATU\\First semester\\OOPassigment\\files\\plainText");
//        setCypheredTextDir("C:\\Users\\renata\\Desktop\\ATU\\First semester\\OOPassigment\\files\\encrypted");
//        setDeCypheredTextDir("C:\\Users\\renata\\Desktop\\ATU\\First semester\\OOPassigment\\files\\decrypted");

        String inputFolderPath = encrypt ? getPlainTextDir() : getCypheredTextDir();
        String outputFolderPath = encrypt ? getCypheredTextDir() : getDeCypheredTextDir();

        File outputFolder;
        // Trows exception if the output file directory is not set.
        if (encrypt && outputFolderPath == null) {
            throw new MissingInputCypheredException();
        }
        if (!encrypt && outputFolderPath == null) {
            throw new MissingInputDecipheredException();
        }
        // Check if output folder exist
        // Otherwise, create it
        outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        // Trows exception if the input file directory is not set.
        if (encrypt && inputFolderPath == null) {
            throw new MissingInputPlainTextException();
        }
        if (!encrypt && inputFolderPath == null) {
            throw new MissingInputCypheredException();
        }

        File inputFolder =new File(inputFolderPath);

        // Throws exception if the input file directory does not exist
        if (inputFolder.exists() == false) {
            throw new InputFolderNotFoundException();
        }

        // Get list of files
        File[] listOfFiles;
        listOfFiles = inputFolder.listFiles();

        int size = listOfFiles.length;
        int i = 0;
        // Playing with Console colors
        System.out.print(ConsoleColour.YELLOW);
        printProgress(i, size);
        for (File file : listOfFiles) {
            if (file.isFile()) {
                // Read files
                try (BufferedReader br = new BufferedReader(new FileReader(file));
                     FileWriter fWriter = new FileWriter(outputFolderPath + "/" + file.getName())) {
                    // Process each line before call encrypt or decrypt
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.length() > 0) {
                            String processedLine = line.trim().replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
                            // Evaluate flag for encrypt or decrypt
                            String result = encrypt ? cypher.encrypt(processedLine) : cypher.decrypt(processedLine);
                            // Write files
                            fWriter.write(result + "\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Update progress bar
            printProgress(++i, size);
        }

    }


    /**
     *  Progress bar used to display the progress of encrypting or decrypting files
     * @param index - Value used to advance the progress bar
     * @param total - Value used to calc the progress bar size
     */
    private void printProgress(int index, int total) {
        if (index > total) return;    //Out of range
        int size = 50;                //Must be less than console width
        char done = '=';			//Change to whatever you like.
        char todo = ' ';			//Change to whatever you like.

        //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        /*
         * A StringBuilder should be used for string concatenation inside a
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        /*
         * The line feed escape character "\r" returns the cursor to the
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");

        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }
}
