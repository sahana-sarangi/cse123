import java.util.*;
import java.io.*;

//This class extends Substitution and implements the CaesarKey cipher using a key that encrypts
//and decrypts strings. The key is used to map all the letters in the alphabet to their encrypted
//counterparts. To encrypt and decrypt, each letter in an input string is substituted with its 
//encrypted/decrypted version.
public class CaesarKey extends Substitution {

    //This constructor creates a new CaesarKey cipher using a given key.
    //Exceptions: if the key is empty/null, has a character whose integer value is less than 
    //MIN_CHAR or greater than MAX_CHAR, or contains duplicate characters, an 
    //IllegalArgumentException is thrown.
    //Parameters:
    //  - key: the given string to be included in the shifter that is necessary in the
    //         substitution process of encryption/decryption (non-null)
    public CaesarKey(String key) {
        super(generateShifter(key));

    }

    //Behavior: This method generates a new shifter string where the first characters in the 
    //shifter are the characters in the key, and the characters following are the rest of the 
    //characters in the alphabet (in alphabetic order) that are not present in the key.
    //Exception: if the key is empty/null, an IllegalArgumentException is thrown.
    //Return: the new shifter string
    //Parameters: 
    //  - key: the key to create a shifter from (non-null)
    private static String generateShifter(String key) {
        if (key == "") {
            throw new IllegalArgumentException("Key must be non-null.");
        }
        
        int currCharacter = Cipher.MIN_CHAR;
        while (currCharacter <= Cipher.MAX_CHAR) {
            if (key.indexOf((char) currCharacter) == -1) {
                key += (char) currCharacter;
            }
            currCharacter++;
        }
        return key;
    }
}
