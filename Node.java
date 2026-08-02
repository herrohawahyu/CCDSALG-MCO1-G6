import java.util.ArrayList;

public class Node {
    private final String Name;
    private final ArrayList<Edge> EDGES = new ArrayList<>();    
    
    public Node(String name) 
    {
        this.Name = name;
    }
    
    public String getName() 
    { 
        return Name; 
    }

    public ArrayList<Edge> getEdges() 
    { 
        return EDGES; 
    }

    public void addEdge(Edge edge) 
    { 
        EDGES.add(edge); 
    }
}
