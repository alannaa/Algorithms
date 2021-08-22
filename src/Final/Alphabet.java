package Final;


import java.util.HashMap;
import java.util.Map;

public class Alphabet implements AlphabetAPI {

  //charToIndex and indexToChar are basically inverse data structures
  private Map<Character, Integer> charToIndexMap;
  private char[] indexToChar;
  private int size;

  public Alphabet(String string) {
    // indexToChar is created by capturing the input string as an array of char
    // this means that if the input is out of our english-defined order of
    // alphabet, this implementation preserves whatever is the order in the
    // input. This means "asdfghjkl" is a valid alphabet even if it's out of
    // order.
    indexToChar = string.toCharArray();
    size = indexToChar.length;

    charToIndexMap = new HashMap<>();

    //populate the reverse data structure
    for(int index = 0; index < indexToChar.length; index++) {
      charToIndexMap.put(indexToChar[index], index);
    }
  }

  //because we store the two data structures, indexToChar and CharToIndex
  // upon initialization, we do not need to perform any work in the moment
  // that we need to retrieve the information by calling this method:
  public char toChar(int index) {
    if (index < 0 || index >= size()) {
      throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
    }
    return indexToChar[index];
  }

  //similar to the above method, method retrieves the index in constant time
  public int toIndex(char character) {
    if (!contains(character)) {
      throw new IllegalArgumentException("Character " + character + " is not in the alphabet");
    }

    return charToIndexMap.get(character);
  }

  public boolean contains(char character) {
    return charToIndexMap.get(character) != null;
  }

  public int size() {
    return size;
  }

}
