package edu.osu.fitnesspandora;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Brendan on 3/21/2016.
 */
public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "PedometerActivity";
    private static boolean pedoStarted = false;

    // Text views
    private TextView mPedometerCounterTextView;

    // Buttons
    private Button mStartStopButton;
    private Button mResetButton;

    // Sensor Manager
    private SensorManager mSensorManager;

    // Values to calculate number of steps
    private float mPreviousY;
    private float mCurrentY;
    private int mNumSteps;
    private int mDelay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        setTitle(R.string.pedometer);

        mPedometerCounterTextView = (TextView)findViewById(R.id.pedometerCounter);
        mPedometerCounterTextView.setText("0");

        mStartStopButton = (Button)findViewById(R.id.startStopPedoButton);
        mStartStopButton.setText(R.string.pedoStart);
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "mStartStopButton onClick() called");
                Toast toast;
                if (!pedoStarted) {
                    Log.i(TAG, "mStartStopButton change text to 'Stop'");
                    mStartStopButton.setText(R.string.pedoStop);
                    pedoStarted = true;
                    toast = Toast.makeText(getApplicationContext(), "Now tracking steps", Toast.LENGTH_SHORT);
                } else {
                    Log.i(TAG, "mStartStopButton change text to 'Start'");
                    mStartStopButton.setText(R.string.pedoStart);
                    pedoStarted = false;
                    toast = Toast.makeText(getApplicationContext(), "No longer tracking steps", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });
        mResetButton = (Button)findViewById(R.id.resetPedoButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "mResetButton onClick() called");
                if (pedoStarted) {
                    Toast toast = Toast.makeText(getApplicationContext(), "No longer tracking steps", Toast.LENGTH_SHORT);
                    toast.show();
                }
                mStartStopButton.setText(R.string.pedoStart);
                mNumSteps = 0;
                mPedometerCounterTextView.setText(String.valueOf(mNumSteps));
                pedoStarted = false;
            }
        });

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        mPreviousY = 0.0f;
        mCurrentY = 0.0f;
        mNumSteps = 0;
        mDelay = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float y = event.values[1];
        if (pedoStarted) {
            mCurrentY = y;
            if (mDelay >= 20) {
                if (Math.abs(mCurrentY - mPreviousY) > 3) {
                    mNumSteps++;
                    mPedometerCounterTextView.setText(String.valueOf(mNumSteps));
                    mDelay = 0;
                }
            } else {
                mDelay++;
            }
        }
        mPreviousY = y;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Needed for implements SensorEventListener
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() called");
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }
}
