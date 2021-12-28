package Employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Branch.BranchInfo;
import Users.Employee;
import Utilities.InputTextValidator;
import Utilities.Utilities;

public class EmployeeViewBranch extends Fragment {

    // Database Reference
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    boolean[] isTextValid = new boolean[1];

    public EmployeeViewBranch() {
        // Required empty public constructor
    }


    public static EmployeeViewBranch newInstance() {
        EmployeeViewBranch fragment = new EmployeeViewBranch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Point to firebase path
        mDatabase = FirebaseDatabase.getInstance().getReference("Branches");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_employee_view_branch, container, false);

        EditText addressEnter = (EditText) rootView.findViewById(R.id.addressEnter);

        // input validation
        addressEnter.addTextChangedListener( new InputTextValidator( addressEnter ) {
            @Override
            public void validate( TextView textView, String text ) {
                String message = "Please insert validate your address text.";
                isTextValid[0] = Utilities.validateTextView_Address( textView, text, message, getContext() );
            }
        } );

        CheckBox monday = (CheckBox) rootView.findViewById(R.id.Monday);
        CheckBox tuesday = (CheckBox) rootView.findViewById(R.id.Tuesday);
        CheckBox wednesday = (CheckBox) rootView.findViewById(R.id.Wednesday);
        CheckBox thursday = (CheckBox) rootView.findViewById(R.id.Thursday);
        CheckBox friday = (CheckBox) rootView.findViewById(R.id.Friday);
        CheckBox saturday = (CheckBox) rootView.findViewById(R.id.Saturday);
        CheckBox sunday = (CheckBox) rootView.findViewById(R.id.Sunday);

        TimePicker openingTime = (TimePicker) rootView.findViewById(R.id.openingTime);
        TimePicker closingTime = (TimePicker) rootView.findViewById(R.id.closingTime);

        Button submit = (Button) rootView.findViewById(R.id.confirmAndOverwrite);

        openingTime.setIs24HourView(true);
        closingTime.setIs24HourView(true);

        displayBranch(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                openingTime, closingTime,
                addressEnter);

        submit.setOnClickListener(new View.OnClickListener() {
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

                modifyBranch(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                        openingTime, closingTime, addressEnter.getText().toString());
            }
        });
        return rootView;
    }

    public void displayBranch(CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun,
                              TimePicker openingTime, TimePicker closingTime,
                              EditText location)
    {
        // Get UID OF BRANCH FROM EMPLOYEE AND MODIFY SHIT TO SHOW THE DETAILS
        String manageKey = mAuth.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(manageKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branchUID = snapshot.getValue(Employee.class).getBranchUID();
                mDatabase.child(branchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BranchInfo branch = snapshot.getValue(BranchInfo.class);

                        // Figure out how to interface the list of days opened ot the checkboxes
                        Log.d("Branch", branch.toString());
                        Log.d("Branch", ""+branch.getDaysOpen());
                        // Call class responsible for checking boxes according to the days
                        dayOpen(mon, tue, wed, thu, fri, sat, sun, branch.getDaysOpen());
                        setTime(openingTime, closingTime, branch.getTimings());
                        location.setText(branch.getLocation());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public List<String> setDays(CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun)
    {
        List<String> daysChecked = new ArrayList<>();

        if (mon.isChecked())
        {
            daysChecked.add("Monday");
        }

        if (tue.isChecked())
        {
            daysChecked.add("Tuesday");
        }

        if (wed.isChecked())
        {
            daysChecked.add("Wednesday");
        }

        if (thu.isChecked())
        {
            daysChecked.add("Thursday");
        }

        if (fri.isChecked())
        {
            daysChecked.add("Friday");
        }

        if (sat.isChecked())
        {
            daysChecked.add("Saturday");
        }

        if (sun.isChecked())
        {
            daysChecked.add("Sunday");
        }

        return daysChecked;

    }

    public List<Integer> setTimings(TimePicker openingTime, TimePicker closingTime)
    {
        List<Integer> timings = new ArrayList<>();
        timings.add(openingTime.getHour());
        timings.add(openingTime.getMinute());

        timings.add(closingTime.getHour());
        timings.add(closingTime.getMinute());
        return timings;
    }

    public void modifyBranch(CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun,
                             TimePicker openingTime, TimePicker closingTime,
                             String location)
    {
        // Get Branch UID from Employee
        String employeeKey = mAuth.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(employeeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branchKey = snapshot.getValue(Employee.class).getBranchUID();

                // Get all changes
                List<String> dayOpen = setDays(mon, tue, wed, thu, fri, sat, sun);
                List<Integer> timings = setTimings(openingTime, closingTime);

                mDatabase.child(branchKey).child("daysOpen").setValue(dayOpen);
                mDatabase.child(branchKey).child("timings").setValue(timings);
                mDatabase.child(branchKey).child("location").setValue(location);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void dayOpen(CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun, List<String> daysOpen)
    {
        if (daysOpen.contains("Monday"))
        {
            mon.setChecked(true);
        }

        if (daysOpen.contains("Tuesday"))
        {
            tue.setChecked(true);
        }

        if (daysOpen.contains("Wednesday"))
        {
            wed.setChecked(true);
        }

        if (daysOpen.contains("Thursday"))
        {
            thu.setChecked(true);
        }

        if (daysOpen.contains("Friday"))
        {
            fri.setChecked(true);
        }

        if (daysOpen.contains("Saturday"))
        {
            sat.setChecked(true);
        }

        if (daysOpen.contains("Sunday"))
        {
            sun.setChecked(true);
        }
    }

    public void setTime(TimePicker openingTime, TimePicker closingTime, List<Integer> timings)
    {
        openingTime.setHour(timings.get(0));
        openingTime.setMinute(timings.get(1));

        closingTime.setHour(timings.get(2));
        closingTime.setHour(timings.get(3));
    }



}