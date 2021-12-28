package Employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class EmployeeCreateBranch extends Fragment {

    // Database Reference
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    boolean[] isTextValid = new boolean[1];

    public EmployeeCreateBranch() {

    }

    public static EmployeeCreateBranch newInstance(String param1, String param2) {
        EmployeeCreateBranch fragment = new EmployeeCreateBranch();
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
        View rootView = inflater.inflate(R.layout.fragment_employee_create_branch, container, false);

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
        CheckBox thursday = (CheckBox) rootView.findViewById(R.id.Tuesday);
        CheckBox friday = (CheckBox) rootView.findViewById(R.id.Friday);
        CheckBox saturday = (CheckBox) rootView.findViewById(R.id.Saturday);
        CheckBox sunday = (CheckBox) rootView.findViewById(R.id.Sunday);

        TimePicker openingTime = (TimePicker) rootView.findViewById(R.id.openingTime);
        TimePicker closingTime = (TimePicker) rootView.findViewById(R.id.closingTime);

        Button submit = (Button) rootView.findViewById(R.id.confirmAndAdd);

        openingTime.setIs24HourView(true);
        closingTime.setIs24HourView(true);

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

                createBranch(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                        openingTime, closingTime, addressEnter.getText().toString());
            }
        });
        return rootView;
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

    public void createBranch(CheckBox mon, CheckBox tue, CheckBox wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun,
                             TimePicker openingTime, TimePicker closingTime,
                             String location)
    {
        BranchInfo branch = new BranchInfo();
        branch.setLocation(location);
        branch.setTimings(setTimings(openingTime, closingTime));
        branch.setDaysOpen(setDays(mon, tue, wed, thu, fri, sat, sun));

        DatabaseReference ref = mDatabase.push();
        String branchKey = ref.getKey();

        String managerKey = mAuth.getUid();

        branch.setBranchUID(branchKey);
        branch.setManagerUID(managerKey);

        mDatabase.child(branchKey).setValue(branch);

        // Get Employee Data and set link branch with employee

        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees/"+managerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                Map<String, Object> updates = new HashMap<String,Object>();

                updates.put("branchUID", branchKey);
                FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(managerKey).updateChildren(updates);
                EmployeeManageBranch parentFrag = ((EmployeeManageBranch) EmployeeCreateBranch.this.getParentFragment());
                parentFrag.viewBranch();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}