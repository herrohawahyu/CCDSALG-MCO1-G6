import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class MailmanDriver {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        ArrayList<School> schoolList = new ArrayList<>();        
        Stack<Mail> mailBag = new Stack<Mail>(); 
        String[] cities = null; 
        String currentCity = null; 
        boolean play = true;
        boolean initialSetupDone = false;

        System.out.println("\n Welcome to the Mailman Simulation!");
        System.out.println("[1] Start!");
        System.out.println("[2] Exit");
        System.out.print("Select an option: ");
        String choice = kb.nextLine();

        if (choice.equals("1")) {
            System.out.print("Location of the Map: ");
            String filePath = kb.nextLine();

            boolean loaded = loadMapData(filePath, schoolList);

            if (loaded) {
                System.out.println("Choose a starting City Post Office from the following list:");
                cities = getCities(schoolList); 
                for (int i = 0; i < cities.length; i++) {
                    System.out.println("[" + (i + 1) + "] " + cities[i]);
                }

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
                initialSetupDone = true;
            } else {
                play = false;
            }
        } else {
            play = false;
        }

        // Core continuous delivery loop
        while (play && initialSetupDone) {
            System.out.println("\n--- CURRENT LOCATION: " + currentCity + " CITY POST OFFICE ---");
            System.out.println("What is your number of mails or quota for the day?");
            int mailQuota = Integer.parseInt(kb.nextLine());

            for (int i = 0; i < mailQuota; i++) {
                System.out.print("Enter the school address no." + (i + 1) + ": ");
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
            
            System.out.println("Extracting mail for " + currentCity + "...");
            ArrayList<Mail> localMail = new ArrayList<>();
            Stack<Mail> tempStack = new Stack<>();
            
            while (!mailBag.isEmpty()) {
                if (mailBag.peek().getDestinationCity().equals(currentCity)) {
                    localMail.add(mailBag.pop());
                } else {
                    tempStack.push(mailBag.pop());
                }
            }
            
            while (!tempStack.isEmpty()) {
                mailBag.push(tempStack.pop());
            }
           
            System.out.println("Sorting mail by distance from the City Post Office using Quicksort...");
            quickSortMail(localMail, 0, localMail.size() - 1, schoolList);

            // ==================== ROUTE SIMULATION VISUALIZER ====================
            System.out.println("\n======================================================================");
            System.out.println("                  --- SIMULATING LOCAL DELIVERY ROUTE ---             ");
            System.out.println("======================================================================");
            System.out.println("[POST OFFICE] Starting from " + currentCity + " Post Office...");

            int stopCounter = 1;
            int deliveryIndex = 0;

            while (deliveryIndex < localMail.size()) {
                Mail currentMail = localMail.get(deliveryIndex);
                School currentSchool = findSchool(currentMail.getSchoolAddress(), schoolList);
                double distance = (currentSchool != null) ? currentSchool.getDistancePO() : 0.0;
                boolean isLast = (deliveryIndex == localMail.size() - 1);
                
                String connector = isLast ? "   └──> " : "   ├──> ";
                String spacing   = isLast ? "        " : "   │    ";
                
                System.out.println("   │");
                System.out.println(connector + "[ROUTE STOP " + stopCounter + "] -> " + currentMail.getSchoolAddress());
                System.out.println(spacing + "[STATUS] Delivered! (Distance: " + distance + " km)");
                
                stopCounter++;
                deliveryIndex++;
            }

            System.out.println("\n======================================================================");
            System.out.println("SUCCESS: All local letters for " + currentCity + " have been successfully delivered!");
            System.out.println("======================================================================");
            
            // Teleportation decision engine based on submission history
            if (!mailBag.isEmpty()) {
                currentCity = mailBag.get(0).getDestinationCity();
                System.out.println("\n[TELEPORT] Teleporting to next city post office: " + currentCity + "...");
            } else {
                System.out.println("\nAll items across all cities have been completely delivered!");
                System.out.println("[1] Re-run simulation from a new post office");
                System.out.println("[2] Exit Program");
                System.out.print("Select an option: ");
                String postChoice = kb.nextLine();
                if (postChoice.equals("1")) {
                    currentCity = null;
                    System.out.println("Choose a starting City Post Office from the following list:");
                    for (int i = 0; i < cities.length; i++) {
                        System.out.println("[" + (i + 1) + "] " + cities[i]);
                    }
                    while (currentCity == null) {
                        int inputOption = Integer.parseInt(kb.nextLine());
                        int index = 0;
                        while (index < cities.length) {
                            if (inputOption == (index + 1)) {
                                currentCity = cities[index];
                            }
                            index++;
                        }
                    }
                    System.out.println("Teleporting to " + currentCity + " City Post Office...");
                } else {
                    play = false;
                }
            }
        }
                  
        System.out.println("Exiting Simulation. Goodbye!");
        kb.close();
    }

    // Restored Original Quicksort methods using standard loop index structures
    private static int partition(ArrayList<Mail> mailBag, int low, int high, ArrayList<School> schoolList) {
        School pivotSchool = findSchool(mailBag.get(high).getSchoolAddress(), schoolList);
        double pivotDistance = (pivotSchool != null) ? pivotSchool.getDistancePO() : 0.0;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            School currentSchool = findSchool(mailBag.get(j).getSchoolAddress(), schoolList);
            double currentDistance = (currentSchool != null) ? currentSchool.getDistancePO() : 0.0;

            if (currentDistance <= pivotDistance) {
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

    private static void quickSortMail(ArrayList<Mail> mailBag, int low, int high, ArrayList<School> schoolList) {
        if (low < high) {
            int pivotIndex = partition(mailBag, low, high, schoolList);
            quickSortMail(mailBag, low, pivotIndex - 1, schoolList);
            quickSortMail(mailBag, pivotIndex + 1, high, schoolList);
        }
    }

    public static boolean loadMapData(String filePath, ArrayList<School> schoolList) {
        boolean success = true;
        schoolList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); 
            line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String city = data[0].trim();
                    String address = data[2].trim();
                    double distance = Double.parseDouble(data[3].trim());

                    School school = new School(city, address, distance);
                    schoolList.add(school);
                }
                line = br.readLine();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[ERROR] Failed to properly read or parse the CSV file structure.");
            success = false;
        }

        return success;
    }

    private static String[] getCities(ArrayList<School> schoolList) {
        ArrayList<String> tempCities = new ArrayList<>();
        for (School school : schoolList) {
            String city = school.getCity();
            if (!tempCities.contains(city)) {
                tempCities.add(city);
            }
        }

        String[] cityArray = new String[tempCities.size()];
        int i = 0;
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
}
