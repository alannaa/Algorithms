package Midterm;

import java.util.NoSuchElementException;
import java.util.Queue;

public class Problem4 {

  /*
  3.3.29 Optimal storage. Modify RedBlackBST so that it does not use any extra
  storage for the color bit, based on the following trick: To color a node red,
  swap its two links. Then, to test whether a node is red, test whether its
  left child is larger than its right child. You have to modify the compares
  to accommodate the possible link swap, and this trick replaces bit compares
  with key compares that are presumably more expensive, but it shows that the
  bit in the nodes can be eliminated, if necessary.

  The basis of this code was derived and modified from:
  https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/RedBlackBST.java
  */


  public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;// root of the BST

    // BST helper node data type
    private class Node {
      private Key key;
      private Value val;
      private Node left, right;
      private int size;
      //private boolean color; //Commented out to implement space optimization


      Node(Key key, Value val, int size) {
        this.key = key;
        this.val = val;
        this.size = size;
      }
    }

    /**
     * Initializes an empty symbol table.
     */
    RedBlackBST(){}



    /***************************************************************************
     *  Node helper methods.
     ***************************************************************************/
    // is node x red; false if x is null
    private boolean isRed(Node x) {
      if (x == null) return false;

      /*From problem statement:
      ...to test whether a node is red, test whether its
      left child is larger than its right child.
       */

      //If the node has TWO children
      if (x.left != null && x.right!= null){
        int cmp  = x.left.key.compareTo(x.right.key);
        return cmp > 0;
      }

      //If the node only has one child, I believe we can assume that it is
      // NOT red.
      if ((x.left == null && x.right!= null) ||
          (x.left != null && x.right== null)) {
        return false;
      }
      //If the node has NO children - it's possible that is is a newly added
      // node that ended up at a leaf. We always add new nodes as RED nodes,
      // so even though it sounds counter intuitive to return TRUE in the
      // case that a node has no children, this is to ensure that when we use
      // isRed() later on in the put method to put a new node at a leaf level
      // with no children, it is seen as a RED node.
      return true;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
      if (x == null) return 0;
      return x.size;
    }


    /***************************************************************************
     *  Red-black tree insertion.
     ***************************************************************************/
    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     */
    public void put(Key key, Value val) {
      if (key == null) throw new IllegalArgumentException("first argument to put() is null");
      if (val == null) {
        //delete(key);
        return;
      }
      //SIGNIFICANT: the following `put` method returns the root node after
      // modifying all of its children nodes accordingly to insert the new
      // element. This is because: in the event that putting a new element
      // causes a rotation of nodes, it's possible that a new node will
      // rotate into root position. The root is stored as part of the RBST
      // data structure so that must be updated.
      // this returning the node feature of the put method also benefits us
      // when it comes to the recursion that happens in the put method. If
      // any child links need to be replaced or altered, again as a result of
      // rotation or 'flipping colors' (or flipping children in this
      // color-less adaptation) then the child links need to lead to new nodes.

      root = put(root, key, val);
      //root.color = BLACK; -- no longer needed
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, Key key, Value val) {
      //if this is an empty tree, just add the new node.
      //This is also the recursive base case/stopping point (if we've reached
      // a leaf/null node and need to insert the element there). Inserting a
      // new node at a leaf often results in still needing to rotate/flip
      // colors so the recursive call that happened before this final one
      // will still go through the second half of this method which does the
      // rotating and flipping colors.
      if (h == null) return new Node(key, val, 1);

      //if the new element...
      int cmp = key.compareTo(h.key);
      //      ...is less than the root, then it needs to go somewhere that is
      //      below the left side of the root. If the following recursive
      //      call of this method returns a new node, it will be added as a
      //      link FROM h.left to this new node.
      if      (cmp < 0) h.left  = put(h.left,  key, val);

      //      ...is greater than the root, then it needs to go somewhere that
        //    is below the right side of the root.
      else if (cmp > 0) h.right = put(h.right, key, val);

      //      if the new element we are inserting shares the same KEY as the
        //      ROOT then that means our intention is actually to update the
        //      value AT the root (h), not to insert a new node
      else              h.val   = val;



      // We've already updated the isRed method to identify the cases when
      // children should be switched (representing a RED node in this
      // implementation) so these should all work as expected.
      if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
      if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
      if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
      h.size = size(h.left) + size(h.right) + 1;

      return h;
    }

    /**
     * Returns the NODE that contains the given key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Node get(Key key) {
      if (key == null) throw new IllegalArgumentException("argument to get() is null");
      return get(root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Node get(Node x, Key key) {

      if (x == null) {
        return null;
      }

      if (key.compareTo(x.key) == 0){
        return x;
      }

      Node maybeLeft = get(x.left, key);
      Node maybeRight = get(x.right, key);

      if(maybeLeft!=null){
        return maybeLeft;
      }

      if(maybeRight!=null){
        return maybeRight;
      }

      return null;

    }

    /***************************************************************************
     *  Red-black tree helper functions.
     ***************************************************************************/

