package model;

// Subclass demonstrating Inheritance and Method Overriding
public class Victim extends Person {

    private int victimId;
    private String medicalStatus; // e.g., "Normal", "Injured", "Critical"
    private String rescueStatus;  // e.g., "Pending", "Assigned", "Rescued", "Shelter Reached", "Completed"
    private int assignedTeamId;   // 0 if unassigned
    private int allocatedShelterId; // 0 if unallocated

    // Default constructor
    public Victim() {
        super(); // Calls Person() default constructor
        this.victimId = 0;
        this.medicalStatus = "Normal";
        this.rescueStatus = "Pending";
        this.assignedTeamId = 0;
        this.allocatedShelterId = 0;
    }

    // Parameterized constructor demonstrating constructor chaining with super()
    public Victim(int victimId, String name, int age, String phone, String location,
                  String medicalStatus, String rescueStatus) {
        super(name, age, phone, location); // Passes base fields to superclass Person
        this.victimId = victimId;
        this.medicalStatus = medicalStatus;
        this.rescueStatus = (rescueStatus == null || rescueStatus.trim().isEmpty()) ? "Pending" : rescueStatus;
        this.assignedTeamId = 0;
        this.allocatedShelterId = 0;
    }

    // Full constructor
    public Victim(int victimId, String name, int age, String phone, String location,
                  String medicalStatus, String rescueStatus, int assignedTeamId, int allocatedShelterId) {
        super(name, age, phone, location);
        this.victimId = victimId;
        this.medicalStatus = medicalStatus;
        this.rescueStatus = rescueStatus;
        this.assignedTeamId = assignedTeamId;
        this.allocatedShelterId = allocatedShelterId;
    }

    // Getters and Setters
    public int getVictimId() {
        return victimId;
    }

    public void setVictimId(int victimId) {
        this.victimId = victimId;
    }

    public String getMedicalStatus() {
        return medicalStatus;
    }

    public void setMedicalStatus(String medicalStatus) {
        this.medicalStatus = medicalStatus;
    }

    public String getRescueStatus() {
        return rescueStatus;
    }

    public void setRescueStatus(String rescueStatus) {
        this.rescueStatus = rescueStatus;
    }

    public int getAssignedTeamId() {
        return assignedTeamId;
    }

    public void setAssignedTeamId(int assignedTeamId) {
        this.assignedTeamId = assignedTeamId;
    }

    public int getAllocatedShelterId() {
        return allocatedShelterId;
    }

    public void setAllocatedShelterId(int allocatedShelterId) {
        this.allocatedShelterId = allocatedShelterId;
    }

    // Demonstrating Method Overriding
    @Override
    public void displayDetails() {
        System.out.println("--- Victim Details (ID: " + victimId + ") ---");
        super.displayDetails(); // Calling base class method using super
        System.out.println("Medical Status: " + medicalStatus);
        System.out.println("Rescue Status: " + rescueStatus);
        System.out.println("Assigned Team ID: " + (assignedTeamId == 0 ? "None" : assignedTeamId));
        System.out.println("Shelter ID: " + (allocatedShelterId == 0 ? "None" : allocatedShelterId));
    }
}
