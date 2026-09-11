# RescueNet – Java OOP Course Project

You are going to build **RescueNet – Disaster Response Management System**, a Second Year engineering Java OOP course project.

I have provided an `implementation_plan.md` file alongside this prompt. **Read the implementation plan completely before writing any code. Treat it as the primary project specification.**

## 1. Main Goal

Build a **fully working but beginner-friendly Java application** based on the implementation plan.

The project is being developed by Second Year students who are learning Java/OOP for the first time.

The most important requirements are:

1. The application must actually work.
2. The code must be easy for a beginner to understand.
3. The code must be easy to explain in a viva.
4. The project must demonstrate the Java OOP concepts listed in the implementation plan.
5. Do NOT overengineer the project.

The goal is NOT to make a professional enterprise disaster-management platform.

The goal is:

> "A simple, clean, functional Java OOP project that looks like Second Year students built it and can explain every part of it."

---

# 2. Technology Restrictions

Use:

- Java
- Java Swing for GUI
- MySQL
- JDBC
- Standard Java libraries

Do NOT introduce:

- Spring
- Spring Boot
- Hibernate
- JavaFX
- React
- Node.js
- Python
- REST APIs
- Cloud services
- Complex frameworks
- ORM libraries
- Advanced third-party libraries
- Microservices
- Authentication frameworks

Use basic Java wherever possible.

---

# 3. Follow the Implementation Plan

The uploaded `implementation_plan.md` contains:

- Project architecture
- Modules
- Classes
- OOP concepts
- Database structure
- GUI structure
- File handling
- Exception handling
- Multithreading
- Development order
- Testing requirements

Follow it closely.

If something in the implementation plan can be implemented in multiple ways, always choose the **simplest beginner-friendly approach**.

Do not add unnecessary functionality just because it is technically possible.

---

# 4. Coding Style

Write code that a student who has recently learned Java can understand.

Prefer simple code such as:

```java
if
else
for
while
ArrayList
HashMap
Scanner
JOptionPane
try
catch
simple methods
constructors
```

Avoid unnecessarily complicated syntax.

For example, prefer:

```java
for (Victim v : victims) {
    v.displayDetails();
}
```

over complicated streams or lambda expressions.

Prefer straightforward methods over highly abstract architectures.

---

# 5. Keep Classes Simple

Use simple classes with:

- Data members
- Constructors
- Getters/setters where necessary
- Simple methods
- `displayDetails()`-type methods where useful

Do not create dozens of unnecessary helper classes.

The classes mentioned in the implementation plan should be implemented first.

---

# 6. OOP Concepts Must Be Genuine

The project must demonstrate the following concepts naturally:

### Classes and Objects

Use classes such as:

- Person
- Victim
- RescueOfficer
- Disaster
- RescueTeam
- Shelter
- Vehicle
- Ambulance
- RescueVan

### Constructors

Demonstrate:

- Default constructor
- Parameterized constructor

### this

Use `this` in constructors where appropriate.

### Static and Instance Members

Demonstrate:

- Static variables
- Instance variables
- Static methods
- Instance methods
- Static blocks
- Instance blocks

### Inheritance

Use:

```text
Person
├── Victim
└── RescueOfficer
```

and:

```text
Vehicle
├── Ambulance
└── RescueVan
```

### Polymorphism

Demonstrate:

- Method overloading
- Method overriding
- Dynamic binding
- Base-class reference

### Abstract Class

Use `Vehicle` as an abstract class if appropriate.

### Interface

Use the `Reportable` interface for report generation.

### final

Demonstrate:

- final variable
- final method
- final class

### Exception Handling

Demonstrate:

- try-catch
- finally where appropriate
- throw
- throws
- user-defined exception

Use:

```text
ShelterFullException
```

as the main custom exception.

### Collections

Use:

- ArrayList
- HashMap
- Iterator
- for-each

### File Handling

Generate a simple text report.

### JDBC

Use JDBC to connect to MySQL and perform basic:

