import java.util.Scanner;
public class LongestConvexSubseq {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = scanner.nextInt();
		}
		scanner.close();
		System.out.println(dpConvex1(arr));
	}
	public static int dpConvex1(int[] arr) {
		int l = arr.length;
		int[][] S = new int[l][l];
		int maxx = 1;
		
		for (int i = l - 2; i >= 0; i--){
			for (int j = l - 1; j >= i + 1; j--) {
				S[i][j] = 2;
				for (int k = j + 1; k < l; k++) {
					if ((arr[i] + arr[k]) >= 2 * arr[j]) {
						S[i][j] = maxNum(S[i][j], 1 + S[j][k]);
					}
				}
				maxx = maxNum(maxx, S[i][j]);
			}
		}
		return maxx;
	}
	public static int dpConvex(int[] arr) {
		int l = arr.length;
		int[] S = new int[l];
		
		S[0] = 1;
		S[1] = 2;
		for (int i = 2; i < l; i++) {
			S[i] = 2;
			//int[] T = new int[i];
			//T[i] = 0;
			for (int j = 1; j < i; j++) {
				for (int k = 0; k < j; k++) {				
					if ((arr[k] + arr[i]) >= 2 * arr[j] && S[j] - 1 == S[k]) {
						S[i] = maxNum(S[i], 1 + S[j]);
					}
				}
			}
			S[i] = max(S);
		}
		return max(S);
	}
	public static int maxNum(int a, int b) {
		if (a > b) return a;
		else return b;
	}
	public static int max(int[] arr) {
		int max= 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
	public static void printResult(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}
}
