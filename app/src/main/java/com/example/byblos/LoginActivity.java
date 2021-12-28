package com.example.byblos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Admin.AdminDashboard;
import Customer.CustomerDashboard;
import Employee.EmployeeDashboard;
import Utilities.Utilities;
import Utilities.InputTextValidator;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Fire authentication object
    boolean[] isTextValid = new boolean[2];
//    private DatabaseReference mDatabase; // Database object
//    private String userType;
//    private FirebaseUser mUser;


    // The Idling Resource which will be null in production.
    @Nullable
    private CountingIdlingResource countingIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link CountingIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (countingIdlingResource == null) {
            String _ACTIVITY_NAME = "LOGIN_ACTIVITY";
            countingIdlingResource = new CountingIdlingResource(_ACTIVITY_NAME);
        }
        return countingIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countingIdlingResource = (CountingIdlingResource) getIdlingResource();

        //References to inputs
        EditText email = findViewById(R.id.emailInput_login);
        EditText password = findViewById(R.id.passwordInput_login);

        // input validation
        email.addTextChangedListener( new InputTextValidator( email ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Email cannot contain illegal characters like *()/> etc";
                isTextValid[0] = Utilities.validateTextView_Email( textView, text, message, LoginActivity.this );
            }
        } );
        password.addTextChangedListener( new InputTextValidator( password ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please Enter a Password over or equal 6 characters";
                isTextValid[1] = Utilities.validateTextView_Password( textView, text, message, LoginActivity.this );
            }
        } );

        //Reference to Login Button
        final Button LOGIN_BUTTON = findViewById(R.id.loginButton_login);

        mAuth = FirebaseAuth.getInstance();
        LOGIN_BUTTON.setOnClickListener(v -> signIn(email, password));
    }

    private void signIn(EditText email, EditText password)
    {
        // TODO @kurtis: TEST customer
//        email.setText("homersimp@gmail.com");
//        password.setText("password");

//        // TODO @kurtis: TEST employee
//        email.setText("gr@gmail.com");
//        password.setText("password");
//        email.setText("rh@gmail.com");
//        password.setText("password");
//        email.setText("ccdd@ic.com");
//        password.setText("666666");

//        // TODO @kurtis: TEST admin
//        email.setText("admin@admin.com");
//        password.setText("_admin");
//        // TODO @kurtis: END TEST

        // input validation
        boolean isAllTextValid = true;
        for ( Boolean b : isTextValid ) isAllTextValid = isAllTextValid && b;
        if ( !isAllTextValid ){
            Toast.makeText( LoginActivity.this, "Please validate your input texts.", Toast.LENGTH_SHORT ).show();
            return;
        }
        login(email.getText().toString(), password.getText().toString());

    }

    public void login(String emailStr, String passwordStr) {

        assert countingIdlingResource != null;

        countingIdlingResource.increment();  // TODO DEBUG
        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
            countingIdlingResource.decrement();  // TODO DEBUG
            if (task.isSuccessful()) {
                Log.d("UID", ""+mAuth.getUid());
                findAndLogin(mAuth.getUid());
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginAccordingly(String userType)
    {
        // Log in for user according to type

        if (userType.equals("_admin"))
        {
            startActivity(new Intent(LoginActivity.this, AdminDashboard.class));
            finish();
        }

        if (userType.equals("_employee"))
        {
            startActivity(new Intent(LoginActivity.this, EmployeeDashboard.class));
            finish();
        }

        if (userType.equals("_customer"))
        {
            startActivity(new Intent(LoginActivity.this, CustomerDashboard.class));
            finish();
        }

    }

    public void findAndLogin(String UID)
    {
        // Check if UID belongs to ADMIN node
        FirebaseDatabase.getInstance().getReference().child("Users").child("Admin/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    loginAccordingly("_admin"); // Login as admin
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Check if UID belongs to Customer node
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    loginAccordingly("_customer"); // Login as customer
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Check if UID belongs to Employee node
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees/"+UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    loginAccordingly("_employee"); // Login as Employee
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
