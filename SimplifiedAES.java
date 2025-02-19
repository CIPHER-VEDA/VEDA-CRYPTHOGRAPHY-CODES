import java.util.Arrays;
public class SimplifiedAES 
{
    int key[] = {1,0,1,0,0,1,1,1,0,0,1,1,1,0,1,1};
    int GF16[][] = 
    {
        {0, 0, 0, 0},  // 4 x 0 = 0
        {0, 1, 0, 0},  // 4 x 1 = 4
        {1, 0, 0, 0},  // 4 x 2 = 8
        {1, 1, 0, 0},  // 4 x 3 = C (12)
        {0, 0, 1, 1},  // 4 x 4 = 3
        {0, 1, 1, 1},  // 4 x 5 = 7
        {1, 0, 1, 1},  // 4 x 6 = B (11)
        {1, 1, 1, 1},  // 4 x 7 = F (15)
        {0, 1, 1, 0},  // 4 x 8 = 6
        {0, 0, 1, 0},  // 4 x 9 = 2
        {1, 1, 1, 0},  // 4 x A = E (14)
        {1, 0, 1, 0},  // 4 x B = A (10)
        {0, 1, 0, 1},  // 4 x C = 5
        {0, 0, 0, 1},  // 4 x D = 1
        {1, 1, 0, 1},  // 4 x E = D (13)
        {1, 0, 0, 1}   // 4 x F = 9
    };
    
    int[] W0 = new int[8];
    int[] W1 = new int[8];
    int[] W2 = new int[8];
    int[] W3 = new int[8];
    int[] W4 = new int[8];
    int[] W5 = new int[8];

    int[][][] Sbox={
        {{1,0,0,1},{0,1,0,0},{1,0,1,0},{1,0,1,1}},
        {{1,1,0,1},{0,0,0,1},{1,0,0,0},{0,1,0,1}},
        {{0,1,1,0},{0,0,1,0},{0,0,0,0},{0,0,1,1}},
        {{1,1,0,0},{1,1,1,0},{1,1,1,1},{0,1,1,1}},
    };
    
