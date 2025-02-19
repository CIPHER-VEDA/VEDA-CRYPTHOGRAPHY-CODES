import java.util.Scanner;

public class HillCipher {
    // 3x3 Key matrix for encryption
    private static final int[][] keyMatrix = {
        { 1, 2, 1 },
        { 2, 3, 2 },
        { 2, 2, 1 }
    };
    
    // Inverse key matrix for decryption
    private static final int[][] inverseKeyMatrix = {
        { -1, 0, 1 },
        { 2, -1, 0 },
        { -2, 2, -1 }
    };
    
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Encrypts a three-character block using row-wise multiplication
    private static String encode(char a, char b, char c) {
        int x = keyMatrix[0][0] * (a - 'A') + keyMatrix[0][1] * (b - 'A') + keyMatrix[0][2] * (c - 'A');
        int y = keyMatrix[1][0] * (a - 'A') + keyMatrix[1][1] * (b - 'A') + keyMatrix[1][2] * (c - 'A');
        int z = keyMatrix[2][0] * (a - 'A') + keyMatrix[2][1] * (b - 'A') + keyMatrix[2][2] * (c - 'A');
        return "" + ALPHABET.charAt((x % 26 + 26) % 26) + ALPHABET.charAt((y % 26 + 26) % 26) + ALPHABET.charAt((z % 26 + 26) % 26);
    }

    // Decrypts a three-character block using row-wise multiplication
    private static String decode(char a, char b, char c) {
        int x = inverseKeyMatrix[0][0] * (a - 'A') + inverseKeyMatrix[0][1] * (b - 'A') + inverseKeyMatrix[0][2] * (c - 'A');
        int y = inverseKeyMatrix[1][0] * (a - 'A') + inverseKeyMatrix[1][1] * (b - 'A') + inverseKeyMatrix[1][2] * (c - 'A');
        int z = inverseKeyMatrix[2][0] * (a - 'A') + inverseKeyMatrix[2][1] * (b - 'A') + inverseKeyMatrix[2][2] * (c - 'A');
        return "" + ALPHABET.charAt((x % 26 + 26) % 26) + ALPHABET.charAt((y % 26 + 26) % 26) + ALPHABET.charAt((z % 26 + 26) % 26);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter message to encrypt: ");
        String msg = scanner.nextLine().toUpperCase().replaceAll("\\s", ""); // Convert to uppercase and remove spaces

        // Ensure message length is a multiple of 3 by padding with 'X'
        int n = msg.length() % 3;
        if (n != 0) {
            msg += "X".repeat(3 - n);
        }

        StringBuilder enc = new StringBuilder();
        StringBuilder dec = new StringBuilder();

        // Encrypt message in 3-character blocks
        for (int i = 0; i < msg.length(); i += 3) {
            enc.append(encode(msg.charAt(i), msg.charAt(i + 1), msg.charAt(i + 2)));
        }

        // Decrypt message in 3-character blocks
        for (int i = 0; i < enc.length(); i += 3) {
            dec.append(decode(enc.charAt(i), enc.charAt(i + 1), enc.charAt(i + 2)));
        }

        // Display results
        System.out.println("Input message: " + msg);
        System.out.println("Encoded message: " + enc);
        System.out.println("Decoded message: " + dec);
        
        scanner.close();
    }
}
