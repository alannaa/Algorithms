package Final;

import Final.EdgeWeightedGraph.Edge;


public class Problem1 {

  //4.3.22 Minimum spanning forest. Develop versions of Prim’s and Kruskal’s
  // algorithms that compute the minimum spanning forest of an edge-weighted
  // graph that is not necessarily connected. Use the connected-components
  // API of Section 4.1 and find MSTs in each component.


  //KRUSKALS logic:
  //for each component (in other words, for each spanning tree):
  // - sort the edges by ascending weight
  // - add the next lowest edge to the tree UNLESS it creates a cycle

  // However: we do not need to specify "for each connected component"
  // because by the nature of the algorithm, we will continue to search for
  // the next largest weight and pick the one that does not create a cycle,
  // and that logic is not compromised by the presence of a dis-connected
  // component. If we keep running that algorithm on a spanning forest until
  // we've traversed V-1 edges, then all spanning-trees in the forest will be
  // accounted for without explicitly dealing with each tree in the algorithm.

  //PS I recognize that the problem statement asks us specifically to use the
  // CC api however doing so adds n^2 overhead to the algorithm that works just
  // fine without the added overhead. therefore I see no point in proving
  // that it can be done. In any case, if you wanted to, you could separate
  // components using the CC api by grouping together vertices with the same
  // CCIdx (I renamed this field so look at my ConnectedComponents.java file
  // to understand what this field does). Then, you could sort the edges of
  // each component and save each of those in separate priority queues.
  // Lastly, you would apply the same while loop below on each priority queue.

  public Queue<Edge> kruskalMinSpanningForest(EdgeWeightedGraph ewg){
    //keeping track of weight just to print it, not to calculate anything
    double weight = 0;
    Queue<Edge> msf = new Queue<>();

    //First, sort the edges in ascending order by WEIGHT
    PriorityQueue<Edge> sortedEdges =
        new PriorityQueue<>(PriorityQueue.Orientation.MIN);
    //The compareTo method in the Edge class compares edges by weight,
    //allowing the priority queue code to prioritize and pop items in
    // ascending order by weight
    for(Edge edge : ewg.getAllEdges()) {
      sortedEdges.insert(edge);
    }

    //keep track of the 'marked' vertices in the graph using the union find API
    //if we 'mark' all of the edges we traverse, we can use the connected
    // method in the union find to traverse all of the 'marked' edges to see
    // if traversing the next edge results in a cycle
    UnionFind uf = new UnionFind(ewg.getNumVertices());

    //While we still have edges left to traverse and the min spanning forest
    // has not yet reached size V-1...
    while (!sortedEdges.isEmpty() && msf.size() < ewg.getNumVertices() - 1) {

      Edge edge = sortedEdges.deleteTop(); //Get the next lowest edge in order
      int vertex1 = edge.either();
      int vertex2 = edge.other(vertex1);

      //IF traversing this edge to vertex 2 creates a cycle..
      if (uf.connected(vertex1, vertex2)) {
        //then do not add it to the MSF; move on
        continue;
      }

      //uf.union connects the vertices as an established edge in the MSF
      uf.union(vertex1, vertex2);
      msf.enqueue(edge); // Add edge to the minimum spanning forest
      weight += edge.weight();

    }
    System.out.println("Weight: " + weight);
    return msf;
  }

  ////////////////////////////////////////////////////

  //PRIMS logic:
  // - start at 0
  // - find the min edge stemming from the entire mst
  // - traverse that edge
  // - now find the min edge stemming from the entire mst again
  public Queue<Edge> primMinSpanningForest(EdgeWeightedGraph ewg){
    double weight = 0;
    Queue<Edge> msf = new Queue<>();
    ConnectedComponents cc = new ConnectedComponents(ewg);

    //for each component...
    for(int CCidx = 0; CCidx < cc.getNumComponents(); CCidx++) {

      //for each vertex on the ewg..
      for(int vertex = 0; vertex < ewg.getNumVertices(); vertex++) {
        //if this vertex is part of the component we are currently focused on:
        if (cc.getCCIdx(vertex) == CCidx) {
          //then using that vertex as the `root` so to speak, traverse over
          // that entire component until you find its minimum spanning tree.
          //EFFECT: The below method modifies the msf to add the current
          // spanning tree's minimum spanning tree to the overall
          // minimum spanning forest
          primOnOneComponent(msf, ewg, cc, vertex, CCidx);
          break;//we break because you only need to find 1 vertex in a single
          // component in order to compute the entire minimum spanning tree
          // of that component. So no need to keep iterating over all of the
          // vertices in the entire edge-weighted graph. Move onto the next
          // COMPONENT (the outer for-loop) and try to find one of ITS
          // vertices next to call primOnOneComponent on.
        }
      }
    }

    //for testing purposes only
    for(Edge e : msf) {
      weight+=e.weight();
    }
    System.out.println("Weight: " + weight);
    return msf;
  }


