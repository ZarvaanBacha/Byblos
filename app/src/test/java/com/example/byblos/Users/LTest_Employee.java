//package com.teamBravo.byblosMobile.Users;
//
//import androidx.test.core.app.ActivityScenario;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.FirebaseDatabase;
//import com.teamBravo.byblosMobile.DB.ServiceDB;
//import com.teamBravo.byblosMobile.DB.UserDB;
//import com.teamBravo.byblosMobile.MainActivity;
//import com.teamBravo.byblosMobile.Test_utilities;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//
//@RunWith(RobolectricTestRunner.class)
//public class LTest_Employee {
//
//  final static String password = "_tony";
//  final static String email = "stark@gmail.com";
//  Employee employee;
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
//      FirebaseDatabase fbLocalDB = Test_utilities.getEmulatorDB();
//      employee = new Employee(this.activity, new UserDB(fbLocalDB));
//      employee.serviceDB = new ServiceDB(fbLocalDB);
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
//}
