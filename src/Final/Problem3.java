package Final;

public class Problem3 {

  //5.1.12 Alphabet. Develop an implementation of the Alphabet API that is
  // given on page 698 and use it to develop LSD and MSD sorts for general
  // alphabets.

  //LSD: Least significant digit sort
  // using the Alphabet API to generalize the sort
  // The Alphabet API allows you to generalize because the properties of the
  // alphabet can be queried using the alphabet object, eg the number of
  // chars in the alphabet, which index the chars are at, etc.
  public void lsdSort(AlphabetAPI alphabet, String[] inputArray,
      int stringsLength) {
    System.out.println("\nInput: ");
    for (String s : inputArray){
      System.out.print(s + " ");
    }
    //we take in stringsLength because LSD sort relies on all strings in the
    // input array being the same length. That is part of what makes it
    // stable. We are always conparing the SAME INDEX on ALL of the input
    // strings. If the input strings were of variable length, it would be
    // difficult to keep track of which index we are in for each string,
    // unless we added arbitrary spaces at the end of shorter strings until
    // all strings reach the length of the longest string. Although that
    // might not work if spaces are considered < chars. Or use MSD.

    int alphabetSize = alphabet.size();
    //thisIterationOrder is an auxilary array that temporarily stores the
    // order of the strings during one iteration of the procedure before
    // copying it back into the inputArray
    String[] thisIterationOrder = new String[inputArray.length];

    //This is how we target one (and the same) index in each string at the
    // same time. Start at the last char and work our way backward one char
    // at a time.
    for (int digit = stringsLength - 1; digit >= 0; digit--) {
      //This computes the frequency with which a particular char appears in
      // the input array.
      int count[] = new int[alphabetSize + 1];
      for (int i = 0; i < inputArray.length; i++) {
        //digitIndex is the index that the char appears in the Alphabet object
        int digitIndex = alphabet.toIndex(inputArray[i].charAt(digit));
        //we start at index 1, not 0, to leave space at the front of the array
        //so that we can perform the next step (transferring to indices)
        //by combining the sum of all previous frequencies with the current one.
        //if we started storing the frequencies at 0, then when we add the
        // frequency of the 0th index with the previous one, the procedure
        // below would malfunction without anything before the 0th index to
        // combine with.
        count[digitIndex+1]++;
      }

      System.out.println("\nFrequencies:");
      for (int i: count) {
        System.out.print(i + " ");
      }

      // Transform counts to indices:
      // because we need the counts to guide us back to which index the
      // element will be placed in the thisIterationOrder array,
      //This works by taking the actual frequency,
      // which is stored at count[r+1]
      // and adding it to the frequency of the previous-indexed char.
      // this is the key mechanism that performs the sort.
      // by preserving the frequency BUT adding it to the previous indexed char
      // we ensure that there is space in the thisIterationOrder to account for
      // same-digit strings. idk if im explaining this clearly
      // example, if the first three chars are "a" and that is followed by "c"
      // we want the indices for the a's to be 0,1,2
      // then, maybe there is only 1 c, but we cant put that c in index 1
      // we need to put it in index 2+1 = 3 -> we put it in index 3 because
      // we had to make space for all of the same-digit chars (the a's) in a row
      for (int r = 0; r < alphabetSize; r++) {
        count[r + 1] += count[r];
      }

      System.out.println("\nTranslate to indices:");
      for (int i: count) {
        System.out.print(i + " ");
      }

      // Distribute the chars over the `thisIterationOrder` array
      for (int i = 0; i < inputArray.length; i++) {
        int digitIndex = alphabet.toIndex(inputArray[i].charAt(digit));
        //count[digitindex] gets incremented with every time we call upon that
        //digit index's count in order to advance the auxarray index for
        // another space that contains that same digit, if that makes sense
        // for example: for input "a", "b", "c", "c", "c"
        // when digitIndex = 0 ; AuxArrIdx = 0 (a)
        // when digitIndex = 1 ; AuxArrIdx = 1 (b)
        // when digitIndex = 2 ; AuxArrIdx = 2 (c)
        // THEN digitIndex still = 2 ; but AuxArrIdx = 3 (c)
        // then digitIndex still = 2 ; but AuxArrIdx = 4 (c)
        int indexInAuxArray = count[digitIndex]++;
        thisIterationOrder[indexInAuxArray] = inputArray[i];
      }

      System.out.println("\nAfter Distribution:");
      for (int i: count) {
        System.out.print(i + " ");
      }

      System.out.println("\nResulting string array:");
      for (String s: thisIterationOrder) {
        System.out.print(s + " ");
      }
      System.out.println();

      // we copy them back at EACH iteration INSTEAD of copying back all at
      // once after the final iteration and sort has happened.
      //this is the line of the code that helps make it STABLE.
      // similar to mergesort that maintains stability by piecing together
      // components in ORDER throughout the ENTIRE sort (not just once at the
      // end) this, too, maintains stability by processing strings in order
      // and copying strings back into the input array in the order they are
      // iterated over (their original order) even during the in-between
      // steps, so that the similar-digit elements never lose their order
      for (int i = 0; i < inputArray.length; i++) {
        inputArray[i] = thisIterationOrder[i];
      }
    }
  }



