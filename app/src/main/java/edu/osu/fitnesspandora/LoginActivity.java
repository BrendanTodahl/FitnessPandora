package edu.osu.fitnesspandora;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private String TAG = this.getClass().getSimpleName();

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // The AsyncTask that attempts to contact Firebase to authenticate user.
    private UserLoginTask mAuthTask = null;

    // Determines if the user is logging in or registering a new account
    private boolean mUserIsNew = false;

    // Firebase variables
    private boolean mUserRegistered;
    private boolean mUserAuthenticated;
    private int mFirebaseError;
    private AuthData mAuthData;

    // User's credentials from SharedPreferences
    private User mUser;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private View mProgressView;
    private View mLoginFormView;
    private View mRegisterFormView;
    private Button mEmailSignInButton;
    private Button mEmailRegisterButton;

    // Used to store the new user's initial data into Firebase
    public class newUser {
        private String firstName;
        private String lastName;
        private String email;
        public newUser(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public String getEmail() {
            return email;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Lifecycle method onCreate() triggered");

        // Initialize Firebase with the context
        Firebase.setAndroidContext(this);

        mUser = User.get(this);

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mFirstNameView = (EditText) findViewById(R.id.first_name);

        mLastNameView = (EditText) findViewById(R.id.last_name);
        mLastNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // If the user already indicated that they were new, attempt the login
                if(mUserIsNew){
                    attemptLogin();
                }else{
                    // Otherwise change the view to registration
                    userIsNew(true);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegisterFormView = findViewById(R.id.email_register_form);

        // If the saved auth token isn't empty string,
        if(mUser.getAuthToken().length() > 0){
            // Prompt the loading screen
            showProgress(true);

            mUserAuthenticated = true;
            // Attempt to log in user first
            Firebase firebaseUserRef = new Firebase(getString(R.string.firebase_url) + "");
            firebaseUserRef.authWithCustomToken(mUser.getAuthToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // Token is good. User's information must already be stored. Proceed to main activity.
                    Log.i("Firebase", "User already has valid token. Logging in.");
                    // Destroy the login activity
                    finish();
                    // User authenticated, intent to start the main activity
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // User's token is invalid. Allow user to login or register
                    mUserAuthenticated = false;
                    // Show the registration page
                    showProgress(false);
                }
            });


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Lifecycle method onDestroy() triggered");
    }


    // If userIsNew is true, hides the login button and shows the registration information.
    // If userIsNew is false, reverses the effect.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void userIsNew(final boolean userIsNew) {
        // If the global flag is not align with the new value, changes need be made
        if(mUserIsNew != userIsNew){
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mEmailSignInButton.setVisibility(userIsNew ? View.GONE : View.VISIBLE);
                mEmailSignInButton.animate().setDuration(shortAnimTime).alpha(
                        userIsNew ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mEmailSignInButton.setVisibility(userIsNew ? View.GONE : View.VISIBLE);
                    }
                });

                mRegisterFormView.setVisibility(userIsNew ? View.VISIBLE : View.GONE);
                mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                        userIsNew ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRegisterFormView.setVisibility(userIsNew ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mRegisterFormView.setVisibility(userIsNew ? View.VISIBLE : View.GONE);
                mEmailSignInButton.setVisibility(userIsNew ? View.GONE : View.VISIBLE);
            }

            // Update the global flag to indicate whether the user is new or not
            mUserIsNew = userIsNew;
        }

    }


    // For the AutoCompleteTextView layout. Email input.
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    // For the AutoCompleteTextView layout. Email input.
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    // For the AutoCompleteTextView layout. Email input.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    // Check all of the information the user submitted before attempting a remote login
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        boolean cancel = false;
        View focusView = null;

        // Store the user information so we can pass it to the Firebase
        Map<String, String> userInformation = new HashMap<String, String>();

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);

        // Store values at the time of the registration attempt.
        String firstName = mFirstNameView.getText().toString();
        userInformation.put("firstName", firstName);
        String lastName = mLastNameView.getText().toString();
        userInformation.put("lastName", lastName);

        // Check for whether the user is new or not
        if(mUserIsNew){

            // Check for valid last name
            if(TextUtils.isEmpty(lastName) || !isNameValid(lastName)){
                mLastNameView.setError(getString(R.string.error_invalid_name));
                focusView = mLastNameView;
                cancel = true;
            }

            // Check for valid first name
            if(TextUtils.isEmpty(firstName) || !isNameValid(firstName)){
                mFirstNameView.setError(getString(R.string.error_invalid_name));
                focusView = mFirstNameView;
                cancel = true;
            }
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        userInformation.put("email", email);
        String password = mPasswordView.getText().toString();
        userInformation.put("password", password);

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            // No errors in the user's initial data. Attempt login with the database.
            mAuthTask = new UserLoginTask(userInformation, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        // Ensure the name has only alphabetical characters
        return name.matches("[a-zA-Z]+");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    // If show is true, hides the login information and shows the loading circle/bar.
    // If show is false, reverses the effect.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    // Used to load emails for the autocomplete.
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    // Used to load emails for the autocomplete.
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    // Used to load emails for the autocomplete.
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    // Used to load emails for the autocomplete.
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    // Used to load emails for the autocomplete.
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    // Contacts FIREBASE and attempts to login or register a user.
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mFirstName;
        private final String mLastName;
        private final Context mContext;

        UserLoginTask(Map<String, String> userInformation, Context context) {
            mEmail = userInformation.remove("email");
            mPassword = userInformation.remove("password");
            mFirstName = userInformation.remove("firstName");
            mLastName = userInformation.remove("lastName");
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // Open Firebase
            Firebase firebaseRef = new Firebase(getString(R.string.firebase_url) + "");

            // If the user is new, they need to register first
            if(mUserIsNew){
                // Attempt to register a new user
                firebaseRef.createUser(mEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Log.i("Firebase User Created", result.get("uid").toString());

                        // Initialize the user's data in Firebase
                        Firebase newUserRef = new Firebase(getString(R.string.firebase_url) + "users/" + result.get("uid"));
                        newUser theNewUser = new newUser(mFirstName, mLastName, mEmail);
                        newUserRef.setValue(theNewUser, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    // TODO User could be registered, but their data could not be initialized error?
                                    Log.e("Firebase New Data Error", firebaseError.getDetails());
                                    mFirebaseError = firebaseError.getCode();
                                    mUserRegistered = false;
                                } else {
                                    Log.i("Firebase", "New user's data successfully initialized.");
                                    mUserRegistered = true;
                                }
                            }
                        });
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        Log.e("Firebase Register Error", firebaseError.getMessage() + firebaseError.getDetails());
                        Log.e("Firebase Register Error", Integer.toString(firebaseError.getCode()));
                        mFirebaseError = firebaseError.getCode();
                        mUserRegistered = false;
                    }
                });

                // Wait for the registration results
                try {
                    // Delay to simulate network access.
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    return false;
                }
            }

            // If the user is not new, or the user registered successfully,
            if(!mUserIsNew || mUserRegistered){

                // Attempt to log user in
                firebaseRef.authWithPassword(mEmail, mPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.i("Firebase", "User logged in successfully.");
                        // Set the global auth token
                        mUser.setAuthToken(authData.getToken());
                        mUser.setAuthUID(authData.getUid());
                        mUserAuthenticated = true;
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        Log.e("Firebase Login Error", firebaseError.getMessage() + firebaseError.getDetails());
                        Log.e("Firebase Login Error", Integer.toString(firebaseError.getCode()));
                        mFirebaseError = firebaseError.getCode();
                        mUserAuthenticated = false;
                    }
                });

                // TODO - Make the wait on the network a little better/faster
                // Wait for the login results
                try {
                    // Delay to simulate network access.
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    return false;
                }

            }

            // Doesn't matter what we return here. Won't use it.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            // If doInBackground returns true,
            if (mUserAuthenticated) {

                mUser.saveUser(this.mContext);

                Log.i("Firebase", "Saving UID:" + mUser.getAuthUID());

                // Destroy the login activity
                finish();

                // User authenticated, intent to start the main activity
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);

            } else {
                // Figure out the error during the login
                if(mFirebaseError == FirebaseError.INVALID_AUTH_ARGUMENTS || mFirebaseError == FirebaseError.INVALID_CREDENTIALS || mFirebaseError == FirebaseError.INVALID_EMAIL || mFirebaseError == FirebaseError.EMAIL_TAKEN){
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();
                }else if(mFirebaseError == FirebaseError.INVALID_PASSWORD){
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }else{
                    mEmailView.setError(getString(R.string.error_unknown_error));
                }

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

