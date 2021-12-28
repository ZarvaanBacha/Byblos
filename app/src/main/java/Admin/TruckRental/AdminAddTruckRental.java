package Admin.TruckRental;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.byblos.R;

import Services.Forms.Rental.TruckInfo;
import Services.truckService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminAddTruckRental extends Fragment {

   DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Services/Trucks");;
    // References

    EditText vehicleMake;
    EditText vehicleModel;
    EditText numberPlate;
    EditText hourlySetRate;
    EditText distanceRestriction;
    Spinner truckType;
    Button confirmAndAdd;


    public AdminAddTruckRental() {
        // Required empty public constructor
    }


    public static AdminAddTruckRental newInstance() {
        AdminAddTruckRental fragment = new AdminAddTruckRental();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_add_truck_rental, container, false);
        vehicleMake = (EditText) rootView.findViewById(R.id.vehicleMake);
        vehicleModel = (EditText) rootView.findViewById(R.id.vehicleModel);
        numberPlate = (EditText) rootView.findViewById(R.id.numberPlate);
        hourlySetRate = (EditText) rootView.findViewById(R.id.hourlySetRate);
        distanceRestriction = (EditText) rootView.findViewById(R.id.distanceRestriction);

        truckType = (Spinner) rootView.findViewById(R.id.truckType);

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

        // Create truckService object
        truckService generatedTruckService = new truckService(hourlySetRate.getText().toString(),vehicleMake.getText().toString(),
                vehicleModel.getText().toString(),
                truckType.getSelectedItem().toString(),
                numberPlate.getText().toString(),
                distanceRestriction.getText().toString());

        generatedTruckService.setIdentifier(identifier); // Set the unique identifier of that node in truckService object

        mDatabase.child(identifier).setValue(generatedTruckService); // Push the value to database node using the unique identifier key

        // Generate a form for the service
        TruckInfo form = new TruckInfo(null, null, null, null, null,
                false, null, null, null, null);

        form.setServiceIdentifier(identifier); // Set the service id that hte form is bounded to

        DatabaseReference formRef = FirebaseDatabase.getInstance().getReference("Forms");
        String formIdentifier = formRef.push().getKey();

        form.setFormIdentifier(formIdentifier); // Set the Form's UID to itself
        generatedTruckService.setFormID(formIdentifier); //Bind the truck to it's form


        formRef.child(formIdentifier).setValue(form); // Push the form to the database using it's UID


        mDatabase.child(identifier).setValue(generatedTruckService); // Push the value to database node using the unique identifier key

        // Determine the activity responsible for this activities creation
        AdminTruckRental parentFrag = ((AdminTruckRental) AdminAddTruckRental.this.getParentFragment());
        parentFrag.view_truck(); // Access method from parents activity to show trucks in fleet display

    }
}