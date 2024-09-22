// Class to hold information about a trip
class Trip {
    private String type;
    private String Source;
    private String destination;
    private boolean isRoundTrip;
    private int numberOfStops;
    private int availableSeats;
    private double price;

    public Trip(String type, String Source, String destination, boolean isRoundTrip, int numberOfStops, int availableSeats, double price) {
        this.type = type;
        this.Source = Source;
        this.destination = destination;
        this.isRoundTrip = isRoundTrip;
        this.numberOfStops = numberOfStops;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getters and setters for Trip attributes

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(int numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void displayTripDetails(){

        System.out.println("Type of trip: " + getType());
        System.out.println("Source: " + getSource());
        System.out.println("Destination: " + getDestination());
        System.out.println("Is it round trip? : " + isRoundTrip());
        System.out.println("Num of Stops: " + getNumberOfStops());
        System.out.println("Available Seats: " + getAvailableSeats());
        System.out.println("Price: " + getPrice());

    }
}
