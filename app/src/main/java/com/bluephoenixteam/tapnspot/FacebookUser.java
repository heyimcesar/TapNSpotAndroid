package com.bluephoenixteam.tapnspot;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by BluePhoenixTeam on 6/15/2016.
 */
public class FacebookUser
{
    SharedPreferences sharedPreferences;
//    private String userId;
//    private String userLastName;
//    private String userFirstName;
//    private String userEmail;
    private Bitmap userProfilePic;

    public FacebookUser(Context context , String userId, String userLastName, String userFirstName)
    {
        sharedPreferences = context.getSharedPreferences("com.bluephoenixteam.tapnspot", Context.MODE_PRIVATE);
        checkUserInMemory(userId, userLastName, userFirstName);
        //this.userId = userId;
        //this.userLastName = userLastName;
        //this.userFirstName = userFirstName;
    }

    public FacebookUser(Context context) {
        sharedPreferences = context.getSharedPreferences("com.bluephoenixteam.tapnspot", Context.MODE_PRIVATE);
    }

    public void setUserId(String userId)
    {
        if (!sharedPreferences.getString("UserId", "").equals(userId) || sharedPreferences.getString("UserId", "").equals("")) {
            sharedPreferences.edit().putString("UserId", userId).apply();
        }
        //this.userId = userId;
    }

    public void setUserLastName(String userLastName)
    {
        if (!sharedPreferences.getString("UserLastName", "").equals(userLastName) || sharedPreferences.getString("UserLastName", "").equals("")) {
            sharedPreferences.edit().putString("UserLastName", userLastName).apply();
        }
       // this.userLastName = userLastName;
    }

    public void setUserFirstName(String userFirstName)
    {
        if (!sharedPreferences.getString("UserFirstName", "").equals(userFirstName) || sharedPreferences.getString("UserFirstName", "").equals("")) {
            sharedPreferences.edit().putString("UserFirstName", userFirstName).apply();
        }
       // this.userFirstName = userFirstName;
    }

    public void setUserEmail(String userEmail)
    {
        if (!sharedPreferences.getString("UserEmail", "").equals(userEmail) || sharedPreferences.getString("UserEmail", "").equals("")) {
            sharedPreferences.edit().putString("UserEmail", userEmail).apply();
        }
        //this.userEmail = userEmail;
    }

    public void setUserProfilePic(Bitmap userProfilePic)
    {
        this.userProfilePic = userProfilePic;
    }

    public String getUserId()
    {
        return sharedPreferences.getString("UserId","");
       // return userId;
    }

    public String getUserLastName()
    {
        return sharedPreferences.getString("UserLastName","");
        //return userLastName;
    }

    public String getUserFirstName()
    {
        return sharedPreferences.getString("UserFirstName","");
        //return userFirstName;
    }

    public String getUserEmail()
    {
        return sharedPreferences.getString("UserEmail","");
        //return userEmail;
    }

    public Bitmap getUserProfilePic()
    {
        return userProfilePic;
    }

    private void checkUserInMemory(String userId, String userLastName, String userFirstName) {
        if (!sharedPreferences.getString("UserId", "").equals(userId) || sharedPreferences.getString("UserId", "").equals("")) {
            sharedPreferences.edit().putString("UserId", userId).apply();
        }
        if (!sharedPreferences.getString("UserLastName", "").equals(userLastName) || sharedPreferences.getString("UserLastName", "").equals("")) {
            sharedPreferences.edit().putString("UserLastName", userLastName).apply();
        }
        if (!sharedPreferences.getString("UserFirstName", "").equals(userFirstName) || sharedPreferences.getString("UserFirstName", "").equals("")) {
            sharedPreferences.edit().putString("UserFirstName", userFirstName).apply();
        }
    }

    public void testGetFacebookUserStuff()
    {
        Log.i("SHAREDPREF", sharedPreferences.getString("UserId", "") + "-"+sharedPreferences.getString("UserLastName", "")+"-"+sharedPreferences.getString("UserFirstName", "")+"-"+sharedPreferences.getString("UserEmail", ""));
    }
}
