package edu.osu.fitnesspandora;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    protected void onStop(){
        super.onStop();

        // Before closing the application, ensure the user's auth data is save or deleted
        SharedPreferences userAuthData = getSharedPreferences("USER_AUTH_DATA", 0);
        SharedPreferences.Editor editor = userAuthData.edit();
        editor.putString("authToken", mAuthToken);
        editor.putString("authUID", mAuthUID);
        editor.commit();
    }

}
