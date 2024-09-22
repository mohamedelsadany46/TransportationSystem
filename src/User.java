import java.util.Scanner;

// Parent class User
class User {
    private String username;
    private String password;
    private int id;

    public User(int id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public int getId() {
        return this.id;
    }


}

// Child class Passenger
class Passenger extends User {

    private String ticketType;
    private Trip tripDetails;
    private int numberOfBookedTickets;

    public Passenger(int id, String username, String password) {
        super(id, username, password);
    }

    public int getNumberOfBookedTickets() {
        return numberOfBookedTickets;
    }

    public void setNumberOfBookedTickets(int numberOfBookedTickets) {
        this.numberOfBookedTickets = numberOfBookedTickets;
    }

    public void setTripDetails(Trip tripDetails) {
        this.tripDetails = tripDetails;
    }

    public void setTicketType(boolean isRoundTrip) {
        if (isRoundTrip)
            this.ticketType = "Round";
        else
            this.ticketType = "Single";
    }

    public int selectTrip(Scanner scanner) {
        while(true){
            System.out.println("Enter the trip number that you want to book: ");
            System.out.print("Enter your choice: ");
            try{
                int TripToBeBooked = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                if (TripToBeBooked < 0 || TripToBeBooked >= Authentication.AvailableTripsList.size()) {
                    System.out.println("Invalid Number for the selected trip");
                } else {
                    System.out.println("You selected the following trip: ");
                    Authentication.AvailableTripsList.get(TripToBeBooked).displayTripDetails();
                    return TripToBeBooked;
                }
            }catch(Exception e){
                System.out.println("Error in selecting the trip.");
            }
        }
    }

    public void bookTicket(Scanner scanner, int TripToBeBooked, int idx) {
        while(true){
            System.out.println("How many tickets do you want to book?");
            System.out.print("Enter number of tickets: ");
            int numTicketsTobeBooked = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            int originalAvailableSeats = Authentication.AvailableTripsList.get(TripToBeBooked).getAvailableSeats();

            if (numTicketsTobeBooked > originalAvailableSeats || numTicketsTobeBooked <= 0) {
                System.out.println("Invalid number for the tickets to be booked!");
            } else {
                double ticketPrice = Authentication.AvailableTripsList.get(TripToBeBooked).getPrice();
                double totalPrice = numTicketsTobeBooked * ticketPrice;
                System.out.println("The total Price for the tickets = " + numTicketsTobeBooked + " x " + ticketPrice + " = " + totalPrice + "EGP");

                System.out.println("Complete Purchase: ");
                System.out.println("1. Confirm");
                System.out.println("2. Cancel");
                System.out.print("Enter your choice: ");
                int purchaseConfirm = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                if (purchaseConfirm == 1) {
                    System.out.println("Purchase Completed. Enjoy Your trip!");
                    Authentication.AvailableTripsList.get(TripToBeBooked).setAvailableSeats(originalAvailableSeats - numTicketsTobeBooked);
                    Authentication.PassengersList.get(idx).setNumberOfBookedTickets(numTicketsTobeBooked);
                    Authentication.PassengersList.get(idx).setTicketType(Authentication.AvailableTripsList.get(TripToBeBooked).isRoundTrip());
                    this.setTripDetails(Authentication.AvailableTripsList.get(TripToBeBooked));
                    return;
                } else if (purchaseConfirm == 2) {
                    System.out.println("Your purchase is canceled.");
                    return;

                } else {
                    System.out.println("Invalid number for confirming/canceling your purchase!");
                }
            }

        }
    }

    public void reviewTicket() {
        // View your tickets:
        System.out.println("Your booked tickets: ");
        this.displayPassengerTrip();
        System.out.println("Ticket Type: " + this.getTicketType());
    }

    public void cancelTicket(Scanner scanner) {

        System.out.print("Enter the number of tickets to be canceled (Enter 0 if you want to keep your tickets): ");
        int ticketNum = scanner.nextInt();
        scanner.nextLine();

        if (ticketNum > this.getNumberOfBookedTickets() || ticketNum < 0) {
            System.out.println("Invalid Number!!");
        } else {
            System.out.println("Canceling " + ticketNum + " tickets.");
            int remainingTickets = this.getNumberOfBookedTickets() - ticketNum;
            this.setNumberOfBookedTickets(remainingTickets);
        }
    }

