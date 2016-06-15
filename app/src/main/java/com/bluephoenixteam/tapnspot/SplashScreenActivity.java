package com.bluephoenixteam.tapnspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class SplashScreenActivity extends Activity {
    private int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook SDK.
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash_screen);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                // Login check (condition: !userIsLogged)
                Intent loginIntent;
                if(Profile.getCurrentProfile() == null){
                    loginIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                }else{
                    loginIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                }
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SplashScreenActivity.this.startActivity(loginIntent);
                SplashScreenActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
