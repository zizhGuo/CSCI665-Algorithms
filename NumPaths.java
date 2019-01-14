/***
 * Foundation of Algorithms. 
 * HW5
 * Problem 1
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class NumPaths {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			
			// The number of vertices
			int n = scanner.nextInt();
			
			// The number of edges 
			int e = scanner.nextInt();
			
			// Allocate a Graph instance
			Graph graph = new Graph(n, e);
			
			// Set the start vertex
			int s = scanner.nextInt();
			
			//Set the end vertex
			int t = scanner.nextInt();
			
			int count = e;
			while (count > 0) {
				// Add edges to the graph
				graph.addEdge(scanner.nextInt() - 1, scanner.nextInt() - 1);
				count--;
			}
			scanner.close();
			
			// Using BFS print out the number of shortest path
			graph.bfs(s - 1, t - 1);
			
		}
		catch(Exception e) {
			System.out.println("Exception is " + e);
		}
		
	}

}


/***
 * The Graph Class
 * @author Zizhun Guo
 *
 */
class Graph {
	private final int V;
	private int E;
	private LinkedList[] adj;
	
	public Graph(int V, int E) {
		if (V < 0) 
			throw new IllegalArgumentException("Number of vertices must be nonnegative.");
		this.V = V;
		this.E = E;
		adj = new LinkedList[V];
		for (int v = 0; v < V; v++) 
			adj[v] = new LinkedList();
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
	 * Adds the undirected edge v-w to this graph.
	 * @param v one vertex in the edge.
	 * @param w the other vertex in the edge.
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V}} and {@code 0 <= w < V}}
	 */
	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		E++;
		adj[v].append(w);
		adj[w].append(v);
	}
	
	/***
	 * Show the vertex v's edges.
	 * @param v
	 * @return
	 */
	public String showEdge(int v) {
		validateVertex(v);
		return adj[v].toString();
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
	
	public void bfs(int s, int t) {
		
		// The boolean array to mark whether vertex v has ever seen or not.
		boolean seen[] = new boolean[V];
		int dist[] = new int[V];
		for (int v = 0; v < V; v++) {
			dist[v] = 100000;
		}
		// The integer array to record the number of shortest path for each vertex.
		int path[] = new int[V];
		for (int v = 0; v < V; v++) {
			path[v] = 0;
		}
		
		// Initialize the value of the starting vertex.
		Queue queue = new Queue();
		seen[s] = true;
		dist[s] = 0;
		path[s] = 1;
		queue.enqueue(s);
		while (queue.count() != 0) {
			// Dequeue a vertex from queue and print it.
			s = queue.dequeue().data;
			
			// Get all adjacent vertices of the dequeued vertex s
			LinkedList neighbors = adj[s];
			
			// For all neighbors of vertex x, enqueue if has never seen.
			for (int v = 0; v < neighbors.count(); v++) {
				int n = neighbors.get(v);
				if (!seen[n]) {
					seen[n] = true;
					queue.enqueue(n);
				}
				
				// If the neighbor's distance has not been assigned,
				// Assign the current vertex distance + 1
				// and initialize the neighbor's number of path with its own distance. 
				if (dist[n] > dist[s] + 1) {
					dist[n] = dist[s] + 1;
					path[n] = path[s];
				}
				
				// If the neighbor is the child of current vertex that has been set distance value.
				// Accumulate the neighbor's NSP with current NSP, and pass it to neighbor.
				else if (dist[n] == dist[s] + 1){
						path[n] += path[s];
				}
			}
		}
		
		// Print the number of shortest path.
		System.out.println(path[t]);
	}
}

/***
 * A node class represents each element of LinkedList.
 * @author Zizhun Guo
 *
 */
class Node {
	int data;
	Node next;
	Node(int data){
		this.data = data;
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
	
	/***
	 *  Enqueue the node with value of key into the queue
	 * @param key
	 */
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
	
	/***
	 * Dequeue the node with value of key out the queue
	 * @return value of node
	 */
	public Node dequeue() {
		if (front == null) return null;
		Node temp = front;
		front = front.next;
		if (front == null) rear = null;
		return temp;
	}
	
	/**
	 * Returns count of of nodes in linked list.
	 * @return count
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
	 * Take index as an argument and return the data if index-th element exists.
	 * @param index
	 * @return data 
	 */
	public int get(int index) {
		Node current = head;
		int count = 0;
		
		while (current != null) {
			if (count == index)	
				return current.data;
			count++;
			current = current.next;
		}
		assert(false);
		return -1;
	}
}