    private int[] RotNib(int[] array ) //rotates nibble of an array
    {
        int[] rotnibarray = new int[array.length];
        for(int i=0;i<8;i++)
        {
            rotnibarray[i] = array[(i+4)%8];
        }
        return rotnibarray;
    }
    private int[] subnibble(int[] array)
    {
        int[] subnibblearray = new int[array.length];
        int left1[] = Arrays.copyOfRange(array,0,4); //left half of rotnib 
        int right1[] = Arrays.copyOfRange(array,4,8);// right half of rotnib
        int lrow = (left1[0]*2)+left1[1];
        int lcolumn = (left1[2]*2)+left1[3];
        int rrow = (right1[0]*2)+right1[1];
        int rcolumn = (right1[2]*2)+right1[3];
        int left[]= Sbox[lrow][lcolumn];
        int right[] = Sbox[rrow][rcolumn];
        //copy elements from left half array to subnibblearray
        System.arraycopy(left, 0, subnibblearray, 0, left.length);
        
        // Copy elements from the right half array to subnibblearray
        System.arraycopy(right, 0, subnibblearray, left.length, right.length);
        return subnibblearray;
    }
    private int[] xorArrays(int[] a, int[] b) 
    {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }
    // concatenate two arrays
    private int[] concatenateArrays(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    public void keyExpansion(int[] key) 
    {
        W0 = Arrays.copyOfRange(key, 0, 8);
        W1 = Arrays.copyOfRange(key, 8, 16);

        int[] constant1 = {1, 0, 0, 0, 0, 0, 0, 0}; 
        int[] rotatedW1 = RotNib(W1);
        int[] subNibW1 = subnibble(rotatedW1);
        // W2 = W0 XOR 10000000 XOR SubNib(RotNib(W1))
        W2 = xorArrays(xorArrays(W0, constant1), subNibW1);
        // W3 = W2 XOR W1
        W3 = xorArrays(W2, W1);

        int[] constant2 = {0, 0, 1, 1, 0, 0, 0, 0}; 
        int[] rotatedW3 = RotNib(W3);
        int[] subNibW3 = subnibble(rotatedW3);
        // W4 = W2 XOR 00110000 XOR SubNib(RotNib(W3))
        W4 = xorArrays(xorArrays(W2, constant2), subNibW3);
        // W5 = W4 XOR W3
        W5 = xorArrays(W4, W3);

        System.out.println("W0: " + Arrays.toString(W0));
        System.out.println("W1: " + Arrays.toString(W1));
        System.out.println("W2: " + Arrays.toString(W2));
        System.out.println("W3: " + Arrays.toString(W3));
        System.out.println("W4: " + Arrays.toString(W4));
        System.out.println("W5: " + Arrays.toString(W5));
    }

    int[] Key0;
    int[] Key1;
    int[] Key2;

    public void generateKeys() {
        Key0 = concatenateArrays(W0, W1);
        Key1 = concatenateArrays(W2, W3);
        Key2 = concatenateArrays(W4, W5);
        System.out.println("Key0: " + Arrays.toString(Key0));
        System.out.println("Key1: " + Arrays.toString(Key1));
        System.out.println("Key2: " + Arrays.toString(Key2));
    }

    private int[] addRoundKey(int[] state, int[] roundKey) // state 1 is nothing but plaintext and remaining states changes
    {
        int[] stateadk = xorArrays(state, roundKey);
        System.out.println("AddRoundKey: " + Arrays.toString(stateadk));
        return stateadk;
    }
    private int[] subBytes(int[] state) 
    {
        int[] subBytesArray = new int[state.length]; 
        for (int i = 0; i < 4; i++) 
        {
            int[] nibble = Arrays.copyOfRange(state, i * 4, (i + 1) * 4);
            int row = (nibble[0] * 2) + nibble[1];
            int column = (nibble[2] * 2) + nibble[3];
            int[] substitutedNibble = Sbox[row][column];
            System.arraycopy(substitutedNibble, 0, subBytesArray, i * 4, 4);
        }
        System.out.println("SubBytes: " + Arrays.toString(subBytesArray));
        return subBytesArray;
    }
    private int[] shiftRows(int[] state) 
    {
        int[] srarray = Arrays.copyOf(state, state.length);
        srarray[4] = state[12];
        srarray[5] = state[13];
        srarray[6] = state[14];
        srarray[7] = state[15];
        srarray[12] = state[4]; 
        srarray[13] = state[5]; 
        srarray[14] = state[6]; 
        srarray[15] = state[7]; 
        System.out.println("ShiftRows: " + Arrays.toString(srarray));
        return srarray;
    }
    int[] gfmul(int array[])
    {
        int indexOFGF16 = ((array[0]*8)+(array[1]*4)+(array[2]*2)+(array[3]*1));
        return GF16[indexOFGF16];
    }
    int[] mixColumns(int state[])
    {
        // for example :
        // 1000 0111 0111 0111
        //  a1   a2   a3   a4
        int[] mixColumnsOutput = new int[state.length];
        int[] a1 = {state[0],state[1],state[2],state[3]};
        int[] a2 = {state[4],state[5],state[6],state[7]};
        int[] a3 = {state[8],state[9],state[10],state[11]};
        int[] a4 = {state[12],state[13],state[14],state[15]};
        // state matrix multiplication with const matrix under gf(16)
        int[] s0 = xorArrays(a1,gfmul(a2));
        int[] s1 = xorArrays(gfmul(a1),a2);
        int[] s2 = xorArrays(a3,gfmul(a4));
        int[] s3 = xorArrays(gfmul(a3),a4);
        System.arraycopy(s0, 0, mixColumnsOutput, 0,4);
        System.arraycopy(s1, 0, mixColumnsOutput, 4, 4);
        System.arraycopy(s2, 0, mixColumnsOutput, 8, 4);
        System.arraycopy(s3, 0, mixColumnsOutput, 12, 4);
        System.out.println("MixColumns: " + Arrays.toString(mixColumnsOutput));
        return mixColumnsOutput;
    }

    int[] encrypt(int plaintext[])
    {
        // initial add round key
        int[] state1 = addRoundKey(plaintext,Key0);
        // round 1
        int[] state2 = subBytes(state1);
        int[] state3 = shiftRows(state2);
        int[] state4 = mixColumns(state3);
        int[] state5 = addRoundKey(state4,Key1);
        // round 2
        int[] state6 = subBytes(state5);
        int[] state7 = shiftRows(state6);
        int[] state8 = addRoundKey(state7,Key2);
        System.err.println();
        System.out.println("Ciphertext: " + Arrays.toString(state8));
        return state8;
    }
    public static void main(String[] args) 
    {
        SimplifiedAES saes = new SimplifiedAES();
        saes.keyExpansion(saes.key);
        saes.generateKeys();
        int[] plaintext = {0,1,1,0,1,1,1,1,0,1,1,0,1,0,1,1};
        int[] ciphertext = saes.encrypt(plaintext);
    }
}
