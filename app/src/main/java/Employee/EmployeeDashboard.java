package Employee;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.byblos.R;
import com.example.byblos.Welcome;
import com.google.firebase.auth.FirebaseAuth;


public class EmployeeDashboard extends AppCompatActivity {
    // Variable Initialization
    DrawerLayout drawerLayout;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("IS LAUNCHED?", "employeeActivity");
        setContentView(R.layout.activity_employee_dashboard);

        //Assignments
        drawerLayout = findViewById(R.id.employeeDash);

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

    public void manageBranch(View view)
    {
        //Open Fragment for Managing Branch
        Log.d("Clicked?", "Manage Branch");

        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, EmployeeManageBranch.class, null)
                .setReorderingAllowed(true).addToBackStack( "manageBranch" )
                .commit();
        closeDrawer(drawerLayout);
    }

    public void manageServicesOffered(View view)
    {
        // Open Activity for managing services offered


        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, EmployeeManageServices.class, null)
                .setReorderingAllowed(true).addToBackStack( "manageServiceOffered" )
                .commit();
        closeDrawer(drawerLayout);
    }

    public void manageRequests(View view)
    {
        // Open Activity for managing customer requests


        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, EmployeeManageRequests.class, null)
                .setReorderingAllowed(true).addToBackStack( "manageRequests" )
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
}
