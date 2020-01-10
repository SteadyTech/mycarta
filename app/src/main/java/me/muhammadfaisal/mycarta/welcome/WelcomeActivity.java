package me.muhammadfaisal.mycarta.welcome;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.TermsOfService;
import me.muhammadfaisal.mycarta.login.LoginActivity;
import me.muhammadfaisal.mycarta.pin.PinBottomSheetFragment;
import me.muhammadfaisal.mycarta.register.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSignIn;
    TextView textCreateAccount, textPrivacyPolicy;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
    }
    @SuppressLint("ResourceType")

    private void initView() {
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textCreateAccount = findViewById(R.id.textCreateAccount);
        textPrivacyPolicy = findViewById(R.id.textPrivacyPolicy);

        buttonSignIn.setOnClickListener(this);
        textCreateAccount.setOnClickListener(this);
        textPrivacyPolicy.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if (sharedPreferences.contains("userid")){
            BottomSheetDialogFragment pinBottom = new PinBottomSheetFragment();
            pinBottom.setCancelable(false);
            pinBottom.show(getSupportFragmentManager(), "PinLogin");
        }

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            methodLogin();
        }
        if (view == textCreateAccount) {
            methodCreateAccount();
        }
        if (view == textPrivacyPolicy){
            methodTermsOfService();
        }
    }

    private void methodTermsOfService() {
        BottomSheetDialogFragment dialogFragment = new TermsOfService();
        dialogFragment.show(getSupportFragmentManager(), "Terms Of Service");
    }

    private void methodCreateAccount() {
        startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void methodLogin() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
