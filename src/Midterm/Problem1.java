package Midterm;

import java.util.Random;

public class Problem1 {

  /*
  1.4.24 Throwing eggs from a building.
  Suppose that you have an N-story building and plenty of eggs. Suppose also
  that an egg is broken if it is thrown off floor F or higher, and unhurt
  otherwise.
  1. devise a strategy to determine the value of F such that the number of
  broken eggs is ~lg N when using ~lg N throws,
  2. then find a way to reduce the cost to ~2 lg F.
   */


  private int numBrokenEggs = 0;
  private int numThrows = 0;//For earlier testing purposes

  private void resetTestStats(){
    numBrokenEggs = 0;
    numThrows = 0;
  }


  /* Strategy to determine the value of F such that
   the number of broken eggs is ~lg N when using ~lg N throws.

   Time complexity: Log N: search space is cut in half with each iteration.
    We know we need binarySearch whenever we want to achieve LogN time
    complexity because the definition of log is iterative division and the
    strategy used in binary search is iteratively dividing the search space
    by 2 with each recursive call.

   Space complexity: Pretty bad because recursion takes up space on the stack
   but that is how you implement binary search*/
  private int strategyLogN(int[] building, int numFloors){
    return binarySearch(building, 0, numFloors-1);
  }

  //A binary search adaptation to achieve Log N broken eggs
  private int binarySearch(int[] building, int low, int high){
    int mid = low + (high - low) / 2;
    numThrows++;

    if (low<=high) {
      if (building[mid] == 0) {
        //Search next upper half, because if the egg didn't break,
        // then you need to keep going up until it does.
        return binarySearch(building, mid + 1, high);

      } else { //building[mid]==1
        numBrokenEggs++;
        //If we got down to the first floor and the egg still breaks, no need to
        // check the case below of the current floor breaking the egg and previous
        // floor saving the egg. Just return 0:
        if (mid == 0) {
          return 0;
        }
        //Otherwise, check if the current floor breaks the egg while previous
        // floor does not. If so, you have found F:
        if (building[mid - 1] == 0) {
          return mid;
        }
        //If you did not find F, Search next lower half, because you're still
        // too high:
        return binarySearch(building, low, mid - 1);
      }
    }
    return building.length; //The case where the egg miraculously does not
    //break on ANY floor, we reach the final level and just return
    // building.length to indicate this
  }




  /* Strategy to determine the value of F such that
  the number of broken eggs is ~2 lg F when using ~2 lg F throws.

  Time complexity: 2logf: logf from first loop + logf from binary search on a
   range of the 'building' of a size < f (see further explanation below)

  Space complexity: While this strategy also employs recursion which is worse
   for space complexity due to the build up of function calls on the stack,
   this strategy at least guarantees LESS recursive calls because we reduce
   our search space to size logF before calling the binarySearch method.*/
  private int strategy2LogF(int[] building, int numFloors){
    int low;
    int high = 0;

    //Starting at the 0th level, incrementally check if the egg breaks.
    // At each increment, the amount you increment doubles, so you check:
    // 0, 1, 2, 4, 8, 16...
    // In other words, the increment grows exponentially.
    // Once you reach a level where egg breaks, stop and set high to that level
    // So that later you can create boundaries around a search space
    // that span between two indices that are both powers of 2, in between which
    // f lies.
    // The time complexity of this work is ~logf because the increment is
    // counting how many times you need to multiply the increment by 2,
    // starting at 1, to reach close to f. That is the equivalent to logf.
    for (int expGrowth = 1; high < numFloors; expGrowth++) {
      numThrows++;
      if (building[high]==0) {
        high = 1 << expGrowth;
      } else {
        numBrokenEggs++;
        break;
      }
    }
    //Might as well early catch the edge case where the egg breaks on ALL
    // floors (building[0]==1)
    if (high==0){
      return 0;
    }

    //Otherwise, reduce the search space down to [high/2 to high] because:
    // We found in the previous loop that F is between two boundaries
    // lower boundary: the previous high (which is high/2)
    // upper boundary: the current high (which may be > building.length,
    //    in which case take the length to avoid index out of bounds error).
    low = high/2;
    high = Math.min(building.length-1, high);

    //Now that we reduced our search space down to [low to high]
    // we created a search space of a length LESS THAN f.
    // we know that the length of this boundary is exactly `low`, because
    // in order to find the value of high we took low * 2
    // so in the range [low to 2low], the length is obviously size `low`
    // Also we know that low < f <= 2low
    // Since f is greater than low, we have effectively reduced our search
    // space down to a length LESS THAN f.
    // THUS we can run binary search with a guaranteed time complexity of < logf
    return binarySearch(building, low, high);

    //And finally, combining the logf loop above with this logf binary search,
    // You get a cost of 2logf!
  }


  //Constructs an int[] representing the building where
  // 0 represents a floor an egg does NOT break from and
  // 1 represents a floor an egg DOES break from
  private int[] buildBuilding(int n, int f){
    int[] building = new int[n];

    for(int i=0; i < f; i++){
      building[i] = 0;
    }
    for(int i=f; i < n; i++){
      building[i] = 1;
    }
    return building;
  }



