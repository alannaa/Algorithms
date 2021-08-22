package Chapter1;


/******************************************************************************
 *  Compilation:  javac ThreeSum.java
 *  Execution:    java ThreeSum input.txt
 *  Dependencies: In.java StdOut.java Stopwatch.java
 *  Data files:   https://algs4.cs.princeton.edu/14analysis/1Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/2Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/4Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/8Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/16Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/32Kints.txt
 *                https://algs4.cs.princeton.edu/14analysis/1Mints.txt
 *
 *  A program with cubic running time. Reads n integers
 *  and counts the number of triples that sum to exactly 0
 *  (ignoring integer overflow).
 *
 *  % java ThreeSum 1Kints.txt
 *  70
 *
 *  % java ThreeSum 2Kints.txt
 *  528
 *
 *  % java ThreeSum 4Kints.txt
 *  4039
 *
 ******************************************************************************/

/**
 *  The {@code ThreeSum} class provides static methods for counting
 *  and printing the number of triples in an array of integers that sum to 0
 *  (ignoring integer overflow).
 *  <p>
 *  This implementation uses a triply nested loop and takes proportional to n^3,
 *  where n is the number of integers.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/14analysis">Section 1.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class ThreeSum {

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

  // Do not instantiate.
  private ThreeSum() {}

  // Returns the number of triples {i, j, k} with i < j < k
  // such that a[i] + a[j] + a[k] == 0
  public static int count(int[] a){
    int n = a.length;
    int count = 0;

    for (int i = 0; i < n; i++){
      for (int j = i+1; j < n; j++) {
        for (int k = j+1; k < n; k++) {
          if (a[i] + a[j] + a[k] == 0){
            count++;
          }
        }
      }
    }
    return count;
  }

  //Counts the number of triples sum to exactly zero
  public static void main(String[] args){
    int[] arr = {0, -40, -10, 5, -20, 10, 40, 30};

    Stopwatch timer = new Stopwatch();
    int count = count(arr);
    System.out.println("elapsed time: " + timer.elapsedTime());
    System.out.println("\ngetNumComponents: " + count);
  }

}
