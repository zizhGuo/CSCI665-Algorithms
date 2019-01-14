/***CSCI 665 Algorithms
 * @author Guo, Zizhun
 * @Homework Homework 2
 * @version 1.8
 * 
 */
import java.util.Scanner;
import java.math.*;

public class WeightedByDiffInversions {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = scanner.nextInt();
		}
		scanner.close();
		System.out.print(mergeSort(arr, n));
	}	
	/***
	 * The Function is used for initiate a mersgeSort.
	 * @param arr: The unsorted array.
	 * @param array_size: The size of the unsorted array.
	 * @return weights counted
	 */
	static int mergeSort(int[] arr, int array_size) {
		int temp[] = new int[array_size];
		return _mergeSort(arr, temp, 0, array_size - 1);
	}
	/***
	 * The recursion function is used for calculate the sum of all weighted values.
	 * @param arr: The unsorted array.
	 * @param temp: The temp array.
	 * @param left: left arrow.
	 * @param right: right arrow.
	 * @return The incremental weighted value.
	 */
	static int _mergeSort(int arr[], int temp[], int left, int right) { 
		int mid, weighted = 0; 
		if (right > left) { 
			mid = (right + left)/2;      
			weighted  = _mergeSort(arr, temp, left, mid); 
			weighted += _mergeSort(arr, temp, mid+1, right);        
			weighted += merge(arr, temp, left, mid+1, right); 
		} 
      	return weighted; 
    }
	/***
	 * This merging function is used for merging and sorting two arrays into one.
	 * @param arr The unsorted array.
	 * @param temp The temp array.
	 * @param left The left arrow index.
	 * @param mid The right arrow index.
	 * @param right The right arrow index.
	 * @return The incremental weighted value.
	 */
	static int merge(int arr[], int temp[], int left, int mid, int right) 
    { 
		int i, j, k;        
		i = left;
		j = mid; 
		k = left; 
      
		int sum = 0;
		int weighted = 0;
		for (int h = left; h <= mid - 1; h++){
			sum = sum + arr[h];
		}
		while ((i <= mid - 1) && (j <= right)) { 
			if (arr[i] <= arr[j]) { 
				sum = sum - arr[i];
				temp[k++] = arr[i++]; 
				} 
			else{
				weighted = (weighted + sum - (mid - i) * arr[j]);
				temp[k++] = arr[j++]; 
			} 
		} 
		while (i <= mid - 1) 
			temp[k++] = arr[i++]; 
		while (j <= right) 
			temp[k++] = arr[j++]; 
		for (i=left; i <= right; i++) 
			arr[i] = temp[i]; 
		return weighted; 
    } 
}
