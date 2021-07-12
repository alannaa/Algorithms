package Midterm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem3notes {

  /*
  2.4.29 Min/max priority queue. Design a data type that supports the following
  operations in logarithmic time:
    --insert
    --delete the maximum, and
    --delete the minimum
  and in constant time:
    --find the maximum and
    --find the minimum
  Hint: Use two heaps.
   */

  //TODO: Justification for using ArrayList versus Comparable[]
  //TODO: explain why the 'hint' of using TWO heaps is unnecessary
  //Decision: To use Comparable for ONE factor:
  //https://stackoverflow.com/questions/30009987/space-complexity-of-java-data-structures
  //ArrayList does not "give back" space when elements are removed!! It takes
  // as much space as the LARGEST N in ALL TIME, not the currently largest N.
  // this could lead to running out of space even when you have an empty array.
  //In order to have more control over your space complexity, it is best to use

  //Pro/con of ArrayList: you can remove at a certain index in constant time,
  // and
  // the arrayList class takes care of what to do next. Specifically, it
  // benefits us in terms of Time Complexity, because under the hood the
  // ArrayList does NOT resize (no need to copy everything over etc when you
  // remove) HOWEVER it is BAD for space complexity because then the array
  // will grow and NOT Resize if an element is removed, so actually if you
  // create the initial array with 1 million objects then remove them all,
  // the empty array is still taking up all the 1 million spaces of memory.

  //Pro/con of array:
  //programmer has COMPLETE control over space complexity - they can chose to
  // be lazy and set unused keys to null (bad idea Imo even tho thats what
  // sedgeick recommends) OR they could delete the element and resize with
  // every deletion, however that requires a FOR LOOP to copy everything over
  // to the new array with new array size each time an element is deleted.

  //Because the problem statement only mentions time complexity, it seems
  // like the better of two options that benefits us in terms of time
  // complexity is to use ArrayList, so that we can completely avoid the for
  // loop that is required to resize the array.

  private ArrayList<Comparable> maxHeap;
  private int minIdx;


  //Logarithmic Time
  public void insert(Comparable item){
    //check if its value is < min, in which case you throw it at a leaf
    //and update the min value

    //otherwise:
    //insert at leaf
    //swim up (worst logn+1)

  }

  //Logarithmic Time
  public Comparable deleteMax(){

    //exchange the last element in the array with the root
    //delete the last element in the array
    //swim the new root down into place
    Comparable removeItem = this.maxHeap.get(0);
    this.maxHeap.remove(0);

    Comparable newRoot = this.maxHeap.get(maxHeap.size()-1);
    this.maxHeap.remove(this.maxHeap.get(maxHeap.size()-1));

    this.maxHeap.add(0,newRoot);

    swimDown(0);
    return removeItem;
  }

  //Logarithmic Time
  public Comparable deleteMin(){
    //In a max heap, the min will always be a leaf, so just delete it
    Comparable temp = this.maxHeap.get(minIdx);
    this.maxHeap.remove(minIdx);
    return temp;
  }

  //Constant Time
  public Comparable findMax(){
    return this.maxHeap.get(0);
  }

  //Constant Time
  public Comparable findMin(){
    return this.maxHeap.get(minIdx);
  }

  public void swimDown(int startingIdx){
    int LC = getLeftChildIdx(startingIdx);
    int RC = getRightChildIdx(startingIdx);

    if(maxHeap.get(LC).compareTo(maxHeap.get(startingIdx)) < 0 &&
        maxHeap.get(RC).compareTo(maxHeap.get(startingIdx)) < 0) {
      return;
    } else {
      int greaterChildIdx = LC;
      if (maxHeap.get(RC).compareTo(maxHeap.get(greaterChildIdx)) > 0){
        greaterChildIdx = RC;
      }

      //TODO this exchange does not work well
      Comparable fish = maxHeap.get(startingIdx);
      maxHeap.add(startingIdx, maxHeap.get(greaterChildIdx));
      maxHeap.add(greaterChildIdx, fish);
      swimDown(greaterChildIdx);//the new location of the fish element
      // swimming down
    }
  }

  public int getParentIdx(int index){
    return (index+1)/2;
  }

  public int getLeftChildIdx(int index){
    return 2*(index+1);

  }

  public int getRightChildIdx(int index){
    return (2*(index+1))+1;
  }

  private static void exchange(Comparable[] a, int i, int j){
    Comparable temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  public static void main(String[] args){





  }


}
