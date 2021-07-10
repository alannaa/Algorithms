package Quiz1;

import com.sun.xml.internal.bind.v2.model.annotation.Quick;
import java.util.Random;

public class QuickSort {

  private int mainLoopRunCount = 0;
  private int partitionLoopRunCount = 0;

  private void resetTestStats(){
    mainLoopRunCount = 0;
    partitionLoopRunCount = 0;
  }

  private static class Stopwatch {
    private final long start;

    /**
     * Initializes a new stopwatch.
     */
    public Stopwatch() {
      start = System.currentTimeMillis();
    }

    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    public double elapsedTime() {
      long now = System.currentTimeMillis();
      return (now - start) / 1000.0;
    }
  }

  private static class StdRandom {
    private static Random random;    // pseudo-random number generator
    private static long seed;        // pseudo-random number generator seed

    // static initializer
    static {
      // this is how the seed was set in Java 1.4
      seed = System.currentTimeMillis();
      random = new Random(seed);
    }

    // don't instantiate
    private StdRandom() { }

    /**
     * Sets the seed of the pseudo-random number generator.
     * This method enables you to produce the same sequence of "random"
     * number for each execution of the program.
     * Ordinarily, you should call this method at most once per program.
     *
     * @param s the seed
     */
    public static void setSeed(long s) {
      seed   = s;
      random = new Random(seed);
    }

