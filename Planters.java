/* CSCI- 665 Algorithm
 * 
 * Problem 3
 * 
 * Author: Zizhun Guo
 * 
 * Version 1.8
 * */
import java.util.Scanner;
import java.math.*;
import java.io.*;

public class Planters {


	
	public static void main(String[] args) {
		int[] plantersP;                                    // Planters P
		int[] plantersT;									// Planters T
		int[] plants;										// plants p (equal to amount of P)
		int[] plantersAll; 									// Planters P + T
		int pLength;										// Number of planters P
		int tLength;										// Number of planters T
		int totalLength;									//Number of planters (P + T)
		
		/*
		   * Instantiate a scanner to get inputs stream.
		   * 
		   	*/
		Scanner scanner = new Scanner(System.in);
		//String inputString  = scanner.nextLine();
        String numArray[] = scanner.nextLine().split(" "); 
        pLength = Integer.parseInt(numArray[0]);
        tLength = Integer.parseInt(numArray[1]);
        totalLength = pLength + tLength;
		
		String stringArray[] = scanner.nextLine().split(" ");                           
        plantersP= new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
        	plantersP[i] = Integer.parseInt(stringArray[i]);
        }
        plants = new int[plantersP.length];
        for (int i = 0; i < plantersP.length; i++) {
        	plants[i] = plantersP[i];
        }
        
        String stringArray1[] = scanner.nextLine().split(" ");
        plantersT= new int[stringArray1.length];
        for (int i = 0; i < stringArray1.length; i++) {
        	plantersT[i] = Integer.parseInt(stringArray1[i]);
        }         
        scanner.close();
        
        plantersAll = Merge(plantersP, plantersT);
		int[] sortedPlanters = binaryInsertSort(plantersAll);
		int[] sprtedPlants = binaryInsertSort(plants);
        
		System.out.println(Judgement(sprtedPlants, sortedPlanters));
		
	}
	/*
	   * Merge two integer arrays together.
	   * @Input: integer array 1 and integer array 2
	   * @return a merged integer array 3 
	   	*/
	private static int[] Merge(int[] arr_1, int[] arr_2) {
		int[] arr_m = new int[arr_1.length + arr_2.length];
		for (int i = 0; i < arr_1.length; i++) {
			arr_m[i] = arr_1[i];
		}
		for (int i = 0; i < arr_2.length; i++) {
			arr_m[arr_1.length+i] = arr_2[i];
		}
		return arr_m;		
	}
	/*
	   * Binary Insert Sort
	   * @Input: An Unsorted Array
	   * @return a sorted array from smallest to largest
	   	*/
	public static int[] binaryInsertSort(int[] array){
		for(int i = 0;i< array.length;i++){
			int temp = array[i];
			int left = 0;
			int right = i-1;
			int middle = 0;
			while(left <= right){
				middle = (left + right)/2;
				if(temp<array[middle]){
					right = middle-1;
				}else{
					left = middle + 1;
				}
			}
			for(int j = i-1;j>=left;j--){	
				array[j+1] = array[j];
			}
			if(left != i ){
				array[left] = temp;
			}
		}
		return array;
	}
	/*
	   * Decide Yes or No
	   * @Input: An array 1 and an array 2.
	   * @return A string either "YES" or "NO".
	   	*/
	public static String Judgement(int[] plants, int[] planters) {
		int index = 0;
		for (int i = 0; i < plants.length; i++) {
			for (int j = index; j < planters.length; j++) {
				if (plants[i] < planters[j]) {
					index = j;
					break;
				}	
				index++;
			}			
		}
		if (index >= planters.length) {
			return "NO";			
		}
		else return "YES";
		
		
	}
	
	

}
