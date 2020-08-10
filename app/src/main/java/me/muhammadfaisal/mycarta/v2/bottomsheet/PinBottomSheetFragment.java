package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.home.fragment.account.view.ChangePinActivity;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.edit.EditCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.activity.HomeActivity;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;
import me.muhammadfaisal.mycarta.v2.model.firebase.PinModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, PinField.OnTextCompleteListener {

    private ArrayList<PinModel> pinModel;
    private PinField inputPIN;
    private Button button;
    private TextView title;
    private TextView textLogout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private LottieAnimationView lottieLoading;
    private LinearLayout layout;

    private ImageView icClose;

    private CartaHelper helper;

    private String encryptedPin, decryptedPin;
    private String TAG;

    private long size;

    public PinBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_fragment_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initView(view);

        this.data();

        this.button.setOnClickListener(this);

    }

    private void data() {
        if (this.getTag() != null) {
            this.TAG = this.getTag();
            this.hideLoading();
            switch (this.getTag()) {
                case "PinLogin":
                    this.tagLogin();
                    break;
                case "PinEditCard":
                    this.tagEditCard();
                    break;
                case "ChangePIN":
                    this.tagChangePIN();
                    break;
            }
        }
    }

    private void tagChangePIN() {
        title.setText(this.getResources().getString(R.string.insert_pin));
        button.setText(this.getResources().getString(R.string.change_pin));
    }

    private void tagEditCard() {
        title.setText(this.getResources().getString(R.string.insert_pin));
        button.setText(this.getResources().getString(R.string.edit_card));
    }

    private void tagLogin() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final long size = dataSnapshot.getChildrenCount();

                PinBottomSheetFragment.this.size = size;

                if (size == 0) {
                    PinBottomSheetFragment.this.title.setText(PinBottomSheetFragment.this.getResources().getString(R.string.setup_pin));
                    PinBottomSheetFragment.this.button.setText(PinBottomSheetFragment.this.getResources().getString(R.string.save_pin));
                } else {
                    PinBottomSheetFragment.this.title.setText(PinBottomSheetFragment.this.getResources().getString(R.string.insert_pin));
                    PinBottomSheetFragment.this.button.setText(PinBottomSheetFragment.this.getResources().getString(R.string.login));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        this.icClose.setVisibility(View.GONE);
        this.textLogout.setVisibility(View.VISIBLE);
    }

    private void initView(View v) {
        this.layout = v.findViewById(R.id.linearContent);
        this.lottieLoading = v.findViewById(R.id.lottie2);
        this.textLogout = v.findViewById(R.id.textLogout);
        this.textLogout.setVisibility(View.GONE);

        this.inputPIN = v.findViewById(R.id.inputNewPin);
        this.button = v.findViewById(R.id.buttonPin);
        this.title = v.findViewById(R.id.textTitle);

        this.icClose = v.findViewById(R.id.icClose);

        this.icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.helper = new CartaHelper();

        this.pinModel = new ArrayList<>();

        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference().child("pin_user").child(user.getUid());

        this.inputPIN.setOnTextCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.button)) {
            this.funButton();
        }
    }

    private void funButton() {
        if (this.getActivity() != null) {
            final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            dialogProgress.setContentText("Loading");
            dialogProgress.show();

            if (this.getTag() != null) {
                switch (this.getTag()) {
                    case "PinLogin":
                        this.pinLogin(dialogProgress, null);
                        break;
                    case "PinEditCard":
                        this.pinForEditCard(dialogProgress, null);
                        break;
                    case "ChangePIN":
                        this.pinForChangePin(dialogProgress, null);
                        break;
                }
            }
        }
    }


    private void pinForChangePin(final SweetAlertDialog dialogProgress, String pin) {
        this.hideLoading();

        title.setText(this.getResources().getString(R.string.insert_pin));
        button.setText(this.getResources().getString(R.string.change_pin));

        dialogProgress.show();

        final String inputtedPin;
        if (pin == null) {
            inputtedPin = inputPIN.getText().toString();
        } else {
            inputtedPin = pin;
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PinModel pinModelUser = dataSnapshot.getValue(PinModel.class);

                if (CartaHelper.encryptString(inputtedPin).equals(pinModelUser.getPin())) {
                    dismiss();
                    dialogProgress.cancel();

                    startActivity(new Intent(getActivity(), ChangePinActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
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

    private void pinForEditCard(final SweetAlertDialog dialogProgress, String pin) {
        Log.d("PinBottomSheet", "PIN FOR EDIT CARD");

        this.hideLoading();
        dialogProgress.show();

        final String inputtedPin;
        if (pin == null) {
            inputtedPin = inputPIN.getText().toString();
        } else {
            inputtedPin = pin;

        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PinModel pinModelUser = dataSnapshot.getValue(PinModel.class);
                decryptedPin = CartaHelper.decryptString(pinModelUser.getPin());

                if (decryptedPin.equals(inputtedPin)) {
                    dismiss();
                    dialogProgress.cancel();
                    CardModel cardModel = (CardModel) PinBottomSheetFragment.this.getArguments().getSerializable("card");
                    System.out.println(cardModel + " Card Model");
                    Bundle b = new Bundle();
                    b.putSerializable("card", cardModel);

                    EditCardBottomSheetFragment editCardBottomSheetFragment = new EditCardBottomSheetFragment();
                    editCardBottomSheetFragment.show(getFragmentManager(), "EditCard");
                    editCardBottomSheetFragment.setArguments(b);

                } else {
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

    public void pinLogin(final SweetAlertDialog dialogProgress, String pin) {
        if (this.size == 0) {
            this.createPIN(dialogProgress, pin);
        } else {
            this.functionLogin(dialogProgress, pin);
        }
    }

    private void functionLogin(final SweetAlertDialog dialogProgress, String pin) {
        this.hideLoading();

        final String inputtedPin;
        if (pin == null) {
            inputtedPin = inputPIN.getText().toString();
        }else{
            inputtedPin = pin;
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PinModel pinModel = dataSnapshot.getValue(PinModel.class);
                if (pinModel != null && getActivity() != null) {
                    if (CartaHelper.encryptString(inputtedPin).equals(pinModel.getPin())) {
                        dismiss();
                        startActivity(new Intent(getActivity(), HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Pin isn't correct", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createPIN(final SweetAlertDialog dialogProgress, String pin) {
        this.hideLoading();

        if (inputPIN.getText().length() < 6 || inputPIN.getText().toString().isEmpty()) {
            inputPIN.setError("Minimum 6 Number");
            dialogProgress.hide();
        } else {

            if (pin == null) {
                this.encryptedPin = CartaHelper.encryptString(inputPIN.getText().toString());
            }else{
                this.encryptedPin = CartaHelper.encryptString(pin);
            }

            final PinModel pinModel = new PinModel(encryptedPin);

            reference.setValue(pinModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dialogProgress.hide();

                    dismiss();

                    Toast.makeText(getActivity(), "Pin Has Been Saved!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                }
            });
        }

    }

    private void hideLoading() {
        lottieLoading.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTextComplete(@NotNull String s) {
        if (this.getActivity() != null) {
            final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            dialogProgress.setContentText("Loading");
            dialogProgress.show();

            if (this.getTag() != null) {
                switch (this.getTag()) {
                    case "PinLogin":
                        this.pinLogin(dialogProgress, s);
                        break;
                    case "PinEditCard":
                        this.pinForEditCard(dialogProgress, s);
                        break;
                    case "ChangePIN":
                        this.pinForChangePin(dialogProgress, s);
                        break;
                }
            }
        }
        return false;
    }
}
