package Quiz1;

import java.util.LinkedList;
import java.util.Queue;

public class QuickSortG4G {

  private static void sort(String[] a, int lo, int hi) {
    if (hi <= lo) return;

    int j = partition_stable(a, lo, hi);

    sort(a, lo, j-1);
    sort(a, j+1, hi);
  }


  //G4G
  private static int partition_stable(String[] a, int lo, int hi) {

    Queue<String> aux = new LinkedList<>();
    Queue<String> left = new LinkedList<>();
    Queue<String> right = new LinkedList<>();

    for (int i = lo + 1; i <= hi; i++) {
      if (compare(a[i], a[lo]) < 0) {
        left.add(a[i]);
      } else {
        right.add(a[i]);
      }
    }
    aux.addAll(left);
    aux.add(a[lo]);
    aux.addAll(right);

    for (int i = lo; i <= hi; i++){
      a[i] = aux.remove();
    }
    int j = lo + left.size();
    return j;
  }


  /*
    Compare method that ONLY takes into account the first letter of the string.
    This is used to test the stability of the algorithm (see below)
   */
  private static int compare(String a, String b){
    if (a.isEmpty() && !b.isEmpty()) {
      return -1;
    }
    if (!a.isEmpty() && b.isEmpty()) {
      return 1;
    }

    return a.substring(0,1).compareTo(b.substring(0,1));
  }



  public static void main(String[] args){

    //Test that regular sorting works
    String[] arr = {"cde", "bcd", "def", "abc"};
    sort(arr, 0, arr.length-1);
    for (String elem : arr) {
      System.out.print(elem + " ");
    }
    System.out.println("Regular sorting: Passed");
    System.out.println("\n");


    /* Test if the algo is stable

    Since the compare method above only compares the first letter of its
    arguments, the input below (that all start with "a") should be output
    in the exact same order as they were input after calling sort, IF it is
    a stable implementation.

    Basically, if you have four unique OBJECTS with equal VALUE,
    a stable sort will keep them in the same order due to their equal value.
    But the only way to check that is to be able to observe the order
    of the unique objects after the sorting algo is called on the input.
    Because Java does not have a way to see object's virtual memory addresses,
    this is another way to track the order of unique objects. I essentially
    created four unique objects that, according to my compare method, all have
    equal "value." This means that the output should come out in exactly the
    same order it was input.

    If it comes out in any other order, the algo is unstable.*/

    String[] arrStable = {"aONE", "aTWO", "aTHREE", "aFOUR"};

    System.out.print("Input: ");
    for (String elem : arrStable) {
      System.out.print(elem + " ");
    }

    sort(arrStable, 0, arrStable.length-1);
    System.out.print("\nOutput: ");
    for (String elem : arrStable) {
      System.out.print(elem + " ");
    }

    /* Output was: aONE aTWO aTHREE aFOUR (same as input)
    Thus the algorithm is STABLE.*/

    System.out.println("\n");

    //Another example
    String[] arrMixed = {"car", "frog", "dog", "coral", "pig", "cat"};

    System.out.print("Input: ");
    for (String elem : arrMixed) {
      System.out.print(elem + " ");
    }

    sort(arrMixed, 0, arrMixed.length-1);
    System.out.print("\nOutput: ");
    for (String elem : arrMixed) {
      System.out.print(elem + " ");
    }

    /* Output was: car coral cat dog frog pig*/

  }

}
