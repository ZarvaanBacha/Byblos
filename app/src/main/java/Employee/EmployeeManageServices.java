package Employee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Admin.CarRental.AdminViewCarRental;

public class EmployeeManageServices extends Fragment {

    FloatingActionButton floatingActionButton;

    // Database Reference
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    Fragment service_add = new EmployeeAddServices();
    Fragment service_view = new EmployeeViewServices();


    public EmployeeManageServices() {
    }

    public static EmployeeManageServices newInstance() {
        EmployeeManageServices fragment = new EmployeeManageServices();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Point to firebase path
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_employee_manage_services, container, false);

        //Assignments
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.add_services);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On floating action button (+) hide button and show the page to add services to Employee branch
                floatingActionButton.hide();
                addServices();
            }
        });

        displayServices();

        return rootView;
    }

    public void displayServices()
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.populate, service_view).commit();
        floatingActionButton.show();

    }

    public void addServices()
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.populate, service_add).commit();
    }
}