  public static void main(String[] args){

    int n = new Random().nextInt(100000);
    int randF;
    int[] randBuilding;
    int fResults;


    /*To Run Tests:
      Run the file. If nothing outputs to terminal, all tests passed.
      If anything outputs to terminal, then a test failed and output
      will describe the failure.

      Tests check that both strategies produce the correct F and that
      numBrokenEggs is <= LogN for the first strategy and
      numBrokenEggs is <= 2LogF for the second strategy
     */


    Problem1 eggThrows = new Problem1();
    int[] building0 = eggThrows.buildBuilding(n, n);      //Base case: all 0's
    int[] building1 = eggThrows.buildBuilding(n, 0);   //Base case: all 1's


    //---------BASE CASE Tests on LogN Strategy---------//
    double logN = (Math.log(n) / Math.log(2));

    //A building made entirely of 0's (egg never breaks)
    fResults = eggThrows.strategyLogN(building0, n);
    if(fResults != n){
      System.out.println("BC FAIL: LogN strategy produced the wrong F result "
          + "for building size " + n
          + "\nexpected f: " + n + "; actual f: " + fResults);
    }
    if(eggThrows.numBrokenEggs > logN) {
      System.out.println("BC FAIL: LogN strategy produced worse than log n "
          + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
          + "\nLogN: " + logN
          + "\nF: " + n);
    }
    eggThrows.resetTestStats();

    //A building made entirely of 1's (egg breaks on very first floor)
    fResults = eggThrows.strategyLogN(building1, n);
    if(fResults != 0){
    System.out.println("BC FAIL: LogN strategy produced the wrong F result "
        + "for building size " + n
        + "\nexpected f: " + 0 + "; actual f: " + fResults);
    }
    if(eggThrows.numBrokenEggs > logN) {
    System.out.println("BC FAIL: LogN strategy produced worse than log n "
        + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
        + "\nLogN: " + logN
        + "\nF: " + 0);
    }
    eggThrows.resetTestStats();


    //---------1000 Tests on LogN Strategy---------//
    for (int i = 0; i < 1000; i++){
      randF = new Random().nextInt(n);
      randBuilding = eggThrows.buildBuilding(n, randF);

      fResults = eggThrows.strategyLogN(randBuilding, n);
      if(fResults != randF){
        System.out.println("FAIL: LogN strategy produced the wrong F result "
            + "on iteration " + i
            + "for building size " + n
            + "\nexpected f: " + randF + "; actual f: " + fResults);
      }
      if(eggThrows.numBrokenEggs > logN) {
        System.out.println("FAIL: LogN strategy produced worse than log n "
            + "result on iteration " + i
            + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
            + "\nLogN: " + logN
            + "\nF: " + randF);
      }
      eggThrows.resetTestStats();
    }




    //---------BASE CASE Tests on 2LogF Strategy---------//
    double twoLogF;

    //A building made entirely of 0's (egg never breaks)
    fResults = eggThrows.strategy2LogF(building0, n);
    twoLogF = 2 * (Math.log(fResults) / Math.log(2));
    if(fResults != n){
      System.out.println("BC FAIL: 2LogF strategy produced the wrong F result "
          + "for building size " + n
          + "\nexpected f: " + n + "; actual f: " + fResults);
    }
    if(eggThrows.numBrokenEggs > twoLogF) {
        System.out.println("BC FAIL: 2LogF strategy produced worse than 2LogF "
            + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
            + "\n2LogF: " + twoLogF
            + "\nF: " + n);
    }
    eggThrows.resetTestStats();

    //A building made entirely of 1's (egg breaks on very first floor)
    fResults = eggThrows.strategy2LogF(building1, n);
    if(fResults != 0){
      System.out.println("BC FAIL: 2LogF strategy produced the wrong F result "
          + "for building size " + n
          + "\nexpected f: " + 0 + "; actual f: " + fResults);
    }
    if(eggThrows.numBrokenEggs > 1) {
      System.out.println("BC FAIL: 2LogF strategy produced worse than 2LogF "
          + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
          + "\nExpected Num Broken Eggs: " + 1);
    }
    eggThrows.resetTestStats();


    //---------1000 Tests on 2LogF Strategy ---------//
    for (int i = 0; i < 1000; i++){
      randF = new Random().nextInt(n);
      randBuilding = eggThrows.buildBuilding(n, randF);

      fResults = eggThrows.strategy2LogF(randBuilding, n);
      if(fResults != randF){
        System.out.println("FAIL: 2LogF strategy produced the wrong F result "
            + "on iteration " + i
            + "for building size " + n
            + "\nexpected f: " + randF + "; actual f: " + fResults);
      }
      twoLogF = 2 * (Math.log(randF) / Math.log(2));
      if(eggThrows.numBrokenEggs > twoLogF) {
        if ((randF==0 && eggThrows.numBrokenEggs==1) ||
            (randF==1 && eggThrows.numBrokenEggs==2)){
          //This is as expected, but this test malfunctions due to:
          // when f is 0, 2LogF is -infinity
          // when f is 1, 2LogF is 0
        } else {
          System.out.println("FAIL: 2LogF strategy produced worse than 2LogF "
              + "result on iteration " + i
              + "\nNum Broken eggs: " + eggThrows.numBrokenEggs
              + "\n2LogF: " + twoLogF
              + "\nF: " + randF);
        }
      }
      eggThrows.resetTestStats();
    }
  }

}
