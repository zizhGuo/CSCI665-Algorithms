/***CSCI 665 Algorithms Homework 3 Question 4
 * @author Guo, Zizhun
 */
import java.util.Scanner;
import java.math.*;

public class LongestIncreasingSubseqCount {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = scanner.nextInt();
		}
		scanner.close();
		// Insert an 0 at the first position of input array.
		int[] S = arrconverter(arr);
		// Update S.
		S = dpCount1(S);
		//
		System.out.println(S[n] % 1000000); 
	}
	/***
	 * The function calculate out the number of all increasing subsequences.
	 * @param arr The input array
	 * @return S The array that storing the number of subsequences. 
	 */
	public static int[] dpCount1(int[] arr) {
		int l = arr.length;
		int[] S = new int[l];
		int[] T = new int[l];
		S[0] = 1;
		T[0] = 1;
		
		for (int j = 1; j < l; j++ ) {
			for (int k = 0; k < j; k++) {
				if (arr[k] < arr[j]) {
					T[j] = T[j] + T[k];
					T[j] = T[j] % 1000000;
				}
			}
			S[j] = S[j - 1] + T[j];
			S[j] = S[j] % 1000000;
		}
		return S;
	}
	/***
	 * The function used to insert a 0 at the first position of an array
	 * @param a Input array
	 * @return b array with 0 inserted in the first position
	 */
	public static int[] arrconverter(int[] a) {
		int[] b = new int[a.length + 1];
		b[0] = 0;
		for (int i = 0; i < a.length; i++) b[i + 1] = a[i];
		return b;
	}
}