  //MSD sort:
  //To even further generalize the above LSD sort, to be able to use variable
  // length string sizes
  // differs from LSD because you observe chars in left-to-right order
  //uses a partitioning similar to that of QuickSort
  public void msdSort(AlphabetAPI alphabet, String[] inputArray) {
    //start the recursive call to sort:
    sort(alphabet, inputArray, 0, inputArray.length - 1, 0);
  }

  private void sort(AlphabetAPI alphabet,String[] inputArray, int low, int high,
      int digit) {
    //thisIterationOrder is an auxilary array that temporarily stores the
    // order of the strings during one iteration of the procedure before
    // copying it back into the inputArray
    String[] thisIterationOrder = new String[inputArray.length];

    int alphabetSize = alphabet.size();

    //this if statement stops the code from running into stackOverflow
    //it is the base case/stopping statement for recursion
    //similar to the stopping case of quicksort
    if (low >= high){
      return;
    }

    // Compute frequency counts
    // this works similarly to the frequency counts that we performed in lsd
    int[] count = new int[alphabetSize + 2];
    for (int i = low; i <= high; i++) {
      int digitIndex = charAt(alphabet, inputArray[i], digit) + 2;
      count[digitIndex]++;
    }

    // Transform counts to indices
    //again this works exactly like lsd (same comment):
    // because we need the counts to guide us back to which index the
    // element will be placed in the thisIterationOrder array,
    //This works by taking the actual frequency,
    // which is stored at count[r+1]
    // and adding it to the frequency of the previous-indexed char.
    // this is the key mechanism that performs the sort.
    // by preserving the frequency BUT adding it to the previous indexed char
    // we ensure that there is space in the thisIterationOrder to account for
    // same-digit strings. idk if im explaining this clearly
    // example, if the first three chars are "a" and that is followed by "c"
    // we want the indices for the a's to be 0,1,2
    // then, maybe there is only 1 c, but we cant put that c in index 1
    // we need to put it in index 2+1 = 3 -> we put it in index 3 because
    // we had to make space for all of the same-digit chars (the a's) in a row
    for (int r = 0; r < alphabetSize + 1; r++) {
      count[r + 1] += count[r];
    }

    // Distribute, same as lsd:
    //count[digitindex] gets incremented with every time we call upon that
    //digit index's count in otder to advance the auxarray index for
    // another space that contains that same digit, if that makes sense
    // for example: for input "a", "b", "c", "c", "c"
    // when digitIndex = 0 ; AuxArrIdx = 0 (a)
    // when digitIndex = 1 ; AuxArrIdx = 1 (b)
    // when digitIndex = 2 ; AuxArrIdx = 2 (c)
    // THEN digitIndex still = 2 ; but AuxArrIdx = 3 (c)
    // then digitIndex still = 2 ; but AuxArrIdx = 4 (c)
    for (int i = low; i <= high; i++) {
      int digitIndex = charAt(alphabet, inputArray[i], digit) + 1;
      int indexInAuxArray = count[digitIndex]++;
      thisIterationOrder[indexInAuxArray] = inputArray[i];
    }

    // Copy back only the subarray that was addressed in this iteration from
    // low to high
    //the `thisIterationOrder` array starts at zero, so the indices do not
    // match up exactly with the input array. Therefore, store in the input
    // array what is at index [i-low] in the `thisIterationOrder` array
    for (int i = low; i <= high; i++) {
      inputArray[i] = thisIterationOrder[i - low];
    }

    // Recursively sort for each character value, offsetting the lows and
    // highs by the number of digits we made space for index r char
    // for example, when r=0, the next 'low' value will be
    // 0 + [the number of instances of char-at-digitIndex-0]
    // say the char at index 0 is "a" and there were 2 strings that began
    // with "a"
    // then you want the next low to be 0+3 = 3 so that we can continue sorting
    // Also, we increment digit by 1 because in this next recursive call to
    // sort, we actually want to sort the strings from their next digit
    // for example, if we just sorted all of the strings that begin with "a",
    // we now want to look at all those strings that begin with "a" and sort
    // them based on their 2nd, 3rd, 4th letters, etc
    for (int r = 0; r < alphabetSize; r++) {
      sort(alphabet, inputArray,
          low + count[r], low + count[r + 1] - 1, digit + 1);
    }
  }

