package Employee;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Barrier;
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

import Branch.BranchInfo;
import Services.Forms.Rental.CarInfo;
import Services.carService;
import Services.movingServices;
import Services.truckService;
import Users.Employee;

public class EmployeeAddServices extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    public EmployeeAddServices() {
        // Required empty public constructor
    }

    public static EmployeeAddServices newInstance() {
        EmployeeAddServices fragment = new EmployeeAddServices();
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
        View rootView = inflater.inflate(R.layout.fragment_employee_add_services, container, false);

        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.populate);
        thiscontext = container.getContext(); // Set context of this view
        generateItems(layoutToDraw);


        return rootView;
    }

    public void generateItems(LinearLayout layoutToDraw)
    {
        // Get BranchUID via employee
        FirebaseDatabase.getInstance().getReference().child("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String branchUID = snapshot.getValue(Employee.class).getBranchUID();

                // List services that are available (not linked to any branch) by branch = "NONE"

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            Log.d("BRO", ds.getKey().getClass().getName());
                            // Return Car, Truck, Moving Assistance

                            // If in Key Type Car
                            if (ds.getKey().equals("Cars"))
                            {
                                Log.d("In Cars", "YE");
                                for (DataSnapshot ds2 : ds.getChildren())
                                {
                                    Log.d("Car UID", ""+ds2.getRef().getKey());
                                    FirebaseDatabase.getInstance().getReference("Services").child("Cars").child(ds2.getRef().getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            carService car = snapshot.getValue(carService.class);
                                            if (car.getBranch().equals("NONE"))
                                            {
                                                displayItem("Car", car.getMake(), car.getModel(), null, car.getPrice(),
                                                        branchUID, car.getIdentifier(), layoutToDraw);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            // If in Key Type Truck
                            if (ds.getKey().equals("Trucks"))
                            {
                                Log.d("In Trucks", "YE");
                                for (DataSnapshot ds2 : ds.getChildren())
                                {
                                    Log.d("Truck UID", ""+ds2.getRef().getKey());
                                    FirebaseDatabase.getInstance().getReference("Services").child("Trucks").child(ds2.getRef().getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            truckService truck = snapshot.getValue(truckService.class);
                                            if (truck.getBranch().equals("NONE"))
                                            {
                                                displayItem("Truck", truck.getMake(), truck.getModel(), null, truck.getPrice(),
                                                        branchUID, truck.getIdentifier(), layoutToDraw);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            // If in Key Type Truck
                            if (ds.getKey().equals("Moving Assistance"))
                            {
                                Log.d("In Moving Assistance", "YE");
                                for (DataSnapshot ds2 : ds.getChildren())
                                {
                                    Log.d("Moving Assistance UID", ""+ds2.getRef().getKey());
                                    FirebaseDatabase.getInstance().getReference("Services").child("Moving Assistance").child(ds2.getRef().getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            movingServices movingService = snapshot.getValue(movingServices.class);
                                            if (movingService.getBranch().equals("NONE"))
                                            {
                                                displayItem("Moving Assistance", null, null, movingService.getNumberOfMovers(), movingService.getPrice(),
                                                        branchUID, movingService.getIdentifier(), layoutToDraw);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }


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

            Button add = new Button(thiscontext);
            add.setText("ADD TO MY BRANCH");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addService(typeService, branchUID, serviceUID);
                }
            });

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
            add.setText("ADD TO MY BRANCH");

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addService(typeService, branchUID, serviceUID);
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

    public void addService(String serviceType, String branchID, String serviceID)
    {
        // Link service to branch, and then branch to service
        FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BranchInfo branch = snapshot.getValue(BranchInfo.class);
                branch.addService(serviceID);
                FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("serviceList").setValue(branch.getServiceList());

                if (serviceType.equals("Car"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Services").child("Cars").child(serviceID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            carService car = snapshot.getValue(carService.class);
                            car.setBranch(branchID);
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
                            truck.setBranch(branchID);
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
                            movingService.setBranch(branchID);
                            FirebaseDatabase.getInstance().getReference().child("Services").child("Moving Assistance").child(serviceID).child("branch").setValue(movingService.getBranch());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                // Determine the activity responsible for this activities creation and switch back to displaying
                EmployeeManageServices parentFrag = ((EmployeeManageServices) EmployeeAddServices.this.getParentFragment());
                parentFrag.displayServices();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}