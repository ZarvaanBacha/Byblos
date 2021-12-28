package Users;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Customer extends User{
    String preferredBranchUID;
    String serviceSelectedUID;
    String formUID;

    public Customer()
    {
        super(null, null, null, null, null);
        preferredBranchUID = null;
        serviceSelectedUID = null;
        formUID = "";
    }

    public Customer(String FirstName, String LastName, String Email, String UserType, String Password)
    {
        super(FirstName, LastName, Email, UserType, Password);
        preferredBranchUID = null;
        serviceSelectedUID = null;
        formUID = "";
    }

    // Setter
    public void setPreferredBranchUID(String input)
    {
        this.preferredBranchUID = input;
    }
    public void setServiceSelectedUID(String input){ this.serviceSelectedUID = input;}
    public void setFormUID(String input){ this.formUID = input;}
    public void resetFormUID(){this.formUID = "";}

    // Getter
    public String getPreferredBranchUID()
    {
        return preferredBranchUID;
    }
    public String getServiceSelectedUID(){return serviceSelectedUID;}
    public String getFormUID(){return formUID;}

    @NonNull
    @Override
    public String toString()
    {
        return super.toString()+
                "\n Preferred Branch='" + getPreferredBranchUID() + '\'' +
                "\n FormUID='" + getFormUID() + '\'' +
                "\n Current Selected Service='" + getServiceSelectedUID() + '\'';
    }
}
