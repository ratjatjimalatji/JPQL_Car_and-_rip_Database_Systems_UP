# Car Management System (ObjectDB CRUD Application) COS326 Database Systems 

This Java application allows users to manage car details using a GUI and ObjectDB as the database. It demonstrates proficiency in OOBMS through the CRUD operations.

## Features
## Vehicle management ## 

### Add a vehicle: ### 
Add a new car by inputting its details like make, model, top speed, year of manufacturing and registration number.
<img width="607" height="428" alt="Add trip Delete" src="https://github.com/user-attachments/assets/08d33dd6-90e1-4418-b196-6db8e9a97971" />


### Search vehicle: ###
Find a vehicle's details by searching for its unique registration number.
<img width="605" height="448" alt="Add vehicle search" src="https://github.com/user-attachments/assets/83afceb6-9965-4b47-a31f-a94aa8af9965" />


### Update vehicle: ### 
Modify the details of an existing car> Enter the car's registration number and the car's details are populated in the respective text fields, ready for easy updating.


### Delete vehicle: ### 
Permanently remove a car's record from the system using its registration number.

### Calculate average top speed: ### 
Get the average top speed of all vehicles currently in the system.

### Exception handling: ### 
Receive descriptive GUI messages for any errors, success or failed operations.

## Trip Management ## 

### Delete vehicle with trips: 
Remove a vehicle and all of it's corresponding trip logs simultaneously.
<img width="606" height="450" alt="Add Vehicle 2" src="https://github.com/user-attachments/assets/ea8f9f87-2085-4046-a8e1-dc58ccee6798" />

### Log a new trip: ###
Select a vehicle and enter the details for a new journey, like distance and duration.
<img width="607" height="432" alt="Add Trip search" src="https://github.com/user-attachments/assets/ff687cd5-155a-4906-b3c6-2ac51bdba6c4" />
### Search for trips: ###
Search for a car's trip history by entering its name or registration number.
<img width="607" height="437" alt="Add trip options" src="https://github.com/user-attachments/assets/5334c3e7-54dc-4cb4-8912-20797a16ad0e" />

## Requirements

* Java 8+
* ObjectDB
* NetBeans / IntelliJ / VS Code (any Java IDE)
* Java Swing or JavaFX

## Project Structure

1. **Entity Class (Car.java)**: Defines Car object with attributes, getters, and setters.
2. **Main Application Class**: Handles GUI design and event listeners for CRUD operations.
3. **Database Operations**: Methods for Create, Read, Update, Delete, and Average calculations using ObjectDB API.
4. **GUI Feedback**: Confirmation and error messages shown through dialogs.

   




## Usage

* Feel free to clone this repository, make use of your preferred Java IDE.
* Ensure that the JAR files that are contained in the ObjectDB's bin folder are correctly configured in project's library folder.
