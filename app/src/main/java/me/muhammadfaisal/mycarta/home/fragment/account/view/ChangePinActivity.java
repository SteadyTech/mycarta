package me.muhammadfaisal.mycarta.home.fragment.account.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.pin.model.Pin;

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSave;
    EditText inputNewPin;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        getSupportActionBar().hide();

        initWidget();

    }

    private void initWidget() {
        inputNewPin = findViewById(R.id.inputLatestPin);
        buttonSave = findViewById(R.id.buttonChangePIN);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("pin_user").child(auth.getCurrentUser().getUid());

        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSave){
            functionUpdatePin();
        }
    }

    private void functionUpdatePin() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pin pinUser = dataSnapshot.getValue(Pin.class);

                pinUser.setPin(Long.parseLong(inputNewPin.getText().toString()));

                reference.setValue(pinUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
