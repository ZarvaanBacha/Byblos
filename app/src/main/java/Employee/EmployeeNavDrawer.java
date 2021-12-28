package Employee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;

public class EmployeeNavDrawer extends Fragment {
    public EmployeeNavDrawer() {
        // Required empty public constructor
    }

    public static EmployeeNavDrawer newInstance() {
        EmployeeNavDrawer fragment = new EmployeeNavDrawer();
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
        return inflater.inflate(R.layout.fragment_employee_nav_drawer, container, false);
    }
}