- INSERT
- SELECT
- UPDATE
- DELETE if needed

### Multithreading

Implement a very simple `StatusMonitor extends Thread`.

Do NOT build complicated concurrent systems.

---

# 7. GUI Requirements

Use Java Swing.

The application should have a clean and simple interface.

Required screens:

1. Login
2. Dashboard
3. Disaster Management
4. Victim Management
5. Rescue Team Management
6. Shelter Management
7. Rescue Assignment
8. Shelter Allocation
9. Status Update
10. Reports

Use simple Swing components:

- JFrame
- JPanel
- JLabel
- JButton
- JTextField
- JTextArea
- JComboBox
- JOptionPane

Keep the GUI visually clean but do not spend excessive effort creating a professional UI.

---

# 8. Login

Implement a basic login.

Default credentials:

```text
Username: admin
Password: admin123
```

For the first working version, simple validation is acceptable.

If JDBC authentication is implemented later, keep the code understandable.

---

# 9. Dashboard

Create a main dashboard with buttons for the major modules.

Example:

```text
--------------------------------------
             RESCUENET
--------------------------------------

[ Manage Disasters ]

[ Manage Victims ]

[ Rescue Teams ]

[ Shelters ]

[ Assign Rescue ]

[ Shelter Allocation ]

[ Update Status ]

[ Reports ]

[ Exit ]

--------------------------------------
```

Every button should open the corresponding screen.

---

# 10. Disaster Management

Allow the user to:

- Add disaster
- View disasters

Fields:

```text
Disaster ID
Type
Location
Severity
```

Use an `ArrayList<Disaster>` initially.

Connect it to MySQL using JDBC when the database layer is ready.

---

# 11. Victim Management

Allow:

- Add victim
- View victims
- Search victim
- Update rescue status

Fields:

```text
Victim ID
Name
Age
Phone
Location
Medical Status
Rescue Status
```

Use:

```java
ArrayList<Victim>
```

for simple in-memory handling.

---

# 12. Rescue Team Management

Allow:

- Add team
- View teams
- Check availability

Fields:

```text
Team ID
Team Name
Members
Vehicle
Availability
```

Keep team/member management simple.

Do not create a complicated employee-management subsystem.

---

# 13. Shelter Management

Allow:

- Add shelter
- View shelters
- Check capacity

Fields:

```text
Shelter ID
Shelter Name
Location
Capacity
Occupied
```

Available space should simply be:

```java
capacity - occupied
```

---

# 14. Rescue Assignment

Allow the user to select:

```text
Victim
Team
```

Then:

1. Check whether the team is available.
2. Assign the team.
3. Change victim status to `Assigned`.
4. Change team status to unavailable/busy.

If the team is unavailable, display a simple message.

---

# 15. Shelter Allocation

Allow the user to select:

```text
Victim
Shelter
```

Before allocation:

```java
if (occupied < capacity)
```

allocate the victim.

Otherwise throw:

```text
ShelterFullException
```

and display a friendly message.

---

# 16. Rescue Status

Use simple String values:

```text
Pending
Assigned
Rescued
Shelter Reached
Completed
```

Do not create a complicated status-management system.

---

# 17. Reports

Create a simple report showing:

```text
Total Disasters
Total Victims
Total Rescue Teams
Total Shelters
Rescued Victims
Pending Victims
Available Shelter Spaces
Available Rescue Teams
```

Allow the user to save the report to:

```text
reports/report.txt
```

Use basic Java file handling.

---

# 18. Database

Create a MySQL database named:

```text
rescuenet
```

Tables:

```text
users
disasters
victims
teams
shelters
```

Follow the table structure in the implementation plan.

Create a simple:

```text
DatabaseConnection.java
```

class containing the JDBC connection method.

Do not repeat JDBC connection code throughout the project.

---

# 19. Database Setup

Create a SQL file:

```text
database.sql
```

It should:

