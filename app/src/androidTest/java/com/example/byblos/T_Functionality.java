package com.example.byblos;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Customer.CustomerDashboard;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith( AndroidJUnit4.class )
@LargeTest
public class T_Functionality {

  final static String ADMIN_EMAIL = "admin@admin.com";
  final static String ADMIN_PASSWD = "_admin";

  final static String EMPLOYEE_EMAIL = "gr@gmail.com";
  final static String EMPLOYEE_PASSWD = "password";

  final static String CUSTOMER_EMAIL = "homersimp@gmail.com";
  final static String CUSTOMER_PASSWD = "password";

  @Rule
  public ActivityScenarioRule<MainActivity>
          activityScenarioRule = new ActivityScenarioRule<>( MainActivity.class );

//  @Test
//  public void useAppContext() {
//    // Context of the app under test.
//    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//    assertEquals("com.example.byblos", appContext.getPackageName());
//  }

  @Test
  private void loginAdmin() {

    onView( withId( R.id.loginButton_main ) ).perform( click() );
    onView( withId( R.id.emailInput_login ) ).perform( typeText( ADMIN_EMAIL ), closeSoftKeyboard() );
    onView( withId( R.id.passwordInput_login ) ).perform( typeText( ADMIN_PASSWD ), closeSoftKeyboard() );
    onView( withId( R.id.loginButton_login ) ).perform( click() );

  }

  @Test
  private void loginEmployee() {

    onView( withId( R.id.loginButton_main ) ).perform( click() );
    onView( withId( R.id.emailInput_login ) ).perform( typeText( EMPLOYEE_EMAIL ), closeSoftKeyboard() );
    onView( withId( R.id.passwordInput_login ) ).perform( typeText( EMPLOYEE_PASSWD ), closeSoftKeyboard() );
    onView( withId( R.id.loginButton_login ) ).perform( click() );

  }

  @Test
  private void loginCustomer() {

    onView( withId( R.id.loginButton_main ) ).perform( click() );
    onView( withId( R.id.emailInput_login ) ).perform( typeText( CUSTOMER_EMAIL ), closeSoftKeyboard() );
    onView( withId( R.id.passwordInput_login ) ).perform( typeText( CUSTOMER_PASSWD ), closeSoftKeyboard() );
    onView( withId( R.id.loginButton_login ) ).perform( click() );

  }

  @Before
  public void initTest() {
//    Intents.init();
  }

  // --- Deliverable 4 ---
  @Test
  public void searchBranchByAddress() {

    loginCustomer();

    onView( withId( R.id.functionalityFragment ) ).check( matches( isDisplayed() ) );
//    onView( withId( R.id.drawer_button ) ).check( matches( isDisplayed() ) );
//    onView( withId( R.id.customerDash ) ).check( matches( isDisplayed() ) );

//    activityScenarioRule.getScenario().onActivity( activity -> {
//
////      View window = activity.getWindow().getDecorView();
//      IdlingRegistry.getInstance().register( activity.getIdlingResource() );
//
////      onView( withId( R.id.drawer_button ) ).check( matches( isDisplayed() ) );
////      onView( withId( R.id.customerDash ) ).check( matches( isDisplayed() ) );
//
//      // @kurtis: [2021-12-4 Sat 16:12] Checking for a Toast
////      onView(withText(R.string.login_successfull))
////              .inRoot(withDecorView(not(window)))
////              .check(matches(isDisplayed()));
//    } );


//    Intents.init();

//    FragmentScenario<CustomerFormStatus> scenario = FragmentScenario.launchInContainer( CustomerFormStatus.class );

//    intended( hasComponent( CustomerDashboard.class.getName() ) );
//    onView( withId( R.id.drawer_button ) ).check( matches(isDisplayed()) );

//    onView( withId( R.id.drawer_button ) ).perform( click() );
//    allOf(withId(R.id.drawerItemNameTextView), hasSibling(withText(((NavDrawerItem)item).getItemName())))
  }

  @Test
  public void searchBranchByWorkingHours() {

  }

  @Test
  public void searchBranchByServiceType() {

  }

  @Test
  public void fillInFormAndSubmit() {

  }

  @Test
  public void rateBranch() {

  }

}
