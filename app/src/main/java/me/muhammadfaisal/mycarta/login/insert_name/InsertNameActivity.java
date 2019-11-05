package me.muhammadfaisal.mycarta.login.insert_name;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.register.model.User;
import me.muhammadfaisal.mycarta.register.welcome.WelcomeNewUserActivity;

public class InsertNameActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputName;
    DatabaseReference reference;
    CardView cardNext;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_name);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        initWidget();
    }

    private void initWidget() {
        inputName = findViewById(R.id.inputName);
        cardNext = findViewById(R.id.cardNext);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());

        cardNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cardNext){
            nextFunction();
        }
    }

    private void nextFunction() {
        String name = inputName.getText().toString();

        User users = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getEmail(), name);
        reference.setValue(users);

        startActivity(new Intent(this, WelcomeNewUserActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
