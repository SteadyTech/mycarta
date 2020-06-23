package me.muhammadfaisal.mycarta.v1.home.fragment.account.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.pin.model.Pin;

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSave;
    EditText inputNewPin;
    FirebaseAuth auth;
    DatabaseReference reference;
    CartaHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        initWidget();

    }

    private void initWidget() {
        inputNewPin = findViewById(R.id.inputLatestPin);
        buttonSave = findViewById(R.id.buttonChangePIN);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("pin_user").child(auth.getCurrentUser().getUid());

        helper = new CartaHelper();

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

                pinUser.setPin(helper.encryptString(inputNewPin.getText().toString()));

                reference.setValue(pinUser).addOnCompleteListener(ChangePinActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ChangePinActivity.this, "Pin has been changed!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChangePinActivity.this, "Uh Oh... Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
