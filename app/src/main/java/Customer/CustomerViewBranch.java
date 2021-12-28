package Customer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.List;

import Branch.BranchInfo;
import Users.Customer;
import Utilities.InputTextValidator;
import Utilities.Utilities;

public class CustomerViewBranch extends Fragment {
  LinearLayout layoutToDraw;
  ViewGroup.LayoutParams layoutParams;
  Context thiscontext;
  boolean isAddressTextValid;
  boolean[] isTimeTextsValid = new boolean[2];
  private DatabaseReference mDatabase;
  private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

  public CustomerViewBranch() {
    // Required empty public constructor
  }

  public static CustomerViewBranch newInstance() {
    CustomerViewBranch fragment = new CustomerViewBranch();
    return fragment;
  }

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    mDatabase = FirebaseDatabase.getInstance().getReference( "Services" );
  }

  @Override
  public View onCreateView( LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState ) {
    // Inflate the layout for this fragment
    // On create View, recover layout assignment by inflating layout
    View rootView = inflater.inflate( R.layout.fragment_customer_view_branch, container, false );

    // Assign Layout in XML that will be populated programmatically
    layoutToDraw = rootView.findViewById( R.id.populate );

    //listBranches(layoutToDraw);
    thiscontext = container.getContext(); // Set context of this view
    layoutParams = rootView.getLayoutParams(); //Get layout parameters and restriction for the parent XML file

    // Input Assignments
    EditText addressSearchInput = rootView.findViewById( R.id.locationSearch );
    EditText startHour = rootView.findViewById( R.id.startHour );
    EditText endHour = rootView.findViewById( R.id.endHour );
    Spinner serviceSpinner = rootView.findViewById( R.id.serviceSpinner );

    // Button assignments
    Button clearSearch = rootView.findViewById( R.id.clearSearch );
    Button clearTime = rootView.findViewById( R.id.clearTime );

    Button searchAddress = rootView.findViewById( R.id.searchAddress );
    Button searchTime = rootView.findViewById( R.id.searchTime );

    // input validation
    addressSearchInput.addTextChangedListener( new InputTextValidator( addressSearchInput ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Please insert validate your address text.";
        isAddressTextValid = Utilities.validateTextView_Address( textView, text, message, getContext() );
      }
    } );
    startHour.addTextChangedListener( new InputTextValidator( startHour ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Time must be exactly 2 digits in 24h format, i.e. 13 for 1 pm";
        isTimeTextsValid[0] = Utilities.validateTextView_Time( textView, text, message, getContext() );
      }
    } );
    endHour.addTextChangedListener( new InputTextValidator( endHour ) {
      @Override
      public void validate( TextView textView, String text ) {
        String message = "Time must be exactly 2 digits in 24h format, i.e. 13 for 1 pm";
        isTimeTextsValid[1] = Utilities.validateTextView_Time( textView, text, message, getContext() );
      }
    } );

    // Button Actions
    clearSearch.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        layoutToDraw.removeAllViews();
        clearSearchText( addressSearchInput );
        listBranches( layoutToDraw );
        serviceSpinner.setSelection( 0, true );

      }
    } );

    clearTime.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        layoutToDraw.removeAllViews();
        clearSearchText( startHour );
        clearSearchText( endHour );
        listBranches( layoutToDraw );
        serviceSpinner.setSelection( 0, true );
      }
    } );

    searchAddress.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        // input validation
        if ( !isAddressTextValid ) {
          Toast.makeText( getContext(), "Please validate your input texts.", Toast.LENGTH_SHORT ).show();
          return;
        }

        layoutToDraw.removeAllViews();
        listBranchesByAddress( addressSearchInput.getText().toString(), layoutToDraw );
        clearSearchText( startHour );
        clearSearchText( endHour );
        serviceSpinner.setSelection( 0, true );
      }
    } );

    searchTime.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        // input validation
        boolean isAllTextValid = true;
        for ( Boolean b : isTimeTextsValid ) isAllTextValid = isAllTextValid && b;
        if ( !isAllTextValid ) {
          Toast.makeText( getContext(), "Please validate input text.", Toast.LENGTH_SHORT ).show();
          return;
        }

        layoutToDraw.removeAllViews();
        listBranchesByHour( Integer.parseInt( startHour.getText().toString() ), Integer.parseInt( endHour.getText().toString() ), layoutToDraw );
        clearSearchText( addressSearchInput );
        serviceSpinner.setSelection( 0, true );

      }
    } );

    serviceSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
        String selectedItem = parent.getItemAtPosition( position ).toString();

        if ( selectedItem.equals( "Car Rental" ) ) {
          layoutToDraw.removeAllViews();
          listBranchesByService( "Cars", layoutToDraw );
          clearSearchText( addressSearchInput );
          clearSearchText( startHour );
          clearSearchText( endHour );
        }

        if ( selectedItem.equals( "Truck Rental" ) ) {
          layoutToDraw.removeAllViews();
          listBranchesByService( "Trucks", layoutToDraw );
          clearSearchText( addressSearchInput );
          clearSearchText( startHour );
          clearSearchText( endHour );
        }

        if ( selectedItem.equals( "Moving Assistance" ) ) {
          layoutToDraw.removeAllViews();
          listBranchesByService( "Moving Assistance", layoutToDraw );
          clearSearchText( addressSearchInput );
          clearSearchText( startHour );
          clearSearchText( endHour );
        }

        if ( selectedItem.equals( "All" ) ) {
          layoutToDraw.removeAllViews();
          listBranches( layoutToDraw );
          clearSearchText( addressSearchInput );
          clearSearchText( startHour );
          clearSearchText( endHour );
        }
      }

      @Override
      public void onNothingSelected( AdapterView<?> parent ) {

      }
    } );


    return rootView;
  }

  public void listBranches( LinearLayout layoutToDraw ) {
    // List all branches
    FirebaseDatabase.getInstance().getReference().child( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        for ( DataSnapshot ds : snapshot.getChildren() ) {
          BranchInfo branch = ds.getValue( BranchInfo.class );
          createItem( branch.getBranchUID(), branch.getLocation(), branch.getManagerUID(), branch.getDaysOpen(), branch.getTimings(), branch.getUserRating(), layoutToDraw );

        }
      }

      @Override
      public void onCancelled( @NonNull DatabaseError error ) {

      }
    } );


  }

  public void createItem( String branchUID, String location, String managerUID, List<String> daysOpen, List<Integer> timings, int userRating, LinearLayout layoutToDraw ) {
    // Create linear layout to store generated elements
    LinearLayout linearContainer = new LinearLayout( thiscontext );
    linearContainer.setPadding( 10, 10, 10, 10 );
    linearContainer.setOrientation( LinearLayout.VERTICAL );

    linearContainer.addView( createTextView( "Location: " + location ) );
    linearContainer.addView( createTextView( "Days open: " + daysOpen.toString() ) );
    linearContainer.addView( createTextView( "Timings: Opening - " + timings.get( 0 ).toString() + ":" + timings.get( 1 ).toString() +
            " | | Closing - " + timings.get( 2 ).toString() + ":" + timings.get( 3 ).toString() ) );
    linearContainer.addView( createTextView( "User Rating: " + userRating + "/5" ) );


    // Create button to Select Branch object
    Button select = new Button( thiscontext );
    select.setText( "Select as my preferred branch" );

    select.setOnClickListener( new View.OnClickListener() {
      @Override
      // On delete click, call method responsible for deleting object
      public void onClick( View v ) {
        selectBranch( branchUID ); // Set this current Branch as the user's preferred branch
      }
    } );

    linearContainer.addView( select );

    layoutToDraw.addView( linearContainer );


  }

  public TextView createTextView( String value ) {
    // Create a text view with set construction
    TextView text = new TextView( thiscontext );
    text.setText( value );
    text.setTextSize( 15 );
    text.setWidth( ViewGroup.LayoutParams.MATCH_PARENT );
    return text;
  }

  public void selectBranch( String branchUID ) {

    // Get user
    FirebaseDatabase.getInstance().getReference( "Users" ).child( "Customers" ).child( mAuth.getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        Customer customer = snapshot.getValue( Customer.class );
        Log.d( "UIDs", customer.toString() );
        customer.setPreferredBranchUID( branchUID );
        FirebaseDatabase.getInstance().getReference().child( "Users" ).child( "Customers" ).child( mAuth.getUid() ).child( "preferredBranchUID" ).setValue( customer.getPreferredBranchUID() );
        // Launch the list services based on branch method
      }


      @Override
      public void onCancelled( @NonNull DatabaseError error ) {

      }
    } );
  }

  public void listBranchesByAddress( String address, LinearLayout layoutToDraw ) {
    // List all branches
    FirebaseDatabase.getInstance().getReference().child( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        for ( DataSnapshot ds : snapshot.getChildren() ) {
          if ( ds.child( "location" ).getValue( String.class ).equals( address ) ) {
            BranchInfo branch = ds.getValue( BranchInfo.class );
            createItem( branch.getBranchUID(), branch.getLocation(), branch.getManagerUID(), branch.getDaysOpen(), branch.getTimings(), branch.getUserRating(), layoutToDraw );
          }
        }
      }

      @Override
      public void onCancelled( @NonNull DatabaseError error ) {
      }
    } );
  }

  public void listBranchesByHour( int startingHour, int endingHour, LinearLayout layoutToDraw ) {
    // List all branches
    FirebaseDatabase.getInstance().getReference().child( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
      @Override
      public void onDataChange( @NonNull DataSnapshot snapshot ) {
        for ( DataSnapshot ds : snapshot.getChildren() ) {
          // Check if branch timings in between what is inputted
          BranchInfo branch = ds.getValue( BranchInfo.class );

          assert branch != null;
          int branchStart = branch.getTimings().get( 0 );
          int branchEnd = branch.getTimings().get( 3 );

//          int filterStartHour = (startingHour == 0) ? 24 : startingHour;
//          int filterEndHour = (endingHour == 0) ? 24 : endingHour;
          if ( startingHour <= branchStart && endingHour >= branchEnd ) {
            createItem(
                    branch.getBranchUID(),
                    branch.getLocation(),
                    branch.getManagerUID(),
                    branch.getDaysOpen(),
                    branch.getTimings(),
                    branch.getUserRating(),
                    layoutToDraw );
          }
        }
      }

      @Override
      public void onCancelled( @NonNull DatabaseError error ) {

      }
    } );


  }

  public void listBranchesByService( String serviceType, LinearLayout layoutToDraw ) {
    // List if Car
    if ( serviceType.equals( "Cars" ) ) {
      // Get the list of services in the branch
      FirebaseDatabase.getInstance().getReference( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
        @Override
        public void onDataChange( @NonNull DataSnapshot snapshot ) {
          for ( DataSnapshot ds : snapshot.getChildren() ) {
            BranchInfo branch = ds.getValue( BranchInfo.class );
            List<String> serviceUIDS = branch.getServiceList();
            Log.d( "Service UIDs", serviceUIDS.toString() );
            for ( String g : serviceUIDS ) {
              mDatabase.child( "Cars" ).addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot snapshot ) {
                  if ( snapshot.hasChild( g ) ) {
                    createItem( branch.getBranchUID(), branch.getLocation(), branch.getManagerUID(), branch.getDaysOpen(), branch.getTimings(), branch.getUserRating(), layoutToDraw );
                  }
                }

                @Override
                public void onCancelled( @NonNull DatabaseError error ) {

                }
              } );

            }


          }
        }

        @Override
        public void onCancelled( @NonNull DatabaseError error ) {

        }
      } );
    }

    // List if Truck
    if ( serviceType.equals( "Trucks" ) ) {
      // Get the list of services in the branch
      FirebaseDatabase.getInstance().getReference( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
        @Override
        public void onDataChange( @NonNull DataSnapshot snapshot ) {
          for ( DataSnapshot ds : snapshot.getChildren() ) {
            BranchInfo branch = ds.getValue( BranchInfo.class );
            List<String> serviceUIDS = branch.getServiceList();
            Log.d( "Service UIDs", serviceUIDS.toString() );
            for ( String g : serviceUIDS ) {
              mDatabase.child( "Trucks" ).addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot snapshot ) {
                  if ( snapshot.hasChild( g ) ) {
                    createItem( branch.getBranchUID(), branch.getLocation(), branch.getManagerUID(), branch.getDaysOpen(), branch.getTimings(), branch.getUserRating(), layoutToDraw );
                  }
                }

                @Override
                public void onCancelled( @NonNull DatabaseError error ) {

                }
              } );

            }


          }
        }

        @Override
        public void onCancelled( @NonNull DatabaseError error ) {

        }
      } );
    }

    // List if Moving Assistance
    if ( serviceType.equals( "Moving Assistance" ) ) {
      // Get the list of services in the branch
      FirebaseDatabase.getInstance().getReference( "Branches" ).addListenerForSingleValueEvent( new ValueEventListener() {
        @Override
        public void onDataChange( @NonNull DataSnapshot snapshot ) {
          for ( DataSnapshot ds : snapshot.getChildren() ) {
            BranchInfo branch = ds.getValue( BranchInfo.class );
            List<String> serviceUIDS = branch.getServiceList();
            Log.d( "Service UIDs", serviceUIDS.toString() );
            for ( String g : serviceUIDS ) {
              mDatabase.child( "Moving Assistance" ).addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot snapshot ) {
                  if ( snapshot.hasChild( g ) ) {
                    createItem( branch.getBranchUID(), branch.getLocation(), branch.getManagerUID(), branch.getDaysOpen(), branch.getTimings(), branch.getUserRating(), layoutToDraw );
                  }
                }

                @Override
                public void onCancelled( @NonNull DatabaseError error ) {

                }
              } );

            }


          }
        }

        @Override
        public void onCancelled( @NonNull DatabaseError error ) {

        }
      } );
    }


  }

  public void clearSearchText( EditText input ) {
    input.getText().clear();
  }
}