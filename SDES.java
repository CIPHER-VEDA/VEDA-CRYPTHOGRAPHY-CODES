

import java.io.*;

public class SDES {
	int key[] = {0, 0, 1, 0, 0, 1, 0, 1, 1, 1};
	int P10[] = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
	int P8[] = {6, 3, 7, 4, 8, 5, 10, 9};

	int key1[] = new int[8];
	int key2[] = new int[8];

	int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
	int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
	int[] P4 = {2, 4, 3, 1};
	int[] IP_inv = {4, 1, 3, 5, 7, 2, 8, 6};

	int[][] S0 = {
		{1, 0, 3, 2},
		{3, 2, 1, 0},
		{0, 2, 1, 3},
		{3, 1, 3, 2}
	};
	int[][] S1 = {
		{0, 1, 2, 3},
		{2, 0, 1, 3},
		{3, 0, 1, 0},
		{2, 1, 0, 3}
	};

	void key_generation() {
		int key_[] = new int[10];

		for (int i = 0; i < 10; i++) {
			key_[i] = key[P10[i] - 1];
		}
		System.out.println("P10 permutation: " + arrayToString(key_));

		int Ls[] = new int[5];
		int Rs[] = new int[5];

		for (int i = 0; i < 5; i++) {
			Ls[i] = key_[i];
			Rs[i] = key_[i + 5];
		}

		System.out.println("Left half (Ls): " + arrayToString(Ls));
		System.out.println("Right half (Rs): " + arrayToString(Rs));

		int[] Ls_1 = shift(Ls, 1);
		int[] Rs_1 = shift(Rs, 1);

		for (int i = 0; i < 5; i++) {
			key_[i] = Ls_1[i];
			key_[i + 5] = Rs_1[i];
		}

		for (int i = 0; i < 8; i++) {
			key1[i] = key_[P8[i] - 1];
		}

		System.out.println("Key-1: " + arrayToString(key1));

		int[] Ls_2 = shift(Ls, 2);
		int[] Rs_2 = shift(Rs, 2);

		for (int i = 0; i < 5; i++) {
			key_[i] = Ls_2[i];
			key_[i + 5] = Rs_2[i];
		}

		for (int i = 0; i < 8; i++) {
			key2[i] = key_[P8[i] - 1];
		}

		System.out.println("Key-2: " + arrayToString(key2));
	}

	int[] shift(int[] ar, int n) {
		while (n > 0) {
			int temp = ar[0];
			for (int i = 0; i < ar.length - 1; i++) {
				ar[i] = ar[i + 1];
			}
			ar[ar.length - 1] = temp;
			n--;
		}
		return ar;
	}

	int[] encryption(int[] plaintext) {
		int[] arr = new int[8];

		for (int i = 0; i < 8; i++) {
			arr[i] = plaintext[IP[i] - 1];
		}
		System.out.println("Initial Permutation (IP): " + arrayToString(arr));

		int[] arr1 = function_(arr, key1);
		System.out.println("After first round (key1): " + arrayToString(arr1));

		int[] after_swap = swap(arr1, arr1.length / 2);
		System.out.println("After swap: " + arrayToString(after_swap));

		int[] arr2 = function_(after_swap, key2);
		System.out.println("After second round (key2): " + arrayToString(arr2));

		int[] ciphertext = new int[8];
		for (int i = 0; i < 8; i++) {
			ciphertext[i] = arr2[IP_inv[i] - 1];
		}

		System.out.println("Cipher Text: " + arrayToString(ciphertext));
		return ciphertext;
	}

	String binary_(int val) {
		if (val == 0)
			return "00";
		else if (val == 1)
			return "01";
		else if (val == 2)
			return "10";
		else
			return "11";
	}

	int[] function_(int[] ar, int[] key_) {
		int[] l = new int[4];
		int[] r = new int[4];

		for (int i = 0; i < 4; i++) {
			l[i] = ar[i];
			r[i] = ar[i + 4];
		}

		int[] ep = new int[8];
		for (int i = 0; i < 8; i++) {
			ep[i] = r[EP[i] - 1];
		}
		System.out.println("Expansion (EP): " + arrayToString(ep));

		for (int i = 0; i < 8; i++) {
			ar[i] = key_[i] ^ ep[i];
		}
		System.out.println("After XOR with key: " + arrayToString(ar));

		int[] l_1 = new int[4];
		int[] r_1 = new int[4];

		for (int i = 0; i < 4; i++) {
			l_1[i] = ar[i];
			r_1[i] = ar[i + 4];
		}

		int row, col, val;

		row = Integer.parseInt("" + l_1[0] + l_1[3], 2);
		col = Integer.parseInt("" + l_1[1] + l_1[2], 2);
		val = S0[row][col];
		String str_l = binary_(val);

		row = Integer.parseInt("" + r_1[0] + r_1[3], 2);
		col = Integer.parseInt("" + r_1[1] + r_1[2], 2);
		val = S1[row][col];
		String str_r = binary_(val);

		int[] r_ = new int[4];
		for (int i = 0; i < 2; i++) {
			r_[i] = Character.getNumericValue(str_l.charAt(i));
			r_[i + 2] = Character.getNumericValue(str_r.charAt(i));
		}
		System.out.println("Substituted (S-boxes): " + arrayToString(r_));

		int[] r_p4 = new int[4];
		for (int i = 0; i < 4; i++) {
			r_p4[i] = r_[P4[i] - 1];
		}
		System.out.println("P4 permutation: " + arrayToString(r_p4));

		for (int i = 0; i < 4; i++) {
			l[i] = l[i] ^ r_p4[i];
		}

		int[] output = new int[8];
		for (int i = 0; i < 4; i++) {
			output[i] = l[i];
			output[i + 4] = r[i];
		}
		return output;
	}

	int[] swap(int[] array, int n) {
		int[] l = new int[n];
		int[] r = new int[n];

		for (int i = 0; i < n; i++) {
			l[i] = array[i];
			r[i] = array[i + n];
		}

		int[] output = new int[2 * n];
		for (int i = 0; i < n; i++) {
			output[i] = r[i];
			output[i + n] = l[i];
		}
		return output;
	}

	int[] decryption(int[] ar) {
		int[] arr = new int[8];

		for (int i = 0; i < 8; i++) {
			arr[i] = ar[IP[i] - 1];
		}
		System.out.println("Decryption Initial Permutation (IP): " + arrayToString(arr));

		int[] arr1 = function_(arr, key2);
		System.out.println("After first round (key2): " + arrayToString(arr1));

		int[] after_swap = swap(arr1, arr1.length / 2);
		System.out.println("After swap: " + arrayToString(after_swap));

		int[] arr2 = function_(after_swap, key1);
		System.out.println("After second round (key1): " + arrayToString(arr2));

		int[] decrypted = new int[8];
		for (int i = 0; i < 8; i++) {
			decrypted[i] = arr2[IP_inv[i] - 1];
		}

		System.out.println("Decrypted Text: " + arrayToString(decrypted));
		return decrypted;
	}

	public static void main(String[] args) {
		SDES obj = new SDES();

		obj.key_generation();

		int[] plaintext = {0, 1, 1, 1, 0, 1, 1, 1};
		System.out.println("\nYour plain Text is: " + arrayToString(plaintext));

		int[] ciphertext = obj.encryption(plaintext);

		int[] decrypted = obj.decryption(ciphertext);
	}

	// Utility function to convert array to string for printing
	private static String arrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();
		for (int num : array) {
			sb.append(num).append(" ");
		}
		return sb.toString().trim();
	}
}