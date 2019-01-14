/***
 * Foundation of Algorithms. 
 * HW5
 * Problem 3
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class DoubleTrouble {
	public static void main(String[] args) {
		try {
			// Initialization for the states
			Scanner scanner = new Scanner(System.in);
			int r = scanner.nextInt();
			int c = scanner.nextInt();
			
			State state = new State(r, c);
			
			/***
			 *  Transfer the String type matrix into Integer type
			 *  @code state.config: set up the configuration of environment
			 *  @code state.setThing1: set position of Thing 1
			 *  @code state.setThing1: set position of Thing 1
			 */
			for (int i = 0; i < r; i++) {
				String[] input = scanner.next().split("");
				for (int j = 0; j < c; j++) {
					switch (input[j]) {
					case ".":
						state.config(i, j, 0);
						break;
					case "x":
						state.config(i, j, 1);
						break;
					case "1":
						state.config(i, j, 1);
						state.setThing1(i, j);
						break;
					case "2":
						state.config(i, j, 1);
						state.setThing2(i, j);
						break;
					}
				}
			}
			scanner.close();
			
			/**
			 * This function used a BFS algorithm to print the shortest steps
			 * for two Things to get out the room
			 */
			bfs(state);
		}
		catch(Exception e) {
			System.out.print(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * The function implemented by Breadth First Search to print the shortest steps
	 *  for the two Things to get out of the room
	 * @param state
	 * @return 
	 */
	public static int bfs(State state) {
		// the starting state
		State current = state;		
		
		// A boolean four-dimensional array to represent whether the state is seen or not
		boolean[][][][] seen = new boolean[current.row][current.column][current.row][current.column];
		
		// Initialize the starting state as being visited
		seen[state.x1][state.y1][state.x2][state.y2] = true;
		
		// An integer four-dimensional array to represent the depth of BFS algorithm. 
		int[][][][] steps = new int[current.row][current.column][current.row][current.column];
		
		// Initialization for the steps array.
		for (int v = 0; v < current.row; v++) {
			for (int i = 0; i < current.column; i++) {
				for (int j = 0; j < current.row; j++) {
					for (int k = 0; k < current.column; k++) {
						steps[v][i][j][k] = 1;
					}
				}
			}
		}
		
		// Initialize a Queue for BFS algorithm.
		SQueue squeue = new SQueue();
		
		// Enqueue the starting state into the queue
		squeue.enqueue(current);
		
		// Traverse all statements in the order of queue format
		while (squeue.count() != 0) {
			
			// Dequeue a state from queue
			state = squeue.dequeue();
			
			// Get all possible neighbor states of the current state.
			SLinkedList neighbors = state.getNeighborStates();

			// For all neighbors of state s, enqueue if it is never seen.
			for (int i = 0; i < neighbors.count(); i++) {
				State s = neighbors.get(i);
				
				/**
				 * Check the neighbor is visited or not.
				 * Assign the corresponding depth of neighbor with its steps
				 * Mark it as seen, so only allowed each state to be visited once.
				 */
				if (!seen[s.x1][s.y1][s.x2][s.y2]) {
					seen[s.x1][s.y1][s.x2][s.y2] = true;
					squeue.enqueue(s);
					steps[s.x1][s.y1][s.x2][s.y2] = steps[state.x1][state.y1][state.x2][state.y2] + 1;
				}
				
				/**
				 * Check if the BFS traverse is end or not.
				 * Print "STUCK" if there no way out.
				 */
				if (s.checkExit()) {
					
					// If the two Things get out the room, print its the steps.
					System.out.print(steps[s.x1][s.y1][s.x2][s.y2]);
					return 0;
				}
			}
		}
		
		// It the BFS traverse ends with no results, then print "STUCK"
		System.out.println("STUCK");
			return 0;
		}
	
}

/***
 * The State Class represents each state of the game.
 * @author Zizhun, Guo
 *
 */
class State{
	int x1;
	int y1;
	int x2;
	int y2;
	
	// Maximum length of the row.
	int row;
	
	// Maximum length of the column.
	int column;
	
	// Location matrix represents the obstacles and available positions.
	int location[][];
	
	// The next state help to build the linked list. 
	State next;
	public State() {}
	
	// Construction to build up with coordinates of two Things.
	public State(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.next = null;
	}
	
	public State(int r, int c) {
		row = r;
		column = c;		
		this.location = new int[r][c];
	}
	public State(State state) {
		this.x1 = state.x1;
		this.y1 = state.y1;
		this.x2 = state.x2;
		this.y2 = state.y2;
		this.next = null;
		this.row = state.row;
		this.column = state.column;		
		this.location = new int[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				this.config(i, j, state.location[i][j]);
			}
		}
		
	}
	
	/*
	 * This function is used for configuring the map of state.
	 */
	public void config(int i, int j, int val) {
		this.location[i][j] = val;
	}
	
	/*
	 * This function is use for setting up the position of Thing1.
	 */
	public void setThing1(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;
	}
	
	/*
	 * This function is use for setting up the position of Thing2.
	 */
	public void setThing2(int x2, int y2) {
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/***
	 * This function is use for getting all possible next states by building
	 * them dynamically.
	 * @return A linked array of possible neighbor states
	 */
	public SLinkedList getNeighborStates() {
		SLinkedList neighbers = new SLinkedList();
		
		// Go WEST
		if (x1 - 1 >= 0 && x2 - 1 >= 0) {
			
			// Thing 1 is non-movable. Thing 2 is Movable.
			if (location[x1 - 1][y1] == 1 && location[x2 - 1][y2] == 0) {
				State temp = new State(this);
				temp.location[x2][y2] = 0;
				temp.location[x2 - 1][y2] = 1;
				temp.x2--;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is non-Movable.
			else if (location[x1 - 1][y1] == 0 && location[x2 - 1][y2] == 1) {
				State temp = new State(this);
				temp.location[x1][y1] = 0;
				temp.location[x1 - 1][y1] = 1;
				temp.x1--;
				neighbers.append(temp);
			}
			
			//Thing 1 is movable. Thing 2 is Movable.
			else if (location[x1 - 1][y1] == 0 && location[x2 - 1][y2] == 0) {
				State temp = new State(this);
				temp.location[x1 - 1][y1] = 1;
				temp.location[x1][y1] = 0;
				temp.location[x2 - 1][y2] = 1;
				temp.location[x2][y2] = 0;
				temp.x1--;
				temp.x2--;
				neighbers.append(temp);
			}
		}
		
		// GO EAST
		if (x1 + 1 <= (row - 1) && x2 + 1 <= (row - 1)) {
			
			// Thing 1 is non-movable. Thing 2 is Movable.
			if (location[x1 + 1][y1] == 1 && location[x2 + 1][y2] == 0) {
				State temp = new State(this);
				temp.location[x2][y2] = 0;
				temp.location[x2 + 1][y2] = 1;
				temp.x2++;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is non-Movable.
			else if (location[x1 + 1][y1] == 0 && location[x2 + 1][y2] == 1) {
				State temp = new State(this);
				temp.location[x1][y1] = 0;
				temp.location[x1 + 1][y1] = 1;
				temp.x1++;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is Movable.
			else if (location[x1 + 1][y1] == 0 && location[x2 + 1][y2] == 0) {
				State temp = new State(this);
				temp.location[x1 + 1][y1] = 1;
				temp.location[x1][y1] = 0;
				temp.location[x2 + 1][y2] = 1;
				temp.location[x2][y2] = 0;
				temp.x1++;
				temp.x2++;
				neighbers.append(temp);
			}
		}
		
		// GO NORTH
		if (y1 - 1 >= 0 && y2 - 1 >= 0) {
			
			// Thing 1 is non-movable. Thing 2 is Movable.
			if (location[x1][y1 - 1] == 1 && location[x2][y2 - 1] == 0) {
				State temp = new State(this);
				
				temp.location[x2][y2 - 1] = 1;
				temp.location[x2][y2] = 0;
				temp.y2--;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is non-Movable.
			else if (location[x1][y1 - 1] == 0 && location[x2][y2 - 1] == 1) {
				State temp = new State(this);
				
				temp.location[x1][y1 - 1] = 1;
				temp.location[x1][y1] = 0;
				temp.y1--;
				
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is Movable.
			else if (location[x1][y1 - 1] == 0 && location[x2][y2 - 1] == 0) {
				State temp = new State(this);
				
				temp.location[x1][y1 - 1] = 1;
				temp.location[x1][y1] = 0;
				temp.location[x2][y2 - 1] = 1;
				temp.location[x2][y2] = 0;
				temp.y1--;
				temp.y2--;
				
				neighbers.append(temp);
			}
		}
		
		// GO SOUTH
		if (y1 + 1 <= (column - 1) && y2 + 1 <= (column - 1)) {
			
			// Thing 1 is non-movable. Thing 2 is Movable.
			if (location[x1][y1 + 1] == 1 && location[x2][y2 + 1] == 0) {
				State temp = new State(this);
				temp.location[x2][y2 + 1] = 1;
				temp.location[x2][y2] = 0;
				temp.y2++;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is non-Movable.
			else if (location[x1][y1 + 1] == 0 && location[x2][y2 + 1] == 1) {
				State temp = new State(this);
				temp.location[x1][y1 + 1] = 1;
				temp.location[x1][y1] = 0;
				temp.y1++;
				neighbers.append(temp);
			}
			
			// Thing 1 is movable. Thing 2 is Movable.
			else if (location[x1][y1 + 1] == 0 && location[x2][y2 + 1] == 0) {
				State temp = new State(this);
				temp.location[x1][y1 + 1] = 1;
				temp.location[x1][y1] = 0;
				temp.location[x2][y2 + 1] = 1;
				temp.location[x2][y2] = 0;
				temp.y1++;
				temp.y2++;
				neighbers.append(temp);
			}
		}
		return neighbers;
	}
	
	/**
	 * This function works for determining whether both Things are exited or not.
	 * If both Things are on the either borders of the environment, 
	 * they are considered to be exited.
	 * @return True if they exited, false if they are not.
	 */
	public boolean checkExit() {
		if (x1 == 0 && x2 == 0) return true;
		if (x1 == (row - 1) && x2 == (row - 1)) return true;
		if (y1 == 0 && y2 == 0) return true;
		if (y1 == (column - 1) && y2 == (column - 1)) return true;
		return false;
	}
}


/***
* The Queue Class works for the BFS algorithm.
* It is implemented by the format of linked list.
* @author Zizhun Guo
*
*/
class SQueue {
	State front, rear;
	public SQueue() {
		this.front =  null;
		this.rear = null;
	}
	
	/**
	 * Enqueue the state.
	 * @param state
	 */
	public void enqueue(State state) {
		
		int r = state.row;
		int c = state.column;
		State temp = new State(r, c);
		temp.setThing1(state.x1, state.y1);
		temp.setThing2(state.x2, state.y2);
		
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				temp.config(i, j, state.location[i][j]);
			}
		}
		
		if (rear == null) {
			rear = temp;
			front = temp;
			return;
		}
		rear.next = temp;
		rear = temp;
	}
	
	/*
	 * Dequeue the state.
	 */
	public State dequeue() {
		if (front == null) return null;
		State temp = front;
		front = front.next;
		if (front == null) rear = null;
		return temp;
	}
	
	/**
	 * Returns count of of states in linked list.
	 * @return
	 */
	public int count() {
		State temp = front;
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
}

/***
 * LinkedList Class
 * @author Zizhun Guo
 *
 */
class SLinkedList {
	State head;
	State next;
	State tail;
	
	public SLinkedList() {}
	
	/***
	 * Append the state at the tail of the linked list
	 * @param new_data
	 */
	public void append(State state) {

		int r = state.row;
		int c = state.column;
		State new_node = new State(r, c);
		new_node.setThing1(state.x1, state.y1);
		new_node.setThing2(state.x2, state.y2);
		
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				new_node.config(i, j, state.location[i][j]);
			}
		}
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
		State last = head;
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
		State temp = head;
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
	
	
	/***
	 * Take index as an argument and return the state if index-th element exists.
	 * @param index
	 * @return
	 */
	public State get(int index) {
		State current = head;
		int count = 0;
		
		while (current != null) {
			if (count == index)	
				return current;
			count++;
			current = current.next;
		}
		assert(false);
		return null;
	}
	
	public String toString() {
		String str = new String();
		State current = head;
		if (current == null) {
			str += "Null LinkedList!";
			return str;
		}
		while(current != null) {
			current = current.next;		
		}
		return str;
	}
}
