
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    // This annotation specifies the primary key of the entity
    @Id
    // generate primary key value automatically
    @GeneratedValue
    private Long id;

    private String registrationNumber;
    private String carMake;
    private String carModel;
    private int manufacturedYear;
    private int topSpeedKmH;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<>();

    public Car() {
    }

    Car(String rn, String cma, String cmo, int my, int tsk, List<Trip> trips) {
        this.registrationNumber = rn;
        this.carMake = cma;
        this.carModel = cmo;
        this.manufacturedYear = my;
        this.topSpeedKmH = tsk;
        this.trips = null;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getManufacturedYear() {
        return manufacturedYear;
    }

    public int gettopSpeedKmH() {
        return topSpeedKmH;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setManufacturedYear(int manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public void setTopSpeedKmH(int topSpeedKmH) {
        this.topSpeedKmH = topSpeedKmH;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setCar(this);
    }

    public void removeTrip(Trip trip) {
        trips.remove(trip);
        trip.setCar(null);
    }

    public String getDisplayName() {
        return carMake + " " + carModel + " (" + registrationNumber + ")";
    }

    @Override
    public String toString() {
        return String.format(
                "%s | %s %s (%d model) Top speed: %d km/h",
                this.registrationNumber, this.carMake, this.carModel, this.manufacturedYear, this.topSpeedKmH);
    }
}
