import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class MCODriver {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        ArrayList<NonLinearMapList> MapList = new ArrayList<NonLinearMapList>();
        ArrayList<PostOfficeList> POmapList = new ArrayList<PostOfficeList>();
        ArrayList<School> schoolList = new ArrayList<School>();
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
            System.out.print("Location of Post Offices: ");
            String filePath = kb.nextLine();
            boolean loadedPO = loadPostOffices(filePath, POmapList);

            System.out.print("Location of the Nonlinear Map: ");
            filePath = kb.nextLine();
            boolean loadedMap = loadNonLinear(filePath, MapList);
            
            if (loadedPO && loadedMap) {
                cities = getCities(MapList);
                System.out.println("Choose a starting City Post Office from the following list:");
                for (int i = 0; i < cities.length; i++) {
                    System.out.println("[" + (i + 1) + "] " + cities[i]);
                }

                while (currentCity == null) {
                    try {
                        int inputOption = Integer.parseInt(kb.nextLine());
                        int index = 0;
                        while (index < cities.length) {
                            if (inputOption == (index + 1)) {
                                currentCity = cities[index];
                            }
                            index++;
                        }
                    } catch (NumberFormatException e) {
                        currentCity = null;
                    }

                    if (currentCity == null) {
                        System.out.println("Invalid option. Please choose a valid starting City Post Office:");
                    }
                }

                System.out.println("Teleporting to " + currentCity + " Post Office...");
                initialSetupDone = true;
            } else {
                play = false;
            }
        } else {
            play = false;
        }

        // Core continuous delivery loop
        while (play && initialSetupDone) {
            System.out.println("\n--- CURRENT LOCATION: " + currentCity + " POST OFFICE ---");
            System.out.println("What is your number of mails or quota for the day?");
            int mailQuota = -1;
			boolean validQuota = false;

			while (!validQuota) {
			String quotaInput = kb.nextLine();
    
			// Simple verification check to ensure the input string contains only numeric characters
			boolean isNumeric = true;
			int charIdx = 0;
    
			if (quotaInput.isEmpty()) {
				isNumeric = false;
			}
			
			// ensures only numeric input
			while (charIdx < quotaInput.length() && isNumeric) {
				char c = quotaInput.charAt(charIdx);
					if (c < '0' || c > '9') {
						isNumeric = false;
					}
				
				charIdx++;
				}

			if (isNumeric) {
				mailQuota = Integer.parseInt(quotaInput);
				if (mailQuota > 0) {
				validQuota = true;
				} 
				else {
					System.out.println("Quota cannot be negative or zero. Please enter a valid number:");
				}
				} else {
					System.out.println("Invalid input! Please enter an actual number for your daily quota:");
				}
		}

            for (int i = 0; i < mailQuota; i++) {
                System.out.print("Enter the destination address no." + (i + 1) + ": ");
                String Address = kb.nextLine();

                String[] locInfo = findLocationInMap(Address, MapList);
                
                if (locInfo != null) {
					String matchedAddress = locInfo[0];
                    String destinationCity = locInfo[1];

                    Mail mail = new Mail(currentCity, destinationCity, matchedAddress);
                    mailBag.push(mail);
                } else {
                    System.out.println("Invalid destination. Please enter a valid destination address.");
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

            // ==================== ROUTE SIMULATION VISUALIZER ====================
            System.out.println("\n======================================================================");
            System.out.println("                  --- SIMULATING LOCAL DELIVERY ROUTE ---             ");
            System.out.println("======================================================================");
            System.out.println("[POST OFFICE] Starting from " + currentCity + " Post Office...");

            int stopCounter = 1;
            int deliveryIndex = 0;
            /* 
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
        */
       
        System.out.println("Exiting Simulation. Goodbye!");
        kb.close();
        }
    }
    

    public static boolean loadNonLinear(String filePath, ArrayList<NonLinearMapList> MapList) {
        boolean success = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                if (!line.isEmpty()) {
                    String[] data = line.split(",");

                    if (data.length >= 4 && !data[0].equalsIgnoreCase("Post Office")) {
                        String postOffice = data[0].trim();
                        String place1 = data[1].trim();
                        String place2 = data[2].trim();

                        try {
                            double distance = Double.parseDouble(data[3].trim());
                            NonLinearMapList edge = new NonLinearMapList(postOffice, place1, place2, distance);
                            MapList.add(edge);
                            success = true;
                        } catch (NumberFormatException e) {

                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to read NonLinear file: " + e.getMessage());
            success = false;
        }

        if (success) {
            System.out.println("~ Successfully loaded " + MapList.size() + " non-linear map routes ~");
        } else {
            System.out.println("[ERROR] Invalid non-linear map data. Please check file format.");
        }

        return success;
    }

    public static boolean loadPostOffices(String filePath, ArrayList<PostOfficeList> POmapList) {
        boolean success = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    String[] data = line.split(",");

                    if (data.length >= 3 && !data[0].equalsIgnoreCase("Post Office 1")) {
                        String postOffice1 = data[0].trim();
                        String postOffice2 = data[1].trim();

                        try {
                            double distance = Double.parseDouble(data[2].trim());
                            PostOfficeList edge = new PostOfficeList(postOffice1, postOffice2, distance);
                            POmapList.add(edge);
                            success = true;
                        } catch (NumberFormatException e) {

                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to read Post Offices file: " + e.getMessage());
            success = false;
        }

        if (success) {
            System.out.println("~ Successfully loaded " + POmapList.size() + " post office routes ~");
        } else {
            System.out.println("[ERROR] Invalid post office data. Please check file format.");
        }

        return success;
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

    private static String[] getCities(ArrayList<NonLinearMapList> MapList) {
        ArrayList<String> tempCities = new ArrayList<String>();
        int index = 0;

        while(index < MapList.size()){
            String city = MapList.get(index).getCity();

            boolean exists = false;
            int currIndex = 0;

            while (currIndex < tempCities.size() && !exists){
                if(city.equalsIgnoreCase(tempCities.get(currIndex)))
                    exists = true;
                currIndex++;
            }

            if(!exists)
                tempCities.add(city);

            index++;
        }
    
        String[] cityArray = new String[tempCities.size()];
        int i = 0;
        while (i < tempCities.size()) {
            cityArray[i] = tempCities.get(i);
            i++;
        }
        return cityArray;
    }

    
    private static String[] findLocationInMap(String address, ArrayList<NonLinearMapList> MapList) {
        String match[] = null;
        String cleanAddress = address.replace("’", "'").replace("`", "'").trim();
        int i = 0;

        while (i < MapList.size() && match == null) {
            NonLinearMapList edge = MapList.get(i);
            String p1 = edge.getPlace1().trim();
            String p2 = edge.getPlace2().trim();

            if (p1.equalsIgnoreCase(cleanAddress) || p1.toLowerCase().contains(cleanAddress.toLowerCase())) {
                match = new String[] {p1, edge.getCity()};
            } else if (p2.equalsIgnoreCase(cleanAddress) || p2.toLowerCase().contains(cleanAddress.toLowerCase())) {
                match = new String[] {p2, edge.getCity()};
            }
            i++;
        }

        return match;
    }
}