  //helper function for finding the index of the char at digit
  //OR signaling that the method was called on a digit that is beyond the
  // length of the string. This helps in handling strings of arbitrary size
  // in the input.
  private int charAt(AlphabetAPI alphabet, String string, int digit) {
    if (digit < string.length()) {
      return alphabet.toIndex(string.charAt(digit));
    } else {
      return -1;
    }
  }




  public static void main(String[] args) {

    Problem3 p3 = new Problem3();

    System.out.println("\nsimple lsd");
    Alphabet abc = new Alphabet("abc");
    String[] inputLsd = {"ca", "bc", "bc", "ab", "ac"};
    int stringLength = 2;
    p3.lsdSort(abc, inputLsd, stringLength);
    for (String s : inputLsd){
      System.out.println(s);
    }

    System.out.println("\nsimple msd");
    String[] inputMsd = {"ca", "bc", "bc", "ab", "ac"};
    p3.msdSort(abc, inputMsd);
    for (String s : inputMsd){
      System.out.println(s);
    }


    System.out.println("\nlonger lsd");
    Alphabet abcdefg = new Alphabet("abcdefg");
    String[] longerInputlsd = {"adcbeb", "bcfadc", "eebcee", "efacfe",
        "abcdfe"};
    stringLength = 6;

    p3.lsdSort(abcdefg, longerInputlsd, stringLength);
    for (String s : longerInputlsd){
      System.out.println(s);
    }

    System.out.println("\nlonger msd");
    String[] longerInputMsd = {"adcbeb", "bcfadc", "eebcee", "efacfe",
        "abcdfe"};
    p3.msdSort(abcdefg, longerInputMsd);
    for (String s : longerInputMsd){
      System.out.println(s);
    }

    System.out.println("\nvariable length long msd");
    String[] superlongInputMsd = {"adcbebadfceacb", "bcfadc", "efbcebcee",
        "efacfe", "abce"};
    p3.msdSort(abcdefg, superlongInputMsd);
    for (String s : superlongInputMsd){
      System.out.println(s);
    }

    System.out.println("\nvariable length long msd");
    String[] superlongInputMsd2 = {
        "adcbebadfceacbadcbebadfceacbaebadfceacb", "bcfadcbcfadcbcfadc", "efbcebceeefbcebcee",
        "efacfeefacfeefacfeefacfeefacfeefacfe", "abceabceabce", "d"};
    p3.msdSort(abcdefg, superlongInputMsd2);
    for (String s : superlongInputMsd2){
      System.out.println(s);
    }

    System.out.println("\ntest: including a letter not present in alphabet(d)");
    String[] notAbc = {"abcd", "abcc", "cbaabcaa", "cbbbba"};
    try {
      p3.msdSort(abc, notAbc);
    } catch (IllegalArgumentException e) {
      System.out.println("Error working as expected, output is: " + e.toString());
    }

  }
}
