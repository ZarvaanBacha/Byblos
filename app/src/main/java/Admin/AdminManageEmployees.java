package Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.byblos.R;
import Users.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminManageEmployees extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;


    public AdminManageEmployees() {
        // Required empty public constructor
    }

    public static AdminManageEmployees newInstance() {
        AdminManageEmployees fragment = new AdminManageEmployees();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Point to Database location
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_manage_employees, container, false);

        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.populate);

        generateItems(layoutToDraw); // Call method responsible for displaying users
        thiscontext = container.getContext(); // Set context of this view
        layoutParams = rootView.getLayoutParams(); //Get layout parameters and restriction for the parent XML file
        return rootView;
    }

    public void generateItems (LinearLayout layoutToDraw) {
        mDatabase.child("Employees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create array storing all the employees users
                ArrayList<User> employees = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // Get value from node
                    User val = postSnapshot.getValue(User.class);

                    if (val.getUserType().equals("_employee")) {
                        employees.add(val);
                        Log.d("ID IS", "" + val.getIdentifier());
                    }
                }
                // Call method responsible for drawing objects
                createItem(employees, layoutToDraw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createItem(ArrayList<User> employees, LinearLayout layoutToDraw)
    {
        // Iterate through list of carService type
        for(int i = 0; i < employees.size(); i++)
        {
            // Create linear layout to store generated elements
            LinearLayout linearContainer = new LinearLayout(thiscontext);
            linearContainer.setPadding(10, 10,10,10);
            linearContainer.setOrientation(LinearLayout.VERTICAL);

            // Add various elements from UserInfo to layout using constructor method

            linearContainer.addView(createTextView("Name: " + employees.get(i).getFirstName() + " " + employees.get(i).getLastName()));
            linearContainer.addView(createTextView("Email: " + employees.get(i).getEmail()));
            linearContainer.addView(createTextView("Unique Identification: " + employees.get(i).getIdentifier()));



            // Create button to delete object
            Button delete = new Button(thiscontext);
            delete.setText("DELETE");

            // Get identification of current users in list
            String identification = employees.get(i).getIdentifier();
            Log.d("ID", ""+identification);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                // On delete click, call method responsible for deleting object
                public void onClick(View v) {
                    deleteUser(identification);
                }
            });
            linearContainer.addView(delete);

            // Add container layout to the main layout
            layoutToDraw.addView(linearContainer);
        }

    }
    public TextView createTextView(String value)
    {

        // Create a text view with set construction
        TextView text = new TextView(thiscontext);
        text.setText(value);
        text.setTextSize(15);
        text.setWidth(layoutParams.MATCH_PARENT);
        return text;
    }

    public void deleteUser(String identifier)
    {

        // Remove carService from database
        mDatabase.child("Employees").child(identifier).removeValue();


        // Refresh layout
        layoutToDraw.removeAllViews();
        generateItems(layoutToDraw);
    }
}