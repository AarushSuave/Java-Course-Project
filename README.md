# RescueNet – Disaster Response Management System
### Second Year Engineering Java OOP Mini Project

RescueNet is a desktop-based Disaster Response and Relief Management Application developed in Java using **Java Swing GUI**, **MySQL + JDBC**, and core **Object-Oriented Programming (OOP)** principles.

---

## 1. Project Overview & Features

RescueNet allows emergency response coordinators to manage relief operations across several integrated modules:

1. **User Authentication**: Secure login system (`admin` / `admin123`).
2. **Command Dashboard**: Live statistical metrics and an active background thread status monitor.
3. **Disaster Management**: Register and categorize events by type and severity.
4. **Victim Management**: Record casualty details, medical condition, contact info, and search via overloaded methods.
5. **Rescue Team Management**: Manage tactical units with polymorphic emergency vehicles (`Ambulance` / `RescueVan`).
6. **Shelter Management**: Track emergency shelter locations and live bed occupancy.
7. **Rescue Assignment**: Dispatch available rescue squads to pending victims.
8. **Shelter Bed Allocation**: Allocate victims to relief shelters with custom `ShelterFullException` enforcement.
9. **Status Pipeline**: Progress victims through stages (`Pending` $\rightarrow$ `Assigned` $\rightarrow$ `Rescued` $\rightarrow$ `Shelter Reached` $\rightarrow$ `Completed`).
10. **Reporting & File Export**: Generate consolidated operational summaries and export text logs to `reports/report.txt`.

---

## 2. Java OOP Concepts Demonstrated

| Concept | Implementation in RescueNet | File / Class |
| :--- | :--- | :--- |
| **Classes & Objects** | Real-world entities modeled as classes | `model.Person`, `model.Victim`, `model.Shelter` |
| **Constructors** | Default and Parameterized constructors | All model classes |
| **`this` Keyword** | Disambiguating instance fields from parameters | `Person.java`, `Victim.java`, `Shelter.java` |
| **Static Block & Variables** | Initializing class-level counters and driver loading | `Person.java`, `DatabaseConnection.java` |
| **Instance Initializer Block** | Executing code on every object instantiation | `Person.java` |
| **Inheritance (`super`)** | Hierarchy: `Person` $\rightarrow$ `Victim`, `RescueOfficer` | `Victim.java`, `RescueOfficer.java` |
| **Abstract Class & Methods** | `abstract class Vehicle` with `abstract void rescueOperation()` | `Vehicle.java` |
| **Polymorphism (Overriding)** | Overriding `displayDetails()` and `rescueOperation()` | `Victim.java`, `Ambulance.java`, `RescueVan.java` |
| **Polymorphism (Overloading)** | Overloaded `searchVictim(int id)` & `searchVictim(String name)` | `RescueManager.java` |
| **Dynamic Method Dispatch** | Calling `vehicle.rescueOperation()` via base class reference | `RescueTeam.java` |
| **Interface** | `Reportable` defining `generateSummary()` & `saveToFile()` | `service.Reportable.java` |
| **`final` Keyword** | `final class Report`, `final String APP_VERSION`, `final` method | `service.Report.java` |
| **User-Defined Exception** | Custom checked `ShelterFullException` | `exception.ShelterFullException.java` |
| **Exception Handling** | `try`, `catch`, `finally`, `throw`, `throws` | `ShelterAllocationFrame.java`, `Report.java` |
| **Collections Framework** | `ArrayList<T>`, `HashMap<K,V>`, `Iterator<T>`, `for-each` | `RescueManager.java` |
| **File Handling** | `File`, `FileWriter`, `PrintWriter` exporting `reports/report.txt` | `Report.java` |
| **Multithreading** | `StatusMonitor extends Thread` running periodic background updates | `StatusMonitor.java`, `DashboardFrame.java` |
| **GUI & Event Handling** | `JFrame`, `JPanel`, `JTable`, `JButton`, `ActionListener` | `gui.*` package |
| **Database & JDBC** | `Connection`, `PreparedStatement`, `ResultSet`, `DriverManager` | `DatabaseConnection.java`, `RescueManager.java` |

---

## 3. Project Directory Structure