  //PRIMS logic:
  // - start at 0 (in this case, `vertex`
  // - find the min edge stemming from `vertex`
  // - traverse that edge
  // - now find the min edge stemming from either `vertex` or the edge you
  // just traversed.
  // - with every traversal, find the min edge stemming from anywhere in the
  // entire MSF

  //While I recognize this method has a time complexity of like O(n^3)
  // the most important thing for me was to fully comprehend the logic behind
  // the algorithm, which I believe I understood correctly.
  public void primOnOneComponent(Queue<Edge> msf,
      EdgeWeightedGraph ewg,
      ConnectedComponents cc, int vertex, int CCIdx) {

    //union find for checking if traversing an edge leads to a cycle
    UnionFind uf = new UnionFind(ewg.getNumVertices());

    //init the msf from `vertex`
    Edge minEdge = new Edge(-1, -1, Integer.MAX_VALUE);
    Bag<Edge> adjacentEdges = ewg.getAllAdjacentEdges(vertex);
    for (Edge e : adjacentEdges) {
      if (e.weight() < minEdge.weight()) {
        minEdge = e;
      }
    }
    msf.enqueue(minEdge);

    //we know the number of vertices in a spanning tree's MST is
    // the total number of vertices - 1
    // so count the number of vertices you've added so far and keep looping
    // until we reach the correct amount
    int numVerticesInMst = 1;
    while(numVerticesInMst < cc.getNumVerticesInComponent(CCIdx)-1) {
      //Set the minEdge to something to avoid compilation error
      minEdge = new Edge(-1, -1, Integer.MAX_VALUE);

      //for each edge in the current MSF
      for (Edge edgeOfMsf : msf) {

        //if that edge is part of the component we are currently focused on,
        //which we derive using the connected component API
        // and ask if that edge is connected by any path to the vertex passed
        // into this method..
        if (cc.connected(vertex, edgeOfMsf.either())) {

          //find the smallest edge stemming from the current edge
          Bag<Edge> eitherEdges = ewg.getAllAdjacentEdges(edgeOfMsf.either());
          Bag<Edge> otherEdges = ewg.getAllAdjacentEdges(edgeOfMsf.other(edgeOfMsf.either()));

          for (Edge e : eitherEdges) {
            if (e.weight() < minEdge.weight()
                && !msf.contains(e) //checks that we didnt already process e
                && !uf.connected(e.either(), e.other(e.either()))) { //checks
              // that adding e to the msf does not create a cycle
              minEdge = e;
            }
          }
          for (Edge e : otherEdges) {
            if (e.weight() < minEdge.weight()
                && !msf.contains(e)
                && !uf.connected(e.either(), e.other(e.either()))) {
              minEdge = e;
            }
          }
        }
      }

      //after doing all of that traversing
      //whatever we landed on was the minEdge stemming from the ENTIRE
      // current MSF, enqueue it:
      msf.enqueue(minEdge);
      numVerticesInMst++;
    }

  }


