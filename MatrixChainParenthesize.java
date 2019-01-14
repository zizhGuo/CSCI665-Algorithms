/***
 * Foundation of Algorithms. 
 * HW4
 * Problem 4
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class MatrixChainParenthesize {

	public static void main(String[] args) {
		try {
			//Use scanner class to get inputs.
			Scanner scanner = new Scanner(System.in);
			int n = scanner.nextInt() + 1;
			int[] chains = new int[n];
			for (int i = 0; i < n; i++) {
				chains[i] = scanner.nextInt();
			}		
			scanner.close();
			
			//Compute the minimum cost of matrix chains.
			//Print the parenthesizing formation of minimum cost of the matrix chain multiplication.
			matrixChainMultiplication(chains);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	/***
	 * This function is used for printing out the corresponding parenthesizing from the minimum chain.
	 * The function employs recursion to conduct it.
	 * @param p The array that records the parenthesized index.
	 * @param i The start index of current parenthesizing. 
	 * @param j The end index of current parenthesizing.
	 */
	static void printParenthesize(int[][] p, int i, int j) {
		//The base case that represents the single specific matrix. 
		if (i == j) {
			System.out.print("A" + i);
		}
		
		//The cases that at least two matrix existing to conduct the multiplication.
		else {
			System.out.print("( ");
			printParenthesize(p, i, p[i][j]);
			System.out.print(" x ");
			printParenthesize(p, p[i][j] + 1, j);
			System.out.print(" )");
		}
	}
	/***
	 * This function is used for computing the minimal number of operations needed
	 * to multiply. 
	 * @param a An array that represents a chain of matrix.
	 */
	static void matrixChainMultiplication(int[] a) {
		//The length of the matrixes array.
		int n = a.length;
		
		//The minimal number of steps required to multiply matrixes from L-th  to the R-th. 
		int[][] S = new int[n][n];
		
		//The parenthesized index under current values of L and R.
		int[][] P = new int[n][n];
		
		//Initialization for all S[i][j] and P[i][j].
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < n; j++) {
				S[i][j] = 0;
				P[i][j] = 0;
			} 
		}
		
		//The single matrix that cannot conduct the multiplication.
		for (int i = 1; i < n; i++) S[i][i] = 0;
		
		//Traverse differences between L and R, from d = 1 to n - 2.
		for (int d = 1; d < n - 1; d++) {
			
			//L is the left start index of current sub-chain matrixes.
			for (int L = 1; L < n - d; L++) {
				
				// R is the right end index of current sub-chain matrixes.
				int R = L + d;
				
				//Initialize the original value for each formation.
				S[L][R] = 100000000;
				
				//k is the parenthesized index to divide current chain into two sub-chains.
				for (int k = L; k < R; k++) {
					
					//The heart of solution that produce the minimum cost among all sub-chains' 
					//matrixes multiplication. 
					//The current cost is the sum of left sub-chain's cost, right sub-chain's
					//cost, and the current two chain's multiplication cost.
					int temp = S[L][k] + S[k + 1][R] + a[L - 1] * a[k] * a[R];
					if (temp < S[L][R]) {
						//Records the minimal one by comparing all possible pairs.
						S[L][R] = temp;	
						
						//Records the parenthesized index.
						P[L][R] = k;
					}
				}
			}
		}
		//Print the minimal cost of current chain formation.
		System.out.println(S[1][n - 1]);
		
		//Print the parenthesizing.
		printParenthesize(P, 1, n-1);
	}

}
