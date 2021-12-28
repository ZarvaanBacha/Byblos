package Users;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Employee extends User {
    protected String branchUID;
    protected ArrayList<String> formUIDs = new ArrayList<String>();

    public Employee()
    {
        super(null, null, null, null, null);
        this.branchUID = null;
        formUIDs.clear();
    }

    public Employee(String FirstName, String LastName, String Email, String UserType, String Password)
    {
        super(FirstName, LastName, Email, UserType, Password);
        this.branchUID = null;
        formUIDs.clear();
    }

    // Getters
    public String getBranchUID()
    {
        return this.branchUID;
    }
    public ArrayList<String> getFormUIDs(){return this.formUIDs;}

    // Setters
    public void setBranchUID(String input)
    {
        this.branchUID = input;
    }
    public void addFormUID(String input){this.formUIDs.add(input);}

    public void removeFormUID(String input)
    {
        this.formUIDs.remove(input);
    }

    @NonNull
    @Override

    public String toString()
    {
        return super.toString()+ //Get User toString method
            "\n Managing Branch='" + getBranchUID() + '\'' +
                "\n Form UIDs='" + getFormUIDs() + '\'';
    }


}
