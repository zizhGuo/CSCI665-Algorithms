/***
 * Foundation of Algorithms. 
 * HW6
 * Problem 0
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class Redirect{

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			
			// The number of vertices
			int n = scanner.nextInt();
			int count = 0;
			
			// Create the directed graph
			DGraph dgraph = new DGraph(n);
			
			// Loop for receiving inputs
			while (scanner.hasNext()) {
				// The next input
				int a = scanner.nextInt();
				
				// If it equals to 0, jump to add edge for the next vertex
				if (a == 0) {
					count++;
				}
				else {
					// Add edge for current count-th vertex
					dgraph.addEdge(count, (a-1));
				}
				
				// If count reaches to end, end the loop
				if (count == n) break;
			}
				
			scanner.close();

			// Topological Sort
			dgraph.TopSort();
			
			// Dynamically calculate the maximum number of cost
			dgraph.getSCC();
			
			// Redirect
			dgraph.redirect();
			}
		catch(Exception e) {
			System.out.println("Exception is " + e);
		}
		
	}
}

/***
 * A directed graph Class
 * @author Zizhun Guo
 *
 */
class DGraph {
	// The number of vertices
	private final int V;
	
	// The original directed graph (dir: prerequisite -> current course)
	private LinkedList[] adj;
	
	// The reversal directed graph (dir: current course -> prerequisite)
	private LinkedList[] adj_rev;
	
	// The stack storing TopSort result
	private Stack topStack = new Stack();
	
	int[] boss;
	
	int numOfSCC = 0;
	
	/***
	 * The directed graph constructor
	 * @param V The number of vertices
	 */
	public DGraph(int V) {
		if (V < 0) 
			throw new IllegalArgumentException("Number of vertices must be nonnegative.");
		this.V = V;
		adj = new LinkedList[V];
		adj_rev = new LinkedList[V];
		boss = new int[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new LinkedList();
			adj_rev[v] = new LinkedList();
			
		}
	}
	/***
	 * Throw an IllegalArgumentException unless {@code 0 <= v < V}
	 * @param v The vertex
	 */
	private void validateVertex(int v) {
		if (v < 0 || v > V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}
	/***
	 * Adds the edge both in normal and reversal order
	 * @param v starting vertex / ending vertex.
	 * @param w ending vertex / starting vertex.
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V}} and {@code 0 <= w < V}}
	 */
	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj[v].append(w);
		adj_rev[w].append(v);
		
	}
	
	/***
	 * Show the vertex v's edges.
	 * @param v
	 * @return
	 */
	public String showEdge(int v) {
		validateVertex(v);
		return adj[v].toString();
		//return adj_rev[v].toString();
	}
	
	/***
	 * Show the vertex v's degree.
	 * @param v
	 * @return
	 */
	public int degree(int v) {
		validateVertex(v);
		return adj[v].count();
	}
	
	/***
	 * Compare size of two integers
	 * @param a Integer a
	 * @param b Integer b
	 * @return the greater integers among a and b
	 */
	public int maxVal(int a, int b) {
		int max = a;
		if (a < b) max = b;
		return max;
	}
	
	/***
	 * This function is used for getting the two vertices that are from two SCC. 
	 */
	public void redirect() {
		
		// Number of SCC
		int count = 0;
		
		
		LinkedList groupA = new LinkedList();
		LinkedList groupB = new LinkedList();
		
		// Get the number of SCC
		for (int i = 0; i < boss.length; i++) {
			//System.out.println("boss = " + boss[i]);
			if (count < (boss[i] + 1))
				count = (boss[i] + 1);
		}
		
		// If there are 2 SCCin the original graph.
		if (count == 2) {
			
			boolean flag = false;
			
			// Check if there are edges that has u from one SCC and v from other SCC
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < adj[i].count(); j++) {
					
					if (boss[i] > boss[adj[i].get(j)]) {
						groupA.append(i);
					}
					if (boss[i] < boss[adj[i].get(j)]) {
						groupB.append(i);
					}
				}
			}
			
