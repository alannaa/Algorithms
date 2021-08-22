package Final;

import Final.EdgeWeightedDigraph.DirectedEdge;

public class Problem2 {

  //4.4.23 Source-sink shortest paths. Develop an API and implementation that
  // use a version of Dijkstra’s algorithm to solve the source-sink shortest
  // path problem on edge-weighted digraphs.

  //on page 656:
  /*
  Source-sink shortest paths: Given an edge-weighted digraph, a source
  vertex s, and a target vertex t, find the shortest path from s to t.
  To solve this problem, use Dijkstra’s algorithm, but terminate the search as soon as t
  comes off the priority queue.*/


  public Queue<DirectedEdge> DijkstraSP(EdgeWeightedDigraph G, int s, int t) {

    DirectedEdge[] edgeTo = new DirectedEdge[G.getNumVertices()];
    double[] distTo = new double[G.getNumVertices()];
    IndexMinPriorityQueue<Double> pq = new IndexMinPriorityQueue<>(G.getNumVertices());

    //set all of the distancesTo to a large unrealistic number so that we can
    //compare when we've found a smaller distanceTo
    for (int v = 0; v < G.getNumVertices(); v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
    }

    //the distance to the source is zero
    distTo[s] = 0.0;
    pq.insert(s, 0.0);

    //while the priority queue still has work in it..
    // (we will add work to it in the relax method)
    while (!pq.isEmpty()) {

      ///////////////////////////relax method:
      int nextVertexToRelax = pq.deleteMin();

      if (nextVertexToRelax == t) {
        //As the quote from page 656 states,
        // when you read t, stop the while loop, youve found the shortest path
        break;
      }

      //for all adj edges
      for(DirectedEdge e : G.getAllAdjacentEdges(nextVertexToRelax)) {

        //this will basically be set to each of the adjacent vertices
        // connected to nextVertexToRelax by an edge
        int eTo = e.to();

        //if the currently recorded distance to the vertex at the END of e
        // (called eTo) is LESS by getting there via the path:
        //     nextVertexToRelax -> e -> e.To
        //     then replace the current path to e.To
        //     with the new less costly path
        if (distTo[eTo] > distTo[nextVertexToRelax] + e.weight()) {
          distTo[eTo] = distTo[nextVertexToRelax] + e.weight();
          edgeTo[eTo] = e;
          if (pq.contains(eTo)) {
            pq.changeKey(eTo, distTo[eTo]);
          } else {
            pq.insert(eTo, distTo[eTo]);
          }
        }
      }
      /////////////////////////////
    }

    //Now follow the edgeTo array from t back to s to get the whole path
    Queue<DirectedEdge> sp = new Queue<>();
    int itr = t;
    while(itr != s) {
      if (edgeTo[itr] != null) {
        sp.enqueue(edgeTo[itr]);
      }
      itr = edgeTo[itr].from();
    }


    System.out.println("Distance to t: " + distTo[t]);
    return sp;
  }


  public static void main(String[] args) {

    Problem2 p2 = new Problem2();
    EdgeWeightedDigraph ewdSimple = new EdgeWeightedDigraph(5);
    ewdSimple.addEdge(new DirectedEdge(0,1, 1));
    ewdSimple.addEdge(new DirectedEdge(0,3, 7));
    ewdSimple.addEdge(new DirectedEdge(1,2, 3));
    ewdSimple.addEdge(new DirectedEdge(3,2, 4));
    ewdSimple.addEdge(new DirectedEdge(3,4, 5));
    ewdSimple.addEdge(new DirectedEdge(4,2, 8));

    Queue<DirectedEdge> kruskalsSPTSimple = p2.DijkstraSP(ewdSimple, 0, 4);
    kruskalsSPTSimple.printQueue();


    //testing using an example from the book to be sure:
    EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(8);
    ewd.addEdge(new DirectedEdge(0,1, 4));
    ewd.addEdge(new DirectedEdge(0,4, 16));
    ewd.addEdge(new DirectedEdge(0,3, 6));
    ewd.addEdge(new DirectedEdge(1,2, 24));
    ewd.addEdge(new DirectedEdge(2,3, 23));
    ewd.addEdge(new DirectedEdge(2,5, 18));
    ewd.addEdge(new DirectedEdge(2,6, 9));
    ewd.addEdge(new DirectedEdge(3,4, 8));
    ewd.addEdge(new DirectedEdge(3,5, 5));
    ewd.addEdge(new DirectedEdge(4,5, 10));
    ewd.addEdge(new DirectedEdge(4,7, 21));
    ewd.addEdge(new DirectedEdge(5,6, 11));
    ewd.addEdge(new DirectedEdge(5,7, 14));
    ewd.addEdge(new DirectedEdge(6,7, 7));

    Queue<DirectedEdge> kruskalsSPT = p2.DijkstraSP(ewd, 0, 7);
    kruskalsSPT.printQueue();

  }



//    Other methods from the API:
//    public boolean hasPathTo(int v) {
//      return distTo[v] < Double.POSITIVE_INFINITY;
//    }
//
//    public Iterable<DirectedEdge> pathTo(int v) {
//      if (!hasPathTo(v)) {
//        return null;
//      }
//      Stack<DirectedEdge> path = new Stack<>();
//      for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
//        path.push(e);
//      }
//      return path;
//    }
}


