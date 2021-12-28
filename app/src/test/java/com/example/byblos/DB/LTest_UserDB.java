//package com.teamBravo.byblosMobile.DB;
//
//import androidx.test.core.app.ActivityScenario;
//
//import com.google.firebase.FirebaseApp;
//import com.teamBravo.byblosMobile.Test_utilities;
//import com.teamBravo.byblosMobile.MainActivity;
//import com.teamBravo.byblosMobile.Users.UserInfo;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//
//import java.util.HashMap;
//
//@RunWith(RobolectricTestRunner.class)
//public class LTest_UserDB {
//
//  UserDB userDB;
//  MainActivity activity;
//  ActivityScenario<MainActivity> scenario;
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
//      userDB = new UserDB(Test_utilities.getEmulatorDB());
//    });
//
//  }
//
//  @After
//  public void cleanup() {
//    scenario.close();
//  }
//
//
//  @Test
//  public void Test_addUser() {
//
//    // TODO @kurtis: 11/20/21 WIP
//
////    // --- prepare ---
////    System.out.println(userDB.dbPath);
////
////    // --- perform test ----
////    userDB.addUser();
////    userDB.addUser(truckUser);
////    userDB.addUser(movingUser);
////    // --- assert ---
//
//  }
//
//  @Test
//  public void Test_queryUser() {
//    HashMap<String, UserInfo> userInfoMap = new HashMap<>();
//    userDB.queryUsersByUserName("admin", userInfoMap);
//  }
//
//  @Test
//  public void Test_deleteUser() {
////    // --- prepare ---
////    ArrayList<String> userKeyList = new ArrayList<>();
////    userKeyList.add("-Mp18e7Bo8lwpFV16KtL");
////    userKeyList.add("-Mp18e7e3unSURlELfVc");
////    userKeyList.add("-Mp18e7e3unSURlELfVd");
////    userKeyList.add("-Mp19_8RvD-CgvTgdEkY");
////    userKeyList.add("-Mp19_9-z3lsWi0YSIrD");
////    userKeyList.add("-Mp19_9-z3lsWi0YSIrE");
////    // --- perform test ----
////    userDB.deleteUsersByKeys(userKeyList);
////    // --- assert ---
//
//  }
//
//
//}
