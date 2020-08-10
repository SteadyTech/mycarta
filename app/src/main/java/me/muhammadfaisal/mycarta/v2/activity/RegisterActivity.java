package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.common.StringUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;
import me.muhammadfaisal.mycarta.v2.helper.Constant;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    EditText inputEmail, inputPassword, inputName;
    public FirebaseAuth auth;
    ImageView imageBack;
    DatabaseReference reference;
    TextView textSignIn;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_register);

        this.initWidget();

        this.setupTransitionAnimation();
    }

    private void setupTransitionAnimation() {
        getWindow().setEnterTransition(new android.transition.Fade().setDuration(1000));
    }

    private void initWidget() {

        btnRegister = findViewById(R.id.buttonRegister);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputName =  findViewById(R.id.inputName);
        imageBack = findViewById(R.id.imageBack);
        reference = FirebaseDatabase.getInstance().getReference();

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
        final String name = inputName.getText().toString().trim();


        if (email.isEmpty()) {
            inputEmail.setError("Email can't be empty");
        } else if (password.isEmpty()) {
            inputPassword.setError("Password can't be empty");
        } else if (password.length() < 6) {
            inputPassword.setError("Password too short");
        } else {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#445EE9"));
            dialog.setContentText("Loading");
            dialog.setCancelable(false);
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
                        createUser(user.getUid() , name , email);
                    }
                }
            });
        }
    }

    private void createUser(String uid,String name, String email) {
        UserModel users = new UserModel(uid, null, email, name);

        reference.child("users").child(uid).setValue(users);

        Bundle b = new Bundle();
        b.putString("email", email);
        startActivity(new Intent(RegisterActivity.this, WelcomeNewUserActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        finish();
    }
}
