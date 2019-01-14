import java.util.Scanner;

public class SmallestTwo {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int num = scanner.nextInt();
		int[] arr = new int[num];
		for (int i = 0; i < num; i++) {
			arr[i] = scanner.nextInt();			
		}
		scanner.close();
		
		BinarySort(arr);
		System.out.println(arr[0]);
		System.out.println(arr[1]);
	}

    public static void BinarySort(int arr[])
    {
        int start;
        int end;
        int temp=0;
        int mid,j;
        
        for(int i = 1;i<arr.length; i++)
        {
            start = 0;
            end = i - 1;
            temp = arr[i];
 
            while(start <= end){
                mid = (start + end)/2;
                if (temp < arr[mid]) end = mid - 1;
                else start = mid + 1;
            }
            
            for(j = i-1; j >= start;j-- ) arr[j + 1] = arr[j];
            arr[start] = temp;
        }
    }

}
