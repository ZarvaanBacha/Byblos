package Admin.MovingServices;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.byblos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AdminMovingAssistance extends Fragment {
    Fragment moving_assistance_add = new AdminAddMovingAssistance(); //Points to fragment responsible for adding cars in fleet
    Fragment moving_assistance_view = new AdminViewMovingAssistance(); //Points to fragment responsible for viewing cars in fleet
    FloatingActionButton floatingActionButton;

    public AdminMovingAssistance() {
        // Required empty public constructor
    }


    public static AdminMovingAssistance newInstance() {
        AdminMovingAssistance fragment = new AdminMovingAssistance();
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
        View rootView = inflater.inflate(R.layout.fragment_admin_moving_assistance, container, false);

        //Assignments
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.add_moving_service);
        view_team();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On floating action button (+) hide button and display car adding process
                floatingActionButton.hide();
                add_team();
            }
        });
        return rootView;
    }

    public void add_team()
    {
        // Display view of adding Moving Assistance Team
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, moving_assistance_add).commit();

    }

    public void view_team()
    {
        //Set floating action button (+) visible button and display Moving Assistance Team process
        floatingActionButton.show();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, moving_assistance_view).commit();

    }
}