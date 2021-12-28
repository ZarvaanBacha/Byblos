package Admin.MovingServices;

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
import Services.movingServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class AdminViewMovingAssistance extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    public AdminViewMovingAssistance() {
        // Required empty public constructor
    }

    public static AdminViewMovingAssistance newInstance() {
        AdminViewMovingAssistance fragment = new AdminViewMovingAssistance();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_view_moving_assistance, container, false);

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
        mDatabase.child("Moving Assistance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create array storing all the car service types retired
                ArrayList<movingServices> services = new ArrayList<>();

                for(DataSnapshot postSnapshot: snapshot.getChildren())
                {
                    // Get value from node
                    movingServices val = postSnapshot.getValue(movingServices.class);
                    services.add(val); // Add to list
                }
                // Call method responsible for drawing objects
                createItem(services, layoutToDraw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createItem(ArrayList<movingServices> services, LinearLayout layoutToDraw)
    {
        // Iterate through list of movingService type
        for(int i = 0; i < services.size(); i++)
        {
            // Create linear layout to store generated elements
            LinearLayout linearContainer = new LinearLayout(thiscontext);
            linearContainer.setPadding(10, 10,10,10);
            linearContainer.setOrientation(LinearLayout.VERTICAL);

            // Add various elements from movingService type to layout using constructor method
            linearContainer.addView(createTextView("Number of Movers: " + services.get(i).getNumberOfMovers()));
            linearContainer.addView(createTextView("$"+ services.get(i).getPrice() + " per hour"));

            linearContainer.addView(createTextView("Unique Identification: "+ services.get(i).getIdentifier()));

            // Create button to delete object
            Button delete = new Button(thiscontext);
            delete.setText("DELETE");

            // Get identification of current movingService in list
            String identification = services.get(i).getIdentifier();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                // On delete click, call method responsible for deleting object
                public void onClick(View v) {
                    deleteMovingService(identification);
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

    public void deleteMovingService(String identifier)
    {
        // Remove movingService and it's form from database


        DatabaseReference form =  FirebaseDatabase.getInstance().getReference();
        form.child("Forms").orderByChild("serviceIdentifier").equalTo(identifier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String formUID = ds.getKey();
                    Log.d("FORM UID", formUID);
                    form.child("Forms/"+formUID).removeValue();
                    mDatabase.child("Moving Assistance/"+identifier).removeValue();
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