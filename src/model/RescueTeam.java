package model;

// Class demonstrating Aggregation / Association with abstract Vehicle and Dynamic Binding
public class RescueTeam {

    private int teamId;
    private String teamName;
    private int memberCount;
    private boolean isAvailable;
    
    // Demonstrating Polymorphism: Base class reference pointing to Ambulance or RescueVan
    private Vehicle assignedVehicle;

    public RescueTeam() {
        this.teamId = 0;
        this.teamName = "General Rescue Squad";
        this.memberCount = 5;
        this.isAvailable = true;
        this.assignedVehicle = new RescueVan();
    }

    public RescueTeam(int teamId, String teamName, int memberCount, boolean isAvailable, Vehicle assignedVehicle) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.memberCount = memberCount;
        this.isAvailable = isAvailable;
        this.assignedVehicle = assignedVehicle;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public void setAssignedVehicle(Vehicle assignedVehicle) {
        this.assignedVehicle = assignedVehicle;
    }

    // Business methods
    public boolean assignToMission() {
        if (!isAvailable) {
            return false;
        }
        this.isAvailable = false;
        return true;
    }

    public void completeMission() {
        this.isAvailable = true;
    }

    // Demonstrating dynamic binding when calling vehicle.rescueOperation()
    public void executeRescue() {
        System.out.println("Team " + teamName + " is executing rescue mission...");
        if (assignedVehicle != null) {
            assignedVehicle.rescueOperation(); // Dynamic Method Dispatch
        }
    }

    public void displayDetails() {
        System.out.println("Team #" + teamId + ": " + teamName + " (Members: " + memberCount 
                           + ") - Available: " + isAvailable 
                           + " | Vehicle: " + (assignedVehicle != null ? assignedVehicle.getVehicleType() : "None"));
    }
}
