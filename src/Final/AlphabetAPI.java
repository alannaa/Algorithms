package Final;

public interface AlphabetAPI {

  char toChar(int index);            // convert index to corresponding alphabet char
  int toIndex(char character);       // convert character to an index between 0 and size - 1
  boolean contains(char character);  // is character in the alphabet?
  int size();                        // radix (number of characters in alphabet)

}
