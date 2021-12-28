package Services;

import androidx.annotation.NonNull;

public class truckService extends vehicle{
    String identifier;
    String numberPlate;
    String price;
    String distanceRestriction;

    public truckService()
    {
        this.identifier = null;
        this.numberPlate = null;
        this.price = null;
        this.distanceRestriction = null;
    }

    public truckService(String price, String make, String model, String type, String numberPlate, String distanceRestriction)
    {
        super(make, model, type);
        this.price = price;
        this.numberPlate = numberPlate;
        this.distanceRestriction = distanceRestriction;
    }

    public String getNumberPlate()
    {
        return numberPlate;
    }

    public String getDistanceRestriction()
    {
        return distanceRestriction;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getPrice()
    {
        return price;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Truck Service {" +
                "Branch ID='" + getBranch()+ '\'' +
                "Unique ID='" + getIdentifier()+ '\'' +
                ", Price='" + getPrice() + '\'' +
                ", Make='" + getMake() + '\'' +
                ", Model='" + getModel() + '\'' +
                ", Type='" + getType() + '\'' +
                ", Number Plate'" + getNumberPlate() + '\'' +
                ", Distance Restriction'" + getDistanceRestriction() + '\''+
                ", Form ID'" + getFormID() + '\'' +
                '}';
    }


}
