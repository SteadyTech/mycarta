package me.muhammadfaisal.mycarta.v1.home.fragment.account.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import org.jetbrains.annotations.NotNull;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.bottomsheet.CautionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.PinModel;

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener {

    LottieAnimationView lottieAnimationView;
    Button buttonSave;
    SquarePinField inputNewPin;
    FirebaseAuth auth;
    DatabaseReference reference;
    CartaHelper helper;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        this.initWidget();

    }

    private void initWidget() {
        inputNewPin = findViewById(R.id.inputLatestPin);
        buttonSave = findViewById(R.id.buttonChangePIN);
        lottieAnimationView = findViewById(R.id.lottie);

        this.handler = new Handler();

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("pin_user").child(auth.getCurrentUser().getUid());

        helper = new CartaHelper();

        this.inputNewPin.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(@NotNull String s) {
                functionUpdatePin(s);
                return false;
            }
        });

        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSave) {
            functionUpdatePin(null);
        }
    }

    private void functionUpdatePin(final String s) {

        lottieAnimationView.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.GONE);

        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (s != null) {

                    if (inputNewPin.getText().toString().length() == 6) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                PinModel pinModelUser = dataSnapshot.getValue(PinModel.class);

                                pinModelUser.setPin(helper.encryptString(s));

                                reference.setValue(pinModelUser).addOnCompleteListener(ChangePinActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePinActivity.this, "PinModel has been changed!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChangePinActivity.this, "Uh Oh... Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        lottieAnimationView.setVisibility(View.GONE);
                        buttonSave.setVisibility(View.VISIBLE);
                        CartaHelper.showCaution(ChangePinActivity.this, getString(R.string.pin_invalid), getString(R.string.pin_filled_incorrectly), Constant.CODE.PIN_INVALID);
                    }
                } else {

                    if (inputNewPin.getText().toString().length() == 6) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                PinModel pinModelUser = dataSnapshot.getValue(PinModel.class);

                                pinModelUser.setPin(helper.encryptString(inputNewPin.getText().toString()));

                                reference.setValue(pinModelUser).addOnCompleteListener(ChangePinActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePinActivity.this, "PinModel has been changed!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChangePinActivity.this, "Uh Oh... Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        lottieAnimationView.setVisibility(View.GONE);
                        buttonSave.setVisibility(View.VISIBLE);
                        CartaHelper.showCaution(ChangePinActivity.this ,getString(R.string.pin_invalid), getString(R.string.pin_filled_incorrectly), Constant.CODE.PIN_INVALID);
                    }
                }
            }
        }, 2000L);
    }
}
