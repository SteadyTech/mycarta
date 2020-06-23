package me.muhammadfaisal.mycarta.v1.forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.login.LoginActivity;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputEmail;
    ImageView imageBack;
    Button btnReset;
    TextView textSignIn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputEmail = findViewById(R.id.inputEmail);
        imageBack = findViewById(R.id.imageBack);
        btnReset = findViewById(R.id.buttonReset);
        textSignIn = findViewById(R.id.textSignIn);

        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnReset){
            methodReset();
        }else if (view == imageBack){
            finish();
        }else if (view == textSignIn){
            methodLogin();
        }
    }

    private void methodLogin() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class),ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void methodReset() {
        final String email = inputEmail.getText().toString().trim();
        if (email.isEmpty()){
            inputEmail.setError("Email can't be empty");
        }else{
            final SweetAlertDialog dialogLoad = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            dialogLoad.setContentText("Loading");
            dialogLoad.setCancelable(false);
            dialogLoad.show();

            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        dialogLoad.cancel();
                        SweetAlertDialog dialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitle("Success");
                        dialog.setContentText("We have sent you instructions to reset your password!");
                        dialog.show();
                        dialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(ForgotPasswordActivity.this).toBundle());
                            }
                        });
                    }else{
                        dialogLoad.cancel();
                        SweetAlertDialog dialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.ERROR_TYPE);
                        dialog.setTitle("Error");
                        dialog.setContentText("Failed to send reset password!");
                        dialog.show();
                        dialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    }
                }
            });
        }
    }
}
