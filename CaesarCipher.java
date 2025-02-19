import java.util.Scanner;

public class CaesarCipher {
    // Define an array of alphabets for reference in encryption and decryption
    public static final String alphabets[] = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
        "U", "V", "W", "X", "Y", "Z"
    };

    // Encrypt the given input string using the provided shift key
    public static String encrypt(String inputStr, int shiftKey) {
        StringBuilder encryptedStr = new StringBuilder(); // To store the encrypted message
        int n = inputStr.length();

        for (int i = 0; i < n; i++) {
            char st = inputStr.charAt(i);
            if (Character.isLetter(st)) { // Ensure character is a letter
                int index = (st - 'A' + shiftKey) % 26; // Compute shifted index
                encryptedStr.append(alphabets[index]); // Append encrypted character
            } else {
                encryptedStr.append(st); // Keep non-letter characters unchanged
            }
        }

        return encryptedStr.toString(); // Return the encrypted message
    }

    // Decrypt the given encrypted string using the shift key
    public static String decrypt(String encryptedStr, int shiftKey) {
        StringBuilder decryptedStr = new StringBuilder(); // To store the decrypted message
        int n = encryptedStr.length();

        for (int i = 0; i < n; i++) {
            char st = encryptedStr.charAt(i);
            if (Character.isLetter(st)) { // Ensure character is a letter
                int index = (st - 'A' - shiftKey + 26) % 26; // Compute original index
                decryptedStr.append(alphabets[index]); // Append decrypted character
            } else {
                decryptedStr.append(st); // Keep non-letter characters unchanged
            }
        }

        return decryptedStr.toString(); // Return the decrypted message
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        // Read user input for encryption
        System.out.println("ENTER YOUR TEXT TO ENCRYPT: ");
        String inputStr = scn.nextLine().toUpperCase().replaceAll("\\s+", ""); // Convert to uppercase and remove spaces

        // Read the shift key from the user
        System.out.println("ENTER YOUR SHIFT KEY BETWEEN 0 - 25: ");
        int shiftKey = scn.nextInt();

        // Perform encryption
        String encryptedStr = encrypt(inputStr, shiftKey);
        System.out.println("ENCRYPTED MESSAGE = " + encryptedStr);

        // Perform decryption
        String decryptedStr = decrypt(encryptedStr, shiftKey);
        System.out.println("DECRYPTED MESSAGE = " + decryptedStr);
    }
}
