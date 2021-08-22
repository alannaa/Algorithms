package OAGoogle;

import java.util.Scanner;
import java.util.stream.Stream;

public class GooglePractice1 {

  static int solution(Integer[] A) {

    //We initialize rowFormation to size A.length in the unique event that
    // the student arrive in strictly ascending order, in which case, we need
    // to represent enough rows to fit each student in a single-element row
    Integer[] rowFormation = new Integer[A.length];
    int rowCounter = 0;

    for(int i = 0; i < A.length; i++){
      //if we know the shortest height present in any given row, we immediately
      //know if the student at A[i] belongs in this row without traversing rows.
      //Therefore, we really only need to track the height of the shortest person
      //in a row (and not all of the individual heights in that row)

      int j = 0;//j is the iterator place as we iterate through rowFormation
      while(j<rowCounter) {
        //NOTE: the problem statement states that "for the ith student, if there is a row
        // in which ALL the students are [strictly] taller than A[i]" the student belongs
        // in that row. I interpret that to mean that if A[i] has the SAME height as another
        // student in a row, they actually belong in a new row. If the problem statement specified,
        // "for the ith student, if there is a row in which all the students are as tall or taller than A[i]"
        // then I would be inclined to allow two students of the same height in the same row. Here, we go
        // with the interpretation that we mean all students are strictly taller than A[i] for A[i] to be able to go there.
        if(rowFormation[j].compareTo(A[i]) > 0) {
          //"If the shortest person in the row (represented by rowFormation[j])
          //   is still taller than the student"
          //"Then that student is now the new shortest person in that row,
          //    and goes there"
          rowFormation[j] = A[i];
          break;
        }
        j++;
      }
      //if we've gotten to the end of comparing A[i] to everyone in rowFormation:
      if (j==rowCounter) {
        rowFormation[rowCounter] = A[i];
        rowCounter++;
      }
    }

    return rowCounter;
  }


  public static void main(String[] args) {
    // Read from stdin, solve the problem, write answer to stdout.
    Scanner in = new Scanner(System.in);
    Integer[] A = getIntegerArray(in.next());

    System.out.print(solution(A));
  }

  private static Integer[] getIntegerArray(String str) {
    return Stream.of(str.split("\\,"))
        .map(Integer::valueOf)
        .toArray(Integer[]::new);
  }

}
