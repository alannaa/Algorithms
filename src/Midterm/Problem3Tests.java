package Midterm;

public class Problem3Tests {

  public static void main(String[] args){

    //pq=Priority Queue
    Problem3 pq = new Problem3();

    //---- Helper Tests ----//
    int parent0 = pq.getParentIdx(0);
    int parent1 = pq.getParentIdx(1);
    int parent2 = pq.getParentIdx(2);
    int parent4 = pq.getParentIdx(4);
    int parent5 = pq.getParentIdx(5);
    int parent6 = pq.getParentIdx(6);
    int parent7 = pq.getParentIdx(7);

    if(parent0!=-1 || parent1!=0 || parent2!=0 || parent4!=1 ||
        parent5!=2 || parent6!=2 || parent7!=3) {
      System.out.println("getParentIdx Failed");
    }

    int LC0 = pq.getLeftChildIdx(0);
    int RC0 = pq.getRightChildIdx(0);
    int LC1 = pq.getLeftChildIdx(1);
    int RC1 = pq.getRightChildIdx(1);
    int LC2 = pq.getLeftChildIdx(2);
    int RC2 = pq.getRightChildIdx(2);
    int LC4 = pq.getLeftChildIdx(4);
    int RC4 = pq.getRightChildIdx(4);
    int LC5 = pq.getLeftChildIdx(5);
    int RC5 = pq.getRightChildIdx(5);

    if(LC0!=1 || RC0!=2 || LC1!=3 || RC1!=4 ||
        LC2!=5 || RC2!=6 || LC4!=9 || RC4!=10 ||
        LC5!=11 || RC5!=12){
      System.out.println("getChildren Indices Failed");
    }

    //---- Insert Tests ----//
    pq.printHeap();
    pq.insert(10);
    pq.insert(1);
    pq.insert(2);
    pq.insert(6);
    pq.insert(12);
    pq.insert(3);
    pq.insert(4);
    pq.insert(15);
    pq.insert(-1);
    pq.insert(-1);
    pq.insert(-1);
    pq.insert(-1);
    pq.insert(-1);
    pq.insert(-1);
    pq.insert(4);
    pq.printHeap();


    //---- Find min/max Tests ----//
    if(pq.findMax().compareTo(15)!=0 || pq.findMin().compareTo(-1)!=0) {
      System.out.println("FindMin or FindMax Failure");
    }

    //---- Delete min/max Tests ----//
    pq.deleteMin();
    pq.deleteMax();
    pq.deleteMax();
    pq.deleteMax();
    pq.deleteMax();
    pq.deleteMax();
    pq.deleteMax();
    pq.deleteMax();

    pq.printHeap();

    pq.deleteMin();
    pq.deleteMin();
    pq.deleteMin();
    pq.deleteMin();
    pq.deleteMin();
    //pq.deleteMin();
    //pq.deleteMin();
    //pq.deleteMin();

    pq.printHeap();





  }

}
