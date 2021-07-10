package Quiz1;


public class MergeSortTopDown {

  private static int[] aux;


  public static void sort(int[] a){
    aux = new int[a.length]; //allocate space just once
    sort(a, 0, a.length-1);

  }


  private static void sort(int[] a, int lo, int hi) {

    if (hi <= lo) return;
    int mid = lo + (hi-lo)/2;

    sort(a, lo, mid);
    sort(a, mid+1, hi);

    merge(a, lo, mid, hi);
  }


  // Merges together two SORTED subarrays:
  // a[lo...mid] + a[mid+1...hi]
  // resulting in a[lo...hi] being fully sorted by the end
  private static void merge(int[] a, int lo, int mid, int hi) {
    // i -> LEFTHAND (sorted) array
    // j -> RIGHTHAND (sorted) array
    int i = lo;
    int j = mid+1;

    //Copy into aux
    for(int k = lo; k <= hi; k++) {
      aux[k] = a[k];
    }

    for(int k = lo; k <= hi; k++) {

      // Stopping conditions:
      // If one side has already been fully put into place,
      //  just finish putting the other side into place in its current order
      if (i > mid) {
        a[k] = aux[j++];
      }
      else if (j > hi) {
        a[k] = aux[i++];
      }

      //Merging conditions:
      // Compare the next elem from each of the LEFTHAND and RIGHTHAND arrays

      // If [RIGHTHAND elem] < [LEFTHAND elem]
      else if (less(aux[j], aux[i])) {
        // take the elem from the right sorted array and put it in place
        // advance j
        a[k] = aux[j++];
      }

      // If [LEFTHAND elem] < [RIGHTHAND elem]
      else {
        // take the elem from the left sorted array and put it in place
        // advance i
        a[k] = aux[i++];
      }

    }
  }

  private static boolean less(int v, int w){
    return v < w;
  }

  public static void main(String[] args){

    int[] arr = {5, 7, 8, 3, 4, 1, 3, 2};
    sort(arr);
    for (int elem : arr) {
      System.out.print(elem + " ");
    }

    System.out.println();

    int[] arreq = {3, 1, 3, 1, 1, 4, 4, 4, 4};
    sort(arreq);
    for (int elem : arreq) {
      System.out.print(elem + " ");
    }


  }


}
