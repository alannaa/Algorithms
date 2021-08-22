package Final;


public class Problem4 {

  //5.2.10 Size. Implement very eager size() (that keeps in each node the
  // number of keys in its subtree) for TrieST and TST.

  //To approach this problem, first we need a Node representation that keeps
  // track of the number of child trees.
  //Then, when "put" -ing, we need to +1 to that numSubKeys all the way down as we
  // traverse the trie
  //Lastly, we need to -1 while deleting from the trie
  public static class TrieST<String> extends Trie<String> {

    private EagerNode root = new EagerNode();
    static final int R = 256; // radix

    //EagerNode is a variation of Node that eagerly stores
    // the number of keys in its subtrie.
    //NOTE: It is unclear to me if the problem statement is requesting to
    // store only the DIRECT children (basically, the number of non-null
    // value children of the node) OR if it is requesting to also store THOSE
    // node's children. Basically, is the point to know how many non-null
    // children this node has, or the TOTAL number of search hits that might
    // possibly need to be queried after this one while searching in the trie.
    private static class EagerNode extends Node {
      private int numSubKeys;
      private EagerNode[] childNodes = new EagerNode[R];
    }

    //this size() method could also be named "getNumSubKeys"
    //as that is what it is referring to
    public int size() {
      if (root != null) {
        return root.numSubKeys;
      }
      return 0;
    }

    private int size(EagerNode nodeWithSize) {
      if (nodeWithSize != null) {
        return nodeWithSize.numSubKeys;
      }
      return 0;
    }

    public boolean contains(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be found");
      }
      return get(key) != null;
    }

    @Override
    public Value get(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be found");
      }

      if (key.length() == 0) {
        throw new IllegalArgumentException("Key must have a positive length");
      }

      Node node = get(root, key, 0);

