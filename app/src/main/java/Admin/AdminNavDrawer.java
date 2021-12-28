package Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.byblos.R;


public class AdminNavDrawer extends Fragment {
    public AdminNavDrawer() {
        // Required empty public constructor
    }

    public static AdminNavDrawer newInstance() {
        AdminNavDrawer fragment = new AdminNavDrawer();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On create View, recover layout assignment by inflating layout
        View view = inflater.inflate(R.layout.fragment_admin_nav_drawer, container, false);
        return view;
    }
}