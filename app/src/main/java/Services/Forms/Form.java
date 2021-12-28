package Services.Forms;

public class Form {
    protected String FirstName, LastName, DateOfBirth, Address, Email, FormIdentifier, ServiceIdentifier;
    protected String isApproved;

    public Form()
    {
        this.FirstName = null;
        this.LastName = null;
        this.DateOfBirth = null;
        this.Address = null;
        this.Email = null;
        this.isApproved = "NO REQUEST";
        this.FormIdentifier = null;
        this.ServiceIdentifier = null;


    }

    public Form(String FirstName, String LastName, String DateOfBirth, String Address, String Email, Boolean isApproved)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.DateOfBirth = DateOfBirth;
        this.Address = Address;
        this.Email = Email;
        this.isApproved = "NO REQUEST";
    }

    // Getters

    public String getFirstName(){return FirstName;}
    public String getLastName(){return LastName;}
    public String getDateOfBirth(){return DateOfBirth;}
    public String getAddress(){return Address;}
    public String getEmail(){return Email;}
    public String getIsApproved(){return isApproved;}
    public String getFormIdentifier(){return FormIdentifier;}
    public String getServiceIdentifier(){return ServiceIdentifier;}

    // Setters

    public void setFirstName(String input){this.FirstName = input;}
    public void setLastName(String input){this.LastName = input;}
    public void setDateOfBirth(String input){this.DateOfBirth = input;}
    public void setAddress(String input){this.Address = input;}
    public void setEmail(String input){this.Email = input;}
    public void setIsApproved(String input){this.isApproved = input;}
    public void setFormIdentifier(String input){this.FormIdentifier = input;}
    public void setServiceIdentifier(String serviceIdentifier) {
        ServiceIdentifier = serviceIdentifier;
    }

    public void resetForm()
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.DateOfBirth = DateOfBirth;
        this.Address = Address;
        this.Email = Email;
        this.isApproved = "NO REQUEST";
    }
}
