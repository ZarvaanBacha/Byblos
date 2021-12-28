package Services;

// Vehicle class that stores generic common elements
public class vehicle {
    public String make;
    public String model;
    public String type;
    public String branchIdentifier;
    public String formID;

    public vehicle()
    {
        this.make = null;
        this.model = null;
        this.type = null;
    }

    public vehicle(String make, String model, String type)
    {
        this.make = make;
        this.model = model;
        this.type = type;
        this.branchIdentifier = "NONE";
    }

    public String getMake()
    {
        return make;
    }

    public String getModel()
    {
        return model;
    }

    public String getType() {
        return type;
    }
    public String getFormID(){return formID;}

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

    public void setFormID( String input)
    {
        this.formID = input;
    }
}