1. Create the database.
2. Create all required tables.
3. Insert the default admin account.
4. Optionally insert a few sample records for testing.

The SQL should be beginner-friendly and easy to run in MySQL.

---

# 20. JDBC

Use basic JDBC:

```java
Connection
PreparedStatement
ResultSet
```

Use simple SQL.

For example:

```sql
INSERT INTO victims ...
```

and:

```sql
SELECT * FROM victims
```

Do not create a complicated repository/DAO architecture unless absolutely necessary.

A simple class structure is preferred.

---

# 21. Exception Handling

Use meaningful exception handling.

Examples:

- Invalid user input
- Invalid victim ID
- Database connection failure
- Shelter full
- Invalid numeric input

Do not use:

```java
catch (Exception e)
```

everywhere without reason.

Where a specific exception is appropriate, use it.

However, keep exception handling understandable for beginners.

---

# 22. Multithreading

Implement a simple:

```java
StatusMonitor extends Thread
```

The thread can periodically display:

```text
Checking rescue status...
```

or update a simple status label.

Use:

```java
start()
run()
sleep()
```

Keep it extremely simple.

The purpose is to demonstrate the syllabus topic, not to build a complex concurrent system.

---

# 23. File Handling

Generate a plain text report.

Example:

```text
RESCUENET REPORT
================

Total Disasters: 3
Total Victims: 10
Rescued Victims: 7
Pending Victims: 3
Total Shelters: 4
Available Shelter Spaces: 120
Available Rescue Teams: 2
```

Save it to:

```text
reports/report.txt
```

Create the directory automatically if it does not exist.

---

# 24. Input Validation

Add basic validation.

Examples:

- ID must be numeric.
- Age must be numeric.
- Capacity must be numeric.
- Empty names should not be accepted.
- Empty locations should not be accepted.

Use simple validation.

Do not use complicated regular expressions unless necessary.

---

# 25. Important Beginner Requirement

The final code should be something that the students can realistically learn and explain.

For every major method:

- Keep it short.
- Use meaningful variable names.
- Avoid deeply nested logic.
- Avoid unnecessary abstraction.
- Add comments where they help understanding.

Do NOT generate huge methods.

---

# 26. Comments

Use comments to explain important OOP concepts.

For example:

```java
// Demonstrating method overriding
@Override
public void rescueOperation()
{
    System.out.println("Ambulance is transporting injured victims.");
}
```

But do not comment every obvious line.

---

# 27. Error Messages

Use simple user-friendly messages.

Examples:

```text
"Please enter all fields."
"Invalid Victim ID."
"Rescue team is currently busy."
"Shelter is full."
"Victim rescued successfully."
"Database connection failed."
```

Avoid exposing raw stack traces to normal users.

---

# 28. Development Strategy

Do NOT try to build the entire project in one step.

Build it incrementally.

Follow this order:

### Step 1

Create the Java project.

### Step 2

Create core classes.

### Step 3

Test constructors and objects.

### Step 4

Implement inheritance.

### Step 5

Implement polymorphism.

### Step 6

Implement abstract class and interface.

### Step 7

Implement collections.

### Step 8

Implement exception handling.

### Step 9

Implement file handling.

### Step 10

Create MySQL database.

### Step 11

Implement JDBC.

### Step 12

Build Swing GUI.

### Step 13

Connect GUI to Java classes.

### Step 14

Connect required modules to JDBC.

### Step 15

Add multithreading.

### Step 16

Test the complete application.

---

# 29. Testing

After implementing each module, test it before moving on.

Test:

## Login

- Correct credentials
- Wrong username
- Wrong password
- Empty fields

## Victims

- Add valid victim
- Search victim
- Invalid ID
- Update status

## Shelters

- Add shelter
- Allocate victim
- Fill shelter
- Test ShelterFullException

## Rescue Teams

- Add team
- Assign team
- Try assigning busy team

## Database

- Insert
- Select
- Update

## Reports

- Generate report
- Save report
- Open report

---

# 30. Do Not Fake Functionality

