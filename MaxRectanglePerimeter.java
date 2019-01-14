/***CSCI 665 Algorithms Homework 2
 * @author Guo, Zizhun
 * @Question 1
 * @version 1.8
 * 
 */
import java.util.Scanner;

public class MaxRectanglePerimeter {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();		
		Point[] points = new Point[n + 1];		
		
		/*** 
		 * Create an array of Points, and add 1 extra point at the end for later iteration.
		 */				
		int pointNumber = n + 1;
		for (int i = 0; i < pointNumber - 1; i++) {
			
			int x = scanner.nextInt();
			int y = scanner.nextInt();	
			points[i] = new Point(x, y);	
		}
		points[pointNumber - 1] = new Point(points[pointNumber - 2].x + 1, points[pointNumber - 2].y);
		scanner.close();

		/*** 
		 * Create an array of all Heights, and store each height into the array.
		 */
		int heightNumber = n / 2 + 1;
		int[] heights = new int[heightNumber];
		for (int i = 0; i < heightNumber; i ++) {
			if (i != heightNumber - 1) heights[i] = points[i * 2].y;
			else heights[i] = 0;	
		}
		
		/*** 
		 * Create a array to store all possible rectangle's perimeter.
		 */
		int[] permiters = new int[heightNumber - 1];
		for (int i = 0; i < heights.length - 1; i++) {			
			
			//Create a front arrow num to represent the left edge of the current rectangle.
			int front = frontIndex(i, heights, heights[i]);
			
			//Create a back arrow num to represent the right edge of the current rectangle.
			int back = backIndex(i, heights, heights[i]);
			
			// Use front index and back index and the current heights to calculate the current rectangle's perimeter. 
			permiters[i] = perimeter(front, back, heights[i], points);
			
		}
		System.out.println(maxPerimeter(permiters));
	}
	/***
	 * This function works for getting the index of the first lower heights on the left.
	 * @param index: The current height's index of Heights Array.
	 * @param heights: The Heights Array.
	 * @param currentHeight: The value of current height.
	 * @return The front index of current height that has the lower y value to current height.
	 */
	static int frontIndex(int index, int[] heights, int currentHeight) {						
		if (index == 0) return 0;
		while (heights[index] >=  currentHeight) {
			index--;
		}
		return index;
	}
	/***
	 * This function works for getting the index of the first lower heights on the right.
	 * @param index: The current height's index of Heights Array.
	 * @param heights The Heights Array.
	 * @param current:Height: The value of current height.
	 * @return The back index of current height that has the lower y value to current height.
	 */
	static int backIndex(int index, int[] heights, int currentHeight) {						
		if (index == heights.length - 1) return index;
		while (index < heights.length - 1 && heights[index] >=  currentHeight) {
			index++;
		}
		return index;							
	}
	/***
	 * This function works for calculating all possible rectangle's perimters.
	 * @param frontIndex: The index of the first lower heights on the left.
	 * @param backIndex: The index of the first lower heights on the right.
	 * @param currentHeight: The value of current height.
	 * @param points: The Array of all points.
	 * @return The perimeter of the current height's rectangle.
	 */
	static int perimeter(int frontIndex, int backIndex, int currentHeight, Point[] points) {
		int offset = points[backIndex * 2].x - points[backIndex * 2 - 1].x;
		return (points[backIndex * 2].x - points[frontIndex * 2].x - offset) * 2 + currentHeight * 2;
	}
	/***
	 * This function works for getting the rectangle's perimeter that has the max perimeter. 
	 * @param perimeters: The array of all perimeters.
	 * @return The max perimeter.
	 */
	static int maxPerimeter(int[] perimeters) {
		int max = perimeters[1];
		for (int perimeter: perimeters) {
			if (perimeter > max) {
				max = perimeter;
			}
		}
		return max;
	}
}
/***
 * This class is created for store the points.
 * @param x represents the coordinate x.
 * @param y represnets the coordinate y.
 * @author Guo, Zizhun
 *
 */
class Point {	
	int x;
	int y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;		
	}
}