    public Trip getTripDetails() {
        return this.tripDetails;
    }

    public void displayPassengerTrip(){
        this.tripDetails.displayTripDetails();
    }

    public String getTicketType() {

        return this.ticketType;
    }

    public void displayProfile() {
        System.out.println("Name or Username: " + this.getUsername());
        System.out.println("Passenger ID: " + this.getId());
        System.out.println("Passenger Trip Details (if any): " );
        if (this.getTripDetails() != null)
            displayPassengerTrip();
        else{
            System.out.println("No Trips");
        }
        System.out.println("Passenger Number of Booked Tickets (if any): " + this.getNumberOfBookedTickets());
        System.out.println("Passenger Ticket Type (if any): " + this.getTicketType());

    }

}

// Child class for drivers
class Driver extends User {
    Trip AssignedTrip;
    public Driver(int id, String username, String password) {
        super(id, username, password);
    }

    public void viewAssignedTrips() {
        this.AssignedTrip.displayTripDetails();
    }

    public void accessBasicInfo() {
        System.out.println("Driver Basic Info: ");
        System.out.println("Driver Id: " + getId());
        System.out.println("Driver Username: " + getUsername());

    }
}

// Child class for managers
class Manager extends User {
    public Manager(int id, String username, String password) {
        super(id, username, password);
    }

    public void addTrip(Scanner scanner) {
        String type;
        String Source;
        String destination;
        boolean isRoundTrip;
        int numberOfStops;
        int availableSeats;
        double price;

        System.out.println("Enter Type of trip: ");
        System.out.println("1.internal 2. external");
        System.out.print("Enter your choice: ");
        type = (scanner.nextInt() == 1)? "internal":(scanner.nextInt() == 2)? "external":"invalid";
        scanner.nextLine(); // Consume newline character
        if(type.equals("invalid")) {
            System.out.println("Invalid Type Input!!");
            return;
        }
        System.out.println("Enter source of trip: ");
        Source = scanner.nextLine();
        scanner.nextLine();

        System.out.println("Enter destination of trip: ");
        destination = scanner.nextLine();
        scanner.nextLine();

        System.out.println("Is it a round trip?");
        System.out.println("1.Yes  2.No");
        System.out.print("Enter your choice: ");
        isRoundTrip = scanner.nextInt() == 1;
        scanner.nextLine();

        System.out.println("Enter Number of stops: ");
        numberOfStops = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Available Seats: ");
        availableSeats = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Ticket Price for this trip: ");
        price = scanner.nextInt();
        scanner.nextLine();

        Trip newTrip = new Trip(type, Source, destination, isRoundTrip, numberOfStops, availableSeats, price);
        Authentication.AvailableTripsList.add(newTrip);
        System.out.println("Trip added successfully!");
    }

    public void removeTrip(Scanner scanner) {
        Authentication.DisplayAllAvailableTrips();
        System.out.println("Enter the trip number to be deleted: ");
        System.out.print("Enter your choice: ");
        int tripTobeDeleted = scanner.nextInt();
        try {
            Authentication.AvailableTripsList.remove(tripTobeDeleted - 1);
        }catch(Exception e){
            System.out.println("Error in deleting the trip");
        }

    }

    public void addVehicle(Scanner scanner) {

        Vehicle.VehicleType type;
        System.out.println("Select the Vehicle Type: 1.BUS 2.MINIBUS 3.LIMOUSINE");
        System.out.print("Enter your choice: ");
        int vehicleType = scanner.nextInt();
        scanner.nextLine();

        if(vehicleType == 1) {
            type = Vehicle.VehicleType.BUS;
        }else if(vehicleType == 2){
            type = Vehicle.VehicleType.MINIBUS;
        } else if (vehicleType == 3) {
            type = Vehicle.VehicleType.LIMOUSINE;
        }else{
            System.out.println("Invalid input for the type of vehicle");
            return;
        }

        System.out.print("Write Vehicle Capacity: ");
        int vehicleCapacity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Write Vehicle Licence Number: ");
        String vehicleLicence = scanner.nextLine();
        scanner.nextLine();

        for(Vehicle currVehicle: Authentication.VehiclesList){
            if(currVehicle.getLicensePlate().equals(vehicleLicence)){
                System.out.print("The entered Licence Number already exists!");
                return;
            }
        }

        Vehicle newVehicleObj = new Vehicle(type, vehicleCapacity, vehicleLicence);
        Authentication.VehiclesList.add(newVehicleObj);
        System.out.print("The Vehicle is registered in the system successfully!");


    }

