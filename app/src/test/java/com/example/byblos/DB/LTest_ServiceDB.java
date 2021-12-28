//package com.teamBravo.byblosMobile.DB;
//
//import androidx.test.core.app.ActivityScenario;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.FirebaseDatabase;
//import com.teamBravo.byblosMobile.Test_utilities;
//import com.teamBravo.byblosMobile.MainActivity;
//import com.teamBravo.byblosMobile.Services.ServiceInfo;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//@RunWith(RobolectricTestRunner.class)
//public class LTest_ServiceDB {
//
//  ServiceDB serviceDB;
//  MainActivity activity;
//  ActivityScenario<MainActivity> scenario;
//
//  // --- prepare ---
//  // --- perform test ----
//  // --- assert ---
//
//  @Before
//  public void initDB() {
//
//    scenario = ActivityScenario.launch(MainActivity.class);
//    // more abt ActivityScenario:  https://developer.android.com/reference/androidx/test/core/app/ActivityScenario
//    scenario.onActivity(a -> {
//      this.activity = a;
//      FirebaseApp.initializeApp(a);
//      // hock up with the local FB DB emulator
//      serviceDB = new ServiceDB(Test_utilities.getEmulatorDB());
//    });
//
//  }
//
//  @After
//  public void cleanup() {
//    try {
//      scenario.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void Test_flush() {
//    FirebaseDatabase db = serviceDB.getDb();
//    Test_utilities.flushEmulatorDB(db);
//  }
//
//  @Test
//  public void Test_addService() {
//
//    // --- prepare ---
//    System.out.println(serviceDB.dbPath);
//    ServiceInfo newServiceInfo = new ServiceInfo("_CarRental", "_CarRentalForm",  1.3, true, false);
//
//    // --- perform test ----
//    serviceDB.addService(newServiceInfo);
//    // add two more to test "delete()"
//    serviceDB.updateEntryByKey("-Moy_FZGB4EqHUHEROmP", "yo this is your package");
//    serviceDB.updateEntryByKey("-Moy_GZYwuDOLoqsW3eh", "-----------------------");
//
//    // --- assert ---
//    System.out.println(newServiceInfo);
//    System.out.println(serviceDB.dbPath);
//
//  }
//
//  @Test
//  public void Test_queryService() {
//    HashMap<String, ServiceInfo> serviceInfoMap = new HashMap<>();
//    serviceDB.queryServicesByType("_CarRental", serviceInfoMap);
//  }
//
//  @Test
//  public void Test_deleteService() {
//    // --- prepare ---
//    ArrayList<String> serviceKeyList = new ArrayList<>();
//    serviceKeyList.add("-Moy_FZGB4EqHUHEROmP");
//    serviceKeyList.add("-Moy_GZYwuDOLoqsW3eh");
//    // --- perform test ----
//    serviceDB.deleteServicesByKeys(serviceKeyList);
//    // --- assert ---
//  }
//
//}