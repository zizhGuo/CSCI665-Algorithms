/***
 * Foundation of Algorithms. 
 * HW4
 * Problem 3
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class MinWeightKnapsack {

	public static void main(String[] args) {
		try {
			//Use scanner class to get inputs.
			Scanner scanner = new Scanner(System.in);
			int n = scanner.nextInt();
			Item[] items = new Item[n];
			final int COST = scanner.nextInt();
			for (int i = 0; i < n; i++) {
				items[i] = new Item(scanner.nextInt(), scanner.nextInt());
			}
			scanner.close();
			
			//Find the minimum weights.
			dpKnapsack(items, n, COST);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	/***
	 * This function implemented a dynamic programming algorithm to find the minimum weights that has total cost at least C
	 * @param Items array.
	 * @param n Amount of items.
	 * @param C Minimal total cost
	 */
	static void dpKnapsack(Item[] items, int n, int C) {
		
		//The minimal weight that has at least total cost of C. 
		int[][] S = new int[C + 1][n + 1];
		
		//Initialization of the array. 
		for (int i = 0; i < C + 1; i++) {
			for (int j = 0; j < n + 1; j++) {
				//Default value for items that have no value, because such item does not exist, the value equals to 0.
				S[i][j] = 0;
				
				//Default value for null items selected, which is infinity.
				if (j == 0) S[i][j] = 10000;
			}
		}
		//Base case for zero item selected and zero cost.
		S[0][0] = 0;
		
		//The incremented cost from 1 to required minimum cost of C.
		for (int i = 1; i < C + 1; i++) {
			
			//The incremented items index from 1 to n.
			for (int j = 1; j < n + 1; j++) {
				
				//The current item's cost.
				int c = items[j - 1].c;
				
				//The current item's weight.
				int w = items[j - 1].w;
				
				//The heart of solution.
				//If the current item's cost is greater than incremented cost that i - c < 0, it is assumed the weight
				//for future increment is implicitly over the required C, by taking the previous situation that cost of 0.
				//Produce the minimal weight at current required cost.
				if (c > i) {
					S[i][j] = Integer.min(S[0][j - 1] + w, S[i][j - 1]);
				}
				
				//Produce the minimal weight at current required cost.
				else {
					S[i][j] = Integer.min(S[i - c][j - 1] + w, S[i][j - 1]);
				}
			}
		}
		//Print out the result.
		System.out.print(S[C][n]);		
	}
}
/***
 * This class is used for representing the item to be picked up into the bag.
 * @author Zizhun Guo
 *
 */
class Item{
	// The weight of the item.
	public int w;
	
	//The cost of the item.
	public int c;
	public Item(int weight, int cost) {
		w = weight;
		c = cost;
	}
}
