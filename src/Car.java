
import java.io.Serializable;
import javax.persistence.*;
/* Car Class
 Author: ObjectDB Manual
 */

@Entity
public class Car implements Serializable {

    //The serialVersionUID is a universal version identifier for a Serializable class
    private static final long serialVersionUID = 1L;
    //This annotation specifies the primary key of the entity
    @Id
    //generate primary key value automatically
    @GeneratedValue
    private long id;

    private String registrationNumber;
    private String carMake;
    private String carModel;
    private int    manufacturedYear;
    private int topSpeedKmH;

    public Car() {}

    Car(String rn,String cma , String cmo, int my, int tsk) {
        this.registrationNumber = rn;
        this.carMake = cma;
        this.carModel = cmo;
        this.manufacturedYear = my;
        this.topSpeedKmH = tsk;
    }

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

    
    @Override
    public String toString() {
        return String.format(
                "%s | %s %s (%d model) Top speed: %d km/h",
                this.registrationNumber ,this.carMake ,this.carModel, this.manufacturedYear, this.topSpeedKmH);
    }
}
