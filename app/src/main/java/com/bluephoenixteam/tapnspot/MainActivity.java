package com.bluephoenixteam.tapnspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    FacebookUser facebookUser;
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized())
        {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        setContentView(R.layout.activity_main);
        setUserData();
    }

    public void logOutBtnClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        LoginManager.getInstance().logOut();
        startActivity(intent);
        finish();
    }

    private void setUserData()
    {
        facebookUser = new FacebookUser(Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getLastName(), Profile.getCurrentProfile().getFirstName());
        retrieveFacebookUserData();
    }

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

                            // Only when email has been downloaded, pass to profile pic downloader
                            retrieveUserProfilePic();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void retrieveUserProfilePic()
    {
        Bundle params = new Bundle();
        params.putString("type", "large");
        params.putBoolean("redirect", false);
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+facebookUser.getUserId()+"/picture", params, HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    public void onCompleted(GraphResponse response)
                    {
                        try
                        {
                            JSONObject data = response.getJSONObject();

                            String profilePicUrl = data.getJSONObject("data").getString("url");

                            ImageDownloader task = new ImageDownloader();
                            Bitmap profileImg;

                            profileImg = task.execute(profilePicUrl).get();
                            facebookUser.setUserProfilePic(profileImg);

                            // Only when picture has been downloaded, set it up in the imageview
                            setInfoViews();

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void setInfoViews()
    {
        TextView userName = (TextView) findViewById(R.id.textview_user_name);
        TextView userId = (TextView) findViewById(R.id.textview_user_id);
        TextView userEmail = (TextView) findViewById(R.id.textview_user_email);
        ImageView userProfilePic = (ImageView) findViewById(R.id.imageview_user_pic);

        String userFullName = facebookUser.getUserFirstName() + " " + facebookUser.getUserLastName();
        userName.setText(userFullName);
        userId.setText(facebookUser.getUserId());
        userEmail.setText(facebookUser.getUserEmail());
        userProfilePic.setImageBitmap(facebookUser.getUserProfilePic());
    }
}
