/*
 * Search algorithm
 */
public class MainApp {
    final static int MAX = 10_000_000;
	public static void main(String[] args) {
		int oriAry[] = new int[MAX];
		for (int i=0; i<oriAry.length; i++) {
			oriAry[i] = i + 1;
		}
		int firstx = 0;
		int lastx = oriAry.length - 1;
		// pick a value somewhere in the middle
		sequentialSearch(oriAry, MAX);
		binSearch(oriAry, firstx, lastx, MAX);
	}

	// split ascending order array in half. worst time O = log(n)
	private static void binSearch(int[] ary, int firstx, int lastx, int target) {
		long start = System.currentTimeMillis();
		int midx = (firstx + lastx) / 2;
		while (firstx <= lastx) {
			if (ary[midx] == target) {
				System.out.println("Binary search: Found at index " + midx);
				// found, stop
				break;
			} else if (ary[midx] < target) {
				firstx = midx + 1; // target on the right
			} else {
				lastx = midx - 1; // target on the left
			}
			// redefine mid point
			midx = (firstx + lastx) / 2;
		} // end loop
		if (firstx > lastx) {
			System.out.println("Not found");
		}
		long stop = System.currentTimeMillis();
		System.out.println("Duration " + (stop - start));
	}

	private static void sequentialSearch(int[] ary, int target) {
		// loop each item, save larger number. worst case O = n
		long start = System.currentTimeMillis();
		boolean found = false;
		int i = 0;
		while (i < ary.length && !found) {
			if (target == ary[i]) {
				found = true;
				System.out.println("Sequential search: Found at index " + i);
			}
			i = i + 1;
		}
		long stop = System.currentTimeMillis();
		if (! found) {
			System.out.println("Not found");
		}
		System.out.println("Duration " + (stop - start));
	}

	private static void reverseNumber(int oriNum) {
		int n = oriNum;
		int revNum = 0;
		int lastDigit = 0;
		while (n > 0) {
			// mod 10 to get the last digit
			lastDigit = n % 10;
			// append to result
			revNum = (revNum * 10) + lastDigit;
			// move up
			n = n / 10;
		}
		System.out.println(revNum);
	}
}
