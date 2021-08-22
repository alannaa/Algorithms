package Final;

public class EdgeWeightedDigraph {

  private int NUMVERTICES; // number of vertices
  private int numEdges; // number of edges
  private Bag<DirectedEdge>[] adjacencyList; // adjacency lists


  public EdgeWeightedDigraph(int V) {
    this.NUMVERTICES = V;
    this.numEdges = 0;
    adjacencyList = (Bag<DirectedEdge>[]) new Bag[V];

    for (int v = 0; v < V; v++) {
      adjacencyList[v] = new Bag<DirectedEdge>();
    }
  }

  public int getNumVertices () {
    return this.NUMVERTICES;
  }

  public int getNumEdges () {
    return this.numEdges;
  }

  public void addEdge (DirectedEdge e) {
    adjacencyList[e.from()].add(e);
    numEdges++;
  }


  public Iterable<DirectedEdge> getAllAdjacentEdges(int v) {
    return adjacencyList[v];
  }

  public Iterable<DirectedEdge> getAllEdges () {
    Bag<DirectedEdge> bag = new Bag<>();
    for (int v = 0; v < NUMVERTICES; v++) {
      for (DirectedEdge e : adjacencyList[v]) {
        bag.add(e);
      }
    }
    return bag;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    for(int vertex = 0; vertex < NUMVERTICES; vertex++) {
      stringBuilder.append(vertex).append(": ");

      for(DirectedEdge neighbor : getAllAdjacentEdges(vertex)) {
        stringBuilder.append(neighbor).append(" ");
      }
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }


  public static class DirectedEdge {

    private final int v; //source
    private final int w; //target
    private final double weight; //weight

    public DirectedEdge(int v, int w, double weight) {
      this.v = v;
      this.w = w;
      this.weight = weight;
    }

    public double weight() {
      return weight;
    }

    public int from() {
      return v;
    }

    public int to() {
      return w;
    }

    public String toString() {
      return String.format("%d->%d %.2f", v, w, weight);
    }

  }

}
