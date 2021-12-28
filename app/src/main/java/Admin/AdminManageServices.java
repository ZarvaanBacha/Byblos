package Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.byblos.R;
import com.example.byblos.VPAdapter;
import com.google.android.material.tabs.TabLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Admin.CarRental.AdminCarRental;
import Admin.MovingServices.AdminMovingAssistance;
import Admin.TruckRental.AdminTruckRental;


public class AdminManageServices extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public AdminManageServices() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("Where am I", "You are in AdminManageService");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View view =  inflater.inflate(R.layout.fragment_admin_manage_services, container, false);

        //Assignments
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        // Setup tabs with support class
        VPAdapter vpAdapter = new VPAdapter(getChildFragmentManager());
        vpAdapter.addFragment(new AdminCarRental(), "Car Rental");
        vpAdapter.addFragment(new AdminTruckRental(), "Truck Rental");
        vpAdapter.addFragment(new AdminMovingAssistance(), "Moving Assistance");

        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Inflate the layout for this fragment
        return view;
    }
}