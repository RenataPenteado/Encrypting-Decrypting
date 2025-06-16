package ie.atu.sw;

import java.util.Arrays;

public class Cypher {

    private String key;

    /**
     * This method calls the validateKey to validate the key before setting it.
     * @param key - Value to be set as Key
     * @throws Exception - An exception will be thrown up if the key cannot be properly validated
     */
    public void setKey(String key) throws Exception{
        validateKey(key);
        this.key = key;

    }

    /**
     * Method used to validate and set key
     * Only validate if the key character is between A-Z and not space
     *
     * @param key - Value to be set as Key
     * @throws Exception - An exception will be thrown up if the key cannot be properly validated
     */
    private void validateKey(String key) throws Exception{
        char c = 0x20;
        if (key != null) {
            for (int i = 0; i < key.length(); i++) {
                if ((key.charAt(i) < 'A' || key.charAt(i) > 'Z') && key.charAt(i) != c) {
                    throw new Exception("[ERROR] Invalid character. Key length must be between A-Z");
                }
            }
        }
    }

    /**
     * encrypt - It is prepared to receive a previously read line from the Plain Text file and encrypt it.
     * @param plainText - plain text to be encrypt
     * @return the encrypted text
     */
    public String encrypt(String plainText) {
        // StringBuilder stores the encryption result of each processed character
        StringBuilder sbPairsResult = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            // getEncryptedCharacter will get the characters encrypted without using the key
            sbPairsResult.append(getEncryptedCharacter(plainText.charAt(i)));
        }
        // Create matrix size
        // See method calcMatrixDimensions to know how it is created
        char[][] matrix = calcMatrixDimensions(sbPairsResult.toString());
        int count = 0;
        // Set characters in specific rows and columns.
        for (int j = 0; j < matrix.length; j++) {
            for (int k = 0; k < matrix[j].length; k++) {
                matrix[j][k] = sbPairsResult.charAt(count++);
            }
        }
        // Calls made to the transposition method to sort the matrix columns by ordering the key alphabetically
        return transposition(matrix, true);
    }

    /**
     * decrypt - It is prepared to receive a previously read line from the Cyphered Text file and decrypt it.
     * @param cipherText - cyphered text to be decrypted
     * @return the decrypted text
     */
    public String decrypt(String cipherText) {
        // Create matrix size
        // See method calcMatrixDimensions to know how it is created
        char[][] matrix = calcMatrixDimensions(cipherText);
        int count = 0;

        for (int j = 0; j < matrix[0].length; j++) {
            for (int k = 0; k < matrix.length; k++) {
                matrix[k][j] = cipherText.charAt(count++);
            }
        }
        // Calls made to method transposition to undo key alphabetically ordered
        String trans = transposition(matrix, false);
        // New matrix created to recover result of undone matrix transposition
        char[] reorganized = new char[trans.length()];
        int countTrans = 0;
        for (int col = 0; col < matrix[0].length; col++) {
            count = col;
            for (int row = 0; row < matrix.length; row++) {
                reorganized[count] = trans.charAt(countTrans++);
                count += matrix[0].length;
            }
        }
        // StringBuilder retrieves the decrypted text
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (reorganized.length -1); i += 2) {
            // getDecryptedCharacter will get the character decrypted
            sb.append(getDecryptedCharacter(reorganized[i], reorganized[i + 1]));
        }

        return sb.toString();
    }
    // Perform a columnar transposition by sorting the key alphabetically and recreate a new matrix with this
    // or undone all the process and return its original matrix.

    /**
     * Perform a columnar transposition sorting the key alphabetically and
     * recreate a new array with that or undo the whole process and return your original array     *
     * @param matrix - Stores a matrix of chars to be transposed or have their transposition reversed
     * @param b - Flag indicating to transpose (true) or to reverse a previously made transposition (false)
     * @return - The String created by reading the matrix with transposition done or undone
     */
    private String transposition(char[][] matrix, boolean b) {
        // calls the method to sort the key
        String sortedKey = generateSortedKey();
        // Define a temporary key
        // It is used as a helper to sort the key alphabetically without breaking on duplicate characters
        String tempKey = b ? key : generateSortedKey();
        // StringBuilder builds the String to be returned
        StringBuilder sbTransposed = new StringBuilder();

        char c;
        int index;
        String before;
        String after;
        for (int i = 0; i < key.length(); i++) {
            c = b ? sortedKey.charAt(i) : key.charAt(i);
            index = tempKey.indexOf(c);
            before = tempKey.substring(0, index);
            after = tempKey.substring(index + 1);
            // Characters already processed will be transformed to '-'
            // It to avoid issues with duplicate characters
            tempKey = before.concat("-").concat(after);
            //Set the matrix
            for (int j = 0; j < matrix.length; j++) {
                sbTransposed.append(matrix[j][index]);
            }
        }

        return sbTransposed.toString();
    }
    // Sort key and return

    /**
     * generateSortedKey - Sort key and return it
     * @return - Key alphabetically ordered
     */
    private String generateSortedKey() {
        char[] keyArray = key.toCharArray();
        Arrays.sort(keyArray);
        return new String(keyArray);
    }

    /**
     * Encrypt the character and return the row and column where the character is with the respective values
     * @param plainChar - char to be encrypted
     * @return - the pair of chars taken from Polybius square that are linked with the char received as argument
     */
    private String getEncryptedCharacter(char plainChar) {
        for (int row = 1; row < ADFGVX_CYPHER.length; row++) {
            for (int col = 1; col < ADFGVX_CYPHER[row].length; col++) {
                if (ADFGVX_CYPHER[row][col] == plainChar) {
                    String result =
                            (String.valueOf(ADFGVX_CYPHER[row][0])
                                    .concat(String.valueOf(ADFGVX_CYPHER[0][col])));
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Create matrix size based in the text and key length
     * @param text - the text that needs to be stored in the matrix
     * @return - a matrix prepared to store the text
     */
    private char[][] calcMatrixDimensions(String text) {
        int cols = key.length();
        // Eliminate character at the end if the key length does not match
        int clip = ((text.length() * 2) % key.length() / 2);
        text = text.substring(0, text.length() - clip);

        return new char[text.length() / key.length()][cols];
    }


    /**
     * Decrypt a pair of characters
     * @param cypher1 - first char of pair of characters to be deciphered
     * @param cypher2 - second char of pair of characters to be deciphered
     * @return - the value of the intersection inside the Polybius square by evaluating its rows and columns
     */
    private char getDecryptedCharacter(char cypher1, char cypher2) {
        int rowIndexFound = 0;
        int colIndexFound = 0;

        // For loop return the row where the first char is found
        for (int row = 1; row < ADFGVX_CYPHER.length; row++) {
            if (ADFGVX_CYPHER[row][0] == cypher1) {
                rowIndexFound = row;
                break;
            }
        }
        // For loop return the col where the second char is found
        for (int col = 1; col < ADFGVX_CYPHER.length; col++) {
            if (ADFGVX_CYPHER[0][col] == cypher2) {
                colIndexFound = col;
                break;
            }
        }
        return ADFGVX_CYPHER[rowIndexFound][colIndexFound];
    }

    //Polybius Square
    private static final char[][] ADFGVX_CYPHER = {
            {' ', 'A', 'D', 'F', 'G', 'V', 'X'},
            {'A', 'P', 'H', '0', 'Q', 'G', '6'},
            {'D', '4', 'M', 'E', 'A', '1', 'Y'},
            {'F', 'L', '2', 'N', 'O', 'F', 'D'},
            {'G', 'X', 'K', 'R', '3', 'C', 'V'},
            {'V', 'S', '5', 'Z', 'W', '7', 'B'},
            {'X', 'J', '9', 'U', 'T', 'I', '8'}
    };

}