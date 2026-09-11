package model;

// Abstract Class demonstrating Abstraction and Polymorphic base reference
public abstract class Vehicle {

    private String vehicleId;
    private String model;
    private int capacity;

    // Default constructor
    public Vehicle() {
        this.vehicleId = "V-000";
        this.model = "Standard Vehicle";
        this.capacity = 4;
    }

    // Parameterized constructor
    public Vehicle(String vehicleId, String model, int capacity) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Concrete method
    public void displayVehicle() {
        System.out.println("Vehicle ID: " + vehicleId + " | Model: " + model + " | Capacity: " + capacity);
    }

    // Pure Abstract Method - Must be implemented by all concrete subclasses
    public abstract void rescueOperation();

    // Abstract method to get vehicle type string
    public abstract String getVehicleType();
}
