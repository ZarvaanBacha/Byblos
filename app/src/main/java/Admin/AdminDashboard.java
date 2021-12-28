package Admin;

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


public class AdminDashboard extends AppCompatActivity {
    // Variable Initialization
    DrawerLayout drawerLayout;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("IS LAUNCHED?", "adminActivity");
        setContentView(R.layout.activity_admin_dashboard);

        //Assignments
        drawerLayout = findViewById(R.id.adminDash);

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

    public void manageServices(View view)
    {
        //Open Fragment for managing Services
        Log.d("Clicked?", "Manage Services");
        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, AdminManageServices.class, null)
                .setReorderingAllowed(true).addToBackStack( "manageService" )
                .commit();
        closeDrawer(drawerLayout);
    }

    public void manageUsers(View view)
    {
        // Open Activity for managing Users

        fragmentManager.beginTransaction()
                .replace(R.id.functionalityFragment, AdminManageUsers.class, null)
                .setReorderingAllowed(true).addToBackStack( "manageService" )
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
