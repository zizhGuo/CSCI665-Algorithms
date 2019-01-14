/***
 * Foundation of Algorithms. 
 * HW4
 * Problem 2
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class LongestCommonSubseqThree {

	public static void main(String[] args) {
		try {
			//Use scanner class to get inputs.
			Scanner scanner = new Scanner(System.in);
			int n1 = scanner.nextInt();
			int n2 = scanner.nextInt();
			int n3 = scanner.nextInt();
			int[] arr1 = new int[n1];
			int[] arr2 = new int[n2];
			int[] arr3 = new int[n3];
			for (int i = 0; i < n1; i++) {
				arr1[i] = scanner.nextInt();
			}
			for (int i = 0; i < n2; i++) {
				arr2[i] = scanner.nextInt();
			}
			for (int i = 0; i < n2; i++) {
				arr3[i] = scanner.nextInt();
			}
			scanner.close();
			
			//Find the longest common sequence. 
			dpNumLongestSubseqThree(arr1, arr2, arr3);
		}
		catch(Exception e) {
			System.out.print(e);
		}
	}
	/***
	 * This function is used for finding the longest common sub-sequence among three arrays.
	 * Implemented in dynamic programming algorithm with linked nodes structure.
	 * @param a The array a.
	 * @param b The array b.
	 * @param c The array c.
	 */
	public static void dpNumLongestSubseqThree(int[] a, int[] b, int[] c) {
		//The length of array a.
		int l1 = a.length;
		
		//The length of array b.
		int l2 = b.length;
		
		//The length of array c.
		int l3 = c.length;
		
		//The node that contains the maximum length of common sequence among three arrays a, b, c,
		//where each length is i, j and k, and the reference from front node.  
		Node[][][] N = new Node[l1 + 1][l2 + 1][l3 + 1];
		
		//Initialization for all instance of nodes.
		for (int i = 0; i < l1 + 1; i++) {
			for (int j = 0; j < l2+ 1; j++) {
				for (int k = 0; k < l3 + 1; k++) {
					N[i][j][k] = new Node();
				}
			}
		}
		
		//For each element in array a.
		for (int i = 1; i < l1 + 1; i++) {
			
			//For each element in array b.
			for (int j = 1; j< l2 + 1; j++) {
				
				//For each element in array c.
				for (int k = 1; k < l3 + 1; k++) {
					
					//The heart of solution.
					//If the three elements are same, the number of longest sub-sequence adds one.
					//Reference N[i - 1][j - 1][k - 1] as the front reference. 
					if (a[i - 1] == b[j - 1] && b[j - 1] == c[k - 1]) {
						N[i][j][k].val = N[i - 1][j - 1][k - 1].val + 1;
						N[i][j][k].front = N[i - 1][j - 1][k - 1];
						N[i][j][k].ele = a[i - 1];
					}
					// If any of element among three is different, record the longest former number.
					//Reference the node of former longest number as the front reference.
					else if(a[i - 1] != b[j - 1] || a[i - 1] != c[k - 1] ||  b[j - 1] != c[k - 1]) {
						N[i][j][k].front = maxNode(N[i - 1][j][k], N[i][j - 1][k], N[i][j][k - 1]);
						N[i][j][k].val = N[i][j][k].front.val; 
					}
				}
			}
		}
		
		//Create a array to contain the longest sub-sequence.
		int[] arrnew = new int[N[l1][l2][l3].val];
		
		// Trace back the front node to print out the longest sub-sequence.
		Node current = N[l1][l2][l3];
		int index = 0;
		while (true) {		
			//If it is the node that records the position of same element among three common sequences,
			//Store the element into the new array.
			if (current.ele != -1) {
				arrnew[index] = current.ele;
				index++;
			}
			//If it is not the node, trace back to the front node.
			if (current.front != null) {
				current = current.front;
			}
			else break;			
		}
		
		//Print out the length of longest common sub-sequence.
		System.out.println(N[l1][l2][l3].val);
		for (int i = arrnew.length - 1; i >= 0; i--) {
			System.out.print(arrnew[i] + " ");
		}
	}
	/***
	 * This function is used for returning the node among three that has the greatest value.
	 * @param a Node a.
	 * @param b Node b.
	 * @param c Node c.
	 * @return The Node that has greatest value.
	 */
	public static Node maxNode(Node a, Node b, Node c) {
		Node max = a;
		if (max.val < b.val) max = b;
		if (max.val < c.val) max = c;
		return max;	
	}
}
/***
 * The class is created for represent all numbers in the arrays as the Node.
 * @author Zizhun Guo
 *
 */
class Node {
	//The number of current node.
	public int val = 0;
	
	//The flag variable.
	//If it is not the common number, it keeps -1. If it is the common number,
	//it records the value of the common number.
	public int ele = -1;
	public Node next;
	public Node front;
	public Node() {}
}
