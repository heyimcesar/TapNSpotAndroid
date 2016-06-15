package com.bluephoenixteam.tapnspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private FacebookUser facebookUser;

    // Call to Facebook API that will get user's Profile.
    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>()
    {
        // Tracker that will know when the user successfully logged in.
        private ProfileTracker mProfileTracker;

        // If log in was successful
        @Override
        public void onSuccess(LoginResult loginResult)
        {
            // Profile.getCurrentProfile() might be null if not logged in yet (internet/time problems may occur).
            if (Profile.getCurrentProfile() == null)
            {
                mProfileTracker = new ProfileTracker()
                {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile)
                    {
                        facebookUser.setUserId(newProfile.getId());
                        facebookUser.setUserFirstName(newProfile.getFirstName());
                        facebookUser.setUserLastName(newProfile.getLastName());
                        mProfileTracker.stopTracking();
                    }
                };
                // no need to call startTracking() on mProfileTracker
                // because it is called by its constructor, internally.
            }
            else
            {
                Profile profile = Profile.getCurrentProfile();

                facebookUser.setUserId(profile.getId());
                facebookUser.setUserFirstName(profile.getFirstName());
                facebookUser.setUserLastName(profile.getLastName());
            }
            retrieveFacebookUserData();
        }

        @Override
        public void onCancel() {
            Log.v("facebook - onCancel", "cancelled");
        }

        @Override
        public void onError(FacebookException e) {
            Log.v("facebook - onError", e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Creates callbackManager to handle Facebook callbacks.
        callbackManager = CallbackManager.Factory.create();

        // Creates current FacebookUser.
        facebookUser = new FacebookUser();

        // Sets up login request permissions for the user.
        LoginButton facebookLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginBtn.setReadPermissions("email");
        facebookLoginBtn.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handles Facebook callbackManager responses
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    // Method to retrieve Facebook User's email (can retrieve other data as well).
    private void retrieveFacebookUserData()
    {
        // Bundle that will ask for specific info of current user.
        Bundle params= new Bundle();
        params.putString("fields", "email");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response)
                    {
                        try
                        {
                            // Gets Facebook Graph object, parses it to JSON and retrieves email from it.
                            JSONObject graphJsonObject = response.getJSONObject();
                            String fbUserEmail = graphJsonObject.getString("email");
                            facebookUser.setUserEmail(fbUserEmail);

                            // Only pass to next activity when email has been retrieved and saved.
                            loginComplete();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void loginComplete() {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(mainActivityIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}

