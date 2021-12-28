package Employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import Admin.CarRental.AdminAddCarRental;
import Users.Employee;

public class EmployeeManageBranch extends Fragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object
    Fragment branch_create = new EmployeeCreateBranch();
    Fragment branch_view = new EmployeeViewBranch();
    public EmployeeManageBranch() {
        // Required empty public constructor
    }

    public static EmployeeManageBranch newInstance() {
        EmployeeManageBranch fragment = new EmployeeManageBranch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_manage_branch, container, false);
    }


    public void setView()
    {
        // This method will check if the Employee logged in has a branch already linked with their account
        // If they don't have a branch linked, the program will prompt them to make a branch
        // If they have a branch, the program will display the branch

        // Get employee details
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees/"+mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                if (employee.getBranchUID() == null)
                {
                    createBranch();
                }else
                    {
                        viewBranch();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void createBranch()
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, branch_create).commit();
    }

    public void viewBranch()
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, branch_view).commit();
    }
}