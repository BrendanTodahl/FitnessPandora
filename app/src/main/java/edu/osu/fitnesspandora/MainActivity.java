package edu.osu.fitnesspandora;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // User's credentials from SharedPreferences
    private String mAuthToken;
    private String mAuthUID;

    private boolean mIsLoggingOut = false;

    // UI references
    private TextView mWelcomeMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore preferences
        SharedPreferences userAuthData = getSharedPreferences("USER_AUTH_DATA", 0);
        mAuthToken = userAuthData.getString("authToken", "");
        mAuthUID = userAuthData.getString("authUID", "");

        // Initialize Firebase with the context
        Firebase.setAndroidContext(this);

        // Open Firebase
        Log.i("Firebase", "Shared Pref" + userAuthData.getString("authUID", ""));
        Log.i("Firebase", "Extracting UID: " + mAuthUID);
        Firebase firebaseUserRef = new Firebase("https://fitnesspandora.firebaseio.com/users/" + mAuthUID + "/");


        setContentView(R.layout.activity_main);

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




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public void logoutUser(MenuItem item){
        // User wants logged out. So set the auth information to null/empty and go back to login activity
        mIsLoggingOut = true;

        mAuthToken = "";
        mAuthUID = "";

        SharedPreferences userAuthData = getSharedPreferences("USER_AUTH_DATA", 0);
        SharedPreferences.Editor editor = userAuthData.edit();
        editor.remove("authToken");
        editor.remove("authUID");
        editor.commit();

        // Destroy this activity
        finish();
        // User authenticated, intent to start the main activity
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }


    @Override
    protected void onStop(){
        super.onStop();

        if(!mIsLoggingOut){
            // Before closing the application, ensure the user's auth data is save or deleted
            SharedPreferences userAuthData = getSharedPreferences("USER_AUTH_DATA", 0);
            SharedPreferences.Editor editor = userAuthData.edit();
            editor.putString("authToken", mAuthToken);
            editor.putString("authUID", mAuthUID);
            editor.commit();
        }

    }

}