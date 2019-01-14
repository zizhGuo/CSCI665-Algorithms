/***
 * Foundation of Algorithms. 
 * HW5
 * Problem 4
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class SpanTree {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			
			// The number of vertices
			int n = scanner.nextInt();
			
			// The number of edges 
			int e = scanner.nextInt();
			
			// Allocate a Graph instance
			WeightedGraph weightedGraph = new WeightedGraph(n, e);
			
			int size = e;
			int offset = 0;	
			while (offset < size) {
				
				// Get the one vertex of the current edge
				int u = scanner.nextInt() - 1;
				
				// Get another vertex of the current edge
				int v = scanner.nextInt() - 1;
				
				// Get the weight of the current edge
				int w = scanner.nextInt();
				
				// Get the boolean flag if the current edge is included in F chain
				int x = scanner.nextInt();
				
				// Add the Weighed edge to the graph
				weightedGraph.addWeighedEdge(u, v, w, offset, x);
				// Add the unweighed edge to the graph
				weightedGraph.addUnweighedEdge(u, v);
				offset++;
			}
			scanner.close();
			
			// Check the graph's connectivity
			if (weightedGraph.checkConnectivity(0)) {						
				weightedGraph.sortEdges();
				
				// If the graph is a connected single graph, then do Union-Find of Kurskal Algorithm
				System.out.print(weightedGraph.kurskal());
			}
			else System.out.println(-1);
			
		}
		catch(Exception e) {
			System.out.println("Exception is " + e);
			e.printStackTrace();
		}
	}
}

/***
 * Edge Class to store edges.
 * @author Zizhun Guo
 *
 */
class Edge{
	int v;
	int u;
	int weight;
	int isInF;
	public Edge() {}
	public Edge(int v, int u, int weight, int isInF) {
		this.v = v;
		this.u = u;
		this.weight = weight;
		this.isInF = isInF;
	}
	
	/**
	 * Get one vertex of the edge
	 * @return vertex v
	 */
	public int either() {
		return v;
	}
	
	/**
	 * Get another vertex of the edge
	 * @param i the chosen vertex
	 * @return vertex u
	 */
	public int other(int i) {
		if (i == v) return u;
		if (i == u) return v;
		return -1;
	}
	
}

/***
 * The Undirected Weighed Graph class
 * @author Zizhun Guo
 *
 */
class WeightedGraph {
	// The final number of vertices
	private final int V;
	
	// The final number of edges
	private final int E;
	
	// The unweighed adjacent linked list
	private LinkedList[] adj;
	
	// The weighed array of Edges
	private Edge[] edges;
	int cost = 0;
	
	// The boss array to represent the index of set that the v vertex belongs to
	int[] boss;
	
	// The size array to represent the number of elements that the boss[v] set has
	int[] size;
	
	// The linked list array represent the set for Union-Find (kurskal())
	private LinkedList[] set;
	
	// Constructor for initializing all instances.
	public WeightedGraph(int V, int E) {
		if (V < 0) 
			throw new IllegalArgumentException("Number of vertices must be nonnegative.");
		this.V = V;
		this.E = E;
		edges = new Edge[E];
		adj = new LinkedList[V];
		set = new LinkedList[V];
		boss = new int[V];
		size = new int[V];
		
		for (int v = 0; v < V; v++) {
			adj[v] = new LinkedList();
			boss[v] = v;
			size[v] = 1;
			set[v] = new LinkedList();
			set[v].append(v);
		}
		for (int e = 0; e < E; e++)
			edges[e] = new Edge();
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
	 * The function used to initiate the edges of graph
	 * @param u
	 * @param v
	 * @param weight
	 * @param offset
	 */
	public void addWeighedEdge(int u, int v, int weight, int offset, int isInF) {
		edges[offset].u = u;
		edges[offset].v = v;
		edges[offset].weight = weight;	
		edges[offset].isInF = isInF;
	}
	/***
	 * Adds the undirected edge v-w to this graph.
	 * @param v one vertex in the edge.
	 * @param w the other vertex in the edge.
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V}} and {@code 0 <= w < V}}
	 */
	public void addUnweighedEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj[v].append(w);
		adj[w].append(v);
	}

	/**
	 * This function works for check whether input graph is a single graph or multiple graph
	 * This function implements a BFS algorithm.
	 * @param s starting vertex of 0
	 * @return true (If it is single); false (if it is multiple graph)  
	 */
	public boolean checkConnectivity(int s) {
		boolean seen[] = new boolean[V];
		int count = 1;
		
		Queue queue = new Queue();
		seen[s] = true;
		queue.enqueue(s);
		
		while (queue.count() != 0) {
			// Dequeue a vertex from queue and print it.
			s = queue.dequeue().data;
			//System.out.print(s + " ");
			
			// Get all adjacent vertices of the dequeued vertex s
			LinkedList neighbors = adj[s];
			
			// For all neighbors of vertex x, enqueue if has never seen.
			for (int v = 0; v < neighbors.count(); v++) {
				int n = neighbors.get(v);
				if (!seen[n]) {
					seen[n] = true;
					count++;
					queue.enqueue(n);
				}
			}
		}
		if (count == V) return true;
		else return false;
	}
	
