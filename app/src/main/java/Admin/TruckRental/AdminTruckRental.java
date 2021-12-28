package Admin.TruckRental;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AdminTruckRental extends Fragment {

    Fragment truck_add = new AdminAddTruckRental(); //Points to fragment responsible for adding cars in fleet
    Fragment truck_view= new AdminViewTruckRental(); //Points to fragment responsible for viewing cars in fleet
    FloatingActionButton floatingActionButton;

    public AdminTruckRental() {
        // Required empty public constructor
    }

    public static AdminTruckRental newInstance() {
        AdminTruckRental fragment = new AdminTruckRental();
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
        // On create View, recover layout assignment by inflating layout
        View rootView = inflater.inflate(R.layout.fragment_admin_truck_rental, container, false);

        //Assignments
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.add_truck_service);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On floating action button (+) hide button and display car adding process
                floatingActionButton.hide();
                add_truck();
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set default view as displaying cars in fleet
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, truck_view).commit();
    }
    public void add_truck()
    {
        // Display view of adding car
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, truck_add).commit();

    }

    public void view_truck()
    {
        //Set floating action button (+) visible button and display car viewing process
        floatingActionButton.show();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, truck_view).commit();

    }

}