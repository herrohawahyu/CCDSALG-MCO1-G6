import java.util.ArrayList;

public class ShortestPath {
    private ArrayList<Node> path;
    private double totalDistance;

    public ShortestPath(ArrayList<Node> path, double totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void printPath() {
        if (path.isEmpty()) {
            System.out.println("No valid path found.");
            return;
        }

        String sRoute = "";
        int i = 0;
        while (i < path.size()) {
            sRoute = sRoute + path.get(i).getName();
            if (i < path.size() - 1) {
                sRoute = sRoute + " --> ";
            }
            i++;
        }

        System.out.println("Route: " + sRoute);
        System.out.println("Total Distance: " + totalDistance + " km");
    }
}