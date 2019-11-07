package me.muhammadfaisal.mycarta.pin;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.HomeActivity;
import me.muhammadfaisal.mycarta.home.fragment.account.view.ChangePinActivity;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.edit.EditCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.hidden.HiddenBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.view.CardPayActivity;
import me.muhammadfaisal.mycarta.pin.model.Pin;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinBottomSheetFragment extends BottomSheetDialogFragment {

    ArrayList<Pin> pin;
    EditText inputPIN;
    Button button;
    TextView title;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressBar progressBar;
    LinearLayout layout;

    ImageView icClose;

    public PinBottomSheetFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_create_pin, container, false);

        initView(v);

        return v;
    }

    private void initView(View v) {
        layout = v.findViewById(R.id.linearContent);
        progressBar = v.findViewById(R.id.progressBar);

        inputPIN = v.findViewById(R.id.inputNewPin);
        button = v.findViewById(R.id.buttonPin);
        title = v.findViewById(R.id.textTitle);

        icClose = v.findViewById(R.id.icClose);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("pin_user").child(user.getUid());


        if (this.getTag().equals("PinLogin")) {
            pinLogin();
        } else if (this.getTag().equals("PinEditCard")) {
            pinForEditCard();
        } else if (this.getTag().equals("ChangePIN")) {
            pinForChangePin();
        } else if(this.getTag().equals("PinPay")){
            pinForPay();
        } else {
            pinForAccessHiddenInformation();
        }
    }

    private void pinForAccessHiddenInformation() {
        hideWidget();

        title.setText("Insert PIN");
        button.setText("Edit Card");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.show();
                final String inputtedPin = inputPIN.getText().toString();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Pin pinUser = dataSnapshot.getValue(Pin.class);
                        String pinString = pinUser.getPin().toString();

                        if (pinString.equals(inputtedPin)){
                            dismiss();
                            dialogProgress.cancel();
                            Bundle b = new Bundle();

                            getAndForwardData(b);

                            HiddenBottomSheetFragment hiddenBottomSheetFragment = new HiddenBottomSheetFragment();
                            hiddenBottomSheetFragment.show(getFragmentManager(), "HiddenInformation");
                            hiddenBottomSheetFragment.setArguments(b);

                        }else{
                            dismiss();
                            dialogProgress.cancel();
                            Toast.makeText(getActivity(), "PIN isn't Correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void pinForPay() {
        hideWidget();

        title.setText("Insert PIN");
        button.setText("Change PIN");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.show();
                final String inputtedPin = inputPIN.getText().toString();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Pin pinUser = dataSnapshot.getValue(Pin.class);
                        String pinString = pinUser.getPin().toString();

                        if (pinString.equals(inputtedPin)){
                            dismiss();
                            dialogProgress.cancel();

                            startActivity(new Intent(getActivity(), CardPayActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        }else{
                            dismiss();
                            dialogProgress.cancel();
                            Toast.makeText(getActivity(), "PIN isn't Correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void pinForChangePin() {
        hideWidget();

        title.setText("Insert PIN");
        button.setText("Change PIN");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.show();
                final String inputtedPin = inputPIN.getText().toString();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Pin pinUser = dataSnapshot.getValue(Pin.class);
                        String pinString = pinUser.getPin().toString();

                        if (pinString.equals(inputtedPin)){
                            dismiss();
                            dialogProgress.cancel();

                            startActivity(new Intent(getActivity(), ChangePinActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        }else{
                            dismiss();
                            dialogProgress.cancel();
                            Toast.makeText(getActivity(), "PIN isn't Correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void pinForEditCard() {
        hideWidget();

        title.setText("Insert PIN");
        button.setText("Edit Card");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.show();
                final String inputtedPin = inputPIN.getText().toString();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Pin pinUser = dataSnapshot.getValue(Pin.class);
                        String pinString = pinUser.getPin().toString();

                        if (pinString.equals(inputtedPin)){
                            dismiss();
                            dialogProgress.cancel();
                            Bundle b = new Bundle();

                            getAndForwardData(b);

                            EditCardBottomSheetFragment editCardBottomSheetFragment = new EditCardBottomSheetFragment();
                            editCardBottomSheetFragment.show(getFragmentManager(), "EditCard");
                            editCardBottomSheetFragment.setArguments(b);

                        }else{
                            dismiss();
                            dialogProgress.cancel();
                            Toast.makeText(getActivity(), "PIN isn't Correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void pinLogin() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    functionNewPIN();
                }else{
                    functionLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void functionLogin() {
        hideWidget();

        title.setText("Insert PIN");
        button.setText("Login");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProgress.show();
                final String inputtedPin = inputPIN.getText().toString();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Pin pin = dataSnapshot.getValue(Pin.class);
                        String pinString = pin.getPin().toString();
                        if (pinString.equals(inputtedPin)){
                            dismiss();
                            dialogProgress.cancel();
                            startActivity(new Intent(getActivity(), HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        }else{
                            dismiss();
                            dialogProgress.cancel();
                            Toast.makeText(getActivity(), "Pin isn't correct", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void functionNewPIN() {

        hideWidget();

        title.setText("Setup PIN");
        button.setText("OK");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogProgress.show();

                if (inputPIN.getText().length() < 4 || inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Minimum 4 Number");
                    dialogProgress.cancel();
                } else {
                    final Long pinEditText = Long.parseLong(inputPIN.getText().toString());

                    Pin pin = new Pin(pinEditText);

                    dialogProgress.cancel();
                    reference.setValue(pin);
                }
            }
        });

    }

    private void getAndForwardData(Bundle b) {
        String name = getArguments().getString("name");
        String description = getArguments().getString("description");
        String expiry = getArguments().getString("expiry");
        String type = getArguments().getString("type");
        int pin = getArguments().getInt("pin");
        long number = getArguments().getLong("number");
        int cvv = getArguments().getInt("cvv");
        String key = getArguments().getString("keyPrimary");

        b.putString("nameOnCard", name);
        b.putString("typeOnCard", type);
        b.putString("desc", description);
        b.putString("expiry", expiry);
        b.putInt("pin", pin);
        b.putInt("cvv", cvv);
        b.putLong("numberOnCard", number);
        b.putString("keyPrimary", key);
    }

    private void hideWidget() {
        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        pin = new ArrayList<>();

    }

}
