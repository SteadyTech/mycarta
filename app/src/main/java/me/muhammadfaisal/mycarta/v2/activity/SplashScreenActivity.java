package me.muhammadfaisal.mycarta.v2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextSwitcher;

import me.muhammadfaisal.mycarta.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextSwitcher textSwitcher;

    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Typeface poppins = ResourcesCompat.getFont(this, R.font.poppins_bold);

        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
                finish();
            }
        }, 2500L);
    }
}
