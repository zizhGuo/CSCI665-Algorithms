/***CSCI 665 Algorithms Homework 3 Question 2 DP
 * @author Guo, Zizhun
 */
import java.util.Scanner;

public class LongestIncreasingSubseqDP {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = scanner.nextInt();
		}
		scanner.close();
		System.out.println(dpLength(arr));
	}
	/***
	 * The function used dynamic programming to output the length of longest increasing subsequence.
	 * @param arr The input array.
	 * @return max lenth
	 */
	public static int dpLength(int[] arr) {
		int l = arr.length;
		int[] S = new int[l];
		S[0] = 1;
		
		for (int j = 1; j < l; j++) {
			S[j] = 1;
			for (int k = 0; k < j; k++) {
				if (arr[j] > arr[k] && S[j] < S[k] + 1) {
					S[j] = S[k] + 1;
				}			
			}			
		}
		return max(S);
	}
	/***
	 * To calculate the max number
	 * @param arr The input array.
	 * @return The max number 
	 */
	public static int max(int[] arr) {
		int max= 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
}
