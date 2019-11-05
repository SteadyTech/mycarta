package me.muhammadfaisal.mycarta.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.forgot_password.ForgotPasswordActivity;
import me.muhammadfaisal.mycarta.home.HomeActivity;
import me.muhammadfaisal.mycarta.login.insert_name.InsertNameActivity;
import me.muhammadfaisal.mycarta.pin.PinBottomSheetFragment;
import me.muhammadfaisal.mycarta.register.RegisterActivity;
import me.muhammadfaisal.mycarta.register.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnLoginWithGoogle;
    EditText inputEmail, inputPassword;
    FirebaseAuth auth;
    ImageView imageBack;
    TextView textForgotPass, textSignUp;
    public FirebaseUser user;
    SharedPreferences sharedPreferences;
    public GoogleSignInClient googleSignInClient;
    DatabaseReference reference;

    public int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();

        initWidget();

        GoogleSignInOptions sign = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, sign);
    }


    private void initWidget() {
        btnLogin = findViewById(R.id.buttonLogin);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        imageBack = findViewById(R.id.imageBack);
        textForgotPass = findViewById(R.id.textForgotPassword);
        textSignUp = findViewById(R.id.textSignUp);
        btnLoginWithGoogle = findViewById(R.id.buttonSignInWithGoogle);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        textForgotPass.setOnClickListener(this);
        textSignUp.setOnClickListener(this);
        btnLoginWithGoogle.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            methodLogin();
        } else if (view == imageBack) {
            finish();
        } else if (view == textForgotPass) {
            methodForgotPass();
        } else if (view == textSignUp) {
            methodRegister();
        } else {
            methodLoginWithGoogle();
        }
    }

    private void methodLoginWithGoogle() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setCancelable(false);
        dialogProgress.setTitle("Loading");
        dialogProgress.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialogProgress.setContentText(task.getException().toString());
                    dialogProgress.cancel();
                } else {
                    intentLoginWithGoogle(dialogProgress);
                }
            }
        });
    }

    private void intentLoginWithGoogle(SweetAlertDialog dialogProgress) {
        user = auth.getCurrentUser();
        dialogProgress.cancel();
        sharedPreferencesForLogin();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    functionInsertName();
                } else {
                    callingPinBottomSheet();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void functionInsertName() {
        startActivity(new Intent(LoginActivity.this, InsertNameActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void callingPinBottomSheet() {
        PinBottomSheetFragment dialogFragment = new PinBottomSheetFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "PinLogin");
    }

    private void sharedPreferencesForLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", auth.getUid());
        editor.apply();
    }

    private void methodRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void methodForgotPass() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private void methodLogin() {
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError("Email can't be empty");
        } else if (password.isEmpty()) {
            inputPassword.setError("Password can't be empty");
        } else {
            IntentLoginWithUsernameAndPassword(email, password);
        }
    }

    private void IntentLoginWithUsernameAndPassword(String email, String password) {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#445EE9"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.cancel();
                    Toast.makeText(LoginActivity.this, "Failed To Login ! Because" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    user = auth.getCurrentUser();
                    dialog.cancel();
                    sharedPreferencesForLogin();
                    callingPinBottomSheet();
                }
            }
        });
    }
}
