package com.bluephoenixteam.tapnspot;

/**
 * Created by BluePhoenixTeam on 6/15/2016.
 */
public class FacebookUser
{
    private String userId;
    private String userLastName;
    private String userFirstName;
    private String userEmail;

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
}
