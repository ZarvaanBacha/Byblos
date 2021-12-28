package Customer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.byblos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Branch.BranchInfo;
import Forms.TruckRentalForm;
import Services.Forms.Form;
import Services.Forms.MovingInfo;
import Services.Forms.Rental.CarInfo;
import Services.Forms.Rental.TruckInfo;
import Services.carService;
import Services.movingServices;
import Services.truckService;
import Users.Customer;
import Users.Employee;


public class CustomerFormStatus extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ViewGroup.LayoutParams layoutParams;
    Context thiscontext;

    public static CustomerFormStatus newInstance(String param1, String param2) {
        CustomerFormStatus fragment = new CustomerFormStatus();
        return fragment;
    }

    public CustomerFormStatus() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_customer_form_status, container, false);

        // Assignments

        //Layouts
        LinearLayout requestInformation = (LinearLayout) rootView.findViewById(R.id.requestInfo);
        LinearLayout ratingContainer = (LinearLayout) rootView.findViewById(R.id.ratingContainer);
        LinearLayout noRequest = (LinearLayout) rootView.findViewById(R.id.noRequest);

        // Text Fields
        TextView serviceType = (TextView) rootView.findViewById(R.id.serviceType);
        TextView status = (TextView) rootView.findViewById(R.id.status);

        //Spinner
        Spinner ratingSpinner = (Spinner) rootView.findViewById(R.id.ratingSpinner);

        //Button
        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        Button refreshButton = (Button) rootView.findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerDashboard parentFrag = ((CustomerDashboard) CustomerFormStatus.this.getActivity());
                parentFrag.formStatus(rootView);
            }
        });

        getRequestStatus(rootView, requestInformation, ratingContainer, noRequest, serviceType, status, ratingSpinner, submitButton);





        return rootView;
    }

    public void getRequestStatus(View view, LinearLayout requestInformation, LinearLayout ratingContainer, LinearLayout noRequest, TextView serviceType, TextView status,
                                 Spinner ratingSpinner, Button submitButton)
    {
        // Check if user has pending form by accessing the form UID customer DB
        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                // Check if form is stored in DB
                Log.d("Status", customer.getFormUID());
                if (customer.getFormUID().isEmpty())
                {
                    ratingContainer.setVisibility(view.GONE);
                    requestInformation.setVisibility(view.GONE);
                }
                else
                    {
                        {
                            // Get form UID and check status
                            String formUID = customer.getFormUID();
                            FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Form form = snapshot.getValue(Form.class);

                                    // check if form is pending
                                    if (form.getIsApproved().equals("REQUEST"))
                                    {
                                        // Form is pending
                                        // Hide Rating view
                                        // Hide No form view

                                        ratingContainer.setVisibility(view.GONE);
                                        noRequest.setVisibility(view.GONE);

                                        // Set Text
                                        serviceType.setText("Service Identifier: " + form.getServiceIdentifier());
                                        status.setText("Status: Pending");
                                        status.setTextColor(Color.parseColor("#0000FF"));
                                    }
                                    else
                                    {
                                        // Hide no form view
                                        noRequest.setVisibility(view.GONE);
                                        serviceType.setText("Service Identifier: " + form.getServiceIdentifier());

                                        if (form.getIsApproved().equals("APPROVED"))
                                        {
                                            status.setText("Status: APPROVED");
                                            status.setTextColor(Color.parseColor("#00FF00"));

                                            submitButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setRating(view, ratingSpinner.getSelectedItem().toString(), formUID);
                                                }
                                            });

                                        }else
                                        {
                                            status.setText("Status: REJECTED");
                                            status.setTextColor(Color.parseColor("#FF0000"));

                                            submitButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setRating(view, ratingSpinner.getSelectedItem().toString(), formUID);
                                                }
                                            });
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setRating(View view, String rating, String formUID)
    {
        // Set branch rating
        // Reset form
        // Remove form from customer DB

        // Get Branch
        FirebaseDatabase.getInstance().getReference("Forms").child(formUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Form form = snapshot.getValue(Form.class);
                Log.d("FORM", form.toString());
                // Get Branch UID from service linked to form

                FirebaseDatabase.getInstance().getReference("Services").child("Cars").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot psnapshot) {
                        if (psnapshot.hasChild(form.getServiceIdentifier()))
                        {
                            // Type Car
                            CarInfo carForm = snapshot.getValue(CarInfo.class);
                            FirebaseDatabase.getInstance().getReference("Services").child("Cars").child(carForm.getServiceIdentifier()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String branchUID = snapshot.getValue(carService.class).getBranch();

                                    FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            BranchInfo branchInfo = snapshot.getValue(BranchInfo.class);
                                            // Get Manager
                                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(branchInfo.getManagerUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    Employee manager = snapshot.getValue(Employee.class);
                                                    // Get customer
                                                    FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Customer customer = snapshot.getValue(Customer.class);

                                                            // Set branch rating and push to DB
                                                            branchInfo.setUserRating(Integer.parseInt(rating));
                                                            FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).setValue(branchInfo);

                                                            // Reset customer form storage
                                                            customer.setFormUID("");
                                                            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(customer.getIdentifier())
                                                                    .setValue(customer);

                                                            // Reset Form
                                                            carForm.resetForm();
                                                            FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(carForm);

                                                            //Return To Different Screen
                                                            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerFormStatus.this.getActivity());
                                                            parentFrag.selectServicesFromBranch(view);

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

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else
                            {
                                FirebaseDatabase.getInstance().getReference("Services").child("Trucks").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot psnapshot) {
                                        if (psnapshot.hasChild(form.getServiceIdentifier()))
                                        {
                                            // Type Truck
                                            TruckInfo truckForm = snapshot.getValue(TruckInfo.class);
                                            FirebaseDatabase.getInstance().getReference("Services").child("Trucks").child(truckForm.getServiceIdentifier()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String branchUID = snapshot.getValue(truckService.class).getBranch();

                                                    FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            BranchInfo branchInfo = snapshot.getValue(BranchInfo.class);
                                                            // Get Manager
                                                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(branchInfo.getManagerUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    Employee manager = snapshot.getValue(Employee.class);
                                                                    // Get customer
                                                                    FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            Customer customer = snapshot.getValue(Customer.class);

                                                                            // Set branch rating and push to DB
                                                                            branchInfo.setUserRating(Integer.parseInt(rating));
                                                                            FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).setValue(branchInfo);

                                                                            // Reset customer form storage
                                                                            customer.setFormUID("");
                                                                            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(customer.getIdentifier())
                                                                                    .setValue(customer);

                                                                            // Reset Form
                                                                            truckForm.resetForm();
                                                                            FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(truckForm);

                                                                            //Return To Different Screen
                                                                            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerFormStatus.this.getActivity());
                                                                            parentFrag.selectServicesFromBranch(view);

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

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }else
                                            {
                                                FirebaseDatabase.getInstance().getReference("Services").child("Moving Assistance").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot psnapshot) {
                                                        if (psnapshot.hasChild(form.getServiceIdentifier()))
                                                        {
                                                            // Type Moving Assistance
                                                            MovingInfo movingForm = snapshot.getValue(MovingInfo.class);
                                                            FirebaseDatabase.getInstance().getReference("Services").child("Moving Assistance").child(movingForm.getServiceIdentifier()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    String branchUID = snapshot.getValue(movingServices.class).getBranch();

                                                                    FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            BranchInfo branchInfo = snapshot.getValue(BranchInfo.class);
                                                                            // Get Manager
                                                                            FirebaseDatabase.getInstance().getReference("Users").child("Employees").child(branchInfo.getManagerUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    Employee manager = snapshot.getValue(Employee.class);
                                                                                    // Get customer
                                                                                    FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            Customer customer = snapshot.getValue(Customer.class);

                                                                                            // Set branch rating and push to DB
                                                                                            branchInfo.setUserRating(Integer.parseInt(rating));
                                                                                            FirebaseDatabase.getInstance().getReference("Branches").child(branchUID).setValue(branchInfo);

                                                                                            // Reset customer form storage
                                                                                            customer.setFormUID("");
                                                                                            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(customer.getIdentifier())
                                                                                                    .setValue(customer);

                                                                                            // Reset Form
                                                                                            movingForm.resetForm();
                                                                                            FirebaseDatabase.getInstance().getReference("Forms").child(formUID).setValue(movingForm);

                                                                                            //Return To Different Screen
                                                                                            CustomerDashboard parentFrag = ((CustomerDashboard) CustomerFormStatus.this.getActivity());
                                                                                            parentFrag.selectServicesFromBranch(view);

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
    }
}