package me.muhammadfaisal.mycarta.home.fragment.account.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.welcome.WelcomeActivity;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseUser user;

    MaterialEditText oldPassword,newPassword, confirmNewPassword;
    Button buttonChangePassword;

    ImageView imageBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().hide();

        oldPassword = findViewById(R.id.inputOldPassword);
        newPassword = findViewById(R.id.inputNewPassword);
        confirmNewPassword = findViewById(R.id.inputConfirmNewPassword);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);

        imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        buttonChangePassword.setOnClickListener(this);

        confirmNewPassword.setVisibility(View.GONE);
        oldPassword.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChangePassword){
            methodChangePassword();
        }
    }

    private void methodChangePassword() {
        if (newPassword.length() < 6){
            newPassword.setError("Password too short!");
        }else{
            final SweetAlertDialog dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            dialogProgress.setContentText("Loading");
            dialogProgress.show();

            final SweetAlertDialog dialogFailure = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

            final SweetAlertDialog dialogSuccess = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

            final FirebaseUser user = auth.getCurrentUser();
            final String stringNewPassword = newPassword.getText().toString();

            user.updatePassword(stringNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        dialogProgress.cancel();

                        dialogFailure.setContentText(task.getException().toString());
                        dialogFailure.show();
                    }else{
                        dialogProgress.cancel();
                        dialogSuccess.setContentText("Success Change Password!");
                        dialogSuccess.show();
                        finish();
                    }
                }
            });
        }
    }
}