    //NEW HELPER METHOD:
    //when a node should be made red, we need to swap its child values
    // such that
    // the left child is GREATER THAN the node and
    // the right child is LESS THAN the node
    private void swapToMakeRed(Node n){
      if(n == null) {
        return;
      }

      if (n.left != null && n.right != null){
        //temporarily store the left
        Key tempKey = n.left.key;
        Value tempVal = n.left.val;

        //reassign left to right
        n.left.key = n.right.key;
        n.left.val = n.right.val;

        //reassign right to left
        n.right.key = tempKey;
        n.right.val = tempVal;
      }

      // --- IF there is only one child, swap it over to the other side --- //
      if (n.left != null && n.right == null){
        //right there IS a left child but right child is NULL
        //put the left child over to the right
        n.right = n.left;
        //and turn the left child null
        n.left = null;
      }

      if (n.left == null && n.right != null){
        //right there IS a left child but right child is NULL
        //put the left child over to the right
        n.left = n.right;
        //and turn the left child null
        n.right = null;
      }

    }


    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
      assert (h != null);

      Node x = h.left;
      h.left = x.right;
      x.right = h;

      /*
      Original Code:
      x.color = x.right.color;
      x.right.color = RED;
      */

      //First, it assigns x.color to whatever was x.right.color
      //another way to do that is:
      if (isRed(x.right)) {
        //then that means x needs to become RED
        //From the problem statement:
        // To color a node red, swap its two links.
        swapToMakeRed(x);

        //if x.right was NOT red, then we dont need to do anything to indicate
        // that its black, its just regular black.
      }

      //Then, it makes x.right.color red, no matter what:
      swapToMakeRed(x.right);

      x.size = h.size;
      h.size = size(h.left) + size(h.right) + 1;
      return x;
    }


    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
      assert (h != null) && isRed(h.right);
      // assert (h != null) && isRed(h.right) && !isRed(h.left);  // for insertion only
      Node x = h.right;
      h.right = x.left;
      x.left = h;

      /*
      Original code
      x.color = x.left.color;
      x.left.color = RED;
      */

      //First, it assigns x.color to whatever was x.left.color
      //another way to do that is:
      if (isRed(x.left)) {
        //then that means x needs to become RED
        //From the problem statement:
        // To color a node red, swap its two links.
        swapToMakeRed(x);

        //if x.left was NOT red, then we dont need to do anything to indicate
        // that its black, its just regular black.
      }

      //Then, it makes x.left.color red, no matter what:
      swapToMakeRed(x.left);

      x.size = h.size;
      h.size = size(h.left) + size(h.right) + 1;
      return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
      // make sure to flip EVERYTHING:
      //  - node itself
      //  - node.left
      //  - node.right

      /*
      Original Code:
      h.color = !h.color;
      h.left.color = !h.left.color;
      h.right.color = !h.right.color;
       */

      //okay the method is called swapToMakeRed but it can also swap in
      // general to turn a `red` node back to regular `black `, in this case
      // indicated by the left child being < h < right child, so just apply
      // the swap method to all:
      swapToMakeRed(h);
      swapToMakeRed(h.left);
      swapToMakeRed(h.right);
    }






    /***************************************************************************
     *  Check integrity of red-black tree data structure.
     ***************************************************************************/
    private boolean check() {
      if (!isBST())            System.out.println("Not in symmetric order");
      if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
      if (!is23())             System.out.println("Not a 2-3 tree");
      if (!isBalanced())       System.out.println("Not balanced");
      return isBST() && isSizeConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
      return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
      if (x == null) return true;
      if (min != null && x.key.compareTo(min) <= 0) return false;
      if (max != null && x.key.compareTo(max) >= 0) return false;
      return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
      if (x == null) return true;
      if (x.size != size(x.left) + size(x.right) + 1) return false;
      return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
      if (x == null) return true;
      if (isRed(x.right)) return false;
      if (x != root && isRed(x) && isRed(x.left))
        return false;
      return is23(x.left) && is23(x.right);
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
      int black = 0;     // number of black links on path from root to min
      Node x = root;
      while (x != null) {
        if (!isRed(x)) black++;
        x = x.left;
      }
      return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
      if (x == null) return black == 0;
      if (!isRed(x)) black--;
      return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

  } //end of RedBlackBST


  //a helper just so that I can access the tree from static main
  public RedBlackBST<Integer, Integer> getRBBST(){
    return new RedBlackBST<>();
  }



  /**
   * Unit tests the {@code RedBlackBST} data type.
   */
  public static void main(String[] args) {

    Problem4 p4 = new Problem4();
    int N = 32;


    RedBlackBST<Integer, Integer> rbbst = p4.getRBBST();

    //build the RBBST
    for (int i=0; i<N; i++) {
      rbbst.put(i, i);
    }

    System.out.println();

    for (int i=0; i<N; i++) {
      if (i != 0 && (i + 1 & (i)) == 0) {
        System.out.print("\n");
      }
      System.out.print(rbbst.get(i).key + " ");
      System.out.print(rbbst.isRed(rbbst.get(i)));
      if (rbbst.get(i).left != null) {
        System.out.print(" LEFTC: " + rbbst.get(i).left.key + " ");
      }
      if (rbbst.get(i).right != null) {
        System.out.print(" RIGHTC: " + rbbst.get(i).right.key + " ");
      }
      System.out.print(" | ");

    }

    System.out.println("\n");
    rbbst.check();

  }



}
