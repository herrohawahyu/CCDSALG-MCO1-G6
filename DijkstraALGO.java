import java.util.ArrayList;

public class DijkstraALGO {

    public static ShortestPath findShortestPath(Graph graph, String startNodeName, String destinationNodeName) {
        Node startNode = graph.getNode(startNodeName);
        Node destinationNode = graph.getNode(destinationNodeName);

        if (startNode == null || destinationNode == null) {
            return new ShortestPath(new ArrayList<Node>(), Double.POSITIVE_INFINITY);
        }
        

        // get vertices
        ArrayList<Node> nodesList = graph.getNodes();
        int numVertices = nodesList.size();

        //Declare two arrays
        double[] distance = new double[numVertices];
        boolean[] visited = new boolean[numVertices];


        //used for construct the path
        int[] parent = new int[numVertices];

        // Find start and destination indices
        //Set distance[S] to 0 and distance[v] = infinity

        int sourceIndex = -1;
        int destIndex = -1;

        int i = 0;
        while (i < numVertices) {
            distance[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
            parent[i] = -1;

            if (nodesList.get(i).getName().equalsIgnoreCase(startNodeName)) {
                sourceIndex = i;
            }
            if (nodesList.get(i).getName().equalsIgnoreCase(destinationNodeName)) {
                destIndex = i;
            }
            i++;
        }

        distance[sourceIndex] = 0.0;

        boolean destinationReached = false;
        boolean run = true;

        while (run && !destinationReached) {
            
            // Find vertex with minimum distance that is not visited yet
            int minIndex = -1;
            double minDistance = Double.POSITIVE_INFINITY;
            
            int v = 0;
            while (v < numVertices) {
                if (!visited[v] && distance[v] < minDistance) {
                    minDistance = distance[v];
                    minIndex = v;
                }
                v++;
            }

            // no unvisited vertex then stop finding the shortest path
            if (minIndex == -1 || minDistance == Double.POSITIVE_INFINITY) {
                run = false;
            } else {
                // add minimum vertex to visited
                visited[minIndex] = true;

                // reached destination then stop finding the shortest path
                if (minIndex == destIndex) {
                    destinationReached = true;
                } else {
                    //get the edges of the currentNode
                    Node currentVertexNode = nodesList.get(minIndex);
                    ArrayList<Edge> neighbors = graph.getEdges(currentVertexNode.getName());

                    int edgeIndex = 0;
                    while (edgeIndex < neighbors.size()) {
                        Edge edge = neighbors.get(edgeIndex);
                        Node neighborNode = edge.getAdjacentNode();
                        
                        // Find neighbor index in nodesList
                        int neighborIndex = getNodeIndex(nodesList, neighborNode);

                        // unvisited vertex, then calculate new distance from currentVertexNode to neighbor
                        if (neighborIndex != -1 && !visited[neighborIndex]) {
                            double newDistance = distance[minIndex] + edge.getWeight();

                            //update distance if new path distance is smaller
                            if (newDistance < distance[neighborIndex]) {
                                distance[neighborIndex] = newDistance;
                                parent[neighborIndex] = minIndex;
                            }
                        }
                        edgeIndex++;
                    }
                }
            }
        }

        ArrayList<Node> pathInReverse = new ArrayList<Node>();
        int currIndex = destIndex;

        if (distance[destIndex] != Double.POSITIVE_INFINITY) {
            while (currIndex != -1) {
                pathInReverse.add(nodesList.get(currIndex));
                currIndex = parent[currIndex];
            }
        }

        ArrayList<Node> finalPath = new ArrayList<Node>();
        int pathIdx = pathInReverse.size() - 1;
        while (pathIdx >= 0) {
            finalPath.add(pathInReverse.get(pathIdx));
            pathIdx--;
        }

        return new ShortestPath(finalPath, distance[destIndex]);
    }

    private static int getNodeIndex(ArrayList<Node> nodesList, Node targetNode) {
        int index = -1;
        int i = 0;
        while (i < nodesList.size() && index == -1) {
            if (nodesList.get(i).getName().equalsIgnoreCase(targetNode.getName())) {
                index = i;
            }
            i++;
        }
        return index;
    }
}