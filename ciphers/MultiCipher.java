import java.util.*;
import java.io.*;

//This class extends Cipher and implements the MultiCipher cipher that encrypts and decrypts
//strings. It encrypts/decrypts by having multiple different ciphers encrypt/decrypt an input
//string successively.
public class MultiCipher extends Cipher {

    private List<Cipher> ciphers;
    
    //This constructor creates a new MultiCipher using a list of ciphers.
    //Exception: if the list of ciphers is null, an IllegalArgumentException is thrown
    //Parameters:
    //  - ciphers: a non-null list of ciphers that will encrypt/decrypt strings successively
    public MultiCipher(List<Cipher> ciphers) {
        if (ciphers == null) {
            throw new IllegalArgumentException("Provided list of ciphers is emtpy.");
        }
        this.ciphers = ciphers;
    }

    //Behavior: this method encrypts a string by applying the ciphers in the list to the string
    //in order. The resulting encrypted string after one cipher is applied to the input serves
    //as the input for the next cipher in the list.
    //Return: the encrypted string after all the ciphers have been applied
    //Parameters:
    //  - input: string to be encrypted (non-null and containing only characters within the
    //           encodable range)
    public String encrypt(String input) {
        for (Cipher cipher : ciphers) {
            input = cipher.encrypt(input);
        }
        return input;
    }

    //Behavior: this method decrypts a string by applying the ciphers in the list to the string
    //in backwards order. The resulting decrypted string after one cipher is applied to the input 
    //serves as the input for the previous cipher in the list.
    //Return: the decrypted string after all the ciphers have been applied
    //Parameters:
    //  - input: the string to be decrypted (non-null and containing only characters within the
    //           encodable range)
    public String decrypt(String input) {
        for (int i = ciphers.size() - 1; i >= 0; i--) {
            input = ciphers.get(i).decrypt(input);
        }
        return input;
    }
}
