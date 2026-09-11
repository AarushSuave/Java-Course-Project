package model;

// Concrete subclass of abstract Vehicle demonstrating method overriding and dynamic binding
public class Ambulance extends Vehicle {

    private boolean hasIcuEquipment;

    // Default constructor
    public Ambulance() {
        super("AMB-01", "Advanced Life Support Ambulance", 2);
        this.hasIcuEquipment = true;
    }

    // Parameterized constructor
    public Ambulance(String vehicleId, String model, int capacity, boolean hasIcuEquipment) {
        super(vehicleId, model, capacity);
        this.hasIcuEquipment = hasIcuEquipment;
    }

    public boolean isHasIcuEquipment() {
        return hasIcuEquipment;
    }

    public void setHasIcuEquipment(boolean hasIcuEquipment) {
        this.hasIcuEquipment = hasIcuEquipment;
    }

    // Implementing the abstract method from Vehicle
    @Override
    public void rescueOperation() {
        System.out.println("[Ambulance Action] Transporting critical victims with life support equipment.");
    }

    @Override
    public String getVehicleType() {
        return "Ambulance" + (hasIcuEquipment ? " (ICU Equipped)" : "");
    }
}
