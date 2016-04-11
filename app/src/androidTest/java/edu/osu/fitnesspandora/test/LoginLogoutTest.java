package edu.osu.fitnesspandora.test;

import edu.osu.fitnesspandora.LoginActivity;
import edu.osu.fitnesspandora.MainActivity;
import edu.osu.fitnesspandora.R;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Brendan on 4/10/2016.
 */
public class LoginLogoutTest extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public LoginLogoutTest() {
		super(LoginActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}

	// Test to login
	public void testA_Login() {
        //Wait for activity: 'edu.osu.fitnesspandora.LoginActivity'
		solo.waitForActivity(LoginActivity.class, 2000);
        //Enter the text: 'b@g.com'
		solo.clearEditText((android.widget.EditText) solo.getView(R.id.email));
		solo.enterText((android.widget.EditText) solo.getView(R.id.email), "b@g.com");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(R.id.password));
        //Enter the text: 'password'
		solo.clearEditText((android.widget.EditText) solo.getView(R.id.password));
		solo.enterText((android.widget.EditText) solo.getView(R.id.password), "password");
        //Click on Login
		solo.clickOnView(solo.getView(R.id.email_sign_in_button));
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		assertTrue("edu.osu.fitnesspandora.MainActivity is not found!", solo.waitForActivity(MainActivity.class));
	}

	// Test to logout
	public void testB_Logout() {
		//Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
		//Click on ImageView
		solo.clickOnView(solo.getView(R.id.logout_button));
		//Wait for activity: 'edu.osu.fitnesspandora.LoginActivity'
		assertTrue("edu.osu.fitnesspandora.LoginActivity is not found!", solo.waitForActivity(LoginActivity.class));
	}
}
