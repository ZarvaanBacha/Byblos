package Customer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Admin.CarRental.AdminAddCarRental;
import Admin.CarRental.AdminCarRental;
import Branch.BranchInfo;
import Services.carService;
import Services.movingServices;
import Services.truckService;
import Users.Customer;

public class CustomerSelectService extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    public CustomerSelectService() {
        // Required empty public constructor
    }

    public static CustomerSelectService newInstance(String param1, String param2) {
        CustomerSelectService fragment = new CustomerSelectService();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("Services");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_select_service, container, false);

        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.layoutToDraw);
        thiscontext = container.getContext(); // Set context of this view

        LinearLayout selectBranch = (LinearLayout) rootView.findViewById(R.id.noBranchSelected);
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.serviceView);

        selectBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToBranchSelect(rootView);
            }
        });

        determineView(layoutToDraw, rootView, selectBranch, scrollView);
        return rootView;
    }


    public void determineView(LinearLayout layoutToDraw, View view, LinearLayout selectBranch, ScrollView scrollView)
    {
        // Check if user has preferred branch stored in their DB
        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Customer.class).getPreferredBranchUID().isEmpty())
                {
                    // Empty so dont show scroll view
                    scrollView.setVisibility(view.GONE);
                }else
                    {
                        selectBranch.setVisibility(view.GONE);
                        viewServices(layoutToDraw);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void viewServices(LinearLayout layoutToDraw)
    {
        // Get preferred branch UID from logged in user
        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String preferredBranchUID = snapshot.getValue(Customer.class).getPreferredBranchUID();
                // Using preferred branch list services available in it using the list of UIDs associated with it
                FirebaseDatabase.getInstance().getReference("Branches").child(preferredBranchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> listOfService = snapshot.getValue(BranchInfo.class).getServiceList();

                        for (String s:listOfService)
                        {
                            // Check if Service UID belongs to Car
                            mDatabase.child("Cars").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(s).exists())
                                    {
                                        // If service car generate it
                                        carService car = snapshot.child(s).getValue(carService.class);
                                        displayItem("Car", car.getMake(), car.getModel(), null, car.getPrice(), car.getIdentifier(), layoutToDraw);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            // Check if Truck Service
                            mDatabase.child("Trucks").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(s).exists())
                                    {
                                        // Generate Trucks
                                        truckService truck = snapshot.child(s).getValue(truckService.class);
                                        displayItem("Truck", truck.getMake(), truck.getModel(), null, truck.getPrice(),truck.getIdentifier(), layoutToDraw);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            // Check if Moving Assistance
                            mDatabase.child("Moving Assistance").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(s).exists())
                                    {
                                        // Generate Moving Assistance
                                        movingServices movingService = snapshot.child(s).getValue(movingServices.class);
                                        displayItem("Moving Assistance", null, null, movingService.getNumberOfMovers(), movingService.getPrice(),
                                                movingService.getIdentifier(), layoutToDraw);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
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

    public void displayItem(String typeService, String typeMake, String typeModel, String numMovers, String price, String serviceUID, LinearLayout layout)
    {
        if (typeService.equals("Truck") || typeService.equals("Car"))
        {
            LinearLayout linearContainer = new LinearLayout(thiscontext);
            linearContainer.setPadding(10, 10,10,10);
            linearContainer.setOrientation(LinearLayout.VERTICAL);

            linearContainer.addView(createTextView("Type: " + typeService));
            linearContainer.addView(createTextView("Make: " + typeMake));
            linearContainer.addView(createTextView("Model: " + typeModel));
            linearContainer.addView(createTextView("Price: $" + price + " per hour"));
            linearContainer.addView(createTextView("Unique Identification: " + serviceUID));

            Button add = new Button(thiscontext);
            add.setText("SELECT THIS SERVICE");

            if (typeService.equals("Car"))
            {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectService(layoutToDraw, serviceUID, "Cars");
                    }
                });
            }
            else
            {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectService(layoutToDraw, serviceUID, "Trucks");
                    }
                });
            }




            linearContainer.addView(add);

            // Add container layout to the main layout
            layout.addView(linearContainer);
        }
        else
        {
            LinearLayout linearContainer = new LinearLayout(thiscontext);
            linearContainer.setPadding(10, 10,10,10);
            linearContainer.setOrientation(LinearLayout.VERTICAL);

            linearContainer.addView(createTextView("Type: " + typeService));
            linearContainer.addView(createTextView("Number of Movers: " + numMovers));

            linearContainer.addView(createTextView("Price: $" + price + " per hour"));
            linearContainer.addView(createTextView("Unique Identification: " + serviceUID));

            Button add = new Button(thiscontext);
            add.setText("SELECT THIS SERVICE");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectService(layoutToDraw, serviceUID, "Moving Assistance");
                }
            });

            linearContainer.addView(add);

            // Add container layout to the main layout
            layout.addView(linearContainer);
        }
    }

    public TextView createTextView(String value)
    {
        TextView text = new TextView(thiscontext);
        text.setText(value);
        text.setTextSize(15);
        text.setWidth(layoutParams.MATCH_PARENT);
        return text;

    }

    public void selectService(LinearLayout layoutToDraw, String serviceUID, String serviceType)
    {
        // Store serviceUID in customer DB
        Log.d("Function Launched?", "Yes");

        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                customer.setServiceSelectedUID(serviceUID);
                // Update customer info in DB
                FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).setValue(customer);
                launchForm(serviceType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void launchForm(String serviceType)
    {
        Log.d("service", serviceType);

        if (serviceType.equals("Cars"))
        {
            // Launch car form parent view

            // Determine the activity responsible for this activities creation
            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerSelectService.this.getActivity());
            parentFrag.viewCarRentalForm();
        }

        if (serviceType.equals("Trucks"))
        {
            // Launch car form parent view

            // Determine the activity responsible for this activities creation
            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerSelectService.this.getActivity());
            parentFrag.viewTruckRentalForm();
        }

        if (serviceType.equals("Moving Assistance"))
        {
            // Launch car form parent view

            // Determine the activity responsible for this activities creation
            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerSelectService.this.getActivity());
            parentFrag.viewMovingAssistanceForm();
        }
    }

    public void switchToBranchSelect(View view)
    {
        CustomerDashboard parentFrag = ((CustomerDashboard) CustomerSelectService.this.getActivity());
        parentFrag.selectBranch(view);
    }
}