			// For each group, if it contains more than two edges starting vertices, then get the first vertex.
			if (groupA.count() >= 2) {
				for (int i = 0; i < groupA.count(); i++) {
					for (int j = 0; j < adj[groupA.get(i)].count(); j++) {
						if (boss[adj[groupA.get(i)].get(j)] != boss[groupA.get(i)] && !flag) {
							flag = true;
							System.out.println("YES");
							System.out.println((groupA.get(i) + 1) + " " + (adj[groupA.get(i)].get(j) + 1));
						}
					}
				}
			}
			if (groupB.count() >= 2) {
				for (int i = 0; i < groupB.count(); i++) {
					for (int j = 0; j < adj[groupB.get(i)].count(); j++) {
						if (boss[adj[groupB.get(i)].get(j)] != boss[groupB.get(i)] && !flag) {
							flag = true;
							System.out.println("YES");
							System.out.println((adj[groupB.get(i)].get(j) + 1) + " " + (groupB.get(i) + 1));
						}
					}
				}
			}
			if (!flag) {
				System.out.println("NO");
			}
			
		}
		else System.out.print("NO");
		
	}
	
	public void getSCC() {
		
		boolean[] seen = new boolean[V];
		
		// Initialization for seen array
		for (int v = 0; v < V; v++) 
			seen[v] = false;		
		
		// Pop out the stack that has elements in the order of top sort result
		while (topStack.count() != 0) {
			
			// traverse all vertices in decreasing order in reversed graph.
			int v = topStack.pop().value - 1;
			
			
			if (!seen[v]) {
				dfs_SCC(v, seen, boss, numOfSCC);
				boss[v] = numOfSCC;
				
				// The vertex that has never been visited, it belongs to another SCC.
				numOfSCC++;
			}
		}
	}
	
	/***
	 * The DFS for each SCC
	 * @param s
	 * @param seen
	 * @param boss
	 * @param num
	 */
	public void dfs_SCC(int s, boolean[] seen, int[] boss, int num){
		// If seen, marked it as true
		seen[s] = true;
		
		//Get all neighbors of current vertex in reversed graph
		LinkedList neighbors = adj_rev[s];
		
		// For each neighbor of current vertex, if not seen, doing DFS till the end
		for (int v = 0; v < neighbors.count(); v++) {
			int n = neighbors.get(v);
			if (!seen[n]) {
				boss[n] = num;
				dfs_SCC(n, seen, boss, num);
			}
		}
	}
	
	/***
	 * This function is used for topological sorting
	 * This algorithm require the implementation of DFS
	 */
	public void TopSort() {
		// Allocate an boolean array to represent the visited status for each vertex
		boolean[] seen = new boolean[V];
		
		// Initialization for seen array
		for (int v = 0; v < V; v++) 
			seen[v] = false;
		
		// Running DFS recursively
		for (int v = 0; v < V; v++) {
			if (!seen[v]) 
				dfs_Rec(v, seen, topStack);
		}

	}
	
	/***
	 * This function works for DFS.
	 * @param s The current vertex
	 * @param seen The vertices' visited status
	 * @param stack The global stack to store the DFS vertices in the order of TopSort
	 */
	public void dfs_Rec(int s, boolean[] seen, Stack stack){
		// If seen, marked it as true
		seen[s] = true;
		
		//Get all neighbors of current vertex
		LinkedList neighbors = adj[s];
		
		// For each neighbor of current vertex, if not seen, doing DFS till the end
		for (int v = 0; v < neighbors.count(); v++) {
			int n = neighbors.get(v);
			if (!seen[n]) {
				dfs_Rec(n, seen, stack);
			}
		}
		
		// Heart of TopSort
		// Push the vertex into the stack
		// The ending vertex would be first out.
		stack.push((s + 1));
		//System.out.println("Stack: " + (s+1));

	}
}

/***
 * The Stack class used to store the TopSort
 * @author Zizhun Guo
 *
 */
class Stack {
	Node top;
	
	public Stack(){
		this.top = null;
	}
	
	/***
	 * Push the node with key value into the stack
	 * @param key The value of the node
	 */
	public void push(int key) {
		Node current = new Node(key);	
		current.next = top;
		top = current;
	}
	
	/***
	 * Pop the node on the top
	 * @return the node at the top position
	 */
	public Node pop() {
		if (top != null) {
			Node temp = top;
			top = top.next;
			return temp;
		}
		return null;
	}
	
	/***
	 * The size of the stack
	 * @return the size in integer
	 */
	public int count() {
		Node temp = top;
		int count = 0;
		
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
}

/***
 * A node class represents each element of LinkedList.
 * @author Zizhun Guo
 *
 */
class Node {
	int value;
	Node next;
	Node(int value){
		this.value = value;
		this.next = null;
	}
}

/***
 * The LinkedList class
 * @author Zizhun Guo
 *
 */
class LinkedList {
	Node head;
	Node next;
	
	public LinkedList() {}
	
	/***
	 * Append the node with value of new_data at the tail of the linked list
	 * @param new_data
	 */
	public void append(int new_data) {

		/*
		 * Allocate the node,
		 * Put in the data,
		 * Set next as null.
		 */
		Node new_node = new Node(new_data);
		new_node.next = null;
		
		/*
		 * Check the Linked List,
		 * if it is empty,
		 * then make a new node as the head.
		 */
		if (head == null) {
			head = new_node;
			return;
		}
		
		/*
		 * Traverse till the last node.
		 */
		Node last = head;
		while(last.next != null) last = last.next;
		
		/*
		 * Append the node to the next of last node.
		 */
		last.next = new_node;
		return;
	}
	/**
	 * Returns count of of nodes in linked list.
	 * @return count
	 */
	public int count() {
		Node temp = head;
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
	
	/***
	 * Check whether the key is present in linked list.
	 * @param key
	 * @return
	 */
	public boolean hasKey(int key) {
		Node current = head;
		while (current != null) {
			if (current.value == key) 
				return true;
			current = current.next;
		}
		return false;	
	}
	
	public String toString() {
	String str = new String();
	Node current = head;
	if (current == null) {
		str += "Null LinkedList!";
		return str;
	}
	while(current != null) {
		str += Integer.toString(current.value + 1) + "  ";
		current = current.next;		
	}
	return str;
}
	
	/***
	 * Take index as an argument and return the data if index-th element exists.
	 * @param index
	 * @return data 
	 */
	public int get(int index) {
		Node current = head;
		int count = 0;
		
		while (current != null) {
			if (count == index)	
				return current.value;
			count++;
			current = current.next;
		}
		assert(false);
		return -1;
	}
	
}

