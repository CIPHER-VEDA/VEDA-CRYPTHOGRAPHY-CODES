import java.util.Scanner;

public class PlayFair {

    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // 'J' is omitted
    private char[][] keyMatrix = new char[5][5];
    private String key;

    public PlayFair(String key) {
        this.key = key.toUpperCase().replace("J", "I"); // Replace 'J' with 'I'
        createKeyMatrix();
    }

    // Create the 5x5 key matrix
    private void createKeyMatrix() {
        boolean[] used = new boolean[26]; // Track used letters
        int k = 0;

        // Fill the key matrix with the key
        for (char ch : key.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z' && !used[ch - 'A']) {
                used[ch - 'A'] = true;
                keyMatrix[k / 5][k % 5] = ch;
                k++;
            }
        }

        // Fill the remaining spaces with the rest of the alphabet
        for (char ch : ALPHABET.toCharArray()) {
            if (!used[ch - 'A']) {
                used[ch - 'A'] = true;
                keyMatrix[k / 5][k % 5] = ch;
                k++;
            }
        }
    }

    // Preprocess the plaintext: remove non-alphabetic characters, handle 'J', and add 'X' for double letters or odd length
    private String preprocessPlaintext(String plaintext) {
        plaintext = plaintext.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i++) {
            char current = plaintext.charAt(i);
            result.append(current);

            // Add 'X' if two same letters are found together
            if (i + 1 < plaintext.length() && current == plaintext.charAt(i + 1)) {
                result.append('X');
            }
        }

        // Add 'X' if the length is odd
        if (result.length() % 2 != 0) {
            result.append('X');
        }

        return result.toString();
    }

    // Get the row and column indices of a character in the key matrix
    private int[] getIndex(char ch) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyMatrix[i][j] == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // Character not found (should not happen with valid input)
    }

    // Encrypt the plaintext
    public String encrypt(String plaintext) {
        String processedText = preprocessPlaintext(plaintext);
        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < processedText.length(); i += 2) {
            char first = processedText.charAt(i);
            char second = processedText.charAt(i + 1);

            int[] firstIndex = getIndex(first);
            int[] secondIndex = getIndex(second);

            if (firstIndex[0] == secondIndex[0]) { 
                // Same row: shift right (wrap around)
                cipherText.append(keyMatrix[firstIndex[0]][(firstIndex[1] + 1) % 5]);
                cipherText.append(keyMatrix[secondIndex[0]][(secondIndex[1] + 1) % 5]);
            } else if (firstIndex[1] == secondIndex[1]) { 
                // Same column: shift down (wrap around)
                cipherText.append(keyMatrix[(firstIndex[0] + 1) % 5][firstIndex[1]]);
                cipherText.append(keyMatrix[(secondIndex[0] + 1) % 5][secondIndex[1]]);
            } else { 
                // Rectangle rule: swap columns
                cipherText.append(keyMatrix[firstIndex[0]][secondIndex[1]]);
                cipherText.append(keyMatrix[secondIndex[0]][firstIndex[1]]);
            }
        }

        return cipherText.toString();
    }

    // Decrypt the ciphertext
    public String decrypt(String cipherText) {
        StringBuilder plainText = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i += 2) {
            char first = cipherText.charAt(i);
            char second = cipherText.charAt(i + 1);

            int[] firstIndex = getIndex(first);
            int[] secondIndex = getIndex(second);

            if (firstIndex[0] == secondIndex[0]) { 
                // Same row: shift left (wrap around)
                plainText.append(keyMatrix[firstIndex[0]][(firstIndex[1] + 4) % 5]);
                plainText.append(keyMatrix[secondIndex[0]][(secondIndex[1] + 4) % 5]);
            } else if (firstIndex[1] == secondIndex[1]) { 
                // Same column: shift up (wrap around)
                plainText.append(keyMatrix[(firstIndex[0] + 4) % 5][firstIndex[1]]);
                plainText.append(keyMatrix[(secondIndex[0] + 4) % 5][secondIndex[1]]);
            } else { 
                // Rectangle rule: swap columns
                plainText.append(keyMatrix[firstIndex[0]][secondIndex[1]]);
                plainText.append(keyMatrix[secondIndex[0]][firstIndex[1]]);
            }
        }

        // Remove any 'X' characters added during encryption
        String result = plainText.toString().replaceAll("X(?=[A-Z])", "").replaceAll("X$", "");
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        PlayFair playfair = new PlayFair(key);

        System.out.print("Enter plaintext to encrypt: ");
        String plaintext = scanner.nextLine();
        String encryptedText = playfair.encrypt(plaintext);
        System.out.println("Encrypted text: " + encryptedText);

        String decryptedText = playfair.decrypt(encryptedText);
        System.out.println("Decrypted text: " + decryptedText);

        scanner.close();
    }
}