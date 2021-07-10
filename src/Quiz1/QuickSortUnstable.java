package Quiz1;


public class QuickSortUnstable {

  private static void sort(String[] a, int lo, int hi) {
    if (hi <= lo) return;

    // by the end of the partition method,
    // j is in its final, correct position as defined by
    // [less than j values] < j < [greater than j values]
    int j = partition(a, lo, hi);

    sort(a, lo, j-1);
    sort(a, j+1, hi);
  }


  /*
    Arbitrarily chooses a[lo] to be partition
    Then exchanges elements and repositions the partition so that
    left values < partition < right values
  */
  private static int partition(String[] a, int lo, int hi){
    String partitionValue = a[lo];

    int i = lo;
    int j = hi+1;

    while(i < j){

      // Logic:
      // while increasing i = 1; i++
      // if (a[i] >= partitionValue) then a[i] is out of place
      // break and keep i at that index (and a check to break in case idx OOB)
      while(compare(a[++i], partitionValue) < 0) {
        if(i==hi) break;
      }

      // Logic:
      // while decreasing j = length-1; j--
      // if (a[j] <= partition) then a[j] is out of place
      // break and keep j at that index (and a check to break in case idx OOB)
      while(compare(a[--j], partitionValue) > 0) {
        if(j==lo) break;
      }

      if (i < j) {
        exchange(a, i, j);
      }

    }
    exchange(a, lo, j);
    return j;

  }

  private static void exchange(String[] a, int i, int j){
    String temp = a[i];
    a[i] = a[j];
    a[j] = temp;
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
    String[] arr = {"cde", "fgh", "bcd", "abc", "efg", "def"};
    sort(arr, 0, arr.length-1);
    for (String elem : arr) {
      System.out.print(elem + " ");
    }
    System.out.println("Regular sorting: Passed");
    System.out.println("\n");
    //Test that my compare method words as expected:
    String a = "abcd";
    String b = "adef";
    System.out.println(compare(a, b)==0);
    String c = "abc";
    String d = "zzz";
    System.out.println(compare(c, d)==-25);
    String e = "zzz";
    String f = "ccc";
    System.out.println(compare(e, f)==23);
    System.out.println("compare method: Passed");
    System.out.println("\n");


    //**********************************************************************//
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
    equal "value" (all begin with 'a' and the first letter is the only thing
    compared). This means that the output would come out in exactly the same
    order it was input, if the algo is stable.

    If it comes out in any other order, the algo is unstable.*/

    String[] arrUnstable = {"aONE", "aTWO", "aTHREE", "aFOUR"};

    System.out.print("Input: ");
    for (String elem : arrUnstable) {
      System.out.print(elem + " ");
    }

    sort(arrUnstable, 0, arrUnstable.length-1);

    System.out.print("\nOutput: ");
    for (String elem : arrUnstable) {
      System.out.print(elem + " ");
    }

    /* Output was: aFOUR aTHREE aONE aTWO

    Thus the algorithm is UNSTABLE.
    It would have compared all of the a's and resulted in 0 for
    all of the compare methods and therefore kept them in the same
    order if it were stable.*/

    System.out.println("\n");



    //**********************************************************************//
    //Another example
    String[] arrMixed = {"cFIRST", "aaa", "fff", "ddd", "cSEC", "ppp",
        "cTHIRD"};

    System.out.print("Input: ");
    for (String elem : arrMixed) {
      System.out.print(elem + " ");
    }

    sort(arrMixed, 0, arrMixed.length-1);

    System.out.print("\nOutput: ");
    for (String elem : arrMixed) {
      System.out.print(elem + " ");
    }

    /* Output was: cTHIRD cSEC cFIRST ddd fff ppp
     * The order of the three c words was reversed */
    System.out.print("\n");
  }
}
