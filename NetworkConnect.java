/***
 * Foundation of Algorithms. 
 * HW6
 * Problem 3
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class NetworkConnect {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			
			// The number of vertices
			int n = scanner.nextInt();
			
			// The number of edges 
			int e = scanner.nextInt();

			// Set the source vertex
			int s = scanner.nextInt() - 1;
			
			//Set the sink vertex
			int t = scanner.nextInt() - 1;
			
			// Allocate a Graph instance
			Graph graph = new Graph(n, e, s, t);
			
			
			int count = e;
			while (count > 0) {
				int u = scanner.nextInt() - 1;
				int v = scanner.nextInt() - 1;
				int w = scanner.nextInt();
				
				// Add edges to the graph
				graph.addEdge(u, v, w);
				count--;
			}
			scanner.close();
			
			//Edmonds-Karp algorithm to get residual network. 
			graph.edmondsKarp(s, t);
			
			// Get set containing source and set containing sink of S-T cut.
			graph.grouping();
			
			// Lexicographically find the edge.
			graph.connect();
		}
		catch(Exception e) {
			System.out.println("Exception is " + e);
			System.out.println("Exception is " + e.getStackTrace());
			e.printStackTrace();
			
		}
		
	}

}


/***
 * The Graph Class
 * @author Zizhun Guo
 *
 */
class Graph {
	// The number of vertices.
	private final int V;
	
	// The number of edges.
	private final int E;
	
	// The "Source" vertex.
	private final int S;
	
	// The "Sink" vertex.
	private final int T;
	
	// The adjacent array list to represent the graph.
	private LinkedList[] g;
	
	// The adjacent matrix to represent the edges.
	private int[][] edge;
	
	// The part of final residual graph which contains the "Source" vertex.
	private int[] groupSource;
	
	// The part of final residual graph which contains the "Sink" vertex.
	private int[] groupSink;
	
