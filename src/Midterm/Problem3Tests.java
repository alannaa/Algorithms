package Midterm;

import java.util.Random;

public class Problem3Tests {

  public static void main(String[] args){

    /*
    TO RUN TESTS: I have set up all tests such that
    if all works as expected there is no output to terminal but
    if anything fails a message will print to output with a description of the
    failure.
     */

    //Priority Queues
    Problem3 orderedQueue = new Problem3();
    Problem3 randQueue = new Problem3();


    //---- Insert Tests ----//
    for (int i=0; i<16; i++) {
      orderedQueue.insert(i);
    }
    orderedQueue.isProperHeap(); //only prints if it is NOT a proper heap



    //Build 100 random Queues and test that they are properly formatted
    // Then test that you can delete them until they are empty and they
    // remain properly formatted:
    for (int i=0; i<1000; i++){
      for (int j=0; j<16; j++) {
        int insert = new Random().nextInt(100);
        randQueue.insert(insert);
        randQueue.isProperHeap();
      }
      for (int j=0; j<17; j++) {
        randQueue.deleteMax();
        randQueue.isProperHeap();
      }
      randQueue.resetHeap();
    }


    //Build 100 random Queues and test that they are properly formatted
    // Then test that you can delete them until they are empty and they
    // remain properly formatted:
    for (int i=0; i<1000; i++){
      for (int j=0; j<16; j++) {
        int insert = new Random().nextInt(100);
        randQueue.insert(insert);
        randQueue.isProperHeap();
      }
      for (int j=0; j<17; j++) {
        randQueue.deleteMin();
        randQueue.isProperHeap();
      }
      randQueue.resetHeap();
    }


    //---- Helper Tests ----//
    //if no "failure" messages print to terminal, then all works as expected
    runHelperTests(orderedQueue);


    //---- Find min/max Tests ----//
    if(orderedQueue.findMax().compareTo(15)!=0 || orderedQueue.findMin().compareTo(0)!=0) {
      System.out.println("Failure\nFindMin expected: 0");
      System.out.println("FindMin actual: " + orderedQueue.findMin());
      System.out.println("FindMax expected: 15");
      System.out.println("FindMax actual: " + orderedQueue.findMax());
    }

    if (System.out.equals("")) {
      System.out.println("ALL PASSED");
    }


  }



  public static void runHelperTests(Problem3 queue){
    int parent0 = queue.getParentIdx(0);
    int parent1 = queue.getParentIdx(1);
    int parent2 = queue.getParentIdx(2);
    int parent4 = queue.getParentIdx(4);
    int parent5 = queue.getParentIdx(5);
    int parent6 = queue.getParentIdx(6);
    int parent7 = queue.getParentIdx(7);

    if(parent0!=-1 || parent1!=0 || parent2!=0 || parent4!=1 ||
        parent5!=2 || parent6!=2 || parent7!=3) {
      System.out.println("getParentIdx Failed");
    }

    int LC0 = queue.getLeftChildIdx(0);
    int RC0 = queue.getRightChildIdx(0);
    int LC1 = queue.getLeftChildIdx(1);
    int RC1 = queue.getRightChildIdx(1);
    int LC2 = queue.getLeftChildIdx(2);
    int RC2 = queue.getRightChildIdx(2);
    int LC4 = queue.getLeftChildIdx(4);
    int RC4 = queue.getRightChildIdx(4);
    int LC5 = queue.getLeftChildIdx(5);
    int RC5 = queue.getRightChildIdx(5);

    if(LC0!=1 || RC0!=2 || LC1!=3 || RC1!=4 ||
        LC2!=5 || RC2!=6 || LC4!=9 || RC4!=10 ||
        LC5!=11 || RC5!=12){
      System.out.println("getChildren Indices Failed");
    }
  }

}
