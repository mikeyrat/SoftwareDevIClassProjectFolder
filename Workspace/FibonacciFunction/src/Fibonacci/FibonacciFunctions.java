package Fibonacci;
import java.util.Scanner;

/** Class to demonstrate recursive and iterative use of the Fibonacci Sequence
 * @author Michael Forman with parts utilized from previous text book:
 * Introduction to Java Programming and Data Structures - Y. Daniel Liang
 */
public class FibonacciFunctions {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Fibonacci using iteration is O(n).");
		System.out.print("Enter an index for a Fibnacci Number: ");
		long index = input.nextLong();
		
		System.out.println("Beginning Fibnacci Search for number at index " + index + " using iteration:");
		long iTimeStart = System.currentTimeMillis();
		System.out.println("Number at index " + index + " using interation is " + iterateMe(index) + ".");
		System.out.println("The first operation took: " + (System.currentTimeMillis() - iTimeStart) + "ms.");
		
		System.out.println("\nNow cycling 10 times for an average time test:");
		System.out.println("Average of ten iteration cycles on index " + index + " is " + runIterAvg(index) + "ms.");
		
		System.out.println("\nFibonacci using recursion is o(n^2).");
		System.out.println("Beginning Fibnacci Search for number at index " + index + " using recursion:");
		long rTimeStart = System.currentTimeMillis();
		System.out.println("Number at index " + index + " using recursion is " + recurseMe(index) + ".");
		System.out.println("The first operation took: " + (System.currentTimeMillis() - rTimeStart) + "ms.");
		
		System.out.println("\nNow cycling 10 times for an average time test:");
		System.out.println("Average of ten recursion cycles on index " + index + " is " + runRecurAvg(index) + "ms.");
		
	}

/** Iterative method of utilizing the Fibonacci Sequence - code from basic Java course assignment
 * @param index - number in the Fibonacci sequence for which we are seeking 
 * @return a - Fibonacci number at the index provided
 */
public static long iterateMe(long index){
	long a = 0;
	long b = 1;
	long c = 1;
	for (int i = 0; i < index; i++) {
		a = b;
		b = c;
		c = a + b;
	}
	return a;
		
}

/** Method for calculating average runtime for iterative. 
 * @param index to be found in Fibonacci Sequence
 * @return average ms for 10 runs
 */
public static long runIterAvg(long index){
	long n = 0;
	for (int i = 0; i == 10; i++){
		long avgTimeStart = System.currentTimeMillis();
		iterateMe(index);
		n = n + (System.currentTimeMillis()- avgTimeStart); 
	}
	return n / 10;
}

/** Recursive method for finding Fibonacci Sequence - taken from textbook listed above
 * @param index - index in sequence to be found
 * @return number located at the index
 */
public static long recurseMe(long index){ 
	if (index == 0)
		return 0;
	else if (index == 1)
		return 1;
	else
		return recurseMe(index - 1) + recurseMe(index - 2);
	
	}

/** Method for calculating average runtime for recursive. 
* @param index to be found in Fibonacci Sequence
* @return average ms for 10 runs
*/
public static long runRecurAvg(long index){
	long n = 0;
	for (int i = 0; i <= 10; i++){
		long avgTimeStart = System.currentTimeMillis();
		recurseMe(index);
		n = n + (System.currentTimeMillis()- avgTimeStart); 
	}
	return n / 10;
}
}
