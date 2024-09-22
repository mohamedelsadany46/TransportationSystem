// Class to hold information about a vehicle
class Vehicle {
    // Define an enumeration for Vehicle types
    public enum VehicleType {
        BUS, MINIBUS, LIMOUSINE
    }
    private VehicleType type;
    private int capacity;
    private String licensePlate;

    // Constructor
    public Vehicle(VehicleType type, int capacity, String licensePlate) {
        this.type = type;
        this.capacity = capacity;
        this.licensePlate = licensePlate;
    }

    // Setters for type and capacity
    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Method to display information about the vehicle
    public void displayInfo() {
        System.out.println("Vehicle Type: " + getType());
        System.out.println("Capacity: " + getCapacity());
        System.out.println("License Plate: " + getLicensePlate());
    }

    // Getters to retrieve field values
    public VehicleType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
