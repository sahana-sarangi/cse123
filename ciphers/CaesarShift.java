import java.util.*;
import java.io.*;

//This class extends Substitution and implements the CaesarShift cipher that encrypts and decrypts
//strings. It encrypts/decrypts by shifting each letter of the alphabet a certain number of times,
//using the shifted alphabet as a shifter string that is then used to substitute characters to
//encrypt/decrypt a string.
public class CaesarShift extends Substitution {
    
    //This constructor creates a new CaesarShift cipher with a certain integer to shift
    //characters by. It creates a shifter string that aids in encryption/decryption via
    //substitution. The shifter is created by shifting all the characters in the alphabet
    //to the left 'shift' number of times. Characters at the beginning of the alphabet are 
    //wrapped around to the end as characters continue to be shifted.
    //Exception: if the given shift is less than or equal to 0, an IllegalArgumentException is
    //thrown.
    //Parameters:
    //  - shift: the number of characters that the alphabet is shifted by to encrypt/decrypt
    //           strings
    public CaesarShift(int shift) {
        if (shift <= 0) {
            throw new IllegalArgumentException("Shift must be positive.");
        }

        String shifter = "";
        for (int i = 0; i < Cipher.TOTAL_CHARS; i++) {
            int shiftedCharIdx = (i + shift) % Cipher.TOTAL_CHARS;
            shifter += (char) (Cipher.MIN_CHAR + shiftedCharIdx);
        }
        
        super.setShifter(shifter);
    }
}
