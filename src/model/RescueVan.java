package model;

// Concrete subclass of abstract Vehicle demonstrating method overriding
public class RescueVan extends Vehicle {

    private boolean hasFloodGear;

    // Default constructor
    public RescueVan() {
        super("VAN-01", "Heavy Duty Rescue Van", 10);
        this.hasFloodGear = true;
    }

    // Parameterized constructor
    public RescueVan(String vehicleId, String model, int capacity, boolean hasFloodGear) {
        super(vehicleId, model, capacity);
        this.hasFloodGear = hasFloodGear;
    }

    public boolean isHasFloodGear() {
        return hasFloodGear;
    }

    public void setHasFloodGear(boolean hasFloodGear) {
        this.hasFloodGear = hasFloodGear;
    }

    // Implementing the abstract method from Vehicle
    @Override
    public void rescueOperation() {
        System.out.println("[Rescue Van Action] Evacuating multiple victims and carrying rescue equipment.");
    }

    @Override
    public String getVehicleType() {
        return "Rescue Van" + (hasFloodGear ? " (Flood Equipped)" : "");
    }
}
