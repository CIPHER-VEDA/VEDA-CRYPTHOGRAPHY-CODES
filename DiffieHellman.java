import java.util.Scanner;
class CaesarCipherSai 
{
    public static final String alphabets[] = 
    {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
        "U", "V", "W", "X", "Y", "Z"
    };

    public String encrypt(String inputStr, long shiftKey) 
    {
        StringBuilder encryptedStr = new StringBuilder();
        int n = inputStr.length();

        for (int i = 0; i < n; i++) 
        {
            char st = inputStr.charAt(i);
            if (Character.isLetter(st)) 
            {
                long index = (st - 'A' + shiftKey) % 26;
                encryptedStr.append(alphabets[(int) index]);
            } 
            else 
            {
                encryptedStr.append(st);
            }
        }

        return encryptedStr.toString();
    }

    public String decrypt(String encryptedStr, long shiftKey) 
{
    StringBuilder decryptedStr = new StringBuilder();
    long n = encryptedStr.length();

    for (int i = 0; i < n; i++) 
    {
        char st = encryptedStr.charAt(i);
        if (Character.isLetter(st)) 
        {
            long index = (st - 'A' - shiftKey + 26) % 26; // Ensure positive result
            if (index < 0) {
                index += 26;  // Ensuring that negative indices are wrapped to valid range
            }
            decryptedStr.append(alphabets[(int) index]);
        }
        else 
        {
            decryptedStr.append(st);
        }
    }

    return decryptedStr.toString();
}

}

public class DiffieHellman 
{
    // calculates gcd of two numbers
    static long gcd(long i, long p) 
    {
        while (p != 0) 
        {
            long temp = p;
            p = i % p;
            i = temp;
        }
        return i;
    }

    //prime checker
    static boolean primeChecker(long a)
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

    // calculates (base^exp) mod modn
    static long calculate(long base, long exp, long modn) 
    {
        base = base%modn;
        long result = 1;
        for (int i = 0; i < exp; i++) {
            result = (result * base) % modn;
        }
        return result;
    }
    // checks whether g is a primitive root of p or not
     static boolean isprimitiveroot(long p,long g)
    {
        long z[] = new long[(int) (p-1)];
        int index=0;

        for(int i=1;i<p;i++)
        {
            if(gcd(i,p)==1)
            {
                z[index]=i; //stores all coprimes of p in z* array
                index++;
                
            }
        }

        long garray[] = new long[(int) (p-1)];
        for(int j=0;j<garray.length;j++)
        {
            garray[j] = calculate(g,j+1,p);  // stores all values of g^1 to g^p-1 
        }
        
        for(int i=0;i<index;i++)
        {
            int a =0;
            for(int j=0;j<garray.length;j++)
            {
                if(z[i]==garray[j]) // check if all elements of g are present in an z* or not
                {
                    a=1;
                    
                    break;
                }
        
            }
            if(a==0)
            {
                return false;
            }
            
        }
        return true;
        
    }

    public static void main(String[] args) 
    {
        Scanner scn = new Scanner(System.in);
        CaesarCipherSai ceaser = new CaesarCipherSai();

        System.out.println("enter p value : ");
        long p = scn.nextLong();
        boolean checker = primeChecker(p);
        if(checker==false)
        {
            System.out.println("GIVEN NUMBER IS NOT A PRIME NUMBER! ");
            return;
        }
        System.out.println("Enter g value : ");
        long g = scn.nextInt();
        boolean check =isprimitiveroot(p,g);
        if(check==false)
        {
            System.out.println("G is not a primitive root of P");
            return;
        }
        else
            System.out.println("G is a primitive root of P");

        // person 1 chooses a private key x, computes public key X = G^x % P
        System.out.println("Enter Person 1's private key x : ");
        long x = scn.nextInt();
        long X = calculate(g, x, p);
        System.out.println("person 1's public key x : " + X);

        // person 2 chooses a private key y, computes public key Y = G^y % P
        System.out.println("enter person 2's private key y : ");
        long y = scn.nextInt();
        long Y = calculate(g, y, p);
        System.out.println("person 2's public key Y : " + Y);

        // both compute the shared secret key 
        long sharedSecretPerson1 = calculate(Y, x, p);  // person 1 computes (Y^x) % P
        long sharedSecretPerson2 = calculate(X, y, p);  // person 2 computes (X^y) % P

        System.out.println("shared secret key computed by Person 1: " + sharedSecretPerson1);
        System.out.println("shared secret key computed by Person 2: " + sharedSecretPerson2);

        System.out.println("ENTER YOUR TEXT TO ENCRYPT: ");
        String inputStr = scn.next().toUpperCase();

        String encryptedStr = ceaser.encrypt(inputStr, sharedSecretPerson1);
        System.out.println("ENCRYPTED MESSAGE = " + encryptedStr);

        String decryptedStr = ceaser.decrypt(encryptedStr,sharedSecretPerson2);
        System.out.println("DECRYPTED MESSAGE = " + decryptedStr);
        

        scn.close();
    }
}
