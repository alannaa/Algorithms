package OAGoogle;

import java.util.Scanner;
import java.util.stream.Stream;

public class GooglePractice2 {

  static int solution(Integer[] loads) {

    int totalSum = GooglePractice2.sum(loads);
    int balance = totalSum/2;

    //Store results of subproblems
    int results[][] = new int[loads.length+1][balance + 1];

    for (int i = 1; i < loads.length+1; i++) {
      System.out.println("I : " + i);
      System.out.println("loads[i-1] : " + loads[i-1]);

      for (int j = 1; j < balance+1; j++) {

        System.out.println("J : " + j);

        //"if ith server is included"
        if (loads[i-1] > j) {
          results[i][j] = results[i-1][j];
        }
        else { //"if ith server is excluded"
          results[i][j] = Math.max(results[i-1][j],
              loads[i-1] + results[i-1][j-loads[i-1]]);
        }
      }


      for (int k = 0; k < loads.length+1; k++) {
        for (int j = 0; j < balance+1; j++) {
          System.out.print(results[k][j] + " ");
        }
        System.out.print("\n");
      }
      System.out.print("--------------\n\n");


    }
    return totalSum - 2*results[loads.length][balance];
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
    Integer[] loads = getIntegerArray(in.next());

    System.out.print(solution(loads));
  }

  private static Integer[] getIntegerArray(String str) {
    return Stream.of(str.split("\\,"))
        .map(Integer::valueOf)
        .toArray(Integer[]::new);
  }

}
