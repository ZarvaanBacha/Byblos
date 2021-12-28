//package com.teamBravo.byblosMobile.DB;
//
//import androidx.test.core.app.ActivityScenario;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.FirebaseDatabase;
//import com.teamBravo.byblosMobile.Services.Forms.FormInfo;
//import com.teamBravo.byblosMobile.Test_utilities;
//import com.teamBravo.byblosMobile.MainActivity;
//import com.teamBravo.byblosMobile.Services.Forms.CarInfo;
//import com.teamBravo.byblosMobile.Services.Forms.MovingInfo;
//import com.teamBravo.byblosMobile.Services.Forms.TruckInfo;
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
//public class LTest_FormInfoDB {
//
//  FormDB formDB;
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
//      formDB = new FormDB(Test_utilities.getEmulatorDB());
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
//    FirebaseDatabase db = formDB.getDb();
//    Test_utilities.flushEmulatorDB(db);
//  }
//
//  @Test
//  public void Test_addForm() {
//
//    // --- prepare ---
//    System.out.println(formDB.dbPath);
//    CarInfo carForm = new CarInfo("Toyota Corolla");
//    TruckInfo truckForm = new TruckInfo("KIN1PQ");
//    MovingInfo movingForm = new MovingInfo("KIN1PQ", "K1NQQQ", 3, 4);
//
//    // --- perform test ----
//    formDB.addForm(carForm);
//    formDB.addForm(truckForm);
//    formDB.addForm(movingForm);
//    // --- assert ---
//
//  }
//
//  @Test
//  public void Test_queryForm() {
//    HashMap<String, FormInfo> formInfoMap = new HashMap<>();
//    formDB.queryFormsByName("_form_rental_car", formInfoMap);
//  }
//
//  @Test
//  public void Test_deleteForm() {
//    // --- prepare ---
//    ArrayList<String> formKeyList = new ArrayList<>();
//    formKeyList.add("-Mp18e7Bo8lwpFV16KtL");
//    formKeyList.add("-Mp18e7e3unSURlELfVc");
//    formKeyList.add("-Mp18e7e3unSURlELfVd");
//    formKeyList.add("-Mp19_8RvD-CgvTgdEkY");
//    formKeyList.add("-Mp19_9-z3lsWi0YSIrD");
//    formKeyList.add("-Mp19_9-z3lsWi0YSIrE");
//    // --- perform test ----
//    formDB.deleteFormsByKeys(formKeyList);
//    // --- assert ---
//  }
//
//}