package OAGoogle;

import java.util.Scanner;

public class Google2 {


  static int solution(int A) {


    //To find the minimum absolute difference possible among the split int,
    // only store the current minimum absolute difference as you iterate
    // over int A and perform the splitting and absolute difference calculating:

    int minAbsDif = Integer.MAX_VALUE;

    //Calculate the number of digits in A by finding log10(A), which returns
    // the number of times you need to divide A by 10 to get 1,
    // then add 1 to account for the leftover identity 1.
    // And floor it to get a whole number:
    int digits = (int)(Math.floor(Math.log10(A)) + 1);

    //I define "splitting at i" to mean
    // the 0th through ith indexed element of A is to the left and
    // the i+1 through N-1 indexed elements to the right
    // (so the ith element is included on the left side of the split)
    for(int i = 1; i < digits; i++) {

      //I recognize there are more optimal ways of performing the action of
      // splitting an integer and that converting to String and back for each
      // iteration it heavy work. In the interest of time I am unable to
      // research the math behind this but would in a work setting!
      String AString = String.valueOf(A);

      String leftString = AString.substring(0,i);
      String rightString = AString.substring(i,digits);

      int leftInt = Integer.parseInt(leftString);
      int rightInt = Integer.parseInt(rightString);

      System.out.println("LEFT: " + leftString);
      System.out.println("RIGHT: " + rightString);


      int absDif = Math.abs(leftInt-rightInt);

      System.out.println("ABSDIF: " + absDif);

      System.out.println("\n-----------\n");

      if (absDif < minAbsDif) {
        minAbsDif = absDif;
      }
    }

    //return the minimum digit stored in the memo
    return minAbsDif;
  }



  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int A = Integer.parseInt(in.next());

    System.out.print(solution(A));
  }


}
