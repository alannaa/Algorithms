package Midterm;

import java.util.Random;
import java.util.Stack;

public class Problem2 {

  private int mainLoopRunCount = 0;
  private int partitionLoopRunCount = 0;
  private int compares = 0;

  private void resetTestStats(){
    mainLoopRunCount = 0;
    partitionLoopRunCount = 0;
    compares = 0;
  }

  /*
   2.3.20 Nonrecursive quicksort. Implement a nonrecursive version of quicksort
   based on a main loop where a subarray is popped from a stack to be
   partitioned, and the resulting subarrays are pushed onto the stack.
   Note : Push the larger of the subarrays onto the stack first, which
   guarantees that the stack will have at most lg N entries.
  */

  //Clarification: The problem states that a 'subarray' is popped from the
  // stack and that resulting subarrays are pushed to the stack. However, if
  // we worked with a Stack<Comparable[]> as is seemingly implied by the
  // language of the problem statement (a stack of actual subarrays) then the
  // original array would never get sorted because we would pop the subarray
  // off the stack, which is a separate object from the original array,
  // partition/sort it and then push other new objects(subarrays) back onto the
  // stack to be partitioned. I personally find the problem statement poorly
  // worded and think it should specify that a better and more realistic
  // implementation is one that uses the stack only to track the indices of
  // the subarrays to be worked on, and then the actual work is performed on
  // the original input array.


  // Subarray is a class for handling ranges of subarrays of a greater array
  public class Subarray {
    final int low;
    final int high;
    public Subarray(int low, int high){
      this.low = low;
      this.high = high;
    }

    public int length(){
      return this.high-this.low;
    }

    public int compareTo(Subarray that){
      Integer thisLength = this.length();
      Integer thatLength = that.length();
      return thisLength.compareTo(thatLength);
    }

    public boolean isValidSubarray(){
      return low < high;
    }
  }

  /*
    Time Complexity:
    --Recursive
    --the while loop that checks if the stack size>0 runs MORE THAN log N times
    --this is because
   */
  //TODO Time complexity
  //TODO space complexity
  // TODO comments about why putting the bigger array first makes the size <logn
  public void nonRecursiveQuickSort(Comparable[] arr){

    // workStack is a stack of Subarrays to be worked on
    Stack<Subarray> workStack = new Stack<>();
    if(arr.length>0){
      workStack.add(new Subarray(0, arr.length-1));
    }

    //While there's still work to be done...
    while(workStack.size() > 0){
      mainLoopRunCount++;

      //pop one element
      Subarray currSubarray = workStack.pop();

      //partition it
      int pIdx = partition(arr, currSubarray.low, currSubarray.high);

      //Determine the Subarrays LEFT and RIGHT of pIdx
      Subarray left = new Subarray(currSubarray.low, pIdx-1);
      Subarray right = new Subarray(pIdx+1, currSubarray.high);

      //Find which Subarray is bigger (left or right) and
      // push back onto stack in two halves, the bigger first
      if (left.compareTo(right) > 0) {
        //if left is bigger than right
        if (left.isValidSubarray()) workStack.push(left);
        if (right.isValidSubarray()) workStack.push(right);
      } else {
        //if right is bigger than left, or if theyre equal in size
        if (right.isValidSubarray())  workStack.push(right);
        if (left.isValidSubarray()) workStack.push(left);
      }
    }
  }

  /*
    Arbitrarily chooses a[lo] to be the partition
    Then exchanges elements on either side of a[lo] so that
    leftside values < partition < rightside values
  */
  private int partition(Comparable[] a, int lo, int hi){
    int i = lo;
    int j = hi+1;

    while(i < j){
      partitionLoopRunCount++;
      Comparable partitionValue = a[lo];
      // Logic: while increasing i = 1; i++
      // if (a[i] >= partition) then a[i] is out of place
      // break and keep i at that index (and a check to break in case idx OOB)
      while(a[++i].compareTo(partitionValue) < 0) {
        compares++;
        if(i==hi) break;
      }
      // Logic: while decreasing j = length-1; j--
      // if (a[j] <= partition) then a[j] is out of place
      // break and keep j at that index (and a check to break in case idx OOB)
      while(a[--j].compareTo(partitionValue) > 0) {
        compares++;
        if(j==lo) break;
      }
      if (i < j) {
        exchange(a, i, j);
      }
    }
    exchange(a, lo, j);
    return j;
  }

  private static void exchange(Comparable[] a, int i, int j){
    Comparable temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }


  public static void main(String[] args) {

    Problem2 problem2 = new Problem2();
    int n = 1000;

    /*To run tests:
    Run file and compare the unordered arrays to the ordered arrays in the
    output.
     */

    //------Base Case: Empty Array------//
    Comparable[] emptyArray = new Comparable[0];
    problem2.printTestResults(emptyArray, "---Empty Array---");


    //----Create a random 10-length array containing ints ranging 0 to 99----//
    Comparable[] unorderedArray10 = new Comparable[10];
    for (int i = 0; i < 10; i++){
      unorderedArray10[i] = new Random().nextInt(100);
    }
    problem2.printTestResults(unorderedArray10, "---Randomly Unordered Array---");


    //----Create a random 50-length array containing ints ranging 0 to 999----//
    Comparable[] unorderedArray50 = new Comparable[50];
    for (int i = 0; i < 50; i++){
      unorderedArray50[i] = new Random().nextInt(1000);
    }
    problem2.printTestResults(unorderedArray50, "---Randomly Unordered Array---");


    //----Create a random 1000-length array ranging 0 to 999----//
    Comparable[] unorderedArray = new Comparable[n];
    for (int i = 0; i < n; i++){
      unorderedArray[i] = new Random().nextInt(1000);
    }
    problem2.printTestResults(unorderedArray, "---Randomly Unordered Array---");


    //----Create an array with duplicated values----//
    if (n%2!=0) {
      n-=1;
    }
    Comparable[] duplicatesArray = new Comparable[n];
    for (int i=0; i<n/2; i++){
      int dup = new Random().nextInt(100);
      duplicatesArray[i] = dup;
      duplicatesArray[n/2+i] = dup;
    }
    problem2.printTestResults(duplicatesArray, "---All Duplicates Array---");


    //----Create an array that's already sorted(worst case)----//
    Comparable[] alreadySortedArray = new Comparable[n];
    for (int i = 0; i < n; i++){
      alreadySortedArray[i] = i;
    }
    problem2.printTestResults(alreadySortedArray, "---Aready Sorted Array---");

  }

  public void printTestResults(Comparable[] arrayToSort, String title) {
    nonRecursiveQuickSort(arrayToSort);
    System.out.println(title);
    System.out.println("N: " + arrayToSort.length);
    System.out.println("Main loop ran: " + mainLoopRunCount + " times");
    System.out.println("Partition loop ran: " + partitionLoopRunCount
        + " times");
    System.out.println("Compares: " + compares);
    System.out.println("Total Cycles (mainLoopRuns + partitionLoopRuns): "
        + (mainLoopRunCount+partitionLoopRunCount));

    resetTestStats();
    System.out.println("\n\n");

  }

}
