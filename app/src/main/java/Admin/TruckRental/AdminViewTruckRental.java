package Admin.TruckRental;

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
import Services.truckService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminViewTruckRental extends Fragment {

    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    public AdminViewTruckRental() {
        // Required empty public constructor
    }

    public static AdminViewTruckRental newInstance() {
        AdminViewTruckRental fragment = new AdminViewTruckRental();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Point to database location
        mDatabase = FirebaseDatabase.getInstance().getReference("Services");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View rootView = inflater.inflate(R.layout.fragment_admin_view_truck_rental, container, false);

        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.populate);


        generateItems(layoutToDraw); // Call method responsible for displaying cars in fleet
        // Inflate the layout for this fragment
        thiscontext = container.getContext(); // Set context of this view
        layoutParams = rootView.getLayoutParams(); //Get layout parameters and restriction for the parent XML file

        return rootView;
    }

    public void generateItems (LinearLayout layoutToDraw)
    {
        // Get UID of Car services and store in array
        mDatabase.child("Trucks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create array storing all the car service types retired
                ArrayList<truckService> trucks = new ArrayList<>();

                for(DataSnapshot postSnapshot: snapshot.getChildren())
                {
                    // Get value from node
                    truckService val = postSnapshot.getValue(truckService.class);
                    trucks.add(val); // Add to list
                }
                // Call method responsible for drawing objects
                createItem(trucks, layoutToDraw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createItem(ArrayList<truckService> trucks, LinearLayout layoutToDraw)
    {
        // Iterate through list of truckService type
        for(int i = 0; i < trucks.size(); i++)
        {
            // Create linear layout to store generated elements
            LinearLayout linearContainer = new LinearLayout(thiscontext);
            linearContainer.setPadding(10, 10,10,10);
            linearContainer.setOrientation(LinearLayout.VERTICAL);

            // Add various elements from truckService type to layout using constructor method
            linearContainer.addView(createTextView(trucks.get(i).getMake()));
            linearContainer.addView(createTextView(trucks.get(i).getModel()));
            linearContainer.addView(createTextView("Type: " + trucks.get(i).getType()));
            linearContainer.addView(createTextView("Number Plate: " + trucks.get(i).getNumberPlate()));
            linearContainer.addView(createTextView("$"+ trucks.get(i).getPrice() + " per hour" ));
            linearContainer.addView(createTextView("Max Distance: "+ trucks.get(i).getDistanceRestriction() + " KM"));



            linearContainer.addView(createTextView("Unique Identification: "+ trucks.get(i).getIdentifier()));

            // Create button to delete object
            Button delete = new Button(thiscontext);
            delete.setText("DELETE");

            // Get identification of current truckService in list
            String identification = trucks.get(i).getIdentifier();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                // On delete click, call method responsible for deleting object
                public void onClick(View v) {
                    deleteTruckService(identification);
                }
            });
            linearContainer.addView(delete);

            // Add container layout to the main layout
            layoutToDraw.addView(linearContainer);
        }

    }
    // Method to create TextViews with set parameters
    public TextView createTextView(String value)
    {

        // Create a text view with set construction
        TextView text = new TextView(thiscontext);
        text.setText(value);
        text.setTextSize(15);
        text.setWidth(layoutParams.MATCH_PARENT);
        return text;
    }

    public void deleteTruckService(String identifier)
    {

        // Remove truckService and it's form from database
        DatabaseReference form =  FirebaseDatabase.getInstance().getReference();
        form.child("Forms").orderByChild("serviceIdentifier").equalTo(identifier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String formUID = ds.getKey();
                    Log.d("FORM UID", formUID);
                    form.child("Forms/"+formUID).removeValue();
                    mDatabase.child("Trucks/"+identifier).removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Refresh layout
        layoutToDraw.removeAllViews();
        generateItems(layoutToDraw);
    }
}