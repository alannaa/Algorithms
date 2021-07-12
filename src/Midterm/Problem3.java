package Midterm;

public class Problem3 {

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

  public int memSpace = 1;
  public Comparable[] maxHeap = new Comparable[memSpace];
  public Comparable[] minHeap = new Comparable[memSpace];
  public int heapSize = 0;
  public int minValIdx;


  //Logarithmic Time LogN
  public void insert(Comparable item){
    if(heapSize == memSpace) {
      doubleSize();
    }

    // Insert at leaf, then update heap stats accordingly
    maxHeap[heapSize] = item;
    minHeap[heapSize] = item;
    heapSize++;

//    // If inserting the first item
//    if (heapSize == 0){
//      minValIdx = 0;
//      heapSize++;
//      return;
//    }

    swimUp(heapSize);


    //check if the new item has a value that is less than the cur min value
    // -If yes: update the minValIdx, increase heap size, keep item at leaf
    if(item.compareTo(maxHeap[minValIdx]) < 0){
      minValIdx = heapSize;
      heapSize++;
    } else {
      //-If no: swim up (worst logn+1)
      swimUp(heapSize);
      heapSize++;
    }
  }

  //Logarithmic Time 2logN
  public Comparable deleteMax(){
    //exchange the last element in the array with the root
    exchange(0, heapSize-1);

    //delete the last element in the array
    Comparable removeItem = maxHeap[heapSize-1];
    heapSize--;
    scanLeavesForMin(heapSize);

    //swim the new root down into place
    swimDown(0);
    if(heapSize <= memSpace/2){
      downSize();
    }
    return removeItem;
  }

  //Logarithmic Time
  public Comparable deleteMin(){
    //In a max heap, the min will always be a leaf, so just delete it
    Comparable removeItem = maxHeap[minValIdx];
    heapSize--;

    scanLeavesForMin(minValIdx);

    if(heapSize <= memSpace/2){
      downSize();
    }
    return removeItem;
  }

  private void scanLeavesForMin(int initMinIdx){
    maxHeap[initMinIdx] = null;
    //set an arbitrary new minValidIdx, the leftmost leaf on the leaf level
    int leafLevel = (memSpace/2)-1;
    minValIdx = leafLevel;

    //search through the leaves for the new min val (at MOST n/2 cycles)
    for (int i=leafLevel+1; i<heapSize; i++){
      if (i>=initMinIdx){
        //copy over elements that came after the deleted value to avoid a gap
        maxHeap[i] = maxHeap[i+1];
      }
      if (maxHeap[i].compareTo(maxHeap[minValIdx]) < 0) {
        minValIdx = i;
      }
    }


  }

  //Complete: Constant time
  public Comparable findMax(){
    return maxHeap[0];
  }

  //Complete: Constant time
  public Comparable findMin(){
    return maxHeap[minValIdx];
  }

  private void doubleSize(){
    memSpace *=2;
    Comparable[] reSized = new Comparable[memSpace];
    for (int i=0; i < heapSize; i++){
      reSized[i] = maxHeap[i];
    }
    maxHeap = reSized;
    //Old maxHeap will be garbage collected
  }

  private void downSize(){
    if (memSpace<=1){
      return;
    }
    memSpace /=2;
    Comparable[] reSized = new Comparable[memSpace];
    for (int i=0; i < heapSize; i++){
      reSized[i] = maxHeap[i];
    }
    maxHeap = reSized;
    //Old maxHeap will be garbage collected
  }

  private void swimUp(int startingIdx){
    int parentIdx = getParentIdx(startingIdx);
    if(parentIdx<0){
      return;
    }
    //if the value at the starting index is greater than its parent...
    if(maxHeap[startingIdx].compareTo(maxHeap[parentIdx]) > 0){
      exchange(startingIdx, parentIdx);

      if(parentIdx==minValIdx){
        minValIdx = startingIdx;
      }
      swimUp(parentIdx);
    }
    //otherwise, stop the recursion here
  }

  private void swimDown(int startingIdx){
    //test to make sure LC and RC are in bounds
    int LC = getLeftChildIdx(startingIdx);
    int RC = getRightChildIdx(startingIdx);

    int greaterChildIdx = -1;

    if(LC < heapSize) {
      greaterChildIdx=LC;
    }

    if(RC < heapSize) {
      greaterChildIdx=RC;
    }

    if(greaterChildIdx==-1 || (
        maxHeap[LC].compareTo(maxHeap[startingIdx]) < 0 &&
        maxHeap[RC].compareTo(maxHeap[startingIdx]) < 0)) {
      return;
    } else {
      //at this point we know at least one child is fair game to exchange with
      if(greaterChildIdx==RC && LC<heapSize) {
        if (maxHeap[LC].compareTo(maxHeap[greaterChildIdx]) > 0) {
          greaterChildIdx = LC;
        }
      }
      exchange(startingIdx, greaterChildIdx);
      swimDown(greaterChildIdx);
    }
  }

  public int getParentIdx(int index){
    if(index==0){
      return -1;
    }
    return ((index+1)/2)-1;
  }

  public int getLeftChildIdx(int index){
    return 2*(index+1)-1;
  }

  public int getRightChildIdx(int index){
    return (2*(index+1));
  }

  private void exchange(int i, int j){
    Comparable temp = maxHeap[i];
    maxHeap[i] = maxHeap[j];
    maxHeap[j] = temp;
  }

  public void printHeap(){
    System.out.println("memory space: " + memSpace);
    System.out.println("heap size: " + heapSize);
    System.out.println("min idx: " + minValIdx);
    for(int i=0; i<heapSize; i++){
      if (i!=0 && (i+1 & (i)) == 0) {
        System.out.print("\n");
      }
      System.out.print(maxHeap[i] + " ");
    }
    System.out.print("\n\n");
  }

}
