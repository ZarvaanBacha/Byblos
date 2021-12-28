package Services.Forms.Rental;

import androidx.annotation.NonNull;

public class CarInfo extends RentalForm{
    protected String CarType;

    public CarInfo()
    {
        super(null, null, null, null, null, null, null, null, null);
        this.CarType = null;
    }
    public CarInfo(String FirstName, String LastName, String DateOfBirth, String Address, String Email, Boolean isApproved,
                   String PickupTime, String ReturnTime, String LicenseType, String CarType)
    {
        super(FirstName, LastName, DateOfBirth, Address, Email, isApproved, PickupTime, ReturnTime, LicenseType);
        this.CarType = CarType;
    }

    // Getters

    public String getCarType() {
        return CarType;
    }

    // Setters
    public void setCarType(String input){ this.CarType = input;}

    @NonNull
    @Override

    public String toString()
    {
        return "CarForm {" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth='" + getDateOfBirth() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", isApproved='" + getIsApproved() + '\'' +
                ", pickUpTime='" + getPickUpTime() + '\'' +
                ", returnTime='" + getReturnTime() + '\'' +
                ", license='" + getLicenseType() + '\'' +
                ", carType='" + getCarType() + '\'' +
                ", serviceIdentifier='" + getServiceIdentifier() + '\'' +
                ", formIdentifier='" + getFormIdentifier() + '\'' +
                '}';
    }
}
