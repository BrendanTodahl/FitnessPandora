package edu.osu.fitnesspandora.test;

import edu.osu.fitnesspandora.LoginActivity;
import edu.osu.fitnesspandora.MainActivity;
import edu.osu.fitnesspandora.PedometerActivity;
import edu.osu.fitnesspandora.R;

import com.robotium.solo.*;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;


/**
 * Created by Brendan on 4/10/2016.
 * Tests Pedometer functionality
 *
 * ***Note: Test of accelerometer doesn't actually utilize the accelerometer since Robotium/JUnit
 * does not have that functionality. Instead, the textView is changed on the UiThread.
 */
public class PedometerTest extends ActivityInstrumentationTestCase2<LoginActivity> {
  	private Solo solo;
  	
  	public PedometerTest() {
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

	public void testB_NavigateToPedometer() {
		//Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
		//Click on Pedometer
		solo.clickOnView(solo.getView(R.id.pedemeter_button));
		//Wait for activity: 'edu.osu.fitnesspandora.PedometerActivity'
		assertTrue("edu.osu.fitnesspandora.PedometerActivity is not found!", solo.waitForActivity(PedometerActivity.class));
	}

	public void testC_RunPedometer() {
		//Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
		//Click on Pedometer
		solo.clickOnView(solo.getView(R.id.pedemeter_button));
		//Wait for activity: 'edu.osu.fitnesspandora.PedometerActivity'
		assertTrue("edu.osu.fitnesspandora.PedometerActivity is not found!", solo.waitForActivity(PedometerActivity.class));
		//Click on Start
		solo.clickOnView(solo.getView(R.id.startStopPedoButton));
		//Change text to emulate a 'step'
		final TextView mStepsTaken = (TextView) solo.getView(R.id.pedometerCounter);
		solo.getCurrentActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStepsTaken.setText("1");
			}
		});
		solo.waitForCondition(new Condition() {
			@Override
			public boolean isSatisfied() {
				return Integer.parseInt(mStepsTaken.getText().toString()) == 1;
			}
		}, 2000);
		assertTrue("edu.osu.fitnesspandora.PedometerActivity did not take any steps!", Integer.parseInt(mStepsTaken.getText().toString()) != 0);
		//Click on Stop
		solo.clickOnView(solo.getView(R.id.startStopPedoButton));
		//Click on Reset
		solo.clickOnView(solo.getView(R.id.resetPedoButton));
		solo.waitForCondition(new Condition() {
			@Override
			public boolean isSatisfied() {
				return Integer.parseInt(mStepsTaken.getText().toString()) == 0;
			}
		}, 2000);
		assertTrue("edu.osu.fitnesspandora.PedometerActivity did not reset step counter!", Integer.parseInt(mStepsTaken.getText().toString()) == 0);
	}

	public void testD_NavigateBackToMain() {
		//Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
		//Click on Pedometer
		solo.clickOnView(solo.getView(R.id.pedemeter_button));
		//Wait for activity: 'edu.osu.fitnesspandora.PedometerActivity'
		assertTrue("edu.osu.fitnesspandora.PedometerActivity is not found!", solo.waitForActivity(PedometerActivity.class));
		//Press menu back key
		solo.goBack();
		assertTrue("edu.osu.fitnesspandora.MainActivity is not found!", solo.waitForActivity(MainActivity.class));
	}

	public void testE_Logout() {
		//Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
		//Click on ImageView
		solo.clickOnView(solo.getView(R.id.logout_button));
		//Wait for activity: 'edu.osu.fitnesspandora.LoginActivity'
		assertTrue("edu.osu.fitnesspandora.LoginActivity is not found!", solo.waitForActivity(LoginActivity.class));
	}
}
