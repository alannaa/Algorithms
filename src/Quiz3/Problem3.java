package Quiz3;


public class Problem3 {

  //LSD Class


  //Develop an implementation of LSD string sort that works for
  // variable-length strings.


  public void sort(String[] a) {
    //W removed from the input to accept variable length strings
    // so that the method can be called on a string array without the
    // programmer knowing what's inside the string array ahead of time


    int N = a.length;
    int totalASCIIChars = 256; //represents the number of ASCII characters
    String[] aux = new String[N];

    //manually calculate the maximum string length
    int W = maxStringLength(a);




    //Starting from the LAST possible char in a string,
    // which, for all strings, is the maximum stringlength for that
    // string-array,
    // work our way to the beginning of the string with each iteration
    for (int d = W-1; d >= 0; d--) {

      // Compute frequency counts.
      // getNumComponents is an int-array that has one space open per ASCII character
      // if we come across that ASCII character, increment the int at that idx
      // (they are initialized to 0, that is default behavior for int[] )
      int[] count = new int[totalASCIIChars+1];

      for(int i = 0; i < N; i++) {
        //charAt returns the integer representation of the ASCII character
        //found at the dth digit (letter) of a[i]
        //it will always be a number from 1 to 256
        int digitIndex = charAt(a[i], d);
        count[digitIndex + 1]++;//increment the getNumComponents in the getNumComponents memo
      }


      // Transform counts to indices.
      for (int r = 0; r < totalASCIIChars; r++)
        count[r+1] += count[r];



      // Distribute.
      for(int i = 0; i < a.length; i++) {
        //charAt returns the integer representation of the ASCII character
        //found at the dth digit (letter) of a[i]
        //it will always be a number from 1 to 256
        int digitIndex = charAt(a[i], d);
        //for a[i], locate its final location (index)
        //its final index is stored in getNumComponents[digitIndex]
        //increment digitIndex
        int indexInAuxArray = count[digitIndex]++;
        aux[indexInAuxArray] = a[i];
      }

      // move everything from the auxilary array back to the original, in order
      for (int i = 0; i < N; i++)
        a[i] = aux[i];
    }
  }



  private int maxStringLength(String[] strings) {
    int maxLen = -1;

    //find the string with the maximum length in the string-array
    for(int i=0; i<strings.length; i++) {
      if (strings[i].length() > maxLen) {
        maxLen = strings[i].length();
      }
    }
    return maxLen;
  }

  //returns the integer representation of the ASCII character
  //found at the digit-th letter of string
  private int charAt(String string, int digit) {
    if (digit < string.length()) {
      return string.charAt(digit);
    } else {
      return 0;
    }
  }


  public static void main(String[] args) {
    Problem3 p3 = new Problem3();


    String[] faangs = {"Google", "Apple", "Netflix", "Amazon", "Facebook",
        "Snap", "Instagram", "TikTok"};

    p3.sort(faangs);

    for(String s : faangs){
      System.out.println(s);
    }

  }
}
