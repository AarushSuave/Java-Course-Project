# RescueNet: 4-Member Project Work Distribution & Viva Guide

This document divides the entire **RescueNet** project equally among **4 students**. Each person has a clear module, distinct OOP concepts, designated file paths, and straightforward explanations ready for strict examiners.

---

```
                                  [ RescueNet Project Architecture ]
                                                 │
          ┌───────────────────────┬──────────────┴───────────────┬───────────────────────┐
          ▼                       ▼                              ▼                       ▼
      MEMBER 1                MEMBER 2                       MEMBER 3                MEMBER 4
   Core Models &           Polymorphism &                 Business Logic &         Reporting, I/O &
   Inheritance             Exceptions                     Sync Engine              GUI Flow
   • Person.java           • Vehicle.java (Abstract)      • RescueManager.java     • Reportable.java
   • Victim.java           • Ambulance.java               • DatabaseConnection     • Report.java
   • RescueOfficer.java    • RescueVan.java               • OfflineSyncService     • Main.java
   • Disaster.java         • Shelter.java                 • StatusMonitor.java     • LoginFrame.java
   • VictimFrame.java      • ShelterFullException                                  • ReportFrame.java
                           • ShelterFrame.java                                     • UITheme.java
```

---

## Quick Summary Table

| Person | Module / Role | Primary Files Owned | Key OOP Concepts Demonstrated |
| :--- | :--- | :--- | :--- |
| **Member 1** | **Casualty & Incident Domain** | `src/model/Person.java`<br>`src/model/Victim.java`<br>`src/model/RescueOfficer.java`<br>`src/model/Disaster.java`<br>`src/gui/VictimFrame.java` | • Class & Objects<br>• Encapsulation (Getters/Setters)<br>• Inheritance (`extends`, `super`)<br>• Constructor Chaining |
| **Member 2** | **Fleet & Shelter Management** | `src/model/Vehicle.java`<br>`src/model/Ambulance.java`<br>`src/model/RescueVan.java`<br>`src/model/Shelter.java`<br>`src/exception/ShelterFullException.java`<br>`src/gui/ShelterFrame.java` | • Abstract Class & Methods<br>• Method Overriding & Dynamic Dispatch<br>• User-Defined Exception (`extends Exception`)<br>• `throw` / `throws` / `try-catch` |
| **Member 3** | **Operations & Offline Sync** | `src/service/RescueManager.java`<br>`src/service/DatabaseConnection.java`<br>`src/service/OfflineSyncService.java`<br>`src/service/StatusMonitor.java` | • Singleton Pattern<br>• Method Overloading<br>• Collections (`ArrayList`, `HashMap`, `PriorityQueue`)<br>• Multithreading (`extends Thread`)<br>• JDBC Connectivity & Transactions |
| **Member 4** | **Reporting, UI & Presentation** | `src/service/Reportable.java`<br>`src/service/Report.java`<br>`src/Main.java`<br>`src/gui/LoginFrame.java`<br>`src/gui/DashboardFrame.java`<br>`src/gui/ReportFrame.java`<br>`src/gui/UITheme.java` | • Interfaces (`implements`)<br>• `final` keyword (classes, constants, methods)<br>• File I/O (`PrintWriter`, `FileReader`)<br>• Java Swing GUI Event-Driven Programming |

---

## Detailed Member Breakdown

### 👤 Member 1: Core Models, Casualty Management & Inheritance

#### 1. Files Owned
- [`src/model/Person.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Person.java)
- [`src/model/Victim.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Victim.java)
- [`src/model/RescueOfficer.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/RescueOfficer.java)
- [`src/model/Disaster.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Disaster.java)
- [`src/gui/VictimFrame.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/VictimFrame.java)

#### 2. What These Files Do (Plain English)
- `Person.java` is the base parent class that holds common personal info: name, age, phone number, and location.
- `Victim.java` inherits from `Person` and adds disaster-specific attributes: medical condition (`Normal`, `Injured`, `Critical`), rescue pipeline status, and IDs of assigned teams and shelters.
- `RescueOfficer.java` also inherits from `Person` and holds rank, station, and team assignment.
- `Disaster.java` represents disaster events (type, severity, location).
- `VictimFrame.java` is the Swing screen where field staff register casualties, view them in a table, and run emergency medical triage.

#### 3. Core OOP Concepts Applied
1. **Encapsulation:** All attributes are marked `private`. Data is read and modified exclusively through public `getters` and `setters` with validation.
2. **Inheritance (`extends`):** Both `Victim` and `RescueOfficer` extend `Person`, preventing code duplication.
3. **Constructor Chaining (`super(...)`):** Child classes invoke `super(name, age, phone, location)` to initialize base fields inside the parent constructor before setting their own fields.
4. **`this` Keyword:** Resolves name collisions between method parameter names and class fields (`this.name = name;`).

#### 4. Typical Examiner Questions & Answers
- **Q: Why create `Person` if you already have `Victim` and `RescueOfficer`?**
  > *“To demonstrate inheritance and code reusability. Both a victim and a rescue officer share core attributes like name, age, phone, and address. Putting them in `Person` follows the DRY (Don't Repeat Yourself) principle.”*
- **Q: What does `super()` do in `Victim.java`?**
  > *“It calls the constructor of the parent class `Person` so the parent's private fields are properly initialized first.”*

---

### 👤 Member 2: Fleet Polymorphism & Shelter Exceptions

#### 1. Files Owned
- [`src/model/Vehicle.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Vehicle.java)
- [`src/model/Ambulance.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Ambulance.java)
- [`src/model/RescueVan.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/RescueVan.java)
- [`src/model/Shelter.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/Shelter.java)
- [`src/model/RescueTeam.java`](file:///d:/Coding%20and%20Stuff/OOP/src/model/RescueTeam.java)
- [`src/exception/ShelterFullException.java`](file:///d:/Coding%20and%20Stuff/OOP/src/exception/ShelterFullException.java)
- [`src/gui/ShelterFrame.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/ShelterFrame.java)

