import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Trip implements Serializable {

    public static long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    Long tripId;
    String pickUpLocation;
    String destinationLocation;
    Double distance;

    Date tripDate;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;//

    public Trip() {
    }

    Trip(Car car, String pickUpLocation, String destinationLocation, double distance, java.sql.Date tripDate) {
        this.car = car;
        this.pickUpLocation = pickUpLocation;
        this.destinationLocation = destinationLocation;
        this.distance = distance;
        this.tripDate = tripDate;

    }

    // Getters
    public Long getTripId() {
        return (long) tripId;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public Double getDistance() {
        return distance;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public Car getCar() {
        return car;
    }

    // Setters
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public void setPickUplocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        String carInfo = car != null
                ? car.getCarMake() + " " + car.getCarModel() + " (" + car.getRegistrationNumber() + ")"
                : "Unknown Car";
        return String.format(
                "%s:\n  %s: Trip from %S to %S (%.2f km's travelled)",
                carInfo, this.tripId, this.pickUpLocation, this.destinationLocation, this.distance);
    }
}
