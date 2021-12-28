package Employee;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Branch.BranchInfo;
import Services.carService;
import Services.movingServices;
import Services.truckService;
import Users.Employee;

public class EmployeeViewServices extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    public EmployeeViewServices() {
        // Required empty public constructor
    }

    public static EmployeeViewServices newInstance() {
        EmployeeViewServices fragment = new EmployeeViewServices();
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
        View rootView = inflater.inflate(R.layout.fragment_employee_view_services, container, false);

        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.populate);
        thiscontext = container.getContext(); // Set context of this view
        displayItems(layoutToDraw);

        return rootView;
    }

    public void displayItems(LinearLayout layoutToDraw)
    {
        // Get the Branch UID, via the employee
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String  branchKey = snapshot.getValue(Employee.class).getBranchUID();

                // Get the list of service keys stored in branch
                FirebaseDatabase.getInstance().getReference().child("Branches").child(branchKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> serviceUIDs = snapshot.getValue(BranchInfo.class).getServiceList();
                        Log.d("serviceUIDS", ""+serviceUIDs);

                        for (String service: serviceUIDs)
                        {
                            // Check if Car Service
                            mDatabase.child("Cars").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(service))
                                    {
                                        // Generate Cars
                                        carService car = snapshot.child(service).getValue(carService.class);
                                        displayItem("Car", car.getMake(), car.getModel(), null, car.getPrice(), branchKey,car.getIdentifier(), layoutToDraw);
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
                                    if (snapshot.hasChild(service))
                                    {
                                        // Generate Trucks
                                        truckService truck = snapshot.child(service).getValue(truckService.class);
                                        displayItem("Truck", truck.getMake(), truck.getModel(), null, truck.getPrice(), branchKey,truck.getIdentifier(), layoutToDraw);
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
                                    if (snapshot.hasChild(service))
                                    {
                                        // Generate Moving Assistance
                                        movingServices movingService = snapshot.child(service).getValue(movingServices.class);
                                        displayItem("Moving Assistance", null, null, movingService.getNumberOfMovers(), movingService.getPrice(),
                                                branchKey, movingService.getIdentifier(), layoutToDraw);

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

    public void displayItem(String typeService, String typeMake, String typeModel, String numMovers, String price, String branchUID, String serviceUID, LinearLayout layout)
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

            Button delete = new Button(thiscontext);
            delete.setText("REMOVE FROM BRANCH");

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteService(typeService, branchUID, serviceUID);
                }
            });

            linearContainer.addView(delete);

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

            Button delete = new Button(thiscontext);
            delete.setText("REMOVE FROM MY BRANCH");

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteService(typeService, branchUID, serviceUID);
                }
            });

            linearContainer.addView(delete);

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

    public void deleteService(String serviceType, String branchID, String serviceID)
    {
        // Link service to branch, and then branch to service
        FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BranchInfo branch = snapshot.getValue(BranchInfo.class);
                branch.removeService(serviceID);
                FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("serviceList").setValue(branch.getServiceList());

                if (serviceType.equals("Car"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Services").child("Cars").child(serviceID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            carService car = snapshot.getValue(carService.class);
                            car.setBranch("NONE");
                            FirebaseDatabase.getInstance().getReference().child("Services").child("Cars").child(serviceID).child("branch").setValue(car.getBranch());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (serviceType.equals("Truck"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Services").child("Trucks").child(serviceID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            truckService truck = snapshot.getValue(truckService.class);
                            truck.setBranch("NONE");
                            FirebaseDatabase.getInstance().getReference().child("Services").child("Trucks").child(serviceID).child("branch").setValue(truck.getBranch());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (serviceType.equals("Moving Assistance"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Services").child("Moving Assistance").child(serviceID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            movingServices movingService = snapshot.getValue(movingServices.class);
                            movingService.setBranch("NONE");
                            FirebaseDatabase.getInstance().getReference().child("Services").child("Moving Assistance").child(serviceID).child("branch").setValue(movingService.getBranch());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                // Refresh layout
                layoutToDraw.removeAllViews();
                displayItems(layoutToDraw);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}