#### 2. What These Files Do (Plain English)
- `Vehicle.java` is an abstract template for all rescue vehicles.
- `Ambulance.java` and `RescueVan.java` provide concrete behaviors for specialized rescue vehicles.
- `Shelter.java` tracks total capacity, current occupancy, and available beds.
- `ShelterFullException.java` is a custom checked exception triggered whenever someone attempts to place a victim into a shelter that has zero free beds.
- `ShelterFrame.java` is the Swing screen where shelters are registered and capacity is monitored.

#### 3. Core OOP Concepts Applied
1. **Abstract Class & Abstract Method:** `abstract class Vehicle` declares `public abstract void rescueOperation();`. You cannot instantiate a bare `Vehicle`; you must instantiate a subclass.
2. **Polymorphism & Method Overriding:** `Ambulance` and `RescueVan` override `rescueOperation()` to execute their own specific procedures:
   - `Ambulance` deploys paramedics and life support.
   - `RescueVan` conducts mass passenger evacuations.
3. **Dynamic Method Dispatch:** `RescueTeam` holds a reference of type `Vehicle`. At runtime, calling `vehicle.rescueOperation()` automatically executes the correct subclass implementation.
4. **User-Defined Exception:** `ShelterFullException extends Exception` (checked exception). It is declared with `throws` on `allocateVictim()` and caught with `try-catch` in the GUI.

#### 4. Typical Examiner Questions & Answers
- **Q: Why is `Vehicle` abstract instead of a normal class or an interface?**
  > *“Because all vehicles share state (like `vehicleNumber`, `model`, `capacity`), but their rescue operation behavior differs. An abstract class lets us share fields and constructors while forcing subclasses to provide their own operation implementation.”*
- **Q: How does your custom exception work?**
  > *“In `Shelter.java`, the `allocateVictim()` method checks `if (occupied >= capacity)`. If true, it uses `throw new ShelterFullException(...)`. The calling method handles it with a `try-catch` block and warns the user in the GUI.”*

---

### 👤 Member 3: Central Manager, Collections, Threading & Offline Sync

#### 1. Files Owned
- [`src/service/RescueManager.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/RescueManager.java)
- [`src/service/DatabaseConnection.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/DatabaseConnection.java)
- [`src/service/OfflineSyncService.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/OfflineSyncService.java)
- [`src/service/StatusMonitor.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/StatusMonitor.java)

#### 2. What These Files Do (Plain English)
- `RescueManager.java` is the single central brain of the entire application. It stores the live lists of victims, teams, shelters, and disasters, and coordinates all assignments.
- `DatabaseConnection.java` manages the JDBC MySQL driver, connection lifecycle, and the simulation toggle for demoing network drops.
- `OfflineSyncService.java` writes emergency operations to `offline_sync_queue.txt` when the database is unreachable and atomically replays them into MySQL when the connection returns.
- `StatusMonitor.java` is a background worker thread that monitors pending casualties and periodically checks if MySQL is back online to trigger automatic synchronization.

#### 3. Core OOP Concepts Applied
1. **Singleton Design Pattern:** `RescueManager` has a `private static RescueManager instance` and a private constructor. `getInstance()` ensures only one instance manages state across all GUI windows.
2. **Compile-Time Polymorphism (Method Overloading):**
   - `searchVictim(int victimId)` searches by numeric ID.
   - `searchVictim(String name)` searches by victim name.
3. **Collections Framework:**
   - `ArrayList<T>` for dynamic lists of victims, teams, and shelters.
   - `HashMap<Integer, Victim>` for $O(1)$ fast lookups by ID.
   - `PriorityQueue<Victim>` with a custom `Comparator` for medical triage (critical victims handled first).
4. **Multithreading:** `StatusMonitor extends Thread` overrides `run()` with a loop and `Thread.sleep(4000)`, updating the UI asynchronously via `SwingUtilities.invokeLater`.
5. **JDBC Connectivity & Transactions:** Uses `PreparedStatement`, `DriverManager`, and `conn.setAutoCommit(false)` with `commit()` and `rollback()` for crash-safe database syncing.

