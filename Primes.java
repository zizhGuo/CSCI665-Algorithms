import java.util.Scanner;

public class Primes {	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int num = scanner.nextInt();
		scanner.close();
		PrintPrimes(num);
	}
	
	static void PrintPrimes(int n) {
		boolean[] Prime = new boolean[n + 1];	
		if (n>1) Prime[2] = true; 
		for (int i = 3; i < n + 1; i += 2) {
			Prime[i] = true;
		}
		for (int i = 2; i< Math.sqrt(n);i++) {
			if (Prime[i]) {
				for(int j = i+i; j<= n; j+=i) {
					Prime[j] = false;
				}
			}
		}
		
		for (int i = 0; i < n + 1; i++) {
			//System.out.println(Prime[i]);
			if (Prime[i]) System.out.println(i);
		}
	}

}