  public static void main(String[] args) {

    Problem1 p1 = new Problem1();
    EdgeWeightedGraph ewgSimple = new EdgeWeightedGraph(5);
    ewgSimple.addEdge(new Edge(0,1, 1));
    ewgSimple.addEdge(new Edge(0,3, 7));
    ewgSimple.addEdge(new Edge(1,2, 3));
    ewgSimple.addEdge(new Edge(3,2, 4));
    ewgSimple.addEdge(new Edge(3,4, 5));
    ewgSimple.addEdge(new Edge(4,2, 8));

    System.out.println("\n\nKruskal, one component:");
    Queue<Edge> kruskalSimple = p1.kruskalMinSpanningForest(ewgSimple);
    kruskalSimple.printQueue();
    System.out.println("Prim, one component:");
    Queue<Edge> primSimple = p1.primMinSpanningForest(ewgSimple);
    primSimple.printQueue();

    //testing using an example from the book to be sure:
    EdgeWeightedGraph ewg = new EdgeWeightedGraph(8);
    ewg.addEdge(new Edge(0,1, 4));
    ewg.addEdge(new Edge(0,4, 16));
    ewg.addEdge(new Edge(0,3, 6));
    ewg.addEdge(new Edge(1,2, 24));
    ewg.addEdge(new Edge(2,3, 23));
    ewg.addEdge(new Edge(2,5, 18));
    ewg.addEdge(new Edge(2,6, 9));
    ewg.addEdge(new Edge(3,4, 8));
    ewg.addEdge(new Edge(3,5, 5));
    ewg.addEdge(new Edge(4,5, 10));
    ewg.addEdge(new Edge(4,7, 21));
    ewg.addEdge(new Edge(5,6, 11));
    ewg.addEdge(new Edge(5,7, 14));
    ewg.addEdge(new Edge(6,7, 7));
    System.out.println("\n\nKruskal, one component:");
    Queue<Edge> kruskalOneComponent = p1.kruskalMinSpanningForest(ewg);
    kruskalOneComponent.printQueue();
    System.out.println("Prim, one component:");
    Queue<Edge> primOneComponent = p1.primMinSpanningForest(ewg);
    primOneComponent.printQueue();


    //using a similar example from the book, but I disconnected one section
    // of it so that there are two components
    EdgeWeightedGraph ewgTwoComponents = new EdgeWeightedGraph(8);
    ewgTwoComponents.addEdge(new Edge(0,1, 4));
    ewgTwoComponents.addEdge(new Edge(0,3, 6));
    ewgTwoComponents.addEdge(new Edge(1,2, 24));
    ewgTwoComponents.addEdge(new Edge(2,3, 23));
    ewgTwoComponents.addEdge(new Edge(2,6, 9));

    ewgTwoComponents.addEdge(new Edge(4,5, 10));
    ewgTwoComponents.addEdge(new Edge(4,7, 21));
    ewgTwoComponents.addEdge(new Edge(5,7, 14));

    System.out.println("\n\nKruskal, two components:");
    Queue<Edge> kruskalTwoComponents =
        p1.kruskalMinSpanningForest(ewgTwoComponents);
    kruskalTwoComponents.printQueue();
    System.out.println("Prim, two components:");
    Queue<Edge> primTwoComponents = p1.primMinSpanningForest(ewgTwoComponents);
    primTwoComponents.printQueue();


    //finally 3 components
    EdgeWeightedGraph ewgTheeComponents = new EdgeWeightedGraph(12);
    ewgTheeComponents.addEdge(new Edge(0,1, 4));
    ewgTheeComponents.addEdge(new Edge(0,3, 6));
    ewgTheeComponents.addEdge(new Edge(1,2, 24));
    ewgTheeComponents.addEdge(new Edge(2,3, 23));
    ewgTheeComponents.addEdge(new Edge(2,6, 9));

    ewgTheeComponents.addEdge(new Edge(4,5, 10));
    ewgTheeComponents.addEdge(new Edge(4,7, 21));
    ewgTheeComponents.addEdge(new Edge(5,7, 14));

    ewgTheeComponents.addEdge(new Edge(8,9, 10));
    ewgTheeComponents.addEdge(new Edge(8,10, 7));
    ewgTheeComponents.addEdge(new Edge(8,11, 1));
    ewgTheeComponents.addEdge(new Edge(10,11, 2));

    System.out.println("\n\nKruskal, three components:");
    Queue<Edge> kruskalThreeComponents =
        p1.kruskalMinSpanningForest(ewgTheeComponents);
    kruskalThreeComponents.printQueue();
    System.out.println("Prim, three components:");
    Queue<Edge> primThreeComponents =
        p1.primMinSpanningForest(ewgTheeComponents);
    primThreeComponents.printQueue();

  }


}
