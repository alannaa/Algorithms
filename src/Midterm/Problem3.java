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


  //TODO the memspace field makes the time complexity slow down but improves
  // space complexity (similarly to how ArrayList works)
  public int memSpace = 1; //the total amount of memory currently allocated
  public int heapSize = 0;
  public Comparable[] maxHeap = new Comparable[memSpace];
  public Comparable[] minHeap = new Comparable[memSpace];
  public int minIndexInMaxHeap = 0;//min value's index on max heap
  public int maxIndexInMinHeap = 0;//max value's index on min heap


  //Occurs in Logarithmic Time
  // Swimming up worst case is LogN+1 compares and swaps.
  // That is because the depth of the heap is LogN and when you perform
  // swimming up the element swimming will be compared/swapped with its
  // parent at most the number of layers there are (because we compare with
  // the layer above, not with any neighbors left or right).
  // LogN depth is guaranteed because we are storing the data structure as an
  // array and then using indices to determine the layers of the heap.
  // thus, the tree stays "balanced" and in heap formation if you print (as I do
  // in printHeap method) the array out with a new line at every index that
  // is a power of 2.
  public void insert(Comparable item) {
    if (heapSize == memSpace) {
      doubleSize();
    }
    // Insert item at both heaps' leaf indexes; increase heapSize; swim up
    // (will handle swimming up both heap directions, min heap and max heap)
    addToBothHeaps(heapSize, item);
  }



  //Occures in Logarithmic Time (worst case 3logN)
  // note that the heapSize is subtracted before the work of deleting Max.

  // Deleting max from the max heap costs 2logN due to having to compare and
  // possibly swap with two children at each depth level (depth == log n).

  // Deleting max from the min heap costs logN.
  // Method: Arbitrarily replace the previous max element with the final
  // element of the array. Then, swim up that element in its new place.
  // Swimming up costs LogN because LogN is largest possible number of times
  // you would have to compare an element to its parent to swim it into place.

  //Finally, add logN with 2logN to get the final time complexity of 3logN
  // (and drop the constant for generally logarithmic time)
  public void deleteMax(){
    if(heapSize==0){
      return;
    }

    heapSize--;
    deleteMaxFromMaxHeap();
    deleteMaxFromMinHeap();

    if(heapSize <= memSpace/2){
      downSize();
    }
  }

  private void deleteMaxFromMaxHeap(){
    //exchange the last element in the array with the root
    exchange(maxHeap, "MAX", 0, heapSize);
    maxHeap[heapSize] = null; //null out the deleted item
    swimDownMaxHeap(0);//swim the new root down into place
  }

  private void deleteMaxFromMinHeap(){
    //ONLY SEARCH THROUGH LEAVES for the new Max value, it is guaranteed
    // to be there, so you can reduce your search space by half at the start
    int leafLevel = (heapSize/2)-1>0 ? (heapSize/2)-1 : 0;

    if(maxIndexInMinHeap == heapSize) {
      minHeap[maxIndexInMinHeap] = null;
      for(int i = leafLevel; i<heapSize; i++) {
        if (minHeap[i]==findMax()) {
          maxIndexInMinHeap = i;
        }
      }

    } else {
      int initMax = maxIndexInMinHeap;
      //Exchange the max value with the last array element
      exchange(minHeap, "MIN", maxIndexInMinHeap, heapSize);
      minHeap[heapSize] = null; //null out deleted item

      for(int i = leafLevel; i<heapSize; i++) {
        if (minHeap[i]==findMax()) {
          maxIndexInMinHeap = i;
        }
      }
      //swim new root of sub heap (starting from where the prev max was) into
      // place. The reason we swim up and not down is because remember the
      // max value of a min heap is guaranteed to be in the leaves. So there
      // is no need to swim down. But it is possible that the exchange caused
      // a small value to be placed under a larger value, which needs to be
      // adjusted.
      swimUpMinHeap(initMax); //worse case logN
    }
  }



  //Occures in Logarithmic Time (worst case 2logN)
  // Explanation is the exact same as deleteMax but with the order of logic
  // reversed
  public void deleteMin(){
    if(heapSize==0){
      return;
    }

    heapSize--;
    deleteMinFromMinHeap();
    deleteMinFromMaxHeap();

    if(heapSize <= memSpace/2){
      downSize();
    }
  }

  private void deleteMinFromMinHeap(){
    //exchange the last element in the array with the root
    exchange(minHeap, "MIN", 0, heapSize);
    minHeap[heapSize] = null; //null out the deleted item
    swimDownMinHeap(0);//swim the new root down into place
  }

  private void deleteMinFromMaxHeap(){
    //ONLY SEARCH THROUGH LEAVES for the new Min value, it is guaranteed
    // to be there, so you can reduce your search space by half at the start
    int leafLevel = (heapSize/2)-1>0 ? (heapSize/2)-1 : 0;

    if(minIndexInMaxHeap == heapSize) {
      maxHeap[minIndexInMaxHeap] = null;
      for(int i = leafLevel; i<heapSize; i++) {
        if (maxHeap[i]==findMin()) {
          minIndexInMaxHeap = i;
        }
      }

    } else {
      int initMin = minIndexInMaxHeap;
      //Exchange the max value with the last array element
      exchange(maxHeap, "MAX", minIndexInMaxHeap, heapSize);
      maxHeap[heapSize] = null; //null out deleted item

      for(int i = leafLevel; i<heapSize; i++) {
        if (maxHeap[i]==findMin()) {
          minIndexInMaxHeap = i;
        }
      }
      //swim new root of sub heap (starting from where the prev min was) into
      // place. The reason we swim up and not down is because remember the
      // min value of a max heap is guaranteed to be in the leaves. So there
      // is no need to swim down. But it is possible that the exchange caused
      // a larger value to be placed under a smaller value, which needs to be
      // adjusted.
      swimUpMaxHeap(initMin); //worse case logN
    }
  }





  //Occurs in Constant time thanks to the use of both heap directions
  // max will always lie at the 0th index of the properly formatted maxHeap
  public Comparable findMax() {
    return maxHeap[0];
  }

  //Occurs in Constant time thanks to the use of both heap directions
  // min will always lie at the 0th index of the properly formatted minHeap
  public Comparable findMin() {
    return minHeap[0];
  }





  //----Swim Helpers----//
  private void addToBothHeaps(int index, Comparable item){
    maxHeap[index] = item;
    minHeap[index] = item;
    heapSize++;
    swimUpMaxHeap(heapSize - 1);
    swimUpMinHeap(heapSize - 1);
  }

  private void swimUpMaxHeap(int startingIdx) {
    int parentIdx = getParentIdx(startingIdx);
    //base case/stopping case: if we've reached the root
    if (parentIdx < 0) {
      return;
    }
    //if the value at the starting index is greater than its parent...
    if (maxHeap[startingIdx].compareTo(maxHeap[parentIdx]) > 0) {
      exchange(maxHeap, "MAX", startingIdx, parentIdx);
      swimUpMaxHeap(parentIdx);
    } else {
      if (maxHeap[startingIdx].compareTo(maxHeap[minIndexInMaxHeap]) < 0){
        minIndexInMaxHeap = startingIdx;
      }
    }
  }

  private void swimUpMinHeap(int startingIdx) {
    int parentIdx = getParentIdx(startingIdx);
    //base case/stopping case: if we've reached the root
    if (parentIdx < 0) {
      return;
    }
    //if the value at the starting index is less than its parent...
    if (minHeap[startingIdx].compareTo(minHeap[parentIdx]) < 0) {
      exchange(minHeap, "MIN", startingIdx, parentIdx);
      swimUpMinHeap(parentIdx);
    }else {
      if (minHeap[startingIdx].compareTo(minHeap[maxIndexInMinHeap]) > 0){
        maxIndexInMinHeap = startingIdx;
      }
    }
  }

  private void swimDownMaxHeap(int startingIdx) {
    int LC = getLeftChildIdx(startingIdx);  //-1 if index out of bounds
    int RC = getRightChildIdx(startingIdx); //-1 if index out of bounds

    //if both children are valid and in bounds
    if (LC >= 0 && RC >= 0) {
      //If both children are smaller than this current element, all is in place
      if (maxHeap[LC].compareTo(maxHeap[startingIdx]) < 0 &&
          maxHeap[RC].compareTo(maxHeap[startingIdx]) < 0) {
        return;
      } else {
        int largerChildIdx = (maxHeap[LC].compareTo(maxHeap[RC]) > 0) ? LC : RC;
        exchange(maxHeap, "MAX", startingIdx, largerChildIdx);
        swimDownMaxHeap(largerChildIdx);
      }
    } else {
      //if the function gets here, then either only one child is valid
      //or neither child is valid (in which case the work is done)
      if (LC >= 0) {
        if (maxHeap[LC].compareTo(maxHeap[startingIdx]) > 0) {
          exchange(maxHeap, "MAX", startingIdx, LC);
          swimDownMaxHeap(LC);
        }
      }
      if (RC >= 0) {
        if (maxHeap[RC].compareTo(maxHeap[startingIdx]) > 0) {
          exchange(maxHeap, "MAX", startingIdx, RC);
          swimDownMaxHeap(RC);
        }
      }
    }
  }

  private void swimDownMinHeap(int startingIdx) {
    int LC = getLeftChildIdx(startingIdx);  //-1 if index out of bounds
    int RC = getRightChildIdx(startingIdx); //-1 if index out of bounds

    //if both children are valid and in bounds
    if (LC >= 0 && RC >= 0) {
      //If both children are greater than this current element, all is in place
      if (minHeap[LC].compareTo(minHeap[startingIdx]) > 0 &&
          minHeap[RC].compareTo(minHeap[startingIdx]) > 0) {
        return;
      } else {
        int smallerChildIdx = (minHeap[LC].compareTo(minHeap[RC]) < 0) ? LC :
            RC;
        exchange(minHeap, "MIN", startingIdx, smallerChildIdx);
        swimDownMinHeap(smallerChildIdx);
      }
    } else {
      //if the function gets here, then either only one child is valid
      //or neither child is valid (in which case the work is done)
      if (LC >= 0) {
        if (minHeap[LC].compareTo(minHeap[startingIdx]) < 0) {
          exchange(minHeap, "MIN", startingIdx, LC);
          swimDownMinHeap(LC);
        }
      }
      if (RC >= 0) {
        if (minHeap[RC].compareTo(minHeap[startingIdx]) < 0) {
          exchange(minHeap, "MIN", startingIdx, RC);
          swimDownMinHeap(RC);
        }
      }
    }
  }





  //----Getters----//
  int getParentIdx(int index) {
    if (index == 0) {
      return -1;
    }
    return ((index + 1) / 2) - 1;
  }

  int getLeftChildIdx(int index) {
    int idx = 2 * (index + 1) - 1;
    if (idx < heapSize) {
      return idx;
    } else {
      return -1;
    }
  }

  int getRightChildIdx(int index) {
    int idx = (2 * (index + 1));
    ;
    if (idx < heapSize) {
      return idx;
    } else {
      return -1;
    }
  }





  //----Utility/For Testing Purposes----//
  private void exchange(Comparable[] heap, String dir, int i, int j) {
    Comparable temp = heap[i];
    heap[i] = heap[j];
    heap[j] = temp;
    if (dir.equals("MAX")) {
      if (minIndexInMaxHeap == i) {
        minIndexInMaxHeap = j;
        return;
      }
      if (minIndexInMaxHeap == j) {
        minIndexInMaxHeap = i;
        return;
      }
    }
    if (dir.equals("MIN")) {
      if (maxIndexInMinHeap == i) {
        maxIndexInMinHeap = j;
        return;
      }
      if (maxIndexInMinHeap == j) {
        maxIndexInMinHeap = i;
      }
    }
  }

  private void doubleSize() {
    memSpace *= 2;

    Comparable[] reSized = new Comparable[memSpace];
    for (int i = 0; i < heapSize; i++) {
      reSized[i] = maxHeap[i];
    }
    maxHeap = reSized;

    reSized = new Comparable[memSpace];
    for (int i = 0; i < heapSize; i++) {
      reSized[i] = minHeap[i];
    }
    minHeap = reSized;
  }

  private void downSize() {
    if (memSpace <= 1) {
      return;
    }
    memSpace /= 2;
    Comparable[] reSized = new Comparable[memSpace];
    for (int i = 0; i < heapSize; i++) {
      reSized[i] = maxHeap[i];
    }
    maxHeap = reSized;

    reSized = new Comparable[memSpace];
    for (int i = 0; i < heapSize; i++) {
      reSized[i] = minHeap[i];
    }
    minHeap = reSized;
  }

  void resetHeap(){
    memSpace = 1;
    heapSize = 0;
    Comparable[] maxHeap = new Comparable[memSpace];
    Comparable[] minHeap = new Comparable[memSpace];
    minIndexInMaxHeap = 0;
    maxIndexInMinHeap = 0;
  }

  boolean isProperHeap(){
    for(int i=0; i<heapSize; i++){
      int LC = getLeftChildIdx(i);
      int RC = getRightChildIdx(i);

      if(LC>=0) {
        if (maxHeap[i].compareTo(maxHeap[LC]) < 0){
          System.out.println("------FAILED: Not a Properly Formatted Max Heap"
              + "\nFailed on index: " + i
              + "\nFailed on child: " + LC + "\n");
          this.printHeap();
        }
      } else if (RC >=0) {
        if (maxHeap[i].compareTo(maxHeap[RC]) < 0){
          System.out.println("------FAILED: Not a Properly Formatted Max Heap"
              + "\nFailed on index: " + i
              + "\nFailed on child: " + RC + "\n");
          this.printHeap();
        }
      }
    }
    for(int i=0; i<heapSize; i++){
      int LC = getLeftChildIdx(i);
      int RC = getRightChildIdx(i);

      if(LC>=0) {
        if (minHeap[i].compareTo(minHeap[LC]) > 0){
          System.out.println("------FAILED: Not a Properly Formatted Min Heap"
              + "\nFailed on index: " + i
              + "\nFailed on child: " + LC + "\n");
          this.printHeap();
        }
      } else if (RC >=0) {
        if (minHeap[i].compareTo(minHeap[RC]) > 0){
          System.out.println("------FAILED: Not a Properly Formatted Min Heap"
              + "\nFailed on index: " + i
              + "\nFailed on child: " + RC + "\n");
          this.printHeap();
        }
      }
    }
    return true;
  }

  void printHeap() {
    System.out.println("memory space: " + memSpace);
    System.out.println("heap size: " + heapSize);
    System.out.println("min idx in max heap: " + minIndexInMaxHeap);
    System.out.println("max idx in min heap: " + maxIndexInMinHeap);
    for (int i = 0; i < heapSize; i++) {
      if (i != 0 && (i + 1 & (i)) == 0) {
        System.out.print("\n");
      }
      System.out.print(maxHeap[i] + " ");
    }
    System.out.print("\n\n");
    for (int i = 0; i < heapSize; i++) {
      if (i != 0 && (i + 1 & (i)) == 0) {
        System.out.print("\n");
      }
      System.out.print(minHeap[i] + " ");
    }
    System.out.print("\n---------------\n\n");
  }

}