      if (node == null) {
        return null;
      }
      return (Value) node.value;
    }

    private Node get(EagerNode node, String key, int digit) {
      if (node == null || node.numSubKeys ==0) {
        return null;
      }

      if (digit == key.length()) {
        return node;
      }

      char nextChar = key
          .charAt(digit); // Use digitTh key char to identify subtrie.
      return get(node.childNodes[nextChar], key, digit + 1);
    }

    @Override
    public void put(String key, Value value) {

      //first do a search
      //use the characters of the key to guide us town the trie until
      // reaching the last character of the key or a null link
      //at this point, one of the following two conds hold:
      // - we encountered a null link before reaching the last character of
      // the key. so we need to create nodes for each of the chars in th ekey
      // not yet encountered and set the calue int hte last one to the value
      // associated with the key.
      // - we encountered the last character of the key before reaching a
      // null link. in this case, we set that node's value to the associated
      // key.

      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      boolean isNewKey = false;

      if (!contains(key)) {
        isNewKey = true;
      }

      if (value == null) {
        delete(key);
      } else {
        root = put(root, key, value, 0, isNewKey);
      }
    }

    private EagerNode put(EagerNode node, String key, Value value,
        int digit, boolean isNewKey) {
      if (node == null) {
        node = new EagerNode();
      }

      //this line is precisely where the very eager implementation takes place
      // as soon as a new key is put into the trie, the root's numSubKeys
      // increments by 1
      if (isNewKey) {
        node.numSubKeys = node.numSubKeys + 1;
      }

      //in this case, node is a EagerNode object, but because that class
      // extends the regular node, we have a 'value' field.
      if (digit == key.length()) {
        node.value = value;
        return node;
      }

      //finally, populate the childNodes field
      char nextChar = key.charAt(digit); //Use digitTh key char to identify subtrie.



      node.childNodes[nextChar] = put(node.childNodes[nextChar], key, value, digit + 1,
          isNewKey);

      return node;
    }

    @Override
    public void delete(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      if (!contains(key)) {
        return;
      }

      root = delete(root, key, 0);
    }

    private EagerNode delete(EagerNode node, String key, int digit) {
      if (node == null) {
        return null;
      }

      node.numSubKeys = node.numSubKeys - 1;

      if (digit == key.length()) {
        node.value = null;
      } else {
        char nextChar = key.charAt(digit);
        node.childNodes[nextChar] = delete(node.childNodes[nextChar], key, digit + 1);
      }

      if (node.value != null) {
        return node;
      }

      for (char nextChar = 0; nextChar < node.numSubKeys; nextChar++) {
        if (node.childNodes[nextChar] != null) {
          return node;
        }
      }

      return null;
    }
  }


  ////////////////////////////////////////////////////////////////////


  public static class TernarySearchTrie<Value> {

    private int size;
    private NodeWithSize root;


    class Node {
      char character;
      Value value;
      int size;

      Node left;
      Node middle;
      Node right;
    }

    class NodeWithSize extends Node {
      private int size;
      private NodeWithSize left;
      private NodeWithSize middle;
      private NodeWithSize right;
    }

    public int size() {
      return size;
    }

    public boolean contains(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      return get(key) != null;
    }


    public Value get(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      if (key.length() == 0) {
        throw new IllegalArgumentException("Key must have a positive length");
      }

      NodeWithSize node = get(root, key, 0);

      if (node == null) {
        return null;
      }
      return (Value) node.value;
    }

    private NodeWithSize get(NodeWithSize node, String key, int digit) {
      if (node == null) {
        return null;
      }

      char currentChar = key.charAt(digit);

      if (currentChar < node.character) {
        return get(node.left, key, digit);
      } else if (currentChar > node.character) {
        return get(node.right, key, digit);
      } else if (digit < key.length() - 1) {
        return get(node.middle, key, digit + 1);
      } else {
        return node;
      }
    }

    public void put(String key, Value value) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      if (value == null) {
        delete(key);
        return;
      }

      boolean isNewKey = false;

      if (!contains(key)) {
        isNewKey = true;
        size++;
      }

      root = put(root, key, value, 0, isNewKey);
    }

    private NodeWithSize put(NodeWithSize node, String key, Value value, int digit, boolean isNewKey) {
      char currentChar = key.charAt(digit);

      if (node == null) {
        node = new NodeWithSize();
        node.character = currentChar;
      }

      if (currentChar < node.character) {
        node.left = put(node.left, key, value, digit, isNewKey);
      } else if (currentChar > node.character) {
        node.right = put(node.right, key, value, digit, isNewKey);
      } else if (digit < key.length() - 1) {
        node.middle = put(node.middle, key, value, digit + 1, isNewKey);

        if (isNewKey) {
          node.size = node.size + 1;
        }
      } else {
        node.value = value;

        if (isNewKey) {
          node.size = node.size + 1;
        }
      }

      return node;
    }

    public void delete(String key) {
      if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
      }

      if (!contains(key)) {
        return;
      }

      root = delete(root, key, 0);
      size--;
    }

    private NodeWithSize delete(NodeWithSize node, String key, int digit) {
      if (node == null) {
        return null;
      }

      if (digit == key.length() - 1) {
        node.size = node.size - 1;
        node.value = null;
      } else {
        char nextChar = key.charAt(digit);

        if (nextChar < node.character) {
          node.left = delete(node.left, key, digit);
        } else if (nextChar > node.character) {
          node.right = delete(node.right, key, digit);
        } else {
          node.size = node.size - 1;
          node.middle = delete(node.middle, key, digit + 1);
        }
      }

      if (node.size == 0) {
        if (node.left == null && node.right == null) {
          return null;
        } else if (node.left == null) {
          return node.right;
        } else if (node.right == null) {
          return node.left;
        } else {
          NodeWithSize aux = node;
          node = min(aux.right);
          node.right = deleteMin(aux.right);
          node.left = aux.left;
        }
      }

      return node;
    }

    private NodeWithSize min(NodeWithSize node) {
      if (node.left == null) {
        return node;
      }

      return min(node.left);
    }

    private NodeWithSize deleteMin(NodeWithSize node) {
      if (node.left == null) {
        return node.right;
      }

      node.left = deleteMin(node.left);
      return node;
    }
  }

  public static void main(String[] args) {
    Problem4 p4 = new Problem4();

    TrieST trieST = new TrieST();
    System.out.println("new trieST numSubKeys: " + trieST.size());

    trieST.put("test", 0);


  }



}
