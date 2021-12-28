package Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.example.byblos.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class AdminManageUsers extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public AdminManageUsers() {
        // Required empty public constructor
    }

    public static AdminManageUsers newInstance() {
        AdminManageUsers fragment = new AdminManageUsers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View view = inflater.inflate(R.layout.fragment_admin_manage_users, container, false);

        //Assignments
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        // Setup tabs with support class
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager());
        vpAdapter.addFragment(new AdminManageCustomers(), "Customers");
        vpAdapter.addFragment(new AdminManageEmployees(), "Employees");
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


}