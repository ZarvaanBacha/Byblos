package Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.byblos.R;
import com.example.byblos.Welcome;
import com.google.firebase.auth.FirebaseAuth;

import Forms.CarRentalForm;
import Forms.MovingAssistanceForm;
import Forms.TruckRentalForm;

public class CustomerDashboard extends AppCompatActivity {
    // Variable Initialization
    DrawerLayout drawerLayout;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        //Assignments
        drawerLayout = findViewById(R.id.customerDash);

        // Display Welcome Fragment
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, Welcome.class, null)
                .setReorderingAllowed(true)
                .commit();
    }

    public void clickNavigationDrawer(View view)
    {
        //Open drawer
        openDrawer(drawerLayout);
    }

    public void selectBranch(View view)
    {
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, CustomerViewBranch.class, null)
                .setReorderingAllowed(true).addToBackStack( "selectBranch" )
                .commit();
        closeDrawer(drawerLayout);
    }

    public void selectServicesFromBranch(View view)
    {
        fragmentManager.beginTransaction()
               .replace(R.id.functionalityFragment, CustomerSelectService.class, null)
               .setReorderingAllowed(true).addToBackStack( "selectServicesFromBranch" )
               .commit();
       closeDrawer(drawerLayout);
    }

    public void formStatus(View view)
    {
      fragmentManager.beginTransaction()
              .replace(R.id.functionalityFragment, CustomerFormStatus.class, null)
              .setReorderingAllowed(true).addToBackStack( "formStatus" )
               .commit();
      closeDrawer(drawerLayout);
    }

    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void viewCarRentalForm()
    {
        // Launch Car Rental Form
        Log.d("Form Launch", "TRUE");
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, CarRentalForm.class, null)
                .setReorderingAllowed(true)
                .commit();
        closeDrawer(drawerLayout);
    }

    public void viewTruckRentalForm()
    {
        // Launch Car Rental Form
        Log.d("Form Launch", "TRUE");
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, TruckRentalForm.class, null)
                .setReorderingAllowed(true)
                .commit();
        closeDrawer(drawerLayout);
    }

    public void viewMovingAssistanceForm()
    {
        // Launch Car Rental Form
        Log.d("Form Launch", "TRUE");
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, MovingAssistanceForm.class, null)
                .setReorderingAllowed(true)
                .commit();
        closeDrawer(drawerLayout);
    }
}