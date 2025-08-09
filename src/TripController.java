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
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> cbCarSelection;
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
    @FXML
    private Label lblSelectedCarInfo;

    private EntityManagerFactory emf;
    private EntityManager em;
    private List<Car> availableCars;

    public void initialize() {
        emf = Persistence.createEntityManagerFactory("$objectdb/db/CarCrudPrac1.odb");
        em = emf.createEntityManager();

        lblTripStatus.setVisible(false);
        displayAllTrips();
        loadAvailableCars();
    }

    private void loadAvailableCars() {
        TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c ORDER BY c.carMake, c.carModel", Car.class);
        availableCars = query.getResultList();

        cbCarSelection.getItems().clear();
        for (Car car : availableCars) {
            cbCarSelection.getItems().add(car.getDisplayName());
        }
    }

    private Car findCarByDisplayName(String displayName) {
        return availableCars.stream()
                .filter(car -> car.getDisplayName().equals(displayName))
                .findFirst()
                .orElse(null);
    }

    @FXML
    void btnCreateTrip(ActionEvent event) {
        createTrip(event);
    }
    void createTrip(ActionEvent event) {
        try {
            lblTripStatus.setVisible(false);

            // Ensure all fields have been entered
            if (cbCarSelection.getValue() == null ||
                    tfPickUp.getText().trim().isEmpty() ||
                    tfDestination.getText().trim().isEmpty() ||
                    tfDistance.getText().trim().isEmpty() ||
                    dpTripDate.getValue() == null) {
                showError("All fields are required!");
                return;
            }

            
            Car selectedCar = findCarByDisplayName(cbCarSelection.getValue());
            if (selectedCar == null) {
                showError("Please select a car from the combo box!");
                return;
            }

            String pickUp = tfPickUp.getText().trim();
            String destination = tfDestination.getText().trim();
            Date date = java.sql.Date.valueOf(dpTripDate.getValue());

            double distance = Double.parseDouble(tfDistance.getText().trim());
            if (distance <= 0) {
                showError("The distance travelled must be greater than 0!");
                return;
            }
            Date today = new Date(System.currentTimeMillis());
            if (date.before(today)) {
                showError("The trip for a past date cannot be logged!");
                return;
            }
            // Create trip with appropriate details
            Trip trip = new Trip(selectedCar, pickUp, destination, distance, date);

            // Save the trip to the database
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();

            //Feedback to user
            System.out.println("Trip saved Successfully: " + trip);
            clearUserInputExceptSearch();
            showSuccess("Trip from " + trip.getPickUpLocation() + " to " + trip.getDestinationLocation()+ " with "+ trip.getCar().getDisplayName()
                    + " was logged successfully!");
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
   
    @FXML
    void btnDeleteCarWithTrip(ActionEvent event) {
        deleteCarWithTrip(event);
    }
    private void deleteCarWithTrip(ActionEvent event) {
        try {
            lblTripStatus.setVisible(false);

            String searchText = tfSearchCar.getText().trim();
            if (searchText.isEmpty()) {
                showError("Enter the details of the car you would like to delete!");
                return;
            }
            TypedQuery<Car> query = em.createQuery(
                    "SELECT c FROM Car c WHERE c.carMake LIKE :search or c.carModel LIKE :search or c.registrationNumber LIKE :search",
                    Car.class);
            query.setParameter("search", "%" + searchText + "%"); // when the query gets sent to JPA the place holder in ^ (search) will have the searchText that the user entered.

            List<Car> cars = query.getResultList();

            if (cars.isEmpty()) {
                showError("No car found matching: " + searchText);
            } else if (cars.size() > 1) {
                showError("Multiple cars found with provided details. Use the car's registration number for an exact match.");

            } else {
                Car car = cars.get(0);
                int tripCount = car.getTrips().size();

                em.getTransaction().begin();
                em.remove(car);
                em.getTransaction().commit();

                clearUserInputExceptSearch();
                loadAvailableCars();
                displayAllTrips();
                showSuccess("Car '" + car.getDisplayName() + " and " + tripCount
                        + " associated trip(s) deleted successfully!");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showError("Error deleting car and trips: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void btnSearchFortrip(ActionEvent event) {
        searchFortrip(event);
    }
    private void searchFortrip(ActionEvent event) {
        try {
            lblTripStatus.setVisible(false);

            String searchText = tfSearchCar.getText().trim();
            if (searchText.isEmpty()) {
                showError("Please enter the name of a car to complete the search!");
                return;
            }

            TypedQuery<Trip> query = em.createQuery(
                    "SELECT t FROM Trip t WHERE " +
                            "t.car.registrationNumber LIKE :search OR " +
                            "t.car.carMake LIKE :search OR " +
                            "t.car.carModel LIKE :search " +
                            "ORDER BY t.tripDate DESC",
                    Trip.class);
            query.setParameter("search", "%" + searchText + "%");
            List<Trip> trips = query.getResultList();

            if (trips.isEmpty()) {
                showError("No trip found with car name: " + searchText);
                taTrip.clear();
                clearUserInputExceptSearch();
            } else {
                taTrip.clear();

                for (Trip trip : trips) {
                    taTrip.appendText(trip.toString() + "\n");
                }

                showSuccess("Found " + trips.size() + " trip(s) for: " + searchText);
            }
        } catch (Exception e) {
            showError("Error searching for trips: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void displayAllTrips() {
        TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t ORDER BY t.car ASC, t.tripDate DESC ",
                Trip.class);
        taTrip.clear();
        List<Trip> trips = query.getResultList();

        if (trips.isEmpty()) {
            taTrip.appendText("No trips found.\n");
        } else {
            for (Trip trip : trips) {
                taTrip.appendText(trip.toString() + "\n");
            }
        }
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

    private void clearUserInputExceptSearch() {
        cbCarSelection.setValue(null);
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

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(scene2);
        window.show();
    }
}