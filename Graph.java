import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Graph {
    private final Map<String, Node> NODES_BY_NAME;
    private final Map<Node, ArrayList<Edge>> ADJACENCY_LIST;
    
    public Graph() 
    {
        this.NODES_BY_NAME = new LinkedHashMap<>();
        this.ADJACENCY_LIST = new LinkedHashMap<>();
    }

    public ArrayList<Node> getNodes() 
    { 
        return new ArrayList<>(NODES_BY_NAME.values()); 
    }

    public void addNode(Node node) 
    { 
        if (!NODES_BY_NAME.containsKey(node.getName())) {
            NODES_BY_NAME.put(node.getName(), node);
            ADJACENCY_LIST.put(node, new ArrayList<>());
        }
    }

    public Node getNode(String nodeName)
    {
        return NODES_BY_NAME.get(nodeName);
    }

    private void addEdge(String fromNodeName, String toNodeName, double weight)
    {
        Node fromNode = getNode(fromNodeName);
        Node toNode = getNode(toNodeName);

        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("Both nodes must exist before adding an edge.");
        }

        ADJACENCY_LIST.get(fromNode).add(new Edge(toNode, weight));
    }

    public void addUndirectedEdge(String nodeName1, String nodeName2, double weight)
    {
        addEdge(nodeName1, nodeName2, weight);
        addEdge(nodeName2, nodeName1, weight);
    }

    public ArrayList<Edge> getEdges(String nodeName)
    {
        Node node = getNode(nodeName);
        if (node == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(ADJACENCY_LIST.get(node));
    }
    
    public boolean containsNode(String nodeName) 
    {
        return NODES_BY_NAME.containsKey(nodeName);
    }
}
