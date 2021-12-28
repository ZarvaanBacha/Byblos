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
//public class LTest_Admin {
//
//  Admin admin;
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
//      admin = new Admin(this.activity, new UserDB(fbLocalDB));
//      admin.serviceDB = new ServiceDB(fbLocalDB);
//      admin.formDB = new FormDB(fbLocalDB);
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
//  // --- User ---
//  // TODO @kurtis: [2021-11-27 Sat 12:11] since FirebaseAuth does NOT work for the local tests
//  //                                      user management has to be test by Espresso
////  @Test
////  public void Test_deleteAccount() {
//////    admin.deleteAccount();
////  }
////
////  @Test
////  public void Test_deleteAccounts() {
////  }
//
//  // TODO @kurtis: [2021-11-27 Sat 11:11] for unit test,
//  //               if a method involves "databaseReference.update()", then the database is only updated
//  //               add a breakpoint on it and run it in debug mode
//
//  // --- Service (branch) Templates ---
//  @Test
//  public void Test_createService() {
//
//    ServiceInfo newService = new ServiceInfo("_CarRental", "_CarRentalForm_by_ADMIN", 9999999.9, true, true);
//    String newServiceKey = admin.createService(newService);
//    System.out.println(newServiceKey);
//  }
//
//  @Test
//  public void Test_setHourlyRate() {
//    admin.setHourlyRate("_CarRental", 0.0001);
//  }
//
//  // ---  Form Templates ---
//  @Test
//  public void Test_createForm() {
//
//    // --- prepare ---
//    FormInfo newForm = new FormInfo("my firstName", "my lastName", "my dateOfBirth", "my address", "my email");
//
//    System.out.println(admin.formDB.dbPath);
//    CarInfo carForm = new CarInfo("Toyota Corolla");
//    TruckInfo truckForm = new TruckInfo("KIN1PQ");
//    MovingInfo movingForm = new MovingInfo("KIN1PQ", "K1NQQQ", 3, 4);
//
//    carForm.copy(newForm);
//    truckForm.copy(newForm);
//    movingForm.copy(newForm);
//    // --- perform test ----
//    System.out.println(admin.createForm(carForm));
//    System.out.println(admin.createForm(truckForm));
//    System.out.println(admin.createForm(movingForm));
//
//    // --- assert ---
//
//  }
//
//  @Test
//  public void Test_updateFormTemplate() {
//    FormInfo newForm = new FormInfo("admin firstName", "admin lastName", "admin dateOfBirth", "admin address", "admin email");
//    CarInfo newCarForm = new CarInfo("Tesla Model X");
//    newCarForm.copy(newForm);
//
//    admin.updateFormTemplate("_form_rental_car", newCarForm);
//  }
//
//}