    /**
     * Returns the seed of the pseudo-random number generator.
     *
     * @return the seed
     */
    public static long getSeed() {
      return seed;
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
      return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     *
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and {@code n} (exclusive)
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public static int uniform(int n) {
      if (n <= 0) throw new IllegalArgumentException("argument must be positive: " + n);
      return random.nextInt(n);
    }

    ///////////////////////////////////////////////////////////////////////////
    //  STATIC METHODS BELOW RELY ON JAVA.UTIL.RANDOM ONLY INDIRECTLY VIA
    //  THE STATIC METHODS ABOVE.
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return     a random real number uniformly in [0, 1)
     * @deprecated Replaced by {@link #uniform()}.
     */
    @Deprecated
    public static double random() {
      return uniform();
    }

    /**
     * Rearranges the elements of the specified array in uniformly random order.
     *
     * @param  a the array to shuffle
     * @throws IllegalArgumentException if {@code a} is {@code null}
     */
    public static void shuffle(int[] a) {
      validateNotNull(a);
      int n = a.length;
      for (int i = 0; i < n; i++) {
        int r = i + uniform(n-i);     // between i and n-1
        int temp = a[i];
        a[i] = a[r];
        a[r] = temp;
      }
    }

    // throw an IllegalArgumentException if x is null
    // (x can be of type Object[], double[], int[], ...)
    private static void validateNotNull(Object x) {
      if (x == null) {
        throw new IllegalArgumentException("argument must not be null");
      }
    }

  }

  /*
    Eliminate dependence on input by shuffling
    If QS is called on a sorted or inversely sorted array,
    its time complexity is O(n^2).

    It is more efficient to pay O(n) to shuffle + O(N log N) to QS
    than to pay O(n^2) on a potentially already sorted array
   */
  public void sort(int[] a) {
    StdRandom.shuffle(a);
    sort(a, 0, a.length - 1);
  }


  /*
    lo and hi are INDICES of the subarray focused in on this iteration of QS
    Initially, lo == 0 ; hi == (a.length-1)
    They iteratively focus in on smaller subarrays on either side of the partition
  */
  private void sort(int[] a, int lo, int hi) {
    mainLoopRunCount++;
    if (hi <= lo) return;

    // j is in its FINAL PLACE defined by:
    // all that's to its left is < j < all that's to its right
    // partition method rearranges sub-array of a[lo:hi] so that the above
    // definition holds
    int j = partition(a, lo, hi);

    // because j is in its final place, call sort recursively on the left
    // and right sides of j
    sort(a, lo, j-1);
    sort(a, j+1, hi);
  }

  /*
    Arbitrarily chooses a[lo] to be partition
    Then exchanges elements on either side of a[lo] so that
    left values < partition < right values
  */
  private int partition(int[] a, int lo, int hi){
    int partitionValue = a[lo];

    int i = lo;
    int j = hi+1;

    while(i < j){
      partitionLoopRunCount++;

      // Logic:
      // while increasing i = 1; i++
      // if (a[i] >= partition) then a[i] is out of place
      // break and keep i at that index (and a check to break in case idx OOB)
      while(a[++i] < partitionValue) {
        if(i==hi) break;
      }

      // Logic:
      // while decreasing j = length-1; j--
      // if (a[j] <= partition) then a[j] is out of place
      // break and keep j at that index (and a check to break in case idx OOB)
      while(a[--j] > partitionValue) {
        if(j==lo) break;
      }

      if (i < j) {
        exchangeij(a, i, j);
      }

      //What makes this UNSTABLE is the fact that we must accommodate for
      // this case:
      // [left is all less than partition except for one element] partition
      // [right is all in place]
      // in the above case, you want the one element thats out of place in
      // the left side to actually switch with the partition. so we say
      // if a[j] less than or EQUAL TO the partition, so that in this case
      // the one side that is out of order will still be sorted.

      //But notice the side effect: What if both sides are sorted, and we are
      // trying to run through and break out of the the while loop i < j
      // by allowing i and j to incrementally get close to one another until
      // they are equal. BUT THEN: the partition and its following element
      // are equal. Technically they are sorted, but in the above note, I
      // said that if a[j] is <= partition THEN a[j] is OUT OF PLACE. Again,
      // if a[j] is EQUAL TO the partition then it is out of place. We have
      // no way of checking whether it's also adjacent to the partition in
      // this implementation. This is precisely what causes the algo to be
      // unstable. If there are two consecutive elements that are equal
      // (technically in order) in order to accommodate for the case outlined
      // above, you must also deal with the side effect of swaping equal
      // elements, leading to an unstable algorithm.
    }
    exchangeij(a, lo, j);
    return j;

  }


  private static void exchangeij(int[] a, int i, int j){
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }






  public static void main(String[] args){

    QuickSort quickSort = new QuickSort();

    int[] arr = {5, 7, 8, 3, 4, 1, 3, 2};
    quickSort.sort(arr, 0, arr.length-1);
    for (int elem : arr) {
      System.out.print(elem + " ");
    } System.out.println();

    int[] arreq = {3, 1, 3, 1, 1, 4, 4, 4, 4};
    quickSort.sort(arreq, 0, arreq.length-1);
    for (int elem : arreq) {
      System.out.print(elem + " ");
    } System.out.println();



    int n = 1000;
    //------Base Case: Empty Array------//
    int[] emptyArray = new int[0];
    quickSort.printTestResults(emptyArray, "---Empty Array---");


    //----Create a random 50-length array containing ints ranging 0 to 999----//
    int[] unorderedArray = new int[n];
    for (int i = 0; i < n; i++){
      unorderedArray[i] = new Random().nextInt(1000);
    }
    quickSort.printTestResults(unorderedArray, "---Randomly Unordered Array---");


    //----Create an array with duplicated values----//
    if (n%2!=0) {
      n-=1;
    }
    int[] duplicatesArray = new int[n];
    for (int i=0; i<n/2; i++){
      int dup = new Random().nextInt(100);
      duplicatesArray[i] = dup;
      duplicatesArray[n/2+i] = dup;
    }
    quickSort.printTestResults(duplicatesArray, "---All Duplicates Array---");


    //----Create an array that's already sorted(worst case)----//
    int[] alreadySortedArray = new int[n];
    for (int i = 0; i < n; i++){
      alreadySortedArray[i] = i;
    }
    quickSort.printTestResults(alreadySortedArray, "---Aready Sorted Array---");


  }


  public void printTestResults(int[] arrayToSort, String title) {
    resetTestStats();
    sort(arrayToSort);
    System.out.println(title);
    System.out.println("N: " + arrayToSort.length);
    System.out.println("Main loop ran: " + mainLoopRunCount + " times");
    System.out.println("Partition loop ran: " + partitionLoopRunCount
        + " times");
    System.out.println("Total Cycles (mainLoopRuns + partitionLoopRuns): "
        + (mainLoopRunCount+partitionLoopRunCount));
    System.out.println("\n\n");
  }
}
