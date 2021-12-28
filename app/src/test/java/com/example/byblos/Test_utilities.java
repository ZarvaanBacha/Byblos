//package com.teamBravo.byblosMobile;
//
//import com.google.firebase.database.FirebaseDatabase;
//import com.teamBravo.byblosMobile.Users.User;
//
//public class Test_utilities {
//
//  final static String ADMIN_PASSWORD = "_admin";
//  final static String ADMIN_EMAIL = "power@gmail.com";
//
//  // from an "Android emulator":
//  // 10.0.2.2 is the special IP address to connect to the '127.0.0.1' of the host computer
//  public static String DEFAULT_HOST = "10.0.2.2";
//  public static int DEFAULT_DB_PORT = 9000;
//  public static int DEFAULT_AUTH_PORT = 9099;
//  public static String LOCAL_EMULATOR_DB = "http://127.0.0.1:9000/?ns=byblos-23f4a";
//
//  public static FirebaseDatabase getEmulatorDB() {
//    return FirebaseDatabase.getInstance(LOCAL_EMULATOR_DB);
//  }
//
//  public static FirebaseDatabase setupForAndroidEmulatorDB(String host, int port) {
//
//    FirebaseDatabase database = FirebaseDatabase.getInstance(LOCAL_EMULATOR_DB);
//    database.useEmulator(host, port);
//
//    return database;
//  }
//
//  public static FirebaseDatabase setupForAndroidEmulatorDB() {
//    return setupForAndroidEmulatorDB("http://127.0.0.1:", DEFAULT_DB_PORT);
//  }
//
//  public static void flushEmulatorDB(FirebaseDatabase database) {
//    // With a DatabaseReference, write null to clear the database.
//    database.getReference().setValue(null);
//  }
//
//  // emulate the sign action
//  // TODO @kurtis: [2021-11-27 Sat 12:11] FirebaseAuth does NOT work for the local tests
//  private void signIn(User user, String email, String password) {
//
//    user.userInfo.email = email;
//    user.userInfo.password = password;
//
//    // auth using emulator https://firebase.google.com/docs/emulator-suite/connect_auth
//    user.userDB.auth.useEmulator(Test_utilities.DEFAULT_HOST, Test_utilities.DEFAULT_AUTH_PORT);
//
//    user.userDB.auth
//            .signInWithEmailAndPassword(user.userInfo.email, user.userInfo.password)
//            .addOnCompleteListener(task -> {
//              if (task.isSuccessful()) {
//                user.fbUser = user.userDB.auth.getCurrentUser();
//                System.out.println("--- signed in successfully as admin ---");
//              } else {
//                System.out.println("--- sign-in unsuccessful ---");
//              }
//            });
//  }
//
//
//}
