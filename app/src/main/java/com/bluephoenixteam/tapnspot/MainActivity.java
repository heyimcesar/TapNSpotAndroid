package com.bluephoenixteam.tapnspot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, InterstitialAdListener {

    FacebookUser facebookUser;
    private SupportMapFragment mapFragment;
    private InterstitialAd interstitialAd;
    private final String INTERSTITIAL_AD_PLACEMENT_ID = "1217088094976599_1224872034198205";

    // INTERSITIAL AD LISTENER METHODS
    @Override
    public void onInterstitialDisplayed(Ad ad) {
        // Where relevant, use this function to pause your app's flow
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        // Use this function to resume your app's flow
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        // Ad failed to load
    }

    @Override
    public void onAdLoaded(Ad ad) {
        // Ad is loaded and ready to be displayed
        // You can now display the full screen ad using this code:
        interstitialAd.show();
    }

    @Override
    public void onAdClicked(Ad ad) {
        // Use this function as indication for a user's click on the ad.
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
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

    private void loadInterstitial() {
        // Added Phone Hash For Testing Ads
        AdSettings.addTestDevice("d3b498e395f8e6d3abf582e03daabcd2");
        interstitialAd = new InterstitialAd(this, INTERSTITIAL_AD_PLACEMENT_ID);
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
            mapFragment.getMapAsync(this);
        } else {
            // Show rationale and request permission.
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }

        loadInterstitial();
        setUserData();
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

    public void logOutBtnClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        LoginManager.getInstance().logOut();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    public void profileBtnClicked(View view){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void setUserData() {
        facebookUser = new FacebookUser(this, Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getLastName(), Profile.getCurrentProfile().getFirstName());
        retrieveFacebookUserData();
    }

    private void retrieveFacebookUserData() {
        // Bundle that will ask for specific info of current user.
        Bundle params = new Bundle();
        params.putString("fields", "email");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            // Gets Facebook Graph object, parses it to JSON and retrieves email from it.
                            JSONObject graphJsonObject = response.getJSONObject();
                            String fbUserEmail = graphJsonObject.getString("email");
                            facebookUser.setUserEmail(fbUserEmail);

                            // Only when email has been downloaded, pass to profile pic downloader
                            retrieveUserProfilePic();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void retrieveUserProfilePic() {
        facebookUser.testGetFacebookUserStuff();
        Bundle params = new Bundle();
        params.putString("type", "large");
        params.putBoolean("redirect", false);
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + facebookUser.getUserId() + "/picture", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject data = response.getJSONObject();

                            String profilePicUrl = data.getJSONObject("data").getString("url");

                            ImageDownloader task = new ImageDownloader();
                            Bitmap profileImg;

                            profileImg = task.execute(profilePicUrl).get();
                            facebookUser.setUserProfilePic(profileImg);

                            // Only when picture has been downloaded, set it up in the imageview
                            setInfoViews();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng arturoHome = new LatLng(20.746118, -103.437228);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(arturoHome, 16));
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).position(arturoHome).draggable(true));
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getApplicationContext(),marker.getPosition().toString(),Toast.LENGTH_SHORT).show();
            }
        });
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
