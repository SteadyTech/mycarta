package me.muhammadfaisal.mycarta.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextSwitcher;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.welcome.WelcomeActivity;

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
