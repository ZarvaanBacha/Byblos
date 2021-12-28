package Services.Forms.Rental;

import Services.Forms.Form;

public class RentalForm extends Form {
    protected String PickUpTime, ReturnTime, LicenseType;

    public RentalForm(String FirstName, String LastName, String DateOfBirth, String Address, String Email, Boolean isApproved,
                      String PickupTime, String ReturnTime, String LicenseType)
    {
        super (FirstName, LastName, DateOfBirth, Address, Email, isApproved);
        this.PickUpTime = PickupTime;
        this.ReturnTime = ReturnTime;
        this.LicenseType = LicenseType;

    }

    // Getters
    public String getPickUpTime() {return PickUpTime;}
    public String getReturnTime() {return ReturnTime;}
    public String getLicenseType() {return LicenseType;}

    // Setters

    public void setLicenseType(String licenseType) {
        LicenseType = licenseType;
    }

    public void setReturnTime(String returnTime) {
        ReturnTime = returnTime;
    }

    public void setPickUpTime(String pickUpTime) {
        PickUpTime = pickUpTime;
    }
}
