package edu.osu.fitnesspandora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpperBodyActivity extends AppCompatActivity {

    private TextView mExerciseNameTextView;
    private Button mLikeButton;
    private Button mDislikeButton;
    private Button mBackButton;
    private static final String TAG = "upperBodyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Lifecycle method onCreate() triggered");
        setContentView(R.layout.activity_upper_body);

        mLikeButton = (Button) findViewById(R.id.like_button);
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpperBodyActivity.this, "Workout Liked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Like Button Pressed");
            }

        });

        mDislikeButton = (Button) findViewById(R.id.dislike_button);
        mDislikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(UpperBodyActivity.this, "Workout Disliked!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Dislike Button Pressed");
            }
        });

        mBackButton = (Button) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent back = new Intent(UpperBodyActivity.this, WorkoutListActivity.class);
                startActivity(back);
                Log.d(TAG, "Back Button Pressed");
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}