#### 4. Typical Examiner Questions & Answers
- **Q: Why did you use both `ArrayList` and `HashMap` for victims?**
  > *“`ArrayList` preserves insertion order and makes table rendering easy, while `HashMap<Integer, Victim>` provides instant $O(1)$ lookup when searching or updating a victim by their unique ID.”*
- **Q: How does offline sync guarantee data won't duplicate when reconnecting?**
  > *“We use SQL `ON DUPLICATE KEY UPDATE` statements. Replaying the queued line updates the existing primary key instead of throwing a duplicate key error.”*

---

### 👤 Member 4: Reporting, File I/O, Final Keyword & GUI Presentation

#### 1. Files Owned
- [`src/service/Reportable.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/Reportable.java)
- [`src/service/Report.java`](file:///d:/Coding%20and%20Stuff/OOP/src/service/Report.java)
- [`src/Main.java`](file:///d:/Coding%20and%20Stuff/OOP/src/Main.java)
- [`src/gui/LoginFrame.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/LoginFrame.java)
- [`src/gui/DashboardFrame.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/DashboardFrame.java)
- [`src/gui/ReportFrame.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/ReportFrame.java)
- [`src/gui/UITheme.java`](file:///d:/Coding%20and%20Stuff/OOP/src/gui/UITheme.java)

#### 2. What These Files Do (Plain English)
- `Reportable.java` is an interface contract requiring any reporting class to provide `generateSummary()` and `saveToFile()`.
- `Report.java` compiles operational statistics (total victims, rescued count, bed availability) and exports them to disk as a text file (`reports/report.txt`).
- `Main.java` is the application entry point that initializes the look-and-feel and launches the login window on the Swing Event Dispatch Thread (EDT).
- `LoginFrame.java` authenticates rescue staff (`admin` / `admin123`).
- `DashboardFrame.java` displays metric summary cards, module launchers, and live sync controls.
- `ReportFrame.java` previews reports and provides one-click file export.
- `UITheme.java` centrally manages colors, high-contrast buttons, and table styling.

#### 3. Core OOP Concepts Applied
1. **Interface (`interface` / `implements`):** `Reportable` defines a pure behavioral contract without implementation details.
2. **`final` Keyword in 3 Scenarios:**
   - **Final Class:** `public final class Report` prevents subclassing.
   - **Final Variable / Constant:** `public static final String APP_VERSION = "1.0.0";` prevents reassignment.
   - **Final Method:** `public final String getReportId()` prevents overriding.
3. **File Handling (I/O Streams):** Uses `File`, `FileWriter`, `BufferedWriter`, and `PrintWriter` with automatic resource management (`try-with-resources`).
4. **GUI Event-Driven Programming:** Utilizes `JFrame`, `JPanel`, `JButton`, and `ActionListener` lambda expressions (`e -> ...`).

#### 4. Typical Examiner Questions & Answers
- **Q: What is the difference between an Interface and an Abstract Class in your project?**
  > *“`Vehicle` is an abstract class because all vehicles share attributes like capacity and model. `Reportable` is an interface because it only defines actions (`generateSummary`, `saveToFile`) with zero state, allowing any class to become reportable.”*
- **Q: What happens if `reports/` folder doesn't exist when saving a report?**
  > *“In `Report.java`, we call `parentDir.mkdirs()` before writing to ensure the folder structure exists, preventing an `IOException`.”*

---

## 5-Minute Presentation Script for Strict Examiners

When the examiner sits down, follow this structured team flow:

1. **Member 1 (Intro & Data Models - 60s):**
   > *"Good morning. Our project is RescueNet, an emergency disaster relief management system. I built the domain model layer. We used inheritance with a base `Person` class and extended it into `Victim` and `RescueOfficer` with constructor chaining using `super(...)` and encapsulated private fields."*

2. **Member 2 (Polymorphism & Fault Handling - 60s):**
   > *"I handled resource dispatch and exception safety. We used an abstract class `Vehicle` with dynamic method dispatch so ambulances and rescue vans execute their own specialized operations. For shelters, I built the user-defined `ShelterFullException` to prevent over-allocating relief camps."*

3. **Member 3 (Architecture, Threading & Offline Sync - 90s):**
   > *"I built the coordination and sync layer. `RescueManager` uses the Singleton pattern and method overloading. When connectivity drops during floods or disasters, our `OfflineSyncService` journals transactions to disk. A background daemon thread in `StatusMonitor` detects reconnection and atomically replays those transactions into MySQL using JDBC."*
   *(Demo: Click '⚡ Simulate DB Failure', register a victim, show the queue file, click '🔌 Reconnect DB', then click 'Sync Now').*

4. **Member 4 (Reporting, File I/O & UI - 60s):**
   > *"I developed the reporting engine and the user interface. We implemented the `Reportable` interface and demonstrated the `final` keyword across classes, methods, and constants. Analytics are exported directly to disk using `PrintWriter`, and the UI is built with Java Swing on the Event Dispatch Thread."*