	/***
	 * This function employs a merge sort algorithm to sort edges in ascending order
	 */
	public void sortEdges() {
		mergeSort(edges, 0, E - 1);
	}
	
	void merge(Edge arr[], int l, int m, int r) 
    { 
        int n1 = m - l + 1; 
        int n2 = r - m; 
  
        Edge L[] = new Edge [n1]; 
        Edge R[] = new Edge [n2]; 
  
        for (int i=0; i<n1; ++i) 
            L[i] = edges[l + i]; 
        for (int j=0; j<n2; ++j) 
            R[j] = edges[m + 1+ j]; 
  
        int i = 0, j = 0; 
        int k = l; 
        while (i < n1 && j < n2) 
        { 
            if (L[i].weight <= R[j].weight) 
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
        	edges[k] = L[i]; 
            i++; 
            k++; 
        } 
  
        while (j < n2) 
        { 
        	edges[k] = R[j]; 
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
    void mergeSort(Edge edges[], int l, int r) 
    { 
        if (l < r) 
        { 
            int m = (l+r)/2; 
            mergeSort(edges, l, m); 
            mergeSort(edges , m+1, r); 
  
            merge(edges, l, m, r); 
        } 
    } 
    
    /**
     * This function is used for finding the boss index for an given vertex
     * @param v
     * @return
     */
    public int find(int v) {
    	return boss[v];
    }
    
    /***
     * This function works for finding the minimal cost for an F-contained MST
     * The algorithm employed in this function is modified by Kurskal Algorithm
     * 		by implementation of Union-Find
     * @return the minimal cost of the F-contain MST
     */
    public int kurskal() {
    	
    	// Allocate an integer weight to record accumulation of each union behavior
    	int weight = 0;
    	
    	// Initialize the F-contain Sets
    	// Traverse all sorted edges
    	for (int e = 0; e < E; e++) {
    		
    		// Get one edge of current edge
    		int either = edges[e].either();
    		
    		// Get another edge of current edge
    		int other = edges[e].other(either);
    		
    		// If the current edge is included in F, 
    		// and the two vertices are from different sets
    		if (edges[e].isInF == 1 && find(either) != find(other)) {
    			
    			// Unify the edges
    			union(either, other);
    			
    			// Add up the current weight to total weight
    			weight += edges[e].weight;
    		}
    		
    		// If the F set contains the cycle, return -1
    		else if (edges[e].isInF == 1 &&  find(either) == find(other)) {
    			return -1;
    		}
    	}
    	
    	// Traverse all rest sorted edges
    	for (int e = 0; e < E; e++) {
    		
    		// Get one edge of current edge
    		int either = edges[e].either();
    		
    		// Get another edge of current edge
    		int other = edges[e].other(either);
    		
    		// If the current edge is not included in F, 
    		// and the two vertices are from different sets 
    		if (edges[e].isInF == 0 && find(either) != find(other)){
    			
    			// Unify the two vertices
    			union(either, other);
    			
    			// Add up the current weight to total weight
    			weight += edges[e].weight;
    		}
    	}
    	return weight;
    }
    
    /**
     * This function works for unifying two sets by updating vertices' boss index,
     * 		and concatenate small size of set to the greater size of set
     * @param u one vertex from the edge
     * @param v another vertex from the edge
     */
    public void union(int u, int v) {
    	
    	// If this size of set boss[u] is greater than the size of set boss[v]
    	if (size[boss[u]] > size[boss[v]]) {
    		
    		// Concatenate two sets and assign it to the greater set
    		set[boss[u]] = set[boss[u]].concatenate(set[boss[u]], set[boss[v]]);
    		
    		// Update the size to the large set
    		size[boss[u]] += size[boss[v]];
    		
    		// for the smaller size of set
    		LinkedList tempSet = set[boss[v]];
    		
    		// foe all its vertices
    		int count = size[boss[v]];
    		for (int i = 0; i < count; i++) {
    			int n = tempSet.get(i);
    			
    			// update their boss index to the greater set's boss index
    			boss[n] = boss[u];
    		}
    	}
    	
    	// Repeat the same Unify function with exchange of u and v
    	else {
    		set[boss[v]] = set[boss[v]].concatenate(set[boss[v]], set[boss[u]]);
    		size[boss[v]] += size[boss[u]];
    		
    		LinkedList tempSet = set[boss[u]];

    		int count = size[boss[u]];		
    		for (int i = 0; i < count; i++) {
    			int n = tempSet.get(i);
    			boss[n] = boss[v];
    		}
    	}
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
 * The LinkedList class
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
	
	/***
	 * Concatenate linked list b to the linked list a
	 * @param a
	 * @param b
	 * @return the union linked list
	 */
	public LinkedList concatenate(LinkedList a, LinkedList b) {
		a.tail.next = b.head;
		a.tail = b.tail;
		return a;
	}
}

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