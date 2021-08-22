package Quiz3;

public class Problem2 {

  //True or false: Adding a constant to every edge weight does not change the
  // solution to the single-source shortest-paths problem.

  /*
  False

  Using the same graph that I used in problem 1:

  A-----B-----C
        |__ __|
         __D__
        |     |
        E-----F-----G


  Where the wight of each edge was:

  A B : 1
  B C : 1
  B D : 4
  C D : 3
  D E : 7
  D F : 8
  E F : 3
  F G : 2

  If we add a constant of 100 to each edge:
  A B : 101
  B C : 101
  B D : 104
  C D : 103
  D E : 107
  D F : 108
  E F : 103
  F G : 102


  The total weight of traversing directly from A to E
  in the original graph compared to the graph with a constant added:
  12 : 412

  The total weight of traversing directly from to G in a single zig zag
  in the original graph compared to the graph with a constant added:
  17 : 617

  Finally, analyze the ratio difference between these two above observations.
  If their results are the same, then that would mean adding a constant
  creates a proportionate increase in the weights of all possible paths.
  If their results are different in any way, then that means the addition of
  a constant created a disproportionately larger impact on some paths within
  the graph, in this case the longer paths:


  12/412 = 0.0291

  17/617 = 0.0275

  Because the results of the ratio division are different, that means that
  the addition of a constant to all edges in the graph actually changed the
  SSSP problem by disproportionately adding more weight to longer paths.

  Multiplying each edge by a constant (not adding) does not change the SSSP
  problem because the impact is proportionate to all size paths.
   */

}
