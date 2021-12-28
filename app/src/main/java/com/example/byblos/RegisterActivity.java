package com.example.byblos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import Utilities.InputTextValidator;
import Utilities.Utilities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Users.Employee;

public class RegisterActivity extends AppCompatActivity {

    private String userType = ""; // Hold string value of user type (_employee, _customer, _admin)
    private FirebaseAuth mAuth; // Fire authentication object
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); // Database object

    boolean[] isTextValid = new boolean[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance(); // initialize authentication object
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Point to database


        // references to radio button(s)
        final RadioButton EMPLOYEE_BUTTON = findViewById(R.id.employeeRadioButton);
        final RadioButton CUSTOMER_BUTTON = findViewById(R.id.customerRadioButton);

        // reference to register button
        // button_2 is for the actual sign-up page, button_1 is just for redirection
        final Button REGISTER_BUTTON = findViewById(R.id.registerButton_reg);

        // references to data entries
        EditText firstName = findViewById(R.id.firstNameInput);
        EditText lastName = findViewById(R.id.lastNameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText password = findViewById(R.id.passwordInput_reg);

        // input validation

        firstName.addTextChangedListener( new InputTextValidator( firstName ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Name cannot contain characters like _*()/> etc";
                isTextValid[0] = Utilities.validateTextView_General( textView, text, message, RegisterActivity.this );
            }
        } );
        lastName.addTextChangedListener( new InputTextValidator( lastName ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Name cannot contain characters like _*()/> etc";
                isTextValid[1] = Utilities.validateTextView_General( textView, text, message, RegisterActivity.this );
            }
        } );
        email.addTextChangedListener( new InputTextValidator( email ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Email cannot contain illegal characters like *()/> etc";
                isTextValid[2] = Utilities.validateTextView_Email( textView, text, message, RegisterActivity.this );
            }
        } );
        password.addTextChangedListener( new InputTextValidator( password ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please Enter a Password over or equal 6 characters";
                isTextValid[3] = Utilities.validateTextView_Password( textView, text, message, RegisterActivity.this );
            }
        } );

        // Event listener for employee button click
        EMPLOYEE_BUTTON.setOnClickListener(v -> { // On click function
            userType = "_employee";
            CUSTOMER_BUTTON.setChecked(false);
            EMPLOYEE_BUTTON.setChecked(true);
        });

        // Event listener for customer button click
        CUSTOMER_BUTTON.setOnClickListener(v -> { // On click function
            userType = "_customer";
            EMPLOYEE_BUTTON.setChecked(false);
            CUSTOMER_BUTTON.setChecked(true);
        });


        REGISTER_BUTTON.setOnClickListener(v -> {
            // input validation
            if ((!EMPLOYEE_BUTTON.isChecked()) && (!CUSTOMER_BUTTON.isChecked())) {
                Toast.makeText(RegisterActivity.this, "Please select if you are an Employee or Customer", Toast.LENGTH_LONG).show();
                return;
            }
            boolean isAllTextValid = true;
            for ( Boolean b : isTextValid ) isAllTextValid = isAllTextValid && b;
            if ( isAllTextValid )
                Toast.makeText( RegisterActivity.this, "Request Sent", Toast.LENGTH_SHORT ).show();  // TODO DEBUG
            else {
                Toast.makeText( RegisterActivity.this, "Please validate your input texts.", Toast.LENGTH_SHORT ).show();
                return;
            }

            signUp(firstName, lastName, email, password);

//            if (validateForm(firstName, lastName, email, password, EMPLOYEE_BUTTON, CUSTOMER_BUTTON))
//            {
//                // Check if username exists and signup
//                signUp(firstName, lastName, email, password);
//            }
        });
    }

    // method to validate fields
    public boolean validateForm(EditText firstName, EditText lastName, EditText email, EditText password, RadioButton employeeButton, RadioButton customerButton)
    {
        boolean status = true;
        if (firstName.getText().toString().matches(""))
        {
            firstName.setTextColor(Color.RED);
            firstName.setText("Please Enter your First Name");
            firstName.setTextColor(Color.BLACK);
            status = false;
        }

        if (lastName.getText().toString().matches(""))
        {
            lastName.setTextColor(Color.RED);
            lastName.setText("Please Enter your Last Name");
            lastName.setTextColor(Color.BLACK);
            status = false;
        }

        if (email.getText().toString().matches(""))
        {
            email.setTextColor(Color.RED);
            email.setText("Please enter an Email");
            email.setTextColor(Color.BLACK);
            status = false;
        }

        if (password.getText().toString().matches(""))
        {
            Toast.makeText(RegisterActivity.this, "Please Enter a Password over or equal 6 characters", Toast.LENGTH_LONG).show();
            status = false;
        }


        // Special Cases to check

        //Password Length
        if (password.getText().toString().length()<6)
        {
            Toast.makeText(RegisterActivity.this, "Please Enter a Password over or equal 6 characters", Toast.LENGTH_LONG).show();
            status = false;
        }


        // Email Validity
        if ( Utilities.emailChecker(email.getText().toString()) != true)
        {
            email.setTextColor(Color.RED);
            email.setText("Please enter a valid Email");
            email.setTextColor(Color.BLACK);
            status = false;
        }

        // Check if Buttons are selected

        if ((!employeeButton.isChecked()) && (!customerButton.isChecked()))
        {
            Toast.makeText(RegisterActivity.this, "Please select if you are an Employee or Customer", Toast.LENGTH_LONG).show();
            status = false;
        }
        return status;
    }

    public void signUp(EditText firstName, EditText lastName, EditText email, EditText password)
    {
        String FIRST_NAME = firstName.getText().toString();
        String LAST_NAME = lastName.getText().toString();
        String EMAIL = email.getText().toString();
        String PASSWORD = password.getText().toString();

        if (userType.equals("_employee"))
        {
            Employee newEmployee = new Employee(FIRST_NAME, LAST_NAME, EMAIL, userType, PASSWORD);

            mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null)
                    {
                        newEmployee.setIdentifier(currentUser.getUid());
                        mDatabase.child("Users").child("Employees").child(currentUser.getUid()).setValue(newEmployee);
                    }else
                        {
                            return;
                        }
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else
                    {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

            });

        }

        if (userType.equals("_customer"))
        {
            Employee newCustomer = new Employee(FIRST_NAME, LAST_NAME, EMAIL, userType, PASSWORD);

            mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null)
                    {
                        newCustomer.setIdentifier(currentUser.getUid());
                        mDatabase.child("Users").child("Customers").child(currentUser.getUid()).setValue(newCustomer);
                    }else
                    {
                        return;
                    }
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else
                {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            });

        }
    }

}