	// The constructor of the graph class.
	public Graph(int V, int E, int S, int T) {
		if (V < 0) 
			throw new IllegalArgumentException("Number of vertices must be nonnegative.");
		this.V = V;
		this.E = E;
		
		this.S = S;
		this.T = T;
		
		g = new LinkedList[V];
		for (int v = 0; v < V; v++) 
			g[v] = new LinkedList();
		
		edge = new int[V][V];
		for (int i = 0; i < V; i++) 
			for (int j = 0; j < V; j++)
				edge[i][j] = 10000;
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
	 * Adds the weighted forward and backward edges on this graph.
	 * All edges are bi-directed, so that to help create the residual graph.  
	 * @param v one vertex in the edge.
	 * @param w the other vertex in the edge.
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V}} and {@code 0 <= w < V}}
	 */
	public void addEdge(int u, int v, int w) {
		validateVertex(u);
		validateVertex(v);
		g[u].append(v);
		g[v].append(u);
		edge[u][v] = w;
		edge[v][u] = 0;
	}
	
	/***
	 * This function is used for printing out the vertices from two set that in lexicographically order.
	 * Estimate running time: O(n^2)  
	 */
	public void connect() {
		int flag = 0;
		for (int i = 0; i < groupSource.length; i++) {
			if (flag == 0) {
				for (int j = 0; j < groupSink.length; j++) {
					if (!isEdge(groupSource[i], groupSink[j])) {
						flag = 1;
						System.out.println((groupSource[i] + 1) + " " + (groupSink[j] + 1));
						break;
					}
				}
			}
		}
	}
	
	/***
	 * This function is used for checking whether the two vertices are connected as the edge
	 * @param v One vertex.
	 * @param w Another vertex.
	 * @return Yes, if they are connected. No, if ther are not.
	 */
	public boolean isEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return (g[v].hasKey(w) && g[w].hasKey(v));
	}
	
	/***
	 * This function is used for getting the two separate set that contains either the "Source"
	 * vertex or the "Sink" vertex.
	 * Use merge sort to sort the vertices in ascending order.
	 */
	public void grouping() {
		groupSource = bfsSource(S);
		mergeSort(groupSource, 0, groupSource.length - 1);
		groupSink = bfsSink(T);
		mergeSort(groupSink, 0, groupSink.length - 1);
		
	}
	
	/***
	 * This function employs BFS search to mark all vertices from the final residual graph that starts at
	 *  the "Sink" vertex.
	 * @param s The starting vertex
	 * @return The array of a vertices' set.
	 */
	public int[] bfsSink(int s) {
		LinkedList list = new LinkedList();
		
		// The boolean array to mark whether vertex v has ever seen or not.
		boolean seen[] = new boolean[V];
		
		// Initialize the value of the starting vertex.
		Queue queue = new Queue();
		seen[s] = true;
		queue.enqueue(s);
		list.append(s);
		while (queue.count() != 0) {
			// Dequeue a vertex from queue and print it.
			s = queue.dequeue().value;

			// Get all adjacent vertices of the dequeued vertex s
			LinkedList neighbors = g[s];
			
			// For all neighbors of vertex x, enqueue if has never seen.
			for (int v = 0; v < neighbors.count(); v++) {
				int n = neighbors.get(v);
				
				// Key algorithm:
				// The search stops at the vertices that has backward edges equaling 0.
				if (!seen[n] && edge[n][s] > 0) {
					seen[n] = true;
					queue.enqueue(n);
					list.append(n);
				}
			}
		}
		
		// Transfer list into array.
		int[] group;
		group = new int[list.count()];
		for (int i = 0; i < list.count(); i++) {
			group[i] = list.get(i);
		}
		return group;
	}
	
	/***
	 * This function employs BFS search to mark all vertices from the final residual graph that starts at
	 *  the "Source" vertex.
	 * @param s The starting vertex
	 * @return The array of a vertices' set.
	 */
	public int[] bfsSource(int s) {
		LinkedList list = new LinkedList();
		
		// The boolean array to mark whether vertex v has ever seen or not.
		boolean seen[] = new boolean[V];
		
		// Initialize the value of the starting vertex.
		Queue queue = new Queue();
		seen[s] = true;
		queue.enqueue(s);
		list.append(s);
		while (queue.count() != 0) {
			// Dequeue a vertex from queue and print it.
			s = queue.dequeue().value;

			// Get all adjacent vertices of the dequeued vertex s
			LinkedList neighbors = g[s];
			
			// For all neighbors of vertex x, enqueue if has never seen.
			for (int v = 0; v < neighbors.count(); v++) {
				int n = neighbors.get(v);
				
				// Key algorithm:
				// The search stops at the vertices that has forward edges equaling 0.
				if (!seen[n] && edge[s][n] > 0) {
					seen[n] = true;
					queue.enqueue(n);
					list.append(n);
				}
			}
		}
		
		// Tranfer list into array.
		int[] group;
		group = new int[list.count()];
		for (int i = 0; i < list.count(); i++) {
			group[i] = list.get(i);
		}
		return group;
	}
	
	public void edmondsKarp(int s, int t) {
		int maxflow;
		int parent[] = new int[V];
		for (int v = 0; v < V; v++) {
			parent[v] = -1;
		}
		
		maxflow = augmentPath(s, t, parent);
		
		while (maxflow != -1) {
			//System.out.print("Hello my dear!");
			residualNetwork(s, t, maxflow, parent);
			for (int v = 0; v < V; v++) {
				parent[v] = -1;
			}
			maxflow = augmentPath(s, t, parent);
		}
	}
	
	/***
	 * This function is used for getting the residual graph each time for Edmonds-Karp Algorithm.
	 * @param s The "Source" vertex.
	 * @param t The "Sink" vertex.
	 * @param maxFlow The max flow of current path.
	 * @param parent The array represents the path.
	 */
	public void residualNetwork(int s, int t, int maxFlow, int[] parent) {
		int index = t;
		while (parent[index] != -1) {
			
			// Forward edges
			edge[parent[index]][index] -= maxFlow;
			
			// Backward edges
			edge[index][parent[index]] += maxFlow;
			
			// Backtrack
			index = parent[index];
		}
	}
	
	/***
	 * This function employs BFS to find a augment path and return the max flow under this path. 
	 * @param s The "Source" vertex.
	 * @param t The "Sink" vertex.
	 * @param parent The array to store the path
	 * @return the max flow
	 */
	public int augmentPath(int s, int t, int[] parent) {
		
		boolean flag = false;
		boolean seen[] = new boolean[V];
		int dist[] = new int[V];
		for (int v = 0; v < V; v++) {
			dist[v] = 100000;
		}
		
		Queue queue = new Queue();
		seen[s] = true;
		dist[s] = 0;
		queue.enqueue(s);
		
		while (queue.count() != 0) {
			s = queue.dequeue().value;
			
			// It ends when path reaches the "Sink" vertex.
			if (s == t) {
				flag = true;
				break;
			}
			// Get all adjacent vertices of the dequeued vertex s
			LinkedList neighbors = g[s];
			
			for (int v = 0; v < neighbors.count(); v++) {
				int n = neighbors.get(v);
				
				// Key point:
				// The forward edge must have the positive weights.
				if (!seen[n] && edge[s][n] > 0) {
					seen[n] = true;
					queue.enqueue(n);
					
					// Store the path into the array.
					parent[n] = s;
				}
			}
		}
		
		// Getting the max flow from the augment path.
		if (flag) {
			int index = t;
			int maxFlow = 10000;
			while (parent[index] != -1) {
				
				if (maxFlow > edge[parent[index]][index]) {
					maxFlow = edge[parent[index]][index];
				}
				index = parent[index];
			}
			return maxFlow;
		}
		else return -1;
	}

	/***
	 * This function works for merging sort.
	 * @param arr
	 * @param l
	 * @param m
	 * @param r
	 */
	void merge(int arr[], int l, int m, int r) 
    { 
        int n1 = m - l + 1; 
        int n2 = r - m; 
  
        int L[] = new int [n1]; 
        int R[] = new int [n2]; 
  
        for (int i=0; i<n1; ++i) 
            L[i] = arr[l + i]; 
        for (int j=0; j<n2; ++j) 
            R[j] = arr[m + 1+ j]; 
  
        int i = 0, j = 0; 
        int k = l; 
        while (i < n1 && j < n2) 
        { 
            if (L[i] <= R[j]) 
            { 
                arr[k] = L[i]; 
                i++; 
            } 
            else
            { 
                arr[k] = R[j]; 
                j++; 
            } 
            k++; 
        } 
  
        while (i < n1) 
        { 
        	arr[k] = L[i]; 
            i++; 
            k++; 
        } 
  
        while (j < n2) 
        { 
        	arr[k] = R[j]; 
            j++; 
            k++; 
        } 
    } 
  
    /**
     * This function works for merging sort an array of type Edge in increasing order 
     * @param edges The array of Edge instances
     * @param l starting index
     * @param r ending index
     */
    void mergeSort(int group[], int l, int r) 
    { 
        if (l < r) 
        { 
            int m = (l+r)/2; 
            mergeSort(group, l, m); 
            mergeSort(group , m+1, r); 
  
            merge(group, l, m, r); 
        } 
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
* The class for queue in LinkedList implementation
* @author Zizhun Guo
*
*/
class Queue {
	Node front, rear;
	public Queue() {
		this.front =  null;
		this.rear = null;
	}
	
	public void enqueue(int key) {
		//Create a new node.
		Node temp = new Node(key);
		
		if (rear == null) {
			rear = temp;
			front = temp;
			return;
		}
		rear.next = temp;
		rear = temp;
	}
	public Node dequeue() {
		if (front == null) return null;
		Node temp = front;
		front = front.next;
		if (front == null) rear = null;
		return temp;
	}
	public void printList() {
		Node current = front;
		if (current == null) {
			System.out.println("Null LinkedList!");
			return;
		}
		while(current != null) {
			System.out.println(current.value);
			current = current.next;		
		}
	}
	
	/**
	 * Returns count of of nodes in linked list.
	 * @return
	 */
	public int count() {
		Node temp = front;
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
}
/***
 * LinkedList class
 * @author Zizhun Guo
 *
 */
class LinkedList {
	Node head;
	Node next;
	Node tail;
	
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
			tail = new_node;
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
		//last.next = new_node;
		tail = new_node;
		return;
	}
	/**
	 * Returns count of of nodes in linked list.
	 * @return
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
	 * Take index as an argument and return the data if index-th element exists.
	 * @param index
	 * @return
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
		//assert(false);
		return -1;
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
	
	public void printList() {
		Node current = head;
		if (current == null) {
			System.out.println("Null LinkedList!");
			return;
		}
		while(current != null) {
			System.out.println(current.value + " ");
			current = current.next;		
		}
	}
}

