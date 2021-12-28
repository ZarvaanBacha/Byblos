package Services;
import androidx.annotation.NonNull;

// Car service class that extends from properties from vehicle

public class carService extends vehicle{
    String identifier;
    String numberPlate;
    String price;

    public carService()
    {
        this.identifier = null;
        this.numberPlate = null;
        this.price = null;
    }
    public carService(String price, String make, String model, String type, String numberPlate)
    {
        super(make, model, type);
        this.price = price;
        this.numberPlate = numberPlate;
    }

    public String getNumberPlate()
    {
        return numberPlate;
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
        return "Car Service {" +
                "Branch ID='" + getBranch()+ '\'' +
                "Unique ID='" + getIdentifier()+ '\'' +
                ", Price='" + getPrice() + '\'' +
                ", Make='" + getMake() + '\'' +
                ", Model='" + getModel() + '\'' +
                ", Type='" + getType() + '\'' +
                ", Number Plate'" + getNumberPlate() + '\'' +
                ", Form ID'" + getFormID() + '\'' +
                '}';
    }

}
