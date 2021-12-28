//package com.teamBravo.byblosMobile.Users;
//
//import androidx.test.core.app.ActivityScenario;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.FirebaseDatabase;
//import com.teamBravo.byblosMobile.DB.FormDB;
//import com.teamBravo.byblosMobile.DB.ServiceDB;
//import com.teamBravo.byblosMobile.DB.UserDB;
//import com.teamBravo.byblosMobile.MainActivity;
//import com.teamBravo.byblosMobile.Services.Forms.CarInfo;
//import com.teamBravo.byblosMobile.Services.Forms.FormInfo;
//import com.teamBravo.byblosMobile.Services.Forms.MovingInfo;
//import com.teamBravo.byblosMobile.Services.Forms.TruckInfo;
//import com.teamBravo.byblosMobile.Services.ServiceInfo;
//import com.teamBravo.byblosMobile.Test_utilities;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//
//@RunWith(RobolectricTestRunner.class)
//public class LTest_Customer {
//
//  final static String password = "_bob";
//  final static String email = "rose@gmail.com";
//  Customer customer;
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
//      customer = new Customer(this.activity, new UserDB(fbLocalDB));
//      customer.serviceDB = new ServiceDB(fbLocalDB);
//      customer.formDB = new FormDB(fbLocalDB);
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
