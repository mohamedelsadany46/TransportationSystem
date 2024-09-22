import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.APPEND;

public class Authentication {
    static int IDs_counter = 0; // A counter that gives an id for each user
    static String USER_DATA_FILE = "UsersDataBase.csv";
    static ArrayList<Passenger> PassengersList = new ArrayList<Passenger>();;
    static ArrayList<Manager> ManagersList = new ArrayList<Manager>();
    static ArrayList<Driver> DriversList  = new ArrayList<Driver>();
    static ArrayList<Trip> AvailableTripsList  = new ArrayList<Trip>();
    static ArrayList<Vehicle> VehiclesList  = new ArrayList<Vehicle>();

    private static Map<String, String> userCredentials = new HashMap<>(); // Store username-password pairs


    public void LaunchSystem(){
        loadUserData();
        initTripsList();
        Scanner scanner = new Scanner(System.in);

        // The login/regitser page - Enters Program Loop
        while (true) {
            System.out.println("Welcome to the Transportation Management System!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option (either 1, 2, or 3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    register(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void initTripsList(){

        // Each trip includes attributes such as Type (internal, external), price, source, destination,
        // one-way/round-trip, number of stops, available seats, and price

        AvailableTripsList.add(new Trip("internal", "Alex","Cairo",true, 1, 10,120));
        AvailableTripsList.add(new Trip("external", "Alex","Rome",true, 0, 50,10500));
        AvailableTripsList.add(new Trip("internal", "Giza","Cairo",false, 1, 30,70));
        AvailableTripsList.add(new Trip("external", "Cairo","Geneva",false, 0, 50,17000));

    }
    // Register a new account
    private static void register(Scanner scanner) {
        System.out.println("Register a new account");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // If username already exists
        if(userCredentials.containsKey(username)){
            System.out.println("Invalid Input!! Username is already taken!");
            System.out.println("Registration failed!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Select Type 1. Passenger 2. Employee : ");
        int type = Integer.parseInt(scanner.nextLine());
        boolean Registeration_status = false;
        int EmployeeType = -1;

        if (type == 1){
            // Increment the id of the user
            IDs_counter++;
            // Add the new passenger to the PassengersList
            PassengersList.add(new Passenger(IDs_counter, username, password));
            Registeration_status = true;
        }else if (type == 2){
            System.out.print("Select Type 1. Manager 2. Driver : ");
            EmployeeType = Integer.parseInt(scanner.nextLine());

            // Add the new Employee to the EmployeesList
            if(EmployeeType == 1) {
                // Increment the id of the user
                IDs_counter++;
                ManagersList.add(new Manager(IDs_counter, username, password));
                Registeration_status = true;

            }else if(EmployeeType == 2) {
                // Increment the id of the user
                IDs_counter++;
                DriversList.add(new Driver(IDs_counter, username, password));
                Registeration_status = true;

            }else{
                System.out.println("Invalid Input!!");
            }

        }else{
            System.out.println("Invalid Input!!");
        }

        if (Registeration_status) {
            System.out.println("Registration successful!");
            try {
                saveUserData(IDs_counter, username, password, type, EmployeeType); // Save new user data to users data base file
            } catch (IOException e) {
                throw new RuntimeException("Error while Appending the data to the CSV File: ", e);
            }

        }else {
            System.out.println("Registration failed!");
        }

    }

    // Login to an existing account
    private static void login(Scanner scanner) {
        System.out.println("Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Check if the username exists and the password matches
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            System.out.println("Login successful!");
            // Select user type after successful login
            selectUserType(scanner, username);

        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    // Select user type (passenger or employee)
    private static void selectUserType(Scanner scanner, String username) {
        System.out.println("Select user type:");
        System.out.println("1. Passenger");
        System.out.println("2. Employee");
        System.out.print("Enter your choice: ");
        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (userType) {
            case 1:
                // Proceed as a passenger
                PassengerProgramLoop(scanner, username);

            case 2:
                // Proceed as an employee
                int employeeIdx = 0;
                boolean isManager = false;

                // Proceed as a manager
                for (Manager currManager : ManagersList) {
                    String currManagerName = currManager.getUsername();
                    if (currManagerName.equals(username)){
                        isManager = true;
                        ManagerProgramLoop(scanner, username, employeeIdx);
                    }
                    employeeIdx++;

                }

                if (isManager) break;

               // Proceed as a Driver
                employeeIdx = 0;
                for (Driver currDriver : DriversList) {
                    String currDriverName = currDriver.getUsername();
                    if (currDriverName.equals(username)){
                        DriverProgramLoop(scanner, username, employeeIdx);
                    }
                    employeeIdx++;
                }
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // Load Program data from CSV file once the system is up
    private static void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            String[] parts = null;
            boolean file_empty = true;
            while ((line = br.readLine()) != null) {
                file_empty = false;
                parts = line.split(",");
                userCredentials.put(parts[1], parts[2]);
                if(parts[3].equals("1")){
                    // Initialize the Passengers List
                    PassengersList.add(new Passenger(Integer.parseInt(parts[0]), parts[1], parts[2]));
                }else if (parts[3].equals("2")){
                    if(parts[4].equals("1")){
                        // Initialize the Managers List
                        ManagersList.add(new Manager(Integer.parseInt(parts[0]), parts[1], parts[2]));
                    }else if(parts[4].equals("2")){
                        // Initialize the Drivers List
                        DriversList.add(new Driver(Integer.parseInt(parts[0]), parts[1], parts[2]));
                    }
                }
            }
            if(!file_empty) IDs_counter = Integer.parseInt(parts[0]);
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    // Save New user data to CSV file when registration is successful
    private static void saveUserData(int IDs_counter, String username, String password, int type, int EmployeeType) throws IOException{
        Path FilePath = Paths.get(USER_DATA_FILE);
        byte[] bytes = (IDs_counter + "," + username + "," + password + "," + type + "," + EmployeeType + '\n').getBytes();
        Files.write(FilePath, bytes, APPEND);
        System.out.println("Success: New User Data Appended to the CSV Database");

    }

    // Display all available Trips
    public static void DisplayAllAvailableTrips(){
        for(int i = 0; i < AvailableTripsList.size(); i++){
            System.out.println("Trip #" +  i );
            AvailableTripsList.get(i).displayTripDetails();
            System.out.println("-------------------------------------");
        }
    }

    // Passenger Program Loop upon successful Login
    private static void PassengerProgramLoop(Scanner scanner, String username){
        boolean passengerFound = false;
        String passengerName = "";
        int idx = 0;
        for (Passenger currPassenger : PassengersList) {
            passengerName = currPassenger.getUsername();
            if (passengerName.equals(username)) {
                passengerFound = true;
                break;
            }
            idx++;
        }
        if (!passengerFound) {
            System.out.print("You are NOT registered as a passenger in the system!");
            return;
        }
        // show profile
        System.out.println("Welcome " + passengerName + " to your profile!");

        // Passenger Does some actions like select trips, booking
        // tickets, reviewing/canceling tickets, and displaying the passenger profile.
        System.out.println("Select the action you want to perform: ");
        System.out.println("1. View All trips");
        System.out.println("2. Review/Cancel Ticket");
        System.out.println("3. Display Passenger Profile");
        System.out.println("4. Log out");
        System.out.print("Enter your choice: ");
        int passengerAction = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (passengerAction == 1) {
            // Display All Available Trips to Select a Trip and then book a ticket
            DisplayAllAvailableTrips();
            int TripToBeBooked = PassengersList.get(idx).selectTrip(scanner);
            PassengersList.get(idx).bookTicket(scanner, TripToBeBooked, idx);

        } else if (passengerAction == 2) {
            // Review or Cancel Ticket
            if (PassengersList.get(idx).getTripDetails() != null) {
                PassengersList.get(idx).reviewTicket();
                PassengersList.get(idx).cancelTicket(scanner);

            } else {
                System.out.println("You have not booked any tickets!!");
            }

        } else if (passengerAction == 3) {
            // Display Passenger's Profile
            System.out.println("Here is a look into your profile: ");
            PassengersList.get(idx).displayProfile();

        }else if (passengerAction == 4) {
            // logging out the passenger by breaking out of the for loop
            System.out.println("Logging you out. See you " + username + " later!");
        } else {
            // Invalid Input -> Log out
            System.out.println("Invalid Input. System will restart...");
        }

    }

    // Manager Program Loop upon successful Login
    private static void ManagerProgramLoop(Scanner scanner, String username, int managerIdx){
        // If you log in as a manager you are able to review all trips in the system,you are able
        // to add / remove trips and assign drivers to the trips in the system.

        // show profile
        System.out.println("Welcome Manager " + username + " to your profile!");

        System.out.println("Select the action you want to perform: ");
        System.out.println("1. Review All trips");
        System.out.println("2. Add A New Trip");
        System.out.println("3. Remove A trip");
        System.out.println("4. Assign a Driver to a Trip");
        System.out.println("5. Generate a Full Report");
        System.out.println("6. Add a new Vehicle");
        System.out.println("7. Add a new Employee");
        System.out.println("8. Log out");
        System.out.print("Enter your choice: ");
        int ManagerAction = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if(ManagerAction == 1){
            // 1. Review All trips
            DisplayAllAvailableTrips();

        }else if (ManagerAction == 2){
            // 2. Add A New Trip
            ManagersList.get(managerIdx).addTrip(scanner);

        } else if (ManagerAction == 3) {
            // 3. Remove A trip
            ManagersList.get(managerIdx).removeTrip(scanner);

        } else if (ManagerAction == 4) {
            // 4. Assign a Driver to a Trip
            ManagersList.get(managerIdx).assignDriver(scanner);

        } else if (ManagerAction == 5) {
            // 5. Generate a Full Report
            ManagersList.get(managerIdx).generateReport();

        }else if (ManagerAction == 6) {
            // 6. Add a new Vehicle
            ManagersList.get(managerIdx).addVehicle(scanner);

        }else if (ManagerAction == 7) {
            // 7. Add a new Employee
            ManagersList.get(managerIdx).addEmployee(scanner);

        }else if (ManagerAction == 8) {
            System.out.println("Logging you out. See you " + username + " later!");

        } else {
            // Invalid Input -> Log out
            System.out.println("Invalid Input. System will restart...");
        }


    }

    // Driver Program Loop upon successful Login
    private static void DriverProgramLoop(Scanner scanner, String username, int driverIdx){

        // If you log in with a driver credentials you are directed to the drivers profile with
        // some basic information about the driver and the trips that are assigned to him by the manager.

        // show profile
        System.out.println("Welcome Driver " + username + " to your profile!");
        DriversList.get(driverIdx).accessBasicInfo();
        System.out.println("Your Assigned Trips: ");
        DriversList.get(driverIdx).viewAssignedTrips();

        System.out.println("See you soon!");

    }
}
