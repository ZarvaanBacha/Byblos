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
import Services.Forms.MovingInfo;
import Services.movingServices;
import Users.Customer;
import Users.Employee;
import Utilities.InputTextValidator;
import Utilities.Utilities;

public class MovingAssistanceForm extends Fragment {
  LinearLayout layoutToDraw;
  ViewGroup.LayoutParams layoutParams;
  Context thiscontext;
  boolean[] isTextValid = new boolean[7];
  private DatabaseReference mDatabase;
  private final FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

  public MovingAssistanceForm() {
    // Required empty public constructor
  }

  public static MovingAssistanceForm newInstance( String param1, String param2 ) {
    MovingAssistanceForm fragment = new MovingAssistanceForm();
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
    View rootView = inflater.inflate( R.layout.fragment_moving_assistance_form, container, false );

    // Assign Layout in XML that will be populated programmatically
    layoutToDraw = rootView.findViewById( R.id.layoutToDraw );
    thiscontext = container.getContext(); // Set context of this view

    // Input Assignments
    EditText firstName = rootView.findViewById( R.id.firstNameInput );
    EditText lastName = rootView.findViewById( R.id.lastNameInput );
    EditText emailInput = rootView.findViewById( R.id.emailInput );
    EditText startLocation = rootView.findViewById( R.id.startLocationInput );
    EditText endLocation = rootView.findViewById( R.id.endLocationInput );
    EditText boxesNeeded = rootView.findViewById( R.id.boxesNeededInput );
    EditText birthdayInput = rootView.findViewById( R.id.birthdayInput );
    // TODO @kurtis: [2021-12-23 Thu 15:12] number of movers

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
    startLocation.addTextChangedListener( new InputTextValidator( startLocation ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Please insert validate your address text.";
        isTextValid[3] = Utilities.validateTextView_Address( textView, text, message, getContext() );
      }
    } );
    endLocation.addTextChangedListener( new InputTextValidator( endLocation ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Please insert validate your address text.";
        isTextValid[4] = Utilities.validateTextView_Address( textView, text, message, getContext() );
      }
    } );
    boxesNeeded.addTextChangedListener( new InputTextValidator( boxesNeeded ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Number of boxes must between 1-99.";
        isTextValid[5] = Utilities.validateTextView_Boxes( textView, text, message, getContext() );
      }
    } );
    birthdayInput.addTextChangedListener( new InputTextValidator( birthdayInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Date of birth must be of format dd/MM/yyyy: 12/01/2012";
        isTextValid[6] = Utilities.validateTextView_DateOfBirth( textView, text, message, getContext() );
      }
    } );


    cancelButton.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        // Determine the activity responsible for this activities creation
        CustomerDashboard parentFrag = ( (CustomerDashboard) MovingAssistanceForm.this.getActivity() );
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
                startLocation.getText().toString(), endLocation.getText().toString(),
                boxesNeeded.getText().toString(), birthdayInput.getText().toString() );

        // Determine the activity responsible for this activities creation
        CustomerDashboard parentFrag = ( (CustomerDashboard) MovingAssistanceForm.this.getActivity() );
        parentFrag.formStatus( rootView );

      }
    } );
    return rootView;
  }

  public void submitForm( String firstName, String lastName, String emailInput,
                          String startLocation, String endLocation,
                          String boxesNeeded, String birthdayInput ) {
    // Get Service UID from user DB
    FirebaseDatabase.getInstance().getReference( "Users" ).child( "Customers" ).child( mAuth.getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        String serviceUID = snapshot.getValue( Customer.class ).getServiceSelectedUID();

        // Get form UID from Service UID

        FirebaseDatabase.getInstance().getReference( "Services" ).child( "Moving Assistance" ).child( serviceUID ).addListenerForSingleValueEvent( new ValueEventListener() {
          @Override
          public void onDataChange( @NonNull DataSnapshot snapshot ) {
            String formUID = snapshot.getValue( movingServices.class ).getFormID();
            movingServices service = snapshot.getValue( movingServices.class );

            FirebaseDatabase.getInstance().getReference( "Forms" ).child( formUID ).addListenerForSingleValueEvent( new ValueEventListener() {
              @Override
              public void onDataChange( @NonNull DataSnapshot snapshot ) {

                MovingInfo form = snapshot.getValue( MovingInfo.class );
                Log.d( "Form", form.toString() );
                form.setFirstName( firstName );
                form.setLastName( lastName );
                form.setEmail( emailInput );
                form.setStartLocation( startLocation );
                form.setEndLocation( endLocation );
                form.setNumberOfBoxes( Integer.parseInt( boxesNeeded ) );
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