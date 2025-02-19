import java.util.Scanner;

public class RSA {

    // finds GCD using the extended euclidean algorithm
    static int gcd(int e, int piofn) {
        while (piofn != 0) {
            int temp = piofn;
            piofn = e % piofn;
            e = temp;
        }
        return e;
    }

    //finds whther the given number is prime or not
    static boolean primeChecker(int a)
    {
        if(a<=1)
            return false;
        for(int i=2;i<(Math.sqrt(a)+1);i++)
        {
            if(a%i==0)
            {
                return false;
            }   
        }
        return true;

    }

 
    static int modInverse(int e, int piofn) {
        for (int d = 1; d < piofn; d++) {
            if ((e * d) % piofn == 1) {
                return d;
            }
        }
        return -1; // if no modular inverse found
    }

    // calculates (base^exp) mod n
    static long calculate(long base, int exp, int modn) 
    {
        base = base%modn;
        long result = 1;
        for (int i = 0; i < exp; i++) {
            result = (result * base) % modn;
        }
        return result;
    }

    // encryption
    static long encrypt(int n, int e, long m) {
        return calculate(m, e, n);
    }

    // decryption
    static long decrypt(long c, int n, int d) 
    {
        return calculate(c, d, n);
    }

    public static void main(String[] args) 
    {
        Scanner scn = new Scanner(System.in);

        // Get prime numbers p and q
        System.out.println("Enter p value (a prime number): ");
        int p = scn.nextInt();
        boolean checker = primeChecker(p);
        if(checker==false)
        {
            System.out.println("GIVEN NUMBER IS NOT A PRIME NUMBER! ");
            return;
        }
        System.out.println("Enter q value (a prime number): ");
        int q = scn.nextInt();

        int n = p * q;
        int piofn = (p - 1) * (q - 1);

        // find e such that 1 < e < phi(n) and gcd(e, phi(n)) == 1
        int e = 2;
        while (e < piofn && gcd(e, piofn) != 1) 
        {
            e++;
        }

        // calculate d (the modular inverse of e mod phi(n))
        int d = modInverse(e, piofn);

        System.out.println("Public Key: (n = " + n + ", e = " + e + ")");
        System.out.println("Private Key: (n = " + n + ", d = " + d + ")");

        // encryption
        System.out.println("enter a message to encrypt (as an integer): ");
        int message = scn.nextInt();
        long ciphertext = encrypt(n, e, message);
        System.out.println("encrypted message (ciphertext): " + ciphertext);

        // decryption
        long decryptedMessage = decrypt(ciphertext, n, d);
        System.out.println("decrypted message (plaintext): " + decryptedMessage);

        scn.close();
    }
}

