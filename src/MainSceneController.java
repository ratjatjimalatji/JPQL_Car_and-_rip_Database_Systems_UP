import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainSceneController {

        @FXML
    private Button btnCreate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tfRegistration;

    @FXML
    private TextField tfCarMake;
    @FXML
    private TextField tfCarModel;
    @FXML
    private TextField tfManuYear;
    @FXML
    private TextField tfTopSpeed;
@FXML
    private TextField tfSearch;
    @FXML
    private TextArea tArea;


    @FXML
    private Label lblAvgSpeed;
    @FXML
    private Label lblError;
    @FXML
    private Label lblCarStatus;

    private EntityManagerFactory emf;
    private EntityManager em;

    public void initialize() {
    emf = Persistence.createEntityManagerFactory("$objectdb/db/CarCrudPrac1.odb");
    em = emf.createEntityManager();
    
        // Clear error label on startup
        lblError.setVisible(false);
        displayAverageSpeed();
        displayAllVehicles();
    }

    private void displayAverageSpeed() {
        TypedQuery <Car> query = em.createQuery
        ("SELECT AVG(c.topSpeedKmH) FROM Car c", Car.class);
        lblAvgSpeed.setText("Average Speed: " + query.getSingleResult()+ "km/h");
        
    }

    @FXML
    void btnCreateCar(ActionEvent event) {
        createCar(event);
    }

    @FXML
    void createCar(ActionEvent event) {
        try {
            lblError.setVisible(false);
            
            if (tfRegistration.getText().trim().isEmpty() ||
                tfCarMake.getText().trim().isEmpty() ||
                tfCarModel.getText().trim().isEmpty() ||
                tfManuYear.getText().trim().isEmpty() ||
                tfTopSpeed.getText().trim().isEmpty()) {
                
                showError("All fields are required!");
                return;
            }

            String reg = tfRegistration.getText().trim();
            String make = tfCarMake.getText().trim();
            String model = tfCarModel.getText().trim();
            
            // Check if car exists
            if (carExists(reg)) {
                showError("Car with this registration number already exists!");
                return;
            }
            
            int year = Integer.parseInt(tfManuYear.getText().trim());
            int speed = Integer.parseInt(tfTopSpeed.getText().trim());
            
            if (speed <= 0) {
                showError("Top speed must be greater than 0!");
                return;
            }

            Car car = new Car(reg, make, model, year, speed);

            em.getTransaction().begin();
            em.persist(car);
            em.getTransaction().commit();

            System.out.println("Car Saved Successfully: " + car);
            clearForm();
            showSuccess(car.getCarMake() + " "+  car.getCarModel() + " was logged successfully!");
            displayAverageSpeed(); // Update & display average speed
            displayAllVehicles();
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for year and top speed!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showError("Error creating car: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayAllVehicles() {
        TypedQuery <Car> query = em.createQuery
        ("SELECT c FROM Car c", Car.class);
        tArea.clear(); 
        List<Car> cars = query.getResultList();
        for (Car car : cars) {
            tArea.appendText(car.toString() + "\n");
        }
    }

    // Helper method to check if car exists
    private boolean carExists(String registration) {
        TypedQuery<Car> query = em.createQuery(
            "SELECT c FROM Car c WHERE c.registrationNumber = :reg", Car.class);
        query.setParameter("reg", registration);
        return !query.getResultList().isEmpty();
    }

    // Helper method to show error messages
    private void showError(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red;");
        lblError.setVisible(true);
    }

    // Helper method to show success messages
    private void showSuccess(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: green;");
        lblError.setVisible(true);
    }

    // Close database connections when done
    public void shutdown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    @FXML
    void btnDeleteCar(ActionEvent event) {
        deleteCar(event);
    }

    private void deleteCar(ActionEvent event) {
        try {
            lblError.setVisible(false);
            
            String reg = tfSearch.getText().trim();
            if (reg.isEmpty()) {
                showError("Enter a registration number to delete!");
                return;
            }

            TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c WHERE c.registrationNumber = :reg", Car.class);
            query.setParameter("reg", reg);
            List<Car> cars = query.getResultList();

            if (cars.isEmpty()) {
                showError("No car found with registration: " + reg);
            } else {
                Car car = cars.get(0);
                
                em.getTransaction().begin();
                em.remove(car);
                em.getTransaction().commit();
                
                clearForm();
                displayAverageSpeed();   // Update & display average speed
                displayAllVehicles();
                showSuccess("Car deleted successfully!");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showError("Error deleting car: " + e.getMessage());
        }
    }
@FXML
    void btnUpdateCar(ActionEvent event) {
        updateCar(event);
    }

    private void updateCar(ActionEvent event) {

        displayAllVehicles();
    }

    private void clearForm() {
        tfRegistration.clear();
 tfCarMake.clear();
        tfCarModel.clear();
        tfManuYear.clear();
        tfTopSpeed.clear();
        tfSearch.clear();
    }

    @FXML
    void btnSearchForCar(ActionEvent event) {
        searchCar(event);
    }

    private void searchCar(ActionEvent event) {
        try {
            lblError.setVisible(false);
            
            String reg = tfSearch.getText().trim();
            if (reg.isEmpty()) {
                lblError.setText("Please enter a registration number to search!");
                lblError.setStyle("-fx-text-fill: red;");
                lblError.setVisible(true);
                return;
            }

            TypedQuery<Car> query = em.createQuery("SELECT c FROM Car c WHERE c.registrationNumber = :reg", Car.class);
            query.setParameter("reg", reg);
            List<Car> cars = query.getResultList();

            if (cars.isEmpty()) {
                lblError.setText("No car found with registration: " + reg);
                lblError.setStyle("-fx-text-fill: red;");
                lblError.setVisible(true);
                clearFormExceptRegistration();
            } else {
                lblError.setVisible(false);
                Car car = cars.get(0);
                
                // Populate form fields with found car data
                tfRegistration.setText(car.getRegistrationNumber());
                tfCarMake.setText(car.getCarMake());
                tfCarModel.setText(car.getCarModel());
                tfManuYear.setText(String.valueOf(car.getManufacturedYear()));
                tfTopSpeed.setText(String.valueOf(car.gettopSpeedKmH()));
                
                System.out.println("Car found: " + car);
                
                lblError.setText("Car found successfully!");
                lblError.setStyle("-fx-text-fill: green;");
                lblError.setVisible(true);
            }
        } catch (Exception e) {
            lblError.setText("Error searching for car: " + e.getMessage());
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setVisible(true);
            e.printStackTrace();
        }
    }

    private void clearFormExceptRegistration() {
        tfRegistration.clear();
        tfCarMake.clear();
        tfCarModel.clear();
        tfManuYear.clear();
        tfTopSpeed.clear();
    }
}