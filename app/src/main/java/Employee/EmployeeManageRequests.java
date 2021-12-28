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

import Customer.CustomerDashboard;
import Customer.CustomerFormStatus;
import Services.Forms.Form;
import Services.Forms.MovingInfo;
import Services.Forms.Rental.CarInfo;
import Services.Forms.Rental.TruckInfo;
import Users.Employee;

public class EmployeeManageRequests extends Fragment {
    LinearLayout layoutToDraw;
    private DatabaseReference mDatabase;
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Fire authentication object

    public EmployeeManageRequests() {
        // Required empty public constructor
    }

    public static EmployeeManageRequests newInstance(String param1, String param2) {
        EmployeeManageRequests fragment = new EmployeeManageRequests();
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
        View rootView = inflater.inflate(R.layout.fragment_employee_manage_requests, container, false);
        // Assign Layout in XML that will be populated programmatically
        layoutToDraw = (LinearLayout) rootView.findViewById(R.id.populate);
        thiscontext = container.getContext(); // Set context of this view

        Button refreshButton = (Button) rootView.findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeDashboard parentFrag = ((EmployeeDashboard) EmployeeManageRequests.this.getActivity());
                parentFrag.manageRequests(rootView);
            }
        });


        displayItems(rootView, layoutToDraw);

        return rootView;
    }

    public void displayItems(View view, LinearLayout layoutToDraw)
    {
        // Get list of pending forms

        FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> forms = snapshot.getValue(Employee.class).getFormUIDs();

                if (forms.size() != 0)
                {
                    for (String UID : forms)
                    {
                        // Get Service UID
                        FirebaseDatabase.getInstance().getReference("Forms").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String serviceUID = snapshot.getValue(Form.class).getServiceIdentifier();

                                FirebaseDatabase.getInstance().getReference("Services").child("Cars").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(serviceUID))
                                        {
                                            // Service Type Car
                                            generateCarRequest(view, layoutToDraw, UID, "Cars");
                                        }else
                                        {
                                            FirebaseDatabase.getInstance().getReference("Services").child("Trucks").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild(serviceUID))
                                                    {
                                                        // Service Type
                                                        generateTruckRequest(view, layoutToDraw, UID, "Trucks");
                                                    }else
                                                    {
                                                        FirebaseDatabase.getInstance().getReference("Services").child("Moving Assistance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.hasChild(serviceUID))
                                                                {
                                                                    // Service Moving Assistance
                                                                    generateMovingAssistanceRequest(view, layoutToDraw, UID, "Moving Assistance");
                                                                }else
                                                                {

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
                        // Build objects according to service type

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void generateCarRequest(View view, LinearLayout layoutToAdd, String formUID, String serviceType)
    {
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CarInfo carForm = snapshot.getValue(CarInfo.class);

                LinearLayout container = new LinearLayout(thiscontext);
                container.setPadding(10, 10,10,10);
                container.setOrientation(LinearLayout.VERTICAL);

                container.addView(createTextView("First Name: " + carForm.getFirstName()));
                container.addView(createTextView("Last Name: " + carForm.getLastName()));
                container.addView(createTextView("Date of Birth: " + carForm.getDateOfBirth()));
                container.addView(createTextView("Address: " + carForm.getAddress()));
                container.addView(createTextView("License: " + carForm.getLicenseType()));
                container.addView(createTextView("Email: " + carForm.getEmail()));
                container.addView(createTextView("Pick up Time: " + carForm.getPickUpTime()));
                container.addView(createTextView("Return Time: " + carForm.getReturnTime()));
                container.addView(createTextView("Car Type: " + carForm.getCarType()));


                Button approve = new Button(thiscontext);
                Button reject = new Button(thiscontext);

                approve.setText("APPROVE");
                reject.setText("REJECT");

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approve(view, serviceType, formUID);
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reject(view, serviceType, formUID);
                    }
                });

                container.addView(reject);
                container.addView(approve);



                layoutToAdd.addView(container);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generateTruckRequest(View view, LinearLayout layoutToAdd, String formUID, String serviceType)
    {
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TruckInfo truckForm = snapshot.getValue(TruckInfo.class);

                LinearLayout container = new LinearLayout(thiscontext);
                container.setPadding(10, 10,10,10);
                container.setOrientation(LinearLayout.VERTICAL);

                container.addView(createTextView("First Name: " + truckForm.getFirstName()));
                container.addView(createTextView("Last Name: " + truckForm.getLastName()));
                container.addView(createTextView("Date of Birth: " + truckForm.getDateOfBirth()));
                container.addView(createTextView("Address: " + truckForm.getAddress()));
                container.addView(createTextView("Area of Usage: " + truckForm.getUsageArea()));
                container.addView(createTextView("License: " + truckForm.getLicenseType()));
                container.addView(createTextView("Email: " + truckForm.getEmail()));
                container.addView(createTextView("Pick up Time: " + truckForm.getPickUpTime()));
                container.addView(createTextView("Return Time: " + truckForm.getReturnTime()));


                Button approve = new Button(thiscontext);
                Button reject = new Button(thiscontext);

                approve.setText("APPROVE");
                reject.setText("REJECT");

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approve(view, serviceType, formUID);
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reject(view, serviceType, formUID);
                    }
                });

                container.addView(reject);
                container.addView(approve);



                layoutToAdd.addView(container);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generateMovingAssistanceRequest(View view, LinearLayout layoutToAdd, String formUID, String serviceType)
    {
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MovingInfo movingForm = snapshot.getValue(MovingInfo.class);

                LinearLayout container = new LinearLayout(thiscontext);
                container.setPadding(10, 10,10,10);
                container.setOrientation(LinearLayout.VERTICAL);

                container.addView(createTextView("First Name: " + movingForm.getFirstName()));
                container.addView(createTextView("Last Name: " + movingForm.getLastName()));
                container.addView(createTextView("Date of Birth: " + movingForm.getDateOfBirth()));
                container.addView(createTextView("Start Location: " + movingForm.getStartLocation()));
                container.addView(createTextView("End Location: " + movingForm.getEndLocation()));
                container.addView(createTextView("Boxes Needed: " + movingForm.getNumberOfBoxes()));
                container.addView(createTextView("Email: " + movingForm.getEmail()));


                Button approve = new Button(thiscontext);
                Button reject = new Button(thiscontext);

                approve.setText("APPROVE");
                reject.setText("REJECT");

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approve(view, serviceType, formUID);
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reject(view, serviceType, formUID);
                    }
                });

                container.addView(reject);
                container.addView(approve);



                layoutToAdd.addView(container);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public TextView createTextView(String value)
    {
        TextView text = new TextView(thiscontext);
        text.setText(value);
        text.setTextSize(15);
        text.setWidth(layoutParams.MATCH_PARENT);
        return text;

    }

    public void approve(View view, String serviceType, String formUID)
    {
        // Update form status to approved
        // Remove UID from Employee DB
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (serviceType.equals("Cars"))
                {
                    CarInfo form = snapshot.getValue(CarInfo.class);
                    form.setIsApproved("APPROVED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (serviceType.equals("Trucks"))
                {
                    TruckInfo form = snapshot.getValue(TruckInfo.class);
                    form.setIsApproved("APPROVED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (serviceType.equals("Moving Assistance"))
                {
                    MovingInfo form = snapshot.getValue(MovingInfo.class);
                    form.setIsApproved("APPROVED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
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

        // Refresh layout
        EmployeeDashboard parentFrag = ((EmployeeDashboard) EmployeeManageRequests.this.getActivity());
        parentFrag.manageRequests(view);

    }

    public void reject(View view, String serviceType, String formUID)
    {
        // Update form status to approved
        // Remove UID from Employee DB
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (serviceType.equals("Cars"))
                {
                    CarInfo form = snapshot.getValue(CarInfo.class);
                    form.setIsApproved("REJECTED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (serviceType.equals("Trucks"))
                {
                    TruckInfo form = snapshot.getValue(TruckInfo.class);
                    form.setIsApproved("REJECTED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (serviceType.equals("Moving Assistance"))
                {
                    MovingInfo form = snapshot.getValue(MovingInfo.class);
                    form.setIsApproved("REJECTED");
                    Log.d("form", form.toString());
                    FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(form);

                    FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Employee employee = snapshot.getValue(Employee.class);
                            employee.removeFormUID(formUID);
                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(mAuth.getUid()).setValue(employee);
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

        // Refresh layout
        EmployeeDashboard parentFrag = ((EmployeeDashboard) EmployeeManageRequests.this.getActivity());
        parentFrag.manageRequests(view);
    }
}