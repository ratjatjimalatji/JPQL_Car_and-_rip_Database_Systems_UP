import java.util.List;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TripController {

    @FXML
    private Button btnSaveTrip;
    @FXML
    private Button btnDeleteCar;
    @FXML
    private Button btnSearchTrips;
    @FXML
    private TextField tfDistance;
    @FXML
    private TextField tfCarName;
    @FXML
    private TextField tfPickUp;
    @FXML
    private TextField tfDestination;
    @FXML
    private DatePicker dpTripDate;
    @FXML
    private TextField tfSearchCar;
    @FXML
    private TextArea taTrip;
    @FXML
    private Label lblTripStatus;

    private EntityManagerFactory emf;
    private EntityManager em;

    public void initialize() {
        emf = Persistence.createEntityManagerFactory("$objectdb/db/CarCrudPrac1.odb");
        em = emf.createEntityManager();
    
        lblTripStatus.setVisible(false);
        displayAllTrips();
    }

    @FXML
    void btnCreateTrip(ActionEvent event) {
        createTrip(event);
    }

    
    void createTrip(ActionEvent event) {
        try {
            lblTripStatus.setVisible(false);
            if (
                tfCarName.getText().trim().isEmpty() ||
                tfPickUp.getText().trim().isEmpty() ||
                    tfDestination.getText().trim().isEmpty() ||
                    tfDistance.getText().trim().isEmpty() ||
                    dpTripDate.getValue() == null) {
                showError("All fields are required!");
                return;
            }

            String tripCarName = tfCarName.getText().trim();
            String pickUp = tfPickUp.getText().trim();
            String destination = tfDestination.getText().trim();
           Date date = java.sql.Date.valueOf(dpTripDate.getValue());

            double distance = Double.parseDouble(tfDistance.getText().trim());
            if (distance <= 0) {
                showError("The distance travelled must be greater than 0!");
                return;
            }

             // Check if car exists using the reg number
            if (!checkIfCarNameUsed(tripCarName)) {
                showError("Select an existing car for your trip!");
                return;
            }
            Trip trip = new Trip(tripCarName, pickUp, destination, distance, date);
            

            //Save the trip to the database
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();

            System.out.println("Trip saved Successfully: " + trip);
            clearUserInputExceptSearch();
            showSuccess("Trip from " + trip.getPickUplocation() + " to " + trip.getDestinationLocation() + " was logged successfully!");
            
            displayAllTrips();

        } catch (NumberFormatException e) {
            showError("Please enter a valid number for distance!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showError("Error creating trip: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayAllTrips() {
        TypedQuery<Trip> query = em.createQuery("SELECT t from Trip t ", Trip.class);
        taTrip.clear();
        List<Trip> trips = query.getResultList();
        for (Trip trip : trips) {
            taTrip.appendText(trips.toString() + "\n");
        }

        // Car car = new Car ();
        // TypedQuery<Car> query = em.createQuery("SELECT c from Car c ", Car.class);
        // taTrip.clear();
        // List<Car> cars = query.getResultList();
        // for (Car car : cars) {
        //     taTrip.appendText(cars.toString() + "\n");
        // }
    }


     // Helper method to check if car has been created
    private boolean checkIfCarNameUsed(String x) {
        TypedQuery<Trip> query = em.createQuery(
                "SELECT t from Trip t WHERE t.carName =: tripCarName", Trip.class);
        query.setParameter("carN", x);
        return !query.getResultList().isEmpty();
    }

    // Helper method to show error messages
    private void showError(String message) {
        lblTripStatus.setText(message);
        lblTripStatus.setStyle("-fx-text-fill: red;");
        lblTripStatus.setVisible(true);
    }

    // Helper method to show success messages
    private void showSuccess(String message) {
        lblTripStatus.setText(message);
        lblTripStatus.setStyle("-fx-text-fill: green;");
        lblTripStatus.setVisible(true);
    }

    public void shutdown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

//     @FXML
//     void btnDeletetrip(ActionEvent event) {
//         deletetrip(event);
//     }

//     private void deletetrip(ActionEvent event) {
//         try {
//             lblTripStatus.setVisible(false);

//             String tripName = tfSearch.getText().trim();
//             if (tripName.isEmpty()) {
//                 showError("Enter the tripNameistration number of the vehicle to be deleted!");
//                 return;
//             }
//             TypedQuery<trip> query = em.createQuery("SELECT c FROM trip c WHERE c.tripNameistrationNumber = :tripName", trip.class);
//             query.setParameter("tripName", tripName);
//             List<trip> trips = query.getResultList();

//             if (trips.isEmpty()) {
//                 showError("No trip found with tripNameistration: " + tripName);
//             } else {
//                 trip trip = trips.get(0);

//                 em.getTransaction().begin();
//                 em.remove(trip);
//                 em.getTransaction().commit();

//                 clearUserInput();
//                 displayAverageSpeed();
//                 displayAllTrips();
//                 showSuccess("trip deleted successfully!");
//             }
//         } catch (Exception e) {
//             if (em.getTransaction().isActive()) {
//                 em.getTransaction().rollback();
//             }
//             showError("Error deleting trip: " + e.getMessage());
//         }
//     }

//     @FXML
//     void btnUpdatetripDetails(ActionEvent event) {
//         updatetripDetails(event);
//     }

//     private void updatetripDetails(ActionEvent event) {
//         try {
//             lblTripStatus.setVisible(false);

//             String tripName = tfCarName.getText().trim();
//             if (tripName.isEmpty()) {
//                 showError("Enter the tripNameistration number of the vehicle to be updated!");
//                 return;
//             }

//             TypedQuery<trip> query = em.createQuery("SELECT c FROM trip c WHERE c.tripNameistrationNumber = :tripName", trip.class);
//             query.setParameter("tripName", tripName);
//             List<trip> trips = query.getResultList();

//             if (trips.isEmpty()) {
//                 showError("No trip found with tripNameistration: " + tripName);
//             } else {
//                 trip trip = trips.get(0);

//                 // Update trip details
//                 em.getTransaction().begin();
//                 trip.settrippickUp(tfPickUp.getText().trim());
//                 trip.settripdestination(tfDistance.getText().trim());
//                 trip.setManufacturedYear(Integer.parseInt(dpTripDate.getText().trim()));
//                 trip.setTopSpeedKmH(Integer.parseInt(tfTopSpeed.getText().trim()));
//                 em.getTransaction().commit();

//                 clearUserInput();
//                 displayAverageSpeed();
//                 displayAllTrips();
//                 showSuccess("trip details updated successfully!");
//             }
//         } catch (NumberFormatException e) {
//             showError("Please enter valid numbers for year and top speed!");
//         } catch (Exception e) {
//             if (em.getTransaction().isActive()) {
//                 em.getTransaction().rollback();
//             }
//             showError("Error updating trip details: " + e.getMessage());
//         }
//     }

    private void clearUserInput() {
        tfCarName.clear();
        tfPickUp.clear();
        tfDestination.clear();
        tfDistance.clear();
        dpTripDate.setValue(null);
        tfSearchCar.clear();
    }

//     @FXML
//     void btnSearchFortrip(ActionEvent event) {
//         searchFortrip(event);
//     }

//     private void searchFortrip(ActionEvent event) {
//         try {
//             lblTripStatus.setVisible(false);

//             String tripName = tfSearch.getText().trim();
//             if (tripName.isEmpty()) {
//                 showError("Please enter a tripNameistration number to search!");
//                 return;
//             }

//             TypedQuery<trip> query = em.createQuery("SELECT c FROM trip c WHERE c.tripNameistrationNumber = :tripName", trip.class);
//             query.setParameter("tripName", tripName);
//             List<trip> trips = query.getResultList();

//             if (trips.isEmpty()) {
//                 showError("No trip found with tripNameistration: " + tripName);
//                 clearUserInputExcepttripNameistration();
//             } else {
//                 lblTripStatus.setVisible(false);
//                 trip trip = trips.get(0);

//                 // Populate form fields with found trip data
//                 tfCarName.setText(trip.gettripNameistrationNumber());
//                 tfPickUp.setText(trip.gettrippickUp());
//                 tfDistance.setText(trip.gettripdestination());
//                 dpTripDate.setText(String.valueOf(trip.getManufacturedYear()));
//                 tfTopSpeed.setText(String.valueOf(trip.gettopSpeedKmH()));
                
//                 showSuccess("trip found successfully!");
//             }
//         } catch (Exception e) {
//             showError("Error searching for trip: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

    private void clearUserInputExceptSearch() {
        tfCarName.clear();
        tfPickUp.clear();
        tfDistance.clear();
        tfDestination.clear();
        dpTripDate.setValue(null);
    }

    public void switchToTripScene(ActionEvent event) throws Exception {
    }

public void btnShowCreateVehiclePage(ActionEvent event) throws Exception {
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene2 = new Scene(scene2Parent);

        // Get the current stage
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene2);
        window.show();
    }
// }
}