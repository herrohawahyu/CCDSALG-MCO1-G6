public class Edge {
    private final double WEIGHT;
    private final Node ADJACENT_NODE;
    
    public Edge(Node adjacentNode, double weight) 
    {
        this.ADJACENT_NODE = adjacentNode;
        this.WEIGHT = weight;
    }

    public double getWeight() 
    { 
        return WEIGHT; 
    }

    public Node getAdjacentNode() 
    { 
        return ADJACENT_NODE; 
    }
}
