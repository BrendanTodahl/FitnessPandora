package edu.osu.fitnesspandora;

import android.content.Context;
import android.content.SharedPreferences;

// SINGLETON to store the users data
public class User {

    private String mAuthToken;
    private String mAuthUID;

    private static User sUser = null;

    private User(Context c){
        // Restore preferences
        SharedPreferences userAuthData = c.getSharedPreferences("USER_AUTH_DATA", 0);
        mAuthToken = userAuthData.getString("authToken", "");
        mAuthUID = userAuthData.getString("authUID", "");
    }

    public static User get(Context c){
        if(sUser == null){
            sUser = new User(c);
        }
        return sUser;
    }

    public String getAuthToken(){
        return mAuthToken;
    }
    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public String getAuthUID(){
        return mAuthUID;
    }
    public void setAuthUID(String authUID) {
        mAuthUID = authUID;
    }

    public void saveUser(Context c){
        // Before closing the application, ensure the user's auth data is save or deleted
        SharedPreferences userAuthData = c.getSharedPreferences("USER_AUTH_DATA", 0);
        SharedPreferences.Editor editor = userAuthData.edit();
        editor.putString("authToken", mAuthToken);
        editor.putString("authUID", mAuthUID);
        editor.commit();
    }

    public void logout(Context c){
        mAuthToken = "";
        mAuthUID = "";
        sUser = null;

        SharedPreferences userAuthData = c.getSharedPreferences("USER_AUTH_DATA", 0);
        SharedPreferences.Editor editor = userAuthData.edit();
        editor.remove("authToken");
        editor.remove("authUID");
        editor.commit();

    }
}
