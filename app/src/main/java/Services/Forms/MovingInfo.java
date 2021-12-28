package Services.Forms;

import androidx.annotation.NonNull;

public class MovingInfo extends Form{
    protected String startLocation, endLocation;
    protected int numberOfMovers, numberOfBoxes;

    public MovingInfo()
    {
        super(null, null, null, null, null, null);
        this.startLocation = null;
        this.endLocation = null;
        this.numberOfBoxes = 0;
        this.numberOfMovers = 0;

    }

    public MovingInfo(String FirstName, String LastName, String DateOfBirth, String Address, String Email, Boolean isApproved,
                      String startLocation, String endLocation, int numberOfMovers, int numberOfBoxes)
    {
        super (FirstName, LastName, DateOfBirth, Address, Email, isApproved);
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.numberOfMovers = numberOfMovers;
        this.numberOfBoxes = numberOfBoxes;
    }

    // Getters
    public String getStartLocation(){return startLocation;}
    public String getEndLocation(){return endLocation;}
    public int getNumberOfMovers(){return numberOfMovers;}
    public int getNumberOfBoxes(){return numberOfBoxes;}

    // Setters
    public void setStartLocation(String input){this.startLocation = input;}
    public void setEndLocation(String input){this.endLocation = input;}
    public void setNumberOfMovers(int input){this.numberOfMovers = input;}
    public void setNumberOfBoxes(int input){this.numberOfBoxes = input;}

    @NonNull
    @Override
    public String toString()
    {
        return "MovingInfo {" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth='" + getDateOfBirth() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", isApproved='" + getIsApproved() + '\'' +
                ", startLocation='" + getStartLocation() + '\'' +
                ", endLocation='" + getEndLocation() + '\'' +
                ", numberOfMovers='" + getNumberOfMovers() + '\'' +
                ", numberOfBoxes='" + getNumberOfBoxes() + '\'' +
                ", serviceIdentifier='" + getServiceIdentifier() + '\'' +
                ", formIdentifier='" + getFormIdentifier() + '\'' +
                '}';
    }
}



