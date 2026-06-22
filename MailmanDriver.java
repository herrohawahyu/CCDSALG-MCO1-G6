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
        Stack<Mail> mailStack = new Stack<Mail>();
        boolean play = true;

        while (play) {
            Stack<Mail> mailBag = new Stack<Mail>();
            int i;
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
                
                    String[] cities = getCities(schoolList);
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

    private static boolean loadMapData(String filePath, ArrayList<School> schoolList) {
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