import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.HashMap;

public class MailmanDriver {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        Graph cityGraph = new Graph();
        HashMap<String, Graph> localGraphs = new HashMap<>();
        boolean play = true;

        while (play) {
            Stack<Mail> mailBag = new Stack<>();
            int i;
            System.out.println("\n Welcome to the Mailman Simulation!");
            System.out.println("[1] Start!");
            System.out.println("[2] Exit");
            System.out.print("Select an option: ");
            String choice = kb.nextLine();

            if (choice.equals("1")) {
                System.out.print("Location of the Post Office Map: ");
                String postOfficeFilePath = kb.nextLine();
                System.out.print("Location of the Map: ");
                String filePath = kb.nextLine();

                boolean loadedPostOffice = loadPostOfficeData(postOfficeFilePath, cityGraph);
                boolean loadedMap = loadMapData(filePath, cityGraph);

                if (loadedMap && loadedPostOffice) {

                    System.out.println("Choose a starting City Post Off/ce from the following list:");
                
                    String[] cities = getCities(cityGraph);
                    for (i = 0; i < cities.length; i++) {
                        System.out.println("[" + (i + 1) + "] " + cities[i]);
                    }

                    String currentCity = null; 

                    while (currentCity == null) {
                        int inputOption = Integer.parseInt(kb.nextLine());

                        int index = 0;
                        while (index < cities.length) {
                            int targetOptionNumber = index + 1;
                            
                            if (inputOption == targetOptionNumber) {
                                currentCity = cities[index];
                            }
                            
                            index++;
                        }
                        
                        if (currentCity == null) {
                            System.out.println("Invalid option. Please choose a valid starting City Post Office:");
                        }
                    }
       
                    System.out.println("Teleporting to " + currentCity + " City Post Office...");

                    System.out.println("What is your number of mails or quota for the day?");
                    
                    int mailQuota = Integer.parseInt(kb.nextLine());

                    for (i = 0; i < mailQuota; i++) {
                        System.out.println("Enter the school address no." + (i + 1) + ": ");
                        String Address = kb.nextLine();


                        School targetSchool = findSchool(Address, schoolList);
                        
                        if (targetSchool != null) {
                            String destinationCity = targetSchool.getCity();
                            double distance = targetSchool.getDistancePO();
                            Mail mail = new Mail(currentCity, destinationCity, Address, distance);
                            mailBag.push(mail);
                        } else {
                            System.out.println("Invalid destination. Please enter a valid school address.");
                            i--;
                        }
                    }
                    System.out.println("Done collecting from PO box!");
                    
                    System.out.println("Getting mail for current city...");
                    ArrayList<Mail> localMail = new ArrayList<>();
                    Stack<Mail> tempStack = new Stack<>();
                    while (!mailBag.isEmpty()) {
                        if (mailBag.peek().getOriginCity().equals(currentCity)) {
                            localMail.add(mailBag.pop());
                        } else {
                            tempStack.push(mailBag.pop());
                        }
                    }
                    
                    //now return the non-local mail back to the mail bag
                    while (!tempStack.isEmpty()) {
                        mailBag.push(tempStack.pop());
                    }
                   
                    System.out.println("Sorting mail by distance from the City Post Office...");
                    quickSortMail(localMail, 0, localMail.size() - 1);


                    System.out.println("Delivering mail...");

                    for (Mail mail : localMail) {
                        System.out.println("Delivering mail from " + mail.getOriginCity() + " to " + mail.getSchoolAddress());
                    }
                    
                
                }
            }
            else
                play = false;
        }


                  
        kb.close();
    }

    private static boolean loadPostOfficeData(String filePath, Graph graph) {
        boolean success = true;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            line = br.readLine();

            while (line != null) {
                 String[] data = line.split(",");
                 if (data.length == 3) {
                        //remove the last two words from the city names and trim whitespace
                        String city1 = data[0].replace(" Post Office", "").trim();
                        String city2 = data[1].replace(" Post Office", "").trim();
                        double distance = Double.parseDouble(data[2].trim());
                        
                        if (!graph.containsNode(city1)) {
                            graph.addNode(new Node(city1));
                        }
                        
                        if (!graph.containsNode(city2)) {
                            graph.addNode(new Node(city2));
                        }
                        
                        //now add the edge between the two nodes
                        graph.addUndirectedEdge(city1, city2, distance);

                    }
                    line = br.readLine();
                }
            }
        catch (IOException | NumberFormatException e) {
            System.out.println("[ERROR] Failed to properly read or parse the CSV file structure.");
            success = false;
        }

        return success;
    }
    private static boolean loadMapData(String filePath, Map <String, Graph> localGraphs) {
        boolean success = true;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            line = br.readLine();

            while (line != null) {
                 String[] data = line.split(",");
                 if (data.length == 4) {
                        String city = data[0].trim();
                        String node1_name = data[1].trim();
                        String node2_name = data[2].trim();
                        double distance = Double.parseDouble(data[3].trim());
                        
                        if (!localGraphs.containsKey(city)) {
                            localGraphs.put(city, new CityGraph(city));
                        }
                        
                        Graph cityGraph = localGraphs.get(city);
                        if (cityGraph.getNode(node1_name) == null) {
                            cityGraph.addNode(new Node(node1_name));
                        }
                        if (cityGraph.getNode(node2_name) == null) {
                            cityGraph.addNode(new Node(node2_name));
                        }
                        cityGraph.addUndirectedEdge(node1_name, node2_name, distance);

                        
                    }
                    line = br.readLine();
                }
            }
        catch (IOException | NumberFormatException e) {
            System.out.println("[ERROR] Failed to properly read or parse the CSV file structure.");
            success = false;
        }

        return success;
    }

    private static String[] getCities(ArrayList<School> schoolList) {
        ArrayList<String> tempCities = new ArrayList<>();
        String city;  
        int i;
        for (School school : schoolList) {
            city = school.getCity();

            if (!tempCities.contains(city)) {
                tempCities.add(city);
            }
        }

        String[] cityArray = new String[tempCities.size()];
        i = 0;
        while (i < tempCities.size()) {
            cityArray[i] = tempCities.get(i);
            i++;
        }
        return cityArray;
    }

    private static School findSchool(String Address, ArrayList<School> schoolList) {
        School matchedSchool = null;
        int i = 0;
    
        while (i < schoolList.size()) {
            if (schoolList.get(i).getAddress().equalsIgnoreCase(Address)) {
                matchedSchool = schoolList.get(i);
            }
            i++;
        }
      return matchedSchool;
    }
    
    private static int partition(ArrayList<Mail> mailBag, int low, int high) {
        // method used for quicksort using Lomuto partition scheme
        Mail pivot = mailBag.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (mailBag.get(j).getDistance() <= pivot.getDistance()) {
                i++;
                Mail temp = mailBag.get(i);
                mailBag.set(i, mailBag.get(j));
                mailBag.set(j, temp);
            }
        }

        Mail temp = mailBag.get(i + 1);
        mailBag.set(i + 1, mailBag.get(high));
        mailBag.set(high, temp);
        return i + 1;
    }

    private static void quickSortMail(ArrayList<Mail> mailBag, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(mailBag, low, high);
            quickSortMail(mailBag, low, pivotIndex - 1);
            quickSortMail(mailBag, pivotIndex + 1, high);
        }
    }
}