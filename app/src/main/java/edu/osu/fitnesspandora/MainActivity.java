package edu.osu.fitnesspandora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    // User's credentials from SharedPreferences
    private User mUser;

    private boolean mIsLoggingOut = false;

    // Initialize data from Firebase
    private ArrayList<Workout> mWorkouts;
    private ArrayList<Exercise> mExercises;
    private ArrayList<ExerciseLog> mExerciseLogs;

    // UI references
    private TextView mWelcomeMessage;
    private Button mWorkoutButton;
    private Button mPedometerButton;
    private Button mStatisticsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Lifecycle method onCreate() triggered");

        // Initialize the user's credentials
        mUser = User.get(this);

        // Initialize Firebase with the context
        Firebase.setAndroidContext(this);

        // Open Firebase
        Firebase firebaseUserRef = new Firebase(getString(R.string.firebase_url) + "users/" + mUser.getAuthUID());

        setContentView(R.layout.activity_main);

        // Custom actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);
        // Back button
        ImageView mBackButton = (ImageView) findViewById(R.id.back_button);
        mBackButton.setEnabled(false);
        mBackButton.setVisibility(View.INVISIBLE);
        // Logout button
        ImageView mLogoutButton = (ImageView) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);

        // Attach an listener to read the data at our posts reference
        firebaseUserRef.child("firstName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    mWelcomeMessage.setText("Hello " + snapshot.getValue().toString() + ".");
                } else {
                    Log.e("Firebase", snapshot.toString());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        // Workout button press
        mWorkoutButton = (Button)findViewById(R.id.workout_button);
        mWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workOutIntent = new Intent(MainActivity.this, WorkoutListActivity.class);
                startActivity(workOutIntent);
            }
        });

        mPedometerButton = (Button) findViewById(R.id.pedemeter_button);
        mPedometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PedometerActivity.class);
                startActivity(i);
            }
        });

        mStatisticsButton = (Button) findViewById(R.id.stats_button);
        mStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StatisticListActivity.class);
                startActivity(i);
            }
        });



        // Initialize data from Firebase
        mWorkouts = WorkoutLab.get().getWorkouts();
        mExercises = ExerciseLab.get().getExercises();
        mExerciseLogs = ExerciseLogLab.get(mUser.getAuthUID()).getExerciseLogs();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Lifecycle method onResume() triggered");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Lifecycle method onPause() triggered");
    }


    public void logoutUser(){
        // User wants logged out. So set the auth information to null/empty and go back to login activity
        mIsLoggingOut = true;

        mUser.logout(this);

        // Destroy this activity
        finish();
        // User authenticated, intent to start the main activity
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "Lifecycle method onStop() triggered");

        if(!mIsLoggingOut){
            mUser.saveUser(this);
        }

    }

}
