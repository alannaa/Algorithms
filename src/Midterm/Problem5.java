package Midterm;

public class Problem5 {

  /*
  3.4.17 Show the result of using the delete() method on page
  471 to delete C from the table resulting from using LinearProbingHashST with
  our standard indexing client (shown on page 469).


***There is a typo in the table on page 469. C's value should be set to 4 but
 *  it is set to 5 in the table. I have adjusted my table here to reflect the
  *  correct initial version of the table.***


  Show the result of delete(C)

  * Initial Table
  IDX   [0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15]
  KEYS  [P  M        A  C  S  H  L     E               size   X ]
  VALS  [10 9        8  4  0  5  11    12              3   7 ]

  * Step 1 delete C
  IDX   [0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15]
  KEYS  [P  M        A  NU S  H  L     E               size   X ]
  VALS  [10 9        8  NU 0  5  11    12              3   7 ]

  * Step 2 redo i=6 (S) which belongs at index 6 (no change)
  IDX   [0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15]
  KEYS  [P  M        A  NU S  H  L     E               size   X ]
  VALS  [10 9        8  NU 0  5  11    12              3   7 ]

  * Step 3 redo i=7 (H) which belongs at index 4, but its occupied by A
  * so it gets pushed into index 5
  IDX   [0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15]
  KEYS  [P  M        A  H  S  NU L     E               size   X ]
  VALS  [10 9        8  5  0  NU 11    12              3   7 ]

  * Step 4 redo i=8 (L) which belongs at index 6, but its occupied by H
  * so it gets the next best option which is index 7
  IDX   [0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15]
  KEYS  [P  M        A  S  H  L  NU    E               size   X ]
  VALS  [10 9        8  0  5  11 NU    12              3   7 ]

  * Step 5 is that keys[9] is NULL so the while loop will stop moving things
  * around to their appropriate place and the deletion is done.



  Also: Explain what the delete() code on page 471 is doing in English.
  (see below in this file for comments explaining in English)
   */

  //Linear Probing Hash class from the book
  public class LinearProbingHashST<Key, Value> {
    private int N;       //the current size of the hashtable
    private int M = 16;  //the memory space currently allocated
    private Key[] keys;  //array of keys (key[i] --> value[i])
    private Value[] vals;//array of values

    public LinearProbingHashST(int M) {
      keys = (Key[])   new Object[M];
      vals = (Value[]) new Object[M];
    }


    private int hash(Key key) {
      return (key.hashCode() & 0x7fffffff) % M;
    }

    private void resize(int cap) {
      LinearProbingHashST<Key, Value> t;
      t = new LinearProbingHashST<>(cap);
      for (int i = 0; i < M; i++)
        if (keys[i] != null) t.put(keys[i], vals[i]);
      keys = t.keys;
      vals = t.vals;
      M    = t.M;
    }


    public void put(Key key, Value val) {
      if (N >= M/2) resize(2*M);  // double M

      int i;
      //starting at index `hash(key)`
      // continue looking for a space that is null (could be `hash(key)` itself)
      // the %M is in case we reach the end of the array
      for (i = hash(key); keys[i] != null; i = (i + 1) % M){
        //if the key already exists, then just replace its value
        if (keys[i].equals(key)) {
          vals[i] = val;
          return;
        }
      }
      //Once we get here, `i` is equal to the first null space that comes after
      // `hash(key)` , which is the space where we can put the new value
      keys[i] = key;
      vals[i] = val;
      N++;//increase the size of the table
    }

    public Value get(Key key) {
      for (int i = hash(key); keys[i] != null; i = (i + 1) % M)
        if (keys[i].equals(key))
          return vals[i];
      return null;
    }

    public void delete(Key key) {
      //If the table does not contain this key, just return, theres nothing
      // to delete. Commented out because i do not have the contains method.

//      if (!contains(key)) {
//        return;
//      }

      int i = hash(key);

      //find the VALUE of the keys array that is the delete key
      // and set i to that precise index (thats the thing we are deleting)
      while (!key.equals(keys[i])) {
        i = (i + 1) % M;
      }

      //at this point i is in the index of the thing we are deleting
      keys[i] = null;//delete it from BOTH keys and values
      vals[i] = null;//delete

      //move onto the next value
      i = (i + 1) % M;

      //now pull the remaining keys (and values) that come after the deleted
      // element until the next null element over. This is significant
      // because the keys and values are bounded by NULL values while
      // searching. So we need to make sure that there is not an arbitrary
      // NULL value in between a group of keys that SHOULD BE consecutive.
      while (keys[i] != null) {
        //Capture the key&value that is being shifted
        Key   keyToRedo = keys[i];
        Value valToRedo = vals[i];
        //delete that key that is being shifted
        keys[i] = null;
        vals[i] = null;
        //this N-- is here because we use the put method below it which
        // increases N by 1. So we need to make up for that fact here so that
        // we dont lose track of N.
        N--;
        //Put the captured shifting key/value pair into its place
        put(keyToRedo, valToRedo);
        //advance i to the next value that needs to be shifted
        i = (i + 1) % M;
      }
      //reduce N by 1 to reflect the deleted element
      N--;

      //resize DOWN if deleting this element has reduced N down to M/8
      if (N > 0 && N == M/8) {
        resize(M/2);
      }
    }

  }

}