```text
RescueNet/
├── src/
│   ├── Main.java                          # Application Entry Point
│   ├── model/
│   │   ├── Person.java                    # Base class (Static block, instance block, this)
│   │   ├── Victim.java                    # Inherits Person, method overriding
│   │   ├── RescueOfficer.java             # Inherits Person
│   │   ├── Vehicle.java                   # Abstract base class
│   │   ├── Ambulance.java                 # Subclass implementing rescueOperation()
│   │   ├── RescueVan.java                 # Subclass implementing rescueOperation()
│   │   ├── Disaster.java                  # Disaster entity
│   │   ├── Shelter.java                   # Shelter with capacity logic
│   │   └── RescueTeam.java                # Team with polymorphic Vehicle reference
│   ├── exception/
│   │   └── ShelterFullException.java      # User-defined checked exception
│   ├── service/
│   │   ├── Reportable.java                # Interface
│   │   ├── Report.java                    # Final class with File I/O
│   │   ├── RescueManager.java             # Singleton data store & Collections
│   │   ├── DatabaseConnection.java        # JDBC connectivity
│   │   └── StatusMonitor.java             # Multithreading background monitor
│   ├── gui/
│   │   ├── UITheme.java                   # Styling & color palette
│   │   ├── LoginFrame.java                # Authentication window
│   │   ├── DashboardFrame.java            # Main navigation & live stats
│   │   ├── DisasterFrame.java             # Disaster management
│   │   ├── VictimFrame.java               # Victim registry & search
│   │   ├── RescueTeamFrame.java           # Rescue team manager & vehicle dispatch
│   │   ├── ShelterFrame.java              # Shelter manager
│   │   ├── RescueAssignmentFrame.java     # Team-to-victim assignment
│   │   ├── ShelterAllocationFrame.java    # Shelter allocation & exception demo
│   │   ├── StatusUpdateFrame.java         # Victim progress pipeline
│   │   └── ReportFrame.java               # Report viewer & file saver
│   └── test/
│       └── RescueNetTest.java             # Automated headless test suite
├── reports/
│   └── report.txt                         # Exported text report
├── database.sql                           # MySQL schema and seed data
└── README.md                              # Documentation & Viva Q&A
```

---

## 4. How to Compile and Run

### Requirements
- **Java Development Kit (JDK 17 or 21 LTS)**
- *(Optional)* MySQL Server (If MySQL is not installed/running, RescueNet runs seamlessly with its in-memory data store).

### Quick Start (One-Click Launch on Windows)
Simply double-click [`run.bat`](file:///d:/Coding%20and%20Stuff/OOP/run.bat) or run:
```powershell
.\run.bat
```

---

### Manual Compilation & Execution

#### Step 1: Compile all source files
Open terminal/PowerShell in the project root folder:
```powershell
javac -d bin src/exception/*.java src/model/*.java src/service/*.java src/gui/*.java src/test/*.java src/Main.java
```

### Step 2: Run Automated OOP Test Suite
```powershell
java -cp bin test.RescueNetTest
```

### Step 3: Run the RescueNet Application
```powershell
java -cp bin Main
```

---

## 5. MySQL Database Setup (Optional)

1. Open MySQL Command Line Client or MySQL Workbench.
2. Run the provided script:
   ```sql
   source database.sql;
   ```
3. Update connection credentials in `src/service/DatabaseConnection.java` if your local password differs from `root`.

---

## 6. Viva Examination Questions & Answers (Quick Reference)

### Q1: What is the difference between Method Overloading and Method Overriding in your project?
- **Method Overloading (Compile-time Polymorphism)**: In `RescueManager.java`, `searchVictim(int id)` and `searchVictim(String name)` share the same method name but have different parameter types.
- **Method Overriding (Runtime Polymorphism)**: In `Victim.java`, the `displayDetails()` method overrides the base `displayDetails()` in `Person.java` using `@Override`.

### Q2: How is Abstraction implemented?
- **Abstract Class**: `Vehicle` is an abstract class containing the abstract method `public abstract void rescueOperation();`. The subclasses `Ambulance` and `RescueVan` provide concrete implementations.
- **Interface**: `Reportable` defines a contract (`generateSummary()` and `saveToFile()`), implemented by the `Report` class.

### Q3: What is Dynamic Method Dispatch?
- In `RescueTeam.java`, the field `assignedVehicle` is declared of type `Vehicle` (the base class). When calling `assignedVehicle.rescueOperation()`, the JVM dynamically decides at runtime whether to call `Ambulance.rescueOperation()` or `RescueVan.rescueOperation()` depending on the actual instantiated object.

### Q4: How is the User-Defined Exception implemented and caught?
- `ShelterFullException` extends `java.lang.Exception` (a checked exception).
- In `Shelter.java`, `allocateVictim()` checks `if (occupied >= capacity) throw new ShelterFullException(...)`.
- In `ShelterAllocationFrame.java`, the call is enclosed in a `try-catch (ShelterFullException e)` block displaying a friendly warning dialog.

### Q5: How is Multithreading demonstrated?
- `StatusMonitor` extends `Thread`. It runs an independent background loop with `Thread.sleep(4000)` and invokes `SwingUtilities.invokeLater()` to safely update the live status banner on the `DashboardFrame`.

### Q6: Where is the `final` keyword used?
- `final class Report`: Prevents inheritance.
- `final String APP_VERSION`: Constant that cannot be re-assigned.
- `final boolean saveToFile(...)`: Method that cannot be overridden by any potential subclass.
