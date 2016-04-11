package edu.osu.fitnesspandora.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import edu.osu.fitnesspandora.LoginActivity;
import edu.osu.fitnesspandora.MainActivity;
import edu.osu.fitnesspandora.R;
import edu.osu.fitnesspandora.WorkoutActivity;
import edu.osu.fitnesspandora.WorkoutListActivity;

/**
 * Created by Brendan on 4/11/2016.
 * Tests Workout activity functionality
 */
public class WorkoutTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public WorkoutTest() {
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

    public void testB_NavigateToWorkout() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
    }

    public void testC_SelectArms() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Arms
        solo.clickOnMenuItem("Arms");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testD_SelectCardio() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Cardio
        solo.clickOnMenuItem("Cardio");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testE_SelectCore() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Core
        solo.clickOnMenuItem("Core");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testF_SelectFullBody() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Full Body
        solo.clickOnMenuItem("Full Body");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testG_SelectLowerBody() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Lower Body
        solo.clickOnMenuItem("Lower Body");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testH_SelectUpperBody() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Upper Body
        solo.clickOnMenuItem("Upper Body");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testI_InstructionalVideoArms() throws InterruptedException {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Arms
        solo.clickOnMenuItem("Arms");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
        //Click on Instructional Video
        solo.clickOnView(solo.getView(R.id.exercise_instructions));
        //Wait for URL to load video for 8 seconds
        Thread.sleep(8000);
        //Go Back
        solo.goBack();
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testJ_EnterRepsAndWeight() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Arms
        solo.clickOnMenuItem("Arms");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
        //Enter the text: '10'
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editReps));
        solo.enterText((android.widget.EditText) solo.getView(R.id.editReps), "10");
        //Enter the text: '45'
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.editWeight));
        solo.enterText((android.widget.EditText) solo.getView(R.id.editWeight), "45");
        //'Like' the workout
        solo.clickOnView(solo.getView(R.id.button_like));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testK_SkipWorkout() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on Workout
        solo.clickOnView(solo.getView(R.id.workout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutListActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutListActivity is not found!", solo.waitForActivity(WorkoutListActivity.class));
        //Click on Arms
        solo.clickOnMenuItem("Arms");
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
        //'Skip' the workout
        solo.clickOnView(solo.getView(R.id.button_skip));
        //Wait for activity: 'edu.osu.fitnesspandora.WorkoutActivity'
        assertTrue("edu.osu.fitnesspandora.WorkoutActivity is not found!", solo.waitForActivity(WorkoutActivity.class));
    }

    public void testL_Logout() {
        //Wait for activity: 'edu.osu.fitnesspandora.MainActivity'
        solo.waitForActivity(MainActivity.class, 2000);
        //Click on ImageView
        solo.clickOnView(solo.getView(R.id.logout_button));
        //Wait for activity: 'edu.osu.fitnesspandora.LoginActivity'
        assertTrue("edu.osu.fitnesspandora.LoginActivity is not found!", solo.waitForActivity(LoginActivity.class));
    }
}
