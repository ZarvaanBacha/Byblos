package Forms;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Branch.BranchInfo;
import Customer.CustomerDashboard;
import Services.Forms.Rental.CarInfo;
import Services.carService;
import Users.Customer;
import Users.Employee;
import Utilities.InputTextValidator;
import Utilities.Utilities;


public class CarRentalForm extends Fragment {

  LinearLayout layoutToDraw;
  ViewGroup.LayoutParams layoutParams;
  Context thiscontext;
  boolean[] isTextValid = new boolean[8];
  private DatabaseReference mDatabase;
  private final FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

  public CarRentalForm() {
    // Required empty public constructor
  }

  public static CarRentalForm newInstance() {
    CarRentalForm fragment = new CarRentalForm();
    return fragment;
  }

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState ) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate( R.layout.fragment_car_rental_form, container, false );

    // Assign Layout in XML that will be populated programmatically
    layoutToDraw = rootView.findViewById( R.id.layoutToDraw );
    thiscontext = container.getContext(); // Set context of this view


    // Input Assignments
    EditText firstName = rootView.findViewById( R.id.firstNameInput );
    EditText lastName = rootView.findViewById( R.id.lastNameInput );
    EditText emailInput = rootView.findViewById( R.id.emailInput );
    EditText addressInput = rootView.findViewById( R.id.addressInput );
    EditText startTimeInput = rootView.findViewById( R.id.startTimeInput );
    EditText endTimeInput = rootView.findViewById( R.id.endTimeInput );
    EditText licenseInput = rootView.findViewById( R.id.licenseInput );
    EditText birthdayInput = rootView.findViewById( R.id.birthdayInput );
    // TODO @kurtis: [2021-12-23 Thu 15:12] pickup date and time
    // TODO @kurtis: [2021-12-23 Thu 15:12] return date and time
    // TODO @kurtis: [2021-12-23 Thu 15:12] preferred car type: compact intermediate SUV

    //Buttons
    Button requestButton = rootView.findViewById( R.id.request );
    Button cancelButton = rootView.findViewById( R.id.cancel );

    // input validation

    firstName.addTextChangedListener( new InputTextValidator( firstName ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Name cannot contain characters like _*()/> etc";
        isTextValid[0] = Utilities.validateTextView_General( textView, text, message, getContext() );
      }
    } );
    lastName.addTextChangedListener( new InputTextValidator( lastName ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Name cannot contain characters like _*()/> etc";
        isTextValid[1] = Utilities.validateTextView_General( textView, text, message, getContext() );
      }
    } );
    emailInput.addTextChangedListener( new InputTextValidator( emailInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Email cannot contain illegal characters like *()/> etc";
        isTextValid[2] = Utilities.validateTextView_Email( textView, text, message, getContext() );
      }
    } );
    addressInput.addTextChangedListener( new InputTextValidator( addressInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Please insert validate your address text.";
        isTextValid[3] = Utilities.validateTextView_Address( textView, text, message, getContext() );
      }
    } );
    startTimeInput.addTextChangedListener( new InputTextValidator( startTimeInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Time must be exactly 2 digits in 24h format, i.e. 13 for 1 pm";
        isTextValid[4] = Utilities.validateTextView_Time( textView, text, message, getContext() );
      }
    } );
    endTimeInput.addTextChangedListener( new InputTextValidator( endTimeInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Time must be exactly 2 digits in 24h format, i.e. 13 for 1 pm";
        isTextValid[5] = Utilities.validateTextView_Time( textView, text, message, getContext() );
      }
    } );
    licenseInput.addTextChangedListener( new InputTextValidator( licenseInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "License must be: G, G1 or G2";
        isTextValid[6] = Utilities.validateTextView_DriverLicense( textView, text, message, getContext() );
      }
    } );
    birthdayInput.addTextChangedListener( new InputTextValidator( birthdayInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Date of birth must be of format dd/MM/yyyy: 12/01/2012";
        isTextValid[7] = Utilities.validateTextView_DateOfBirth( textView, text, message, getContext() );
      }
    } );

    cancelButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        // Determine the activity responsible for this activities creation
        CustomerDashboard parentFrag = ( (CustomerDashboard) CarRentalForm.this.getActivity() );
        parentFrag.selectServicesFromBranch( rootView );
      }
    } );

    requestButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        // input validation
        boolean isAllTextValid = true;
        for ( Boolean b : isTextValid ) isAllTextValid = isAllTextValid && b;
        if ( isAllTextValid )
          Toast.makeText( getContext(), "Request Sent", Toast.LENGTH_SHORT ).show();  // TODO DEBUG
        else {
          Toast.makeText( getContext(), "Please validate your input texts.", Toast.LENGTH_SHORT ).show();
          return;
        }

        submitForm( firstName.getText().toString(), lastName.getText().toString(), emailInput.getText().toString(),
                addressInput.getText().toString(), startTimeInput.getText().toString(),
                endTimeInput.getText().toString(), licenseInput.getText().toString(),
                birthdayInput.getText().toString() );

        // Determine the activity responsible for this activities creation
        CustomerDashboard parentFrag = ( (CustomerDashboard) CarRentalForm.this.getActivity() );
        parentFrag.formStatus( rootView );

      }
    } );
    return rootView;
  }

  public void submitForm( String firstName, String lastName, String emailInput,
                          String addressInput, String startTimeInput,
                          String endTimeInput, String licenseInput,
                          String birthdayInput ) {
    // Get Service UID from user DB
    FirebaseDatabase.getInstance().getReference( "Users" ).child( "Customers" ).child( mAuth.getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        String serviceUID = snapshot.getValue( Customer.class ).getServiceSelectedUID();

        // Get form UID from Service UID

        FirebaseDatabase.getInstance().getReference( "Services" ).child( "Cars" ).child( serviceUID ).addListenerForSingleValueEvent( new ValueEventListener() {
          @Override
          public void onDataChange( @NonNull DataSnapshot snapshot ) {
            String formUID = snapshot.getValue( carService.class ).getFormID();
            carService service = snapshot.getValue( carService.class );

            FirebaseDatabase.getInstance().getReference( "Forms" ).child( formUID ).addListenerForSingleValueEvent( new ValueEventListener() {
              @Override
              public void onDataChange( @NonNull DataSnapshot snapshot ) {

                CarInfo form = snapshot.getValue( CarInfo.class );
                Log.d( "Form", form.toString() );
                form.setFirstName( firstName );
                form.setLastName( lastName );
                form.setEmail( emailInput );
                form.setAddress( addressInput );
                form.setPickUpTime( startTimeInput );
                form.setReturnTime( endTimeInput );
                form.setLicenseType( licenseInput );
                form.setDateOfBirth( birthdayInput );
                form.setIsApproved( "REQUEST" );

                // Update values for form in DB
                FirebaseDatabase.getInstance().getReference( "Forms" ).child( form.getFormIdentifier() ).setValue( form );

                // Add form UID to Customer
                FirebaseDatabase.getInstance().getReference( "Users" ).child( "Customers" ).child( mAuth.getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
                  @Override
                  public void onDataChange( @NonNull DataSnapshot snapshot ) {
                    Customer customer = snapshot.getValue( Customer.class );
                    customer.setFormUID( form.getFormIdentifier() );

                    FirebaseDatabase.getInstance().getReference( "Users" ).child( "Customers" ).child( mAuth.getUid() ).setValue( customer );

                    // Add form id to branch manager's DB

                    // Get manager from branch UID which is stored in service DB
                    String branchUID = service.getBranch();
                    FirebaseDatabase.getInstance().getReference( "Branches" ).child( branchUID ).addListenerForSingleValueEvent( new ValueEventListener() {
                      @Override
                      public void onDataChange( @NonNull DataSnapshot snapshot ) {
                        String managerUID = snapshot.getValue( BranchInfo.class ).getManagerUID();

                        FirebaseDatabase.getInstance().getReference( "Users" ).child( "Employees" ).child( managerUID ).addListenerForSingleValueEvent( new ValueEventListener() {
                          @Override
                          public void onDataChange( @NonNull DataSnapshot snapshot ) {
                            Employee manager = snapshot.getValue( Employee.class );

                            manager.addFormUID( formUID );

                            FirebaseDatabase.getInstance().getReference( "Users" ).child( "Employees" ).child( managerUID ).setValue( manager );
                          }

                          @Override
                          public void onCancelled( @NonNull DatabaseError error ) {

                          }
                        } );
                      }

                      @Override
                      public void onCancelled( @NonNull DatabaseError error ) {

                      }
                    } );


                  }

                  @Override
                  public void onCancelled( @NonNull DatabaseError error ) {

                  }
                } );

              }

              @Override
              public void onCancelled( @NonNull DatabaseError error ) {

              }
            } );


          }

          @Override
          public void onCancelled( @NonNull DatabaseError error ) {

          }
        } );

      }

      @Override
      public void onCancelled( @NonNull DatabaseError error ) {

      }
    } );
  }
}