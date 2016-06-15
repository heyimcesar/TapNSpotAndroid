package com.bluephoenixteam.tapnspot;

import android.graphics.Bitmap;

/**
 * Created by BluePhoenixTeam on 6/15/2016.
 */
public class FacebookUser
{
    private String userId;
    private String userLastName;
    private String userFirstName;
    private String userEmail;
    private Bitmap userProfilePic;

    public FacebookUser(String userId, String userLastName, String userFirstName)
    {
        this.userId = userId;
        this.userLastName = userLastName;
        this.userFirstName = userFirstName;
    }

    public FacebookUser() {}

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserLastName(String userLastName)
    {
        this.userLastName = userLastName;
    }

    public void setUserFirstName(String userFirstName)
    {
        this.userFirstName = userFirstName;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public void setUserProfilePic(Bitmap userProfilePic)
    {
        this.userProfilePic = userProfilePic;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserLastName()
    {
        return userLastName;
    }

    public String getUserFirstName()
    {
        return userFirstName;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public Bitmap getUserProfilePic()
    {
        return userProfilePic;
    }
}
