package Admin.CarRental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.byblos.R;

import Services.Forms.Rental.CarInfo;
import Services.carService;
import Utilities.InputTextValidator;
import Utilities.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddCarRental extends Fragment {

    // Database Reference
    private DatabaseReference mDatabase;

    // References
    EditText vehicleMake;
    EditText vehicleModel;
    EditText numberPlate;
    EditText hourlySetRate;
    Spinner vehicleType;
    Button confirmAndAdd;

    boolean[] isTextValid = new boolean[4];

    public AdminAddCarRental() {
        // Required empty public constructor
    }

    public static AdminAddCarRental newInstance() {
        AdminAddCarRental fragment = new AdminAddCarRental();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Point to firebase path
        mDatabase = FirebaseDatabase.getInstance().getReference("Services/Cars");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View rootView = inflater.inflate(R.layout.fragment_admin_add_car_rental, container, false);


        // Get Views
        vehicleMake = (EditText) rootView.findViewById(R.id.vehicleMake);
        vehicleModel = (EditText) rootView.findViewById(R.id.vehicleModel);
        numberPlate = (EditText) rootView.findViewById(R.id.numberPlate);
        hourlySetRate = (EditText) rootView.findViewById(R.id.hourlySetRate);

        // input validation

        vehicleMake.addTextChangedListener( new InputTextValidator( vehicleMake ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please insert validate your input text.";
                isTextValid[0] = Utilities.validateTextView_CarNames( textView, text, message, getContext() );
            }
        } );
        vehicleModel.addTextChangedListener( new InputTextValidator( vehicleModel ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please insert validate your input text.";
                isTextValid[1] = Utilities.validateTextView_CarNames( textView, text, message, getContext() );
            }
        } );
        numberPlate.addTextChangedListener( new InputTextValidator( numberPlate ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please insert validate your input text.";
                isTextValid[2] = Utilities.validateTextView_CarNames( textView, text, message, getContext() );
            }
        } );
        hourlySetRate.addTextChangedListener( new InputTextValidator( hourlySetRate ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please insert validate your input text.";
                isTextValid[3] = Utilities.validateTextView_Price( textView, text, message, getContext() );
            }
        } );

        vehicleType = (Spinner) rootView.findViewById(R.id.vehicleType);

        confirmAndAdd = (Button) rootView.findViewById(R.id.confirmAndAdd);

        confirmAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // input validation
                boolean isAllTextValid = true;
                for ( Boolean b : isTextValid ) isAllTextValid = isAllTextValid && b;
                if ( isAllTextValid )
                    Toast.makeText( getContext(), "Request Sent", Toast.LENGTH_SHORT ).show();  // TODO DEBUG
                else {
                    Toast.makeText( getContext(), "Please validate your input texts.", Toast.LENGTH_SHORT ).show();
                    return;
                }

                // On click of confirm and add, add data to database
                addData();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void addData() {

        DatabaseReference ref = mDatabase.push(); //Start database write process
        String identifier= ref.getKey(); //Get unique identifier key associated with node

        // Create carService object
        carService generatedCarService = new carService(hourlySetRate.getText().toString(),vehicleMake.getText().toString(),
                vehicleModel.getText().toString(),
                vehicleType.getSelectedItem().toString(),
                numberPlate.getText().toString());

        generatedCarService.setIdentifier(identifier); // Set the unique identifier of that node in carService object

        mDatabase.child(identifier).setValue(generatedCarService); // Push the value to database node using the unique identifier key

        // Generate a form for the service
        CarInfo form = new CarInfo(null, null, null, null, null,
                false, null, null, null, vehicleType.getSelectedItem().toString());

        form.setServiceIdentifier(identifier); // Set the service id that hte form is bounded to

        DatabaseReference formRef = FirebaseDatabase.getInstance().getReference("Forms");
        String formIdentifier = formRef.push().getKey();

        form.setFormIdentifier(formIdentifier); // Set the Form's UID to itself
        generatedCarService.setFormID(formIdentifier); //Bind the car to it's form

        formRef.child(formIdentifier).setValue(form); // Push the form to the database using it's UID

        mDatabase.child(identifier).setValue(generatedCarService); // Push the value to database node using the unique identifier key

        // Determine the activity responsible for this activities creation
        AdminCarRental parentFrag = ((AdminCarRental) AdminAddCarRental.this.getParentFragment());
        parentFrag.view_car(); // Access method from parents activity to show cars in fleet display

    }
}