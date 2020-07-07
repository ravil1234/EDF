package com.example.edf;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
public class SplashScreenActivity extends Activity
{
    Handler handler;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                    Intent intent=new Intent(SplashScreenActivity.this, IntroScreen.class);
                    startActivity(intent);
                finish();
            }
        },2000);
    }

}
