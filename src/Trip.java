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
    @JoinColumn(name = "registrationNumber")
    String carName;

    public Trip() {
    }

    Trip(String cn, String pul, String dl, double dis, java.sql.Date date) {
        this.carName = cn;
        this.pickUpLocation = pul;
        this.destinationLocation = dl;
        this.distance = dis;
        this.tripDate = date;
        
    }

    //Getters
    public Long getTripId() {
        return (long) tripId;
    }

    public String getPickUplocation() {
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

    public String getCarName() {
        return carName;
    }

    //Setters
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

    public void setCarName(String carName) {
        this.carName = carName;
    }
    //Prints trip details
@Override
    public String toString() {
        return String.format(
                "%s| %s: Trip from %S to %S (%.2f km's travelled)",
                this.tripId, this.tripDate, this.pickUpLocation, this.destinationLocation, this.distance);
    }
}
