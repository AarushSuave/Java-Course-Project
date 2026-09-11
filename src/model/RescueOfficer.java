package model;

// Subclass demonstrating Inheritance and constructor reuse
public class RescueOfficer extends Person {

    private int officerId;
    private String designation; // e.g., "Team Lead", "Paramedic", "Rescue Specialist"
    private int teamId;

    // Default constructor
    public RescueOfficer() {
        super();
        this.officerId = 0;
        this.designation = "Rescue Officer";
        this.teamId = 0;
    }

    // Parameterized constructor
    public RescueOfficer(int officerId, String name, int age, String phone, String location,
                         String designation, int teamId) {
        super(name, age, phone, location);
        this.officerId = officerId;
        this.designation = designation;
        this.teamId = teamId;
    }

    // Getters and Setters
    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    // Overriding displayDetails
    @Override
    public void displayDetails() {
        System.out.println("--- Rescue Officer Details (Badge ID: " + officerId + ") ---");
        super.displayDetails();
        System.out.println("Designation: " + designation);
        System.out.println("Assigned Team ID: " + teamId);
    }
}
