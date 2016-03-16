package edu.osu.fitnesspandora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WorkoutActivityOld extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();

    // UI elements
    private Button mUpperBodyButton;
    private Button mLowerBodyButton;
    private Button mMatButton;
    private Button mCardioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Lifecycle method onCreate() triggered");
        setContentView(R.layout.activity_workout);

        // Get references to UI Buttons
        mUpperBodyButton = (Button)findViewById(R.id.upper_body_button);
        mUpperBodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upperIntent = new Intent(WorkoutActivityOld.this, UpperBodyActivity.class);
                startActivity(upperIntent);
            }
        });
        mLowerBodyButton = (Button)findViewById(R.id.lower_body_button);
        mLowerBodyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent lowerIntent = new Intent(WorkoutActivityOld.this,LowerBodyActivity.class);
                startActivity(lowerIntent);
            }
        });
        mMatButton = (Button)findViewById(R.id.mats_button);
        mMatButton.setOnClickListener(this);
        mCardioButton = (Button)findViewById(R.id.cardio_button);
        mCardioButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // User has selected the type of workout they wish to attempt
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upper_body_button:
                Log.d(TAG, "Upper Body button pressed");
                break;
            case R.id.lower_body_button:
                Log.d(TAG, "Lower Body button pressed");
                break;
            case R.id.mats_button:
                Log.d(TAG, "Mats button pressed");
                break;
            case R.id.cardio_button:
                Log.d(TAG, "Cardio button pressed");
                break;
            default:
                break;
        }
    }

    public void logoutUser(MenuItem item){
        // Handle user logging out
        Log.i(TAG, "Logout button pushed from menu");
    }
}
