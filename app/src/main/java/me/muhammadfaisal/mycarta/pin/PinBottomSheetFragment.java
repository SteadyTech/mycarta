package me.muhammadfaisal.mycarta.pin;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
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
import me.muhammadfaisal.mycarta.login.LoginActivity;
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

        if (this.getTag().equals("PinLogin")) {
            pinForLogin();
        } else if (this.getTag().equals("PinEditCard")) {
            pinForEditCards();
        } else if (this.getTag().equals("ChangePIN")) {
            pinForChangePin();
        } else if(this.getTag().equals("PinPay")){
            pinForPay();
        } else {
            pinForAccessHiddenInformation();
        }
    }

    private void pinForPay() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final String userID = user.getUid();

        reference = database.getReference().child("pin").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                pin = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pin pinUser = snapshot.getValue(Pin.class);
                    pinUser.setKey(snapshot.getKey());

                    pin.add(pinUser);
                }
                methodAuthPinAccessForPay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodAuthPinAccessForPay() {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText("You're Logged in");
        dialog.setTitle("Success");

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialogError.setContentText("PIN isn't Correct!");
        dialogError.setTitle("Error");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Checking Your Pin");
        dialogProgress.setCancelable(false);

        final EditCardBottomSheetFragment editCardBottomSheetFragment = new EditCardBottomSheetFragment();

        title.setText("Insert Your PIN");
        inputPIN.setHint("Your PIN");
        button.setText("Access Data");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Pin Required!");
                } else {
                    dialogProgress.show();
                    final Long pin = Long.parseLong(inputPIN.getText().toString());

                    final Handler handler = new Handler();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getValue(Pin.class);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pin pinUser = snapshot.getValue(Pin.class);
                                pinUser.setKey(snapshot.getKey());

                                if (pin.equals(pinUser.getPin())) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String name = getArguments().getString("name");
                                            long number = getArguments().getLong("number");
                                            int cvv = getArguments().getInt("cvv");
                                            String key = getArguments().getString("keyPrimary");

                                            dialog.show();
                                            dialogProgress.cancel();
                                            dismiss();

                                            Intent i = new Intent(getActivity(), CardPayActivity.class);
                                            i.putExtra("nameOnCard", name);
                                            i.putExtra("cvv", cvv);
                                            i.putExtra("numberOnCard", number);
                                            i.putExtra("keyPrimary", key);
                                            startActivity(i);

                                            dialog.cancel();
                                            dismiss();
                                        }
                                    }, 2000L);
                                } else {
                                    dismiss();
                                    dialogProgress.cancel();
                                    dialogError.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }

    private void pinForChangePin() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final String userID = user.getUid();

        reference = database.getReference().child("pin").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                pin = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pin pinUser = snapshot.getValue(Pin.class);
                    pinUser.setKey(snapshot.getKey());

                    pin.add(pinUser);
                }
                methodAuthPinAcessForChangePIN();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodAuthPinAcessForChangePIN() {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText("You're Logged in");
        dialog.setTitle("Success");

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialogError.setContentText("PIN isn't Correct!");
        dialogError.setTitle("Error");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Checking Your Pin");
        dialogProgress.setCancelable(false);

        final EditCardBottomSheetFragment editCardBottomSheetFragment = new EditCardBottomSheetFragment();

        title.setText("Insert Your PIN");
        inputPIN.setHint("Your PIN");
        button.setText("Change PIN");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Pin Required!");
                } else {
                    dialogProgress.show();
                    final Long pin = Long.parseLong(inputPIN.getText().toString());

                    final Handler handler = new Handler();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getValue(Pin.class);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pin pinUser = snapshot.getValue(Pin.class);
                                pinUser.setKey(snapshot.getKey());

                                if (pin.equals(pinUser.getPin())) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            dialog.show();
                                            dialogProgress.cancel();
                                            dismiss();
                                            startActivity(new Intent(getActivity(), ChangePinActivity.class));
                                            dialog.cancel();
                                            dismiss();
                                        }
                                    }, 2000L);
                                } else {
                                    dismiss();
                                    dialogProgress.cancel();
                                    dialogError.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }

    private void pinForEditCards() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final String userID = user.getUid();

        reference = database.getReference().child("pin").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                pin = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pin pinUser = snapshot.getValue(Pin.class);
                    pinUser.setKey(snapshot.getKey());

                    pin.add(pinUser);
                }
                methodAuthPinAccessForEditCards();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodAuthPinAccessForEditCards() {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText("You're Logged in");
        dialog.setTitle("Success");

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialogError.setContentText("PIN isn't Correct!");
        dialogError.setTitle("Error");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Checking Your Pin");
        dialogProgress.setCancelable(false);

        final EditCardBottomSheetFragment editCardBottomSheetFragment = new EditCardBottomSheetFragment();

        title.setText("Insert Your PIN");
        inputPIN.setHint("Your PIN");
        button.setText("Edit Data");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Pin Required!");
                } else {
                    dialogProgress.show();
                    final Long pin = Long.parseLong(inputPIN.getText().toString());

                    final Handler handler = new Handler();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getValue(Pin.class);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pin pinUser = snapshot.getValue(Pin.class);
                                pinUser.setKey(snapshot.getKey());

                                if (pin.equals(pinUser.getPin())) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String name = getArguments().getString("name");
                                            String description = getArguments().getString("description");
                                            String expiry = getArguments().getString("expiry");
                                            String type = getArguments().getString("type");
                                            int pin = getArguments().getInt("pin");
                                            long number = getArguments().getLong("number");
                                            int cvv = getArguments().getInt("cvv");
                                            String key = getArguments().getString("keyPrimary");

                                            Bundle b = new Bundle();
                                            b.putString("nameOnCard", name);
                                            b.putString("typeOnCard", type);
                                            b.putString("desc", description);
                                            b.putString("expiry", expiry);
                                            b.putInt("pin", pin);
                                            b.putInt("cvv", cvv);
                                            b.putLong("numberOnCard", number);
                                            b.putString("keyPrimary", key);


                                            dialog.show();
                                            dialogProgress.cancel();
                                            dismiss();
                                            editCardBottomSheetFragment.show(getFragmentManager(), "EditCard");
                                            editCardBottomSheetFragment.setArguments(b);
                                            dialog.cancel();
                                            dismiss();
                                        }
                                    }, 2000L);
                                } else {
                                    dismiss();
                                    dialogProgress.cancel();
                                    dialogError.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }

    public void pinForAccessHiddenInformation() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final String userID = user.getUid();

        reference = database.getReference().child("pin").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                pin = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pin pinUser = snapshot.getValue(Pin.class);
                    pinUser.setKey(snapshot.getKey());

                    pin.add(pinUser);
                }
                methodAuthPinAccessForHiddenInformation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodAuthPinAccessForHiddenInformation() {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText("You're Logged in");
        dialog.setTitle("Success");

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialogError.setContentText("PIN isn't Correct!");
        dialogError.setTitle("Error");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Checking Your Pin");
        dialogProgress.setCancelable(false);

        final HiddenBottomSheetFragment hiddenBottomSheetFragment = new HiddenBottomSheetFragment();

        title.setText("Insert Your PIN");
        inputPIN.setHint("Your PIN");
        button.setText("Access Data");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Pin Required!");
                } else {
                    dialogProgress.show();
                    final Long pin = Long.parseLong(inputPIN.getText().toString());

                    final Handler handler = new Handler();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getValue(Pin.class);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pin pinUser = snapshot.getValue(Pin.class);
                                pinUser.setKey(snapshot.getKey());

                                if (pin.equals(pinUser.getPin())) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String name = getArguments().getString("name");
                                            String description = getArguments().getString("description");
                                            String expiry = getArguments().getString("expiry");
                                            String type = getArguments().getString("type");
                                            int pin = getArguments().getInt("pin");
                                            long number = getArguments().getLong("number");
                                            int cvv = getArguments().getInt("cvv");

                                            Bundle b = new Bundle();
                                            b.putString("nameOnCard", name);
                                            b.putString("typeOnCard", type);
                                            b.putString("desc", description);
                                            b.putString("expiry", expiry);
                                            b.putInt("pin", pin);
                                            b.putInt("cvv", cvv);
                                            b.putLong("numberOnCard", number);

                                            dialog.show();
                                            dialogProgress.cancel();
                                            dismiss();
                                            hiddenBottomSheetFragment.show(getFragmentManager(), "HiddenCard");
                                            hiddenBottomSheetFragment.setArguments(b);
                                            dialog.cancel();
                                            dismiss();
                                        }
                                    }, 2000L);
                                } else {
                                    dismiss();
                                    dialogProgress.cancel();
                                    dialogError.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }

    public void pinForLogin() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final String userID = user.getUid();

        reference = database.getReference().child("pin").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                pin = new ArrayList<>();

                final long size = dataSnapshot.getChildrenCount();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pin pinUser = snapshot.getValue(Pin.class);
                    pinUser.setKey(snapshot.getKey());

                    pin.add(pinUser);
                }

                if (size == 0) {
                    methodAddNewPin();
                } else {
                    methodLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodLogin() {

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        dialogError.setContentText("PIN isn't Correct!");
        dialogError.setTitle("Error");

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Checking Your Pin");
        dialogProgress.setCancelable(false);


        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText("You're Logged in");
        dialog.setTitle("Success");

        title.setText("Insert Your PIN");
        inputPIN.setHint("Your PIN");
        button.setText("Login");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPIN.getText().toString().isEmpty()) {
                    inputPIN.setError("Pin Required!");
                } else {
                    final Long pin = Long.parseLong(inputPIN.getText().toString());

                    dialogProgress.show();

                    final Handler handler = new Handler();


                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    database = FirebaseDatabase.getInstance();

                    final String userID = user.getUid();

                    reference = database.getReference().child("pin").child(userID);

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getValue(Pin.class);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pin pinUser = snapshot.getValue(Pin.class);
                                pinUser.setKey(snapshot.getKey());

                                if (pin.equals(pinUser.getPin())) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.show();
                                            dialogProgress.cancel();
                                            startActivity(new Intent(getActivity(), HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                                            getActivity().finish();
                                            dialog.cancel();
                                            dismiss();
                                        }
                                    }, 2000L);
                                } else {
                                    dismiss();
                                    dialogProgress.cancel();
                                    dialogError.show();
                                    BottomSheetDialogFragment dialogFragment = new PinBottomSheetFragment();
                                    dialogFragment.show(getFragmentManager(), "PinLogin");
                                    dialogFragment.setCancelable(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }

    private void methodAddNewPin() {

        title.setText("Setup Your PIN");
        button.setText("OK");

        final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);

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
                    dialogProgress.cancel();
                    reference.push()
                            .setValue(new Pin(pinEditText));
                    Toast.makeText(getActivity(), "Your Pin Has Been Saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    getActivity().finish();
                    dismiss();
                }
            }
        });

    }

}
