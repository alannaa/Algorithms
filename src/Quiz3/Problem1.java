package Quiz3;

public class Problem1 {

  //4.3.13 Give a counterexample that shows why the following strategy does
  // not necessarily find the MST: ‘Start with any vertex as a single-vertex
  // MST, then add V-1 edges to it, always taking next a min-weight edge
  // incident to the vertex most recently added to the MST.


  /*

  Take the graph:

  A-----B-----C
        |__ __|
         __D__
        |     |
        E-----F-----G


  Where the wight of each edge is as follows:

  A B : 1

  B C : 1

  B D : 4

  C D : 3

  D E : 7

  D F : 8

  E F : 3

  F G : 2


  From this we can see that a MST would look like:
  A -> B -> C -> D -> E -> F -> G

  with a total cost of: 17



  However, using the above strategy, the resulting path would be:
  A -> B -> C -> D -> B -> C -> D -> E -> F -> G

  with a total cost of: 25
  thus, not a minimum spanning tree.

  The problem with the strategy above is that sometimes the next
  minimum-weight edge incident to the current vertex could trick you into
  going down a path that revisits an already visited vertex.
   */

}
