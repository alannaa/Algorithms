package Final;


import Final.EdgeWeightedGraph.Edge;

//api for connected components
public class ConnectedComponents {

  private boolean[] marked;
  //I have explicitly renamed this field to convey exactly what the field is for
  //basically, call whichComponentIs_v_In[v] on any vertex,
  //it will return what I call the CCidx, or basically the index of the
  // connected component
  private int[] whichComponentIs_v_In;
  //renamed this field (in the book this is called getNumComponents)
  //this `getNumComponents` counts the number of components in a graph
  //that is why i call it CC idx, it is like indexing the components
  private int CCidx = 0;


  public ConnectedComponents(EdgeWeightedGraph G) {

    marked = new boolean[G.getNumVertices()];
    whichComponentIs_v_In = new int[G.getNumVertices()];

    for (int v = 0; v < G.getNumVertices(); v++) {
      //dfs ends up marking v AND all of the vertices connected to v by
      // traversing via DFS method
      //then whichComponentIs_v_In[v] becomes `CCidx`
      //the `CCidx` represents which component it is part of. For example, if
      // there are two separate, connected-components in a graph
      // FIRST - this will perform DFS on the first component so that whichComponentIs_v_In[v]
      // for every single vertex on that first component is == 0;
      // THEN - this will perform DFS on the second component so that whichComponentIs_v_In[v]
      // for every single vertex on that second component is == 0;
      if (!marked[v]) {
        dfs(G, v);
        CCidx++;
      }
    }
  }

  private void dfs(EdgeWeightedGraph G, int v) {
    marked[v] = true;
    whichComponentIs_v_In[v] = CCidx;

    //mark v as visited, give v a CC index
    for (int adjV : G.getAllAdjacentVertices(v)) {
      if (!marked[adjV]) {
        //recursively perform DFS, MARKING each vertex along the way
        dfs(G, adjV);
      }
    }
  }

  //Clarification:
  //this connected method is different from the connected method in the
  // UnionFind API
  //This connected method just tells you if there is an existing path between
  // v and w
  //the union find connected tells you if specifically the path YOU traversed
  // connects v to w
  public boolean connected(int v, int w) {
    //this checks out because the 'whichComponentIs_v_In' array returns what
    // I call the CCidx
    // the CCidx is what I use to index the components
    // so if whichComponentIs_v_In[v] == whichComponentIs_v_In[w]
    //     then that means they are part of the same connected component
    return whichComponentIs_v_In[v] == whichComponentIs_v_In[w];
  }

  public int getCCIdx(int v) {
    return whichComponentIs_v_In[v];
  }

  public int getNumComponents() {
    return CCidx;
  }

  public void printSpanningForest(EdgeWeightedGraph G){
    for (int i = 0; i < CCidx; i++){
      for (int j = 0; j < G.getNumVertices(); j++) {
        if (whichComponentIs_v_In[j]==i){
          System.out.println("component: " + i + " vertex: " + j);
        }
      }
    }
  }

  public int getNumVerticesInComponent(int componentIdx) {
    int count = 0;
    for (int i : whichComponentIs_v_In) {
      if(i==componentIdx) {
        count++;
      }
    }
    return count;
  }

  public static void main(String[] args) {

    //using a similar example from the book, but I disconnected one section
    // of it so that there are two components
    EdgeWeightedGraph ewg = new EdgeWeightedGraph(8);
    ewg.addEdge(new Edge(0,1, 4));
    ewg.addEdge(new Edge(0,3, 6));
    ewg.addEdge(new Edge(1,2, 24));
    ewg.addEdge(new Edge(2,3, 23));
    ewg.addEdge(new Edge(2,6, 9));

    ewg.addEdge(new Edge(4,5, 10));
    ewg.addEdge(new Edge(4,7, 21));
    ewg.addEdge(new Edge(5,7, 14));

    ConnectedComponents cc2 = new ConnectedComponents(ewg);
    System.out.println("num components: " + cc2.CCidx);
    cc2.printSpanningForest(ewg);

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

    ConnectedComponents cc3 = new ConnectedComponents(ewgTheeComponents);
    System.out.println("\nnum components: " + cc3.CCidx);
    cc3.printSpanningForest(ewgTheeComponents);
  }


}
