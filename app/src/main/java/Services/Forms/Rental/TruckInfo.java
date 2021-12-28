package Services.Forms.Rental;

import androidx.annotation.NonNull;

public class TruckInfo extends RentalForm{
    protected String usageArea;

    public TruckInfo()
    {
        super(null, null, null, null, null, null, null, null, null);
        usageArea = null;
    }

    public TruckInfo(String FirstName, String LastName, String DateOfBirth, String Address, String Email, Boolean isApproved,
                   String PickupTime, String ReturnTime, String LicenseType, String usageArea)
    {
        super(FirstName, LastName, DateOfBirth, Address, Email, isApproved, PickupTime, ReturnTime, LicenseType);
        this.usageArea = usageArea;
    }

    // Getters

    public String getUsageArea() {
        return usageArea;
    }

    // Setters
    public void setUsageArea(String input){ this.usageArea = input;}

    @NonNull
    @Override

    public String toString()
    {
        return "TruckForm {" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth='" + getDateOfBirth() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", isApproved='" + getIsApproved() + '\'' +
                ", pickUpTime='" + getPickUpTime() + '\'' +
                ", returnTime='" + getReturnTime() + '\'' +
                ", license='" + getLicenseType() + '\'' +
                ",usageArea='" + getUsageArea() + '\'' +
                ", serviceIdentifier='" + getServiceIdentifier() + '\'' +
                ", formIdentifier='" + getFormIdentifier() + '\'' +
                '}';
    }
}