    public void addEmployee(Scanner scanner) {
        System.out.println("Select the Type of Employee: 1.Manager 2.Driver");
        System.out.print("Enter your choice: ");
        int TypeOfEmployee = scanner.nextInt();
        scanner.nextLine();

        if(TypeOfEmployee == 1){
            System.out.print("Enter Manager User Name: ");
            String name = scanner.nextLine();
            scanner.nextLine();
            for(Manager currManager: Authentication.ManagersList){
                if(currManager.getUsername().equals(name)){
                    System.out.println("Manager User exists already in the system!");
                    return;
                }
            }
            Authentication.IDs_counter++;

            System.out.print("Enter Manager Password: ");
            String pass = scanner.nextLine();
            scanner.nextLine();

            Authentication.ManagersList.add(new Manager(Authentication.IDs_counter, name, pass));


        } else if (TypeOfEmployee == 2) {
            System.out.print("Enter Driver User Name: ");
            String name = scanner.nextLine();
            scanner.nextLine();
            for(Driver currDriver: Authentication.DriversList){
                if(currDriver.getUsername().equals(name)){
                    System.out.println("Driver User exists already in the system!");
                    return;
                }
            }
            Authentication.IDs_counter++;

            System.out.print("Enter Driver Password: ");
            String pass = scanner.nextLine();
            scanner.nextLine();

            Authentication.DriversList.add(new Driver(Authentication.IDs_counter, name, pass));

        }else {
            System.out.println("Invalid Input for the Employee Type.");
        }


    }

    public void generateReport() {
        // Logic for generating a report
        Authentication.DisplayAllAvailableTrips();
        DisplayAllDrivers();
        DisplayAllPassengers();
        DisplayAllManagers();
    }

    public void DisplayAllDrivers(){
        System.out.println("Displaying all drivers: ");
        int i = 0;
        for(Driver currDriver: Authentication.DriversList){
            System.out.println("Driver #" + i);
            currDriver.accessBasicInfo();
            System.out.println("-------------------------------------");
            i++;
        }
    }

    public void DisplayAllPassengers(){
        System.out.println("Displaying all passengers: ");
        int i = 0;
        for(Passenger currPassenger: Authentication.PassengersList){
            System.out.println("Passenger #" + i);
            currPassenger.displayProfile();
            System.out.println("-------------------------------------");
            i++;
        }
    }

    public void DisplayAllManagers(){
        System.out.println("Displaying all managers: ");
        int i = 0;
        for(Manager currManager: Authentication.ManagersList){
            System.out.println("Manager #" + i);
            currManager.displayManager();
            System.out.println("-------------------------------------");
            i++;
        }
    }

    public void assignDriver(Scanner scanner) {

        System.out.println("Assigning a driver to a trip: ");
        Authentication.DisplayAllAvailableTrips();
        System.out.println("Enter the trip number to be assigned to the driver: ");
        System.out.print("Enter your choice: ");
        int tripTobeAssigned = scanner.nextInt();
        scanner.nextLine();
        DisplayAllDrivers();
        System.out.println("Enter the driver choice number: ");
        System.out.print("Enter your choice: ");
        int AssignedDriverIdx = scanner.nextInt();

        // Assigning the trip to the driver
        Authentication.DriversList.get(AssignedDriverIdx).AssignedTrip = Authentication.AvailableTripsList.get(tripTobeAssigned);

    }

    public void displayManager() {
        System.out.println("Manager Basic Info: ");
        System.out.println("Manager Id: " + getId());
        System.out.println("Manager Username: " + getUsername());

    }
}
