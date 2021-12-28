package Services;

public class movingServices {
    String identifier;
    String price;
    String numberOfMovers;
    String branchIdentifier;
    public String formID;

    public movingServices()
    {
        this.identifier = null;
        this.price = null;
        this.numberOfMovers = null;
    }

    public movingServices(String price, String numberOfMovers)
    {
        this.branchIdentifier = "NONE";
        this.price = price;
        this.numberOfMovers = numberOfMovers;
    }

    public String getNumberOfMovers(){return numberOfMovers;}

    public String getIdentifier(){return identifier;}

    public String getPrice(){return price;}

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public void setFormID(String input){this.formID = input;}

    public void setBranch(String BranchUID)
    {
        branchIdentifier = BranchUID;
    }

    public void removeBranch()
    {
        branchIdentifier = "NONE";
    }

    public String getBranch()
    {
        return branchIdentifier;
    }
    public String getFormID(){ return formID;}

    public String toString()
    {
        return "Moving Service {" +
                "Branch ID='" + getBranch()+ '\'' +
                "Unique ID='" + getIdentifier()+ '\'' +
                ", Price='" + getPrice() + '\'' +
                ", Number of Movers Make='" + getNumberOfMovers() + '\'' +
                '}';
    }
}
