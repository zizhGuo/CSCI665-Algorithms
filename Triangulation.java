/***
 * Foundation of Algorithms. 
 * HW4
 * Problem 5
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class Triangulation {

	public static void main(String[] args) {
		try {
			// Use scanner class to get inputs.
			Scanner scanner = new Scanner(System.in);
			int n = scanner.nextInt();
			Vertice[] vertices = new Vertice[n];
			for (int i = 0; i < n; i++) {
				vertices[i] = new Vertice(scanner.nextFloat(), scanner.nextFloat());
			}
			scanner.close();
			
			//Finds the minimum possible length of a triangulation of the given polygon.
			triangulation(vertices);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
	/***
	 * This function employs a dynamic programming algorithm to find the the minimum
	 * possible length of polygon.
	 * 
	 * @param v An array of vertices that formulate a convex polygon.
	 * 
	 * @output the minimum possible length of polygon. 
	 */
	static void triangulation(Vertice[] v) {
		//The number of vertices.
		int n = v.length;
		
		//A two-dimensional array contains the minimum length started from i to j. 
		float[][] S = new float[n+ 1][n + 1];
		
		//Initialization for the instances of all S[i][j].
		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < n + 1; j++) {
				S[i][j] = 0;
			} 
		}
		
		//Initialization for S[i][j]].
		//S[i][j] = 0 if i = j. 
		for (int i = 1; i < n + 1; i++) S[i][i] = 0;
		
		//Initialization for S[i][j]].
		//S[i][j] = 0 if i + 1 = j. 
		for (int i = 1; i < n; i++) S[i][i + 1] = 0;
		
		//Traverse differences between i and j, from d = 2 to n - 1.
		for (int d = 2; d < n; d++) {
			
			//i is the start index of a polygon.
			for (int i = 1; i < n - d + 1; i++) {
				
				//j is the end index of a polygon.
				int j = i + d;
				
				//Initialize the originated value for each possible polygons (i, j).
				S[i][j] = 100000000;
				
				//k is the segmentation index to divide current polygon to two sub-polygons. 
				for (int k = i + 1; k < j; k++) {
					float temp;
					
					//In this case, S[i][j] represents minimum cost for input goal polygon, so distance
					//between i and j is the edge ij, so the edge should not be added to overall cost.
					if (d == n - 1) temp = S[i][k] + S[k][j];
					
					//This is heart of solution.
					//In other cases, use dynamic programming algorithm to incremented minimum cost from
					//sub-polygons. The minimum value will be updated if it has less cost.
					else {
						temp = S[i][k] + S[k][j] + (float)chordAbs(v[i - 1], v[j - 1]);						
					}
					if (temp < S[i][j]) {
						S[i][j] = temp;	
					}
				}
			
			}
		}
		System.out.print(round(S[1][n], 4));
	}
	/***
	 * This function is used for rounding down the value to the desired scale of decimal digits.
	 * @param value The double type value.
	 * @param scale The scale of desired decimal digits.
	 * @return The rounded value.
	 */
	public static double round(double value, int scale) {
	    return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
	}
	/***
	 * This function works for producing the distance between two vertices.
	 * @param a Vertice a.
	 * @param b Vertice b.
	 * @return distance between vertice a and vertice b.
	 */
	static double chordAbs(Vertice a, Vertice b) {
		return Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
}
/***
 * This class is used for representing the vertices of polygons.
 * @author Zizhun Guo
 *
 */
class Vertice{
	//The x coordinate of a vertice.
	float x;
	
	//The y coordinate of a vertice.
	float y;
	public Vertice(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/***
	 * This function is used for printing out the vertices.
	 */
	public void print() {
		System.out.print("X = " + this.x);
		System.out.print("  Y = " + this.y);
		System.out.println();
	}
}
