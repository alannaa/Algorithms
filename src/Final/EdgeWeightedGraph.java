package Final;


import java.util.Iterator;

//Edge Weighted Graph code form page 611
//but with better variable and method names
public class EdgeWeightedGraph {

  private int NUMVERTICES;
  private int numEdges;
  private Bag<Edge>[] adjacencyList; // an array of Bag that represents an
  // adjacency list



  //EdgeWeightedGraph Constructor
  public EdgeWeightedGraph(int V) {

    this.NUMVERTICES = V;
    this.numEdges = 0;

    //Initialize the `adjacent` array as an array of Bags
    //So the structure looks like this:
    // adjacent[0] => Bag{ <all vertices adjacent to vertex adjacent[0]> }
    // adjacent[1] => Bag{ <all vertices adjacent to vertex adjacent[1]> }
    // adjacent[2] => Bag{ <all vertices adjacent to vertex adjacent[2]> }
    // and so on
    adjacencyList = new Bag[V];

    //So set up the adjacent arary as described above, with empty bags for now
    for (int vertex = 0; vertex < V; vertex++) {
      adjacencyList[vertex] = new Bag<>();
    }
  }


  public int getNumVertices() {
    return NUMVERTICES;
  }

  public int getNumEdges() {
    return numEdges;
  }

  public void addEdge(Edge edge) {
    int vertex1 = edge.either();
    int vertex2 = edge.other(vertex1);

    adjacencyList[vertex1].add(edge);
    adjacencyList[vertex2].add(edge);
    numEdges++;
  }

  public Bag<Edge>[] getAdjacencyList(){
    return adjacencyList;
  }


  public Bag<Edge> getAllAdjacentEdges(int vertex) {
    return adjacencyList[vertex];
  }

  public int[] getAllAdjacentVertices(int vertex) {
    //First, grab all the edges we need to traverse
    Bag<Edge> edges = getAllAdjacentEdges(vertex);

    //Next, set up an array where we'll store the adjacent vertices
    int[] adjacentVertices = new int[edges.size()];

    //Finally, set up the iterator to traverse the edges
    Iterator<Edge> itr = edges.iterator();

    //And a count to keep track of which index in the array we are at
    int idx = 0;

    while (itr.hasNext()) {
      Edge next = itr.next();
      adjacentVertices[idx++] = next.other(vertex);
    }

    return adjacentVertices;
  }



  public Iterable<Edge> getAllEdges() {

    Bag<Edge> edges = new Bag<>();

    //for each vertex in the vertices list
    for(int vertex = 0; vertex < NUMVERTICES; vertex++) {

      //for each edge in that vertex's adjacency list
      for(Edge edge : adjacencyList[vertex]) {

        //if any of the edges adjacent to this vertex is GREATER than
        //the current vertex, then add the edge to the edges list
        //This ensures that each edge is added to the bag only once and also
        //IN ORDER by vertex number (NOT weight)
        if (edge.other(vertex) > vertex) {
          edges.add(edge);
        }
      }
    }
    return edges;
  }


  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    for(int vertex = 0; vertex < NUMVERTICES; vertex++) {
      stringBuilder.append(vertex).append(": ");

      for(Edge neighbor : getAllAdjacentEdges(vertex)) {
        stringBuilder.append(neighbor).append(" ");
      }
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }



  public static class Edge implements Comparable<Edge> {

    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
      this.v = v;
      this.w = w;
      this.weight = weight;
    }

    double weight() {  return weight;  }

    int either() {  return v;  }

    int other(int vertex) {
      //returns NOT v
      if      (vertex == v) return w;
      else if (vertex == w) return v;
      else throw new RuntimeException("Inconsistent edge");
    }

    public int compareTo(Edge that) {
      if      (this.weight() < that.weight()) return -1;
      else if (this.weight() > that.weight()) return +1;
      else                                    return  0;
    }

    public String toString() {
      return String.format("%d-%d %.2f", v, w, weight);
    }
  }


}