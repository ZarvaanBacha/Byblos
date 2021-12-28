package Admin.MovingServices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.byblos.R;

import Services.Forms.MovingInfo;
import Services.movingServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddMovingAssistance extends Fragment {

    // Database Reference
    private DatabaseReference mDatabase;

    //References
    EditText numberOfMovers;
    EditText hourlySetRate;
    Button confirmAndAdd;

    public AdminAddMovingAssistance() {
        // Required empty public constructor
    }


    public static AdminAddMovingAssistance newInstance(String param1, String param2) {
        AdminAddMovingAssistance fragment = new AdminAddMovingAssistance();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Point to firebase path
        mDatabase = FirebaseDatabase.getInstance().getReference("Services/Moving Assistance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_add_moving_assistance, container, false);

        //Get views

        numberOfMovers = (EditText) rootView.findViewById(R.id.numberOfMovers);
        hourlySetRate = (EditText) rootView.findViewById(R.id.hourlySetRate);


        confirmAndAdd = (Button) rootView.findViewById(R.id.confirmAndAdd);

        confirmAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On click of confirm and add, add data to database
                addData();
            }
        });
        return rootView;
    }

    public void addData()
    {
        DatabaseReference ref = mDatabase.push(); //Start database write process
        String identifier= ref.getKey(); //Get unique identifier key associated with node

        // Create movingService object
        movingServices generatedMovingService = new movingServices(hourlySetRate.getText().toString(),
                numberOfMovers.getText().toString());

        generatedMovingService.setIdentifier(identifier); // Set the unique identifier of that node in movingService object

        mDatabase.child(identifier).setValue(generatedMovingService); // Push the value to database node using the unique identifier key

        MovingInfo form = new MovingInfo(null, null, null, null, null,
                false, null, null, 0, 0);

        form.setServiceIdentifier(identifier); // Set the service id that hte form is bounded to

        DatabaseReference formRef = FirebaseDatabase.getInstance().getReference("Forms");
        String formIdentifier = formRef.push().getKey();

        form.setFormIdentifier(formIdentifier); // Set the Form's UID to itself
        generatedMovingService.setFormID(formIdentifier); //Bind the moving service to it's form


        formRef.child(formIdentifier).setValue(form); // Push the form to the database using it's UID


        mDatabase.child(identifier).setValue(generatedMovingService); // Push the value to database node using the unique identifier key
        // Determine the activity responsible for this activities creation
        AdminMovingAssistance parentFrag = ((AdminMovingAssistance) AdminAddMovingAssistance.this.getParentFragment());
        parentFrag.view_team(); // Access method from parents activity to show moving teams

    }
}