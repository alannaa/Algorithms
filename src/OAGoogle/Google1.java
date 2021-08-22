package OAGoogle;

import java.util.Scanner;
import java.util.stream.Stream;

public class Google1 {


  static int solution(int[] A) {


    int n = A.length;
    //Immediately, I see that this is a dynamic programming problem due to
    // the need to find the most optimal split and then return the largest sum
    // of the pairs' elements within that optimal split. We'll need to keep
    // track of the pairs sums as we calculate further into the problem.

    //The memo will take the format of a table (2D array) where each
    // element of the table will represent the sum of the pair of ints in A.
    // Example: memo[i][j] == A[i] + A[j]
    int[][] memo = new int[n][n];

    //The number of possible pairs among A's elements:
    int numPairs = (n * (n-1)) / 2;

    for (int i =0; i < n; i++){
      for (int j = 0; j < n; j++) {

        //ask: if this is a new element we need to store/include in the memo:
        if(j > i) {

          //Then find the sum of the pair (only store unique pairs):
          memo[i][j] = A[i] + A[j];


        } else {
          //If we're at a pair that we've already processed, we can now
          //look into the data we've saved in the memo to check if the
          //data at memo[i][j] is the largest sum of the total possible pairs
          //in that set of pairs from A.

          //Either memo[i][j] gets the same info as memo[j][i] (as the
          // unique pair has the commutative property) OR
          // there is a pair in that set of pairs with a larger sum, in which
          // case set memo[i][j] to that sum.
          memo[i][j] = Math.max(memo[j][i], A[i+1] + A[i+2]);
        }

      }

    }
    return 0;
  }


  static int sum(Integer[] aLoad) {
    int sum = 0;
    for(int i = 0; i < aLoad.length; i++) {
      sum+= aLoad[i];
    }
    return sum;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    Integer[] A = getIntegerArray(in.next());

    //System.out.print(solution(A));
  }

  private static Integer[] getIntegerArray(String str) {
    return Stream.of(str.split("\\,"))
        .map(Integer::valueOf)
        .toArray(Integer[]::new);
  }
}