Every button that exists in the GUI should either:

1. Work properly, or
2. Not be included yet.

Do NOT create buttons that simply show:

```text
"Coming Soon"
```

unless there is a genuine reason.

The final application should be demonstrable.

---

# 31. Data Consistency

When an action changes data, update the relevant object/database.

For example:

When a victim is rescued:

```text
Victim status = Rescued
```

When a victim is allocated to a shelter:

```text
Shelter occupied += 1
```

When a team is assigned:

```text
Team available = false
```

Keep these relationships simple and understandable.

---

# 32. Code Quality

Before considering the project complete:

- Remove unused imports.
- Remove duplicate code where practical.
- Fix compilation errors.
- Fix runtime errors.
- Check all button actions.
- Check database connections.
- Check file paths.
- Check exception handling.
- Make sure all classes compile.
- Make sure the project can be run from a clean setup.

---

# 33. Documentation

Create:

```text
README.md
```

containing:

- Project title
- Project objective
- Features
- Technologies used
- How to install/run
- MySQL setup
- Default login credentials
- Project structure
- OOP concepts demonstrated

Also include:

```text
database.sql
```

for database setup.

---

# 34. Final Project Structure

Aim for approximately:

```text
RescueNet/
│
├── src/
│   ├── Main.java
│   ├── Login.java
│   ├── Dashboard.java
│   ├── Person.java
│   ├── Victim.java
│   ├── RescueOfficer.java
│   ├── Disaster.java
│   ├── RescueTeam.java
│   ├── Shelter.java
│   ├── Vehicle.java
│   ├── Ambulance.java
│   ├── RescueVan.java
│   ├── RescueManager.java
│   ├── ShelterManager.java
│   ├── Report.java
│   ├── Reportable.java
│   ├── ShelterFullException.java
│   ├── DatabaseConnection.java
│   └── StatusMonitor.java
│
├── reports/
│   └── report.txt
│
├── database.sql
├── README.md
└── implementation_plan.md
```

You may adjust the structure if there is a clear reason, but keep it simple.

---

# 35. Important: No Overengineering

Whenever you have a choice between:

### Option A

A complicated architecture with many classes, interfaces, abstractions, patterns, and frameworks.

### Option B

A straightforward Java implementation that a Second Year student can understand.

**Always choose Option B.**

The project should look like:

> A well-made student Java OOP project.

It should NOT look like:

> A production enterprise software system.

---

# 36. Before Finalizing

After implementation, verify that the following concepts can be demonstrated from the actual code:

- Classes
- Objects
- Constructors
- Default constructor
- Parameterized constructor
- `this`
- Access modifiers
- Static variables
- Instance variables
- Static methods
- Instance methods
- Static block
- Instance block
- Inheritance
- Constructor in inheritance
- `final`
- Superclass access
- Method overloading
- Method overriding
- Dynamic binding
- Method hiding where appropriate
- Abstract class
- Interface
- Nested/inner class only if genuinely useful
- Exception handling
- Checked/unchecked exceptions
- `throw`
- `throws`
- User-defined exception
- Array
- String
- ArrayList
- HashMap
- Iterator
- for-each
- File handling
- JDBC
- Swing
- Event handling
- Multithreading

If a syllabus concept is not naturally required by the application, implement a **small, isolated demonstration** rather than complicating the main application.

---

# 37. Final Instruction

Start by reading `implementation_plan.md`.

Then inspect the existing project directory before making changes.

If the project is empty, create the project structure.

Implement the application **incrementally and test each stage**.

Do not dump an enormous amount of code without verifying it.

At every stage, prioritize:

```text
Simplicity
↓
Correctness
↓
Understandability
↓
OOP demonstration
↓
Visual polish
```

The final result should be a **working, beginner-friendly Java Swing + JDBC OOP project** that Second Year students can confidently demonstrate and explain during their practical examination and viva.

Do not add features that are not required by the implementation plan unless they are necessary for the application to function.