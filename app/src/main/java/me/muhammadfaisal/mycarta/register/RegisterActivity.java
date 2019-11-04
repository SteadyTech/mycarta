package me.muhammadfaisal.mycarta.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.HomeActivity;
import me.muhammadfaisal.mycarta.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    EditText inputEmail, inputPassword;
    public FirebaseAuth auth;
    ImageView imageBack;
    TextView textSignIn;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        initWidget();

        setupTransitionAnimation();
    }

    private void setupTransitionAnimation() {
        getWindow().setEnterTransition(new android.transition.Fade().setDuration(1000));
    }

    private void initWidget() {

        btnRegister = findViewById(R.id.buttonRegister);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        imageBack = findViewById(R.id.imageBack);

        textSignIn = findViewById(R.id.textSignIn);

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
        imageBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister){
            methodRegister();
        }else if (view == textSignIn){
            methodLogin();
        }else{
            finish();
        }
    }

    private void methodLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void methodRegister() {
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();


        if (email.isEmpty()) {
            inputEmail.setError("Email can't be empty");
        } else if (password.isEmpty()) {
            inputPassword.setError("Password can't be empty");
        } else if (password.length() < 6) {
            inputPassword.setError("Password too short");
        } else {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#445EE9"));
            dialog.setTitle("Loading");
            dialog.show();

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        dialog.cancel();
                        Toast.makeText(RegisterActivity.this, "Failed To Login ! Because" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.cancel();

                        user = auth.getCurrentUser();
                        Bundle b = new Bundle();
                        b.putString("email", email);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
                        finish();
                    }
                }
            });
        }
    }
}
