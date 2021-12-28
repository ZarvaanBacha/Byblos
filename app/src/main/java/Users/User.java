package Users;

import androidx.annotation.NonNull;

public class User {
    protected String firstName, lastName, email, userType, password, identifier;

    public User()
    {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.userType = null;
        this.password = null;
        this.identifier = null;

    }

    public User(String FirstName, String LastName, String Email, String UserType, String Password)
    {
        this.firstName = FirstName;
        this.lastName = LastName;
        this.email = Email;
        this.userType = UserType;
        this.password = Password;

    }

    // Getters
    public String getFirstName() {return firstName;}
    public String getLastName(){return lastName;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getUserType(){return userType;}
    public String getIdentifier(){return identifier;}

    //Setters
    public void setIdentifier(String InputIdentifier)
    {
        this.identifier = InputIdentifier;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "User {" +
                "identifier='" + getIdentifier() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", userType='" + getUserType() + '\'' +
                '}';
    }



}
