import java.util.*;
import java.io.*;

//This class extends Cipher and implements the Substitution cipher that encrypts and decrypts
//strings. To encrypt and decrypt, the cipher maps each letter of the alphabet to another letter.
//It then substitutes each letter in a string with the encrypted/decrypted character it maps to.
public class Substitution extends Cipher {

    private String shifter;
    
    //This constructor creates a new substitution cipher without a specific pattern to use
    //to encode or decode a string.
    public Substitution() {
    }


    //This constructor creates a substitution cipher with a shifter used to encode or decode a
    //string. Each character in the shifter matches to a letter in the alphabet
    //Exception: if the shifter contains a character whose integer value is less than MIN_CHAR 
    //or greater than MAX_CHAR, is not as long as TOTAL_CHARS, is null, or contains duplicate
    //characters, an IllegalArgumentException is thrown. 
    //Parameters:
    //  - shifter: the non-null string that is used to encode or decode the input string
    public Substitution(String shifter) {
        setShifter(shifter);
    }

    //Behavior: this method sets the shifter string that the cipher uses
    //Exceptions: if a character in the shifter string has an integer value greater than MAX_CHAR
    //or less than MIN_CHAR, the length of the shifter doesn't match TOTAL_CHARS, is null, or the
    //shifter contains duplicate characters, an IllegalArgumentException is thrown.
    //Parameters:
    //  - shifter: the non-null string that is used to encode or decode the input string
    public void setShifter(String shifter) {
        if (shifter.length() != Cipher.TOTAL_CHARS) {
            throw new IllegalArgumentException("Invalid shifter--shifter length does not match" +
                    " TOTAL_CHARS.");
        }
        Set<Character> shifterCharacters = new HashSet<>();
        for (int i = 0; i < shifter.length(); i++) {
            if ((int) shifter.charAt(i) < Cipher.MIN_CHAR || (int) shifter.charAt(i)
                    > Cipher.MAX_CHAR) {
                throw new IllegalArgumentException("Invalid shifter--characters in shifter are" +
                        " outside of encodable range.");
            }
            shifterCharacters.add(shifter.charAt(i));
        }

        if (shifterCharacters.size() != Cipher.TOTAL_CHARS) {
            throw new IllegalArgumentException("Invalid shifter--shifter contains duplicate" +
                    " characters.");
        }
        this.shifter = shifter;
    }

    //Behavior: this method encrypts an input string using the given shifter
    //Exception: if the cipher's shifter is null, an IllegalStateException is thrown
    //Return: a string containing the encrypted input
    //Parameters:
    //  - input: the string to encrypt (non-null and containing only characters within 
    //           the encodable range)
    public String encrypt(String input) {
        if (shifter == null) {
            throw new IllegalStateException("Shifter is null.");
        }

        String encryptedInput = "";
        
        Map<Character, Character> encryptionMap = new HashMap<>();

        int currCharacter = Cipher.MIN_CHAR;
        for (int i = 0; i < shifter.length(); i++) {
            encryptionMap.put((char) currCharacter, shifter.charAt(i));
            currCharacter++;
        }

        for (int i = 0; i < input.length(); i++) {
            encryptedInput += encryptionMap.get(input.charAt(i));
        }
        return encryptedInput;
    }

    //Behavior: this method decrypts an input string using the given shifter
    //Exception: if the cipher's shifter is null, an IllegalStateException is thrown
    //Return: a string containing the decrypted input
    //Parameters:
    //  - input: the string to decrypt (non-null and containing only characters within the
    //encodable range)
    public String decrypt(String input) {
        if (shifter == null) {
            throw new IllegalStateException("Shifter is null.");
        }

        String decryptedInput = "";
        
        Map<Character, Character> decryptionMap = new HashMap<>();

        int currCharacter = Cipher.MIN_CHAR;
        for (int i = 0; i < shifter.length(); i++) {
            decryptionMap.put(shifter.charAt(i), (char) currCharacter);
            currCharacter++;
        }

        for (int i = 0; i < input.length(); i++) {
            decryptedInput += decryptionMap.get(input.charAt(i));
        }
        return decryptedInput;
    } 
}
