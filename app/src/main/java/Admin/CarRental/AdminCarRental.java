package Admin.CarRental;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AdminCarRental extends Fragment {

    Fragment car_add = new AdminAddCarRental(); //Points to fragment responsible for adding cars in fleet
    Fragment car_view= new AdminViewCarRental(); //Points to fragment responsible for viewing cars in fleet
    FloatingActionButton floatingActionButton;

    public AdminCarRental() {
        // Required empty public constructor
    }

    public static AdminCarRental newInstance() {
        AdminCarRental fragment = new AdminCarRental();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("Where am I? ", "You are in Car Rental");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View rootView = inflater.inflate(R.layout.fragment_admin_car_rental, container, false);

        //Assignments
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.add_car_service);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On floating action button (+) hide button and display car adding process
                floatingActionButton.hide();
                add_car();
            }
        });


        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set default view as displaying cars in fleet
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, car_view).commit();
    }

    public void add_car()
    {
        // Display view of adding car
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, car_add).commit();

    }

    public void view_car()
    {
        //Set floating action button (+) visible button and display car viewing process
        floatingActionButton.show();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, car_view).commit();

    }
}