/***
 * Foundation of Algorithms. 
 * HW6
 * Problem 1
 * @author Zizhun Guo
 */
import java.util.Scanner;

public class NegativeCycle {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			
			// To mark if there has a negative cycle
			boolean flag = false;
			
			// To mark if it could exit the iteration beforehand
			int check;
			
			// The number of vertices
			int V = scanner.nextInt();
			
			// The number of edges 
			int E = scanner.nextInt();
		
			int[] u = new int[E];
			int[] v = new int[E];
			int[] w = new int[E];
			int[] dist = new int[V];
				
			for (int e = 0; e < E; e++) {
				u[e] = scanner.nextInt();
				v[e] = scanner.nextInt();
				w[e] = scanner.nextInt();
			}
			scanner.close();
			
			// Initialize the distance array
			for (int i = 0; i < V; i++)
				dist[i] = 10000;
			dist[0] = 0;
			
		    for (int j = 0; j < V-1; ++j)
		    {
		        // A flag variable to mark if dist[] array gets updated
		    	check = 0;
		        
		    	// For all edges in the graph, traverse to see whether distance[] needs update.
		    	// Bellman - Ford Algorithm.
		    	for (int i = 0; i < E; i++)       
		        {
		            if (dist[v[i] - 1] > dist[u[i] - 1] + w[i])
		            {
		                dist[v[i]- 1] = dist[u[i] - 1] + w[i];
		                
		                // Mark each iteration.
		                check = 1;
		            }
		        }
		        // Check if dist[] array gets updated
		        if (check==0)     
		        {
		        	// break if there's no update
		        	break; 
		        }
		    }
		    
		    // Check whether there still has updates, if it has, it proves there exists a negative cycle.
		    for (int i = 0; i < E; i++) {
		    	if (dist[v[i] - 1] > dist[u[i] - 1] + w[i])
		        {
		            flag = true;
		        }
		    }
		    
		    if (flag) System.out.print("YES");
		    else System.out.print("NO");
		}
		catch(Exception e) {
			System.out.println("Exception is " + e);
			e.printStackTrace();
		}
	}
}
