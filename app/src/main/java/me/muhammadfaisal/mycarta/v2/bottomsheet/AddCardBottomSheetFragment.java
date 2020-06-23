package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.adapter.SpinnerAdapter;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText inputOwner;
    private EditText inputName;
    private EditText inputNumber;
    private EditText inputDesc;
    private Spinner inputType;
    private CheckBox ckbPrioritize;

    public FirebaseUser user;
    private DatabaseReference getReference;

    public AddCardBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_fragment_add_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.inputOwner = view.findViewById(R.id.inputCardName);
        this.inputNumber = view.findViewById(R.id.inputCardNumber);
        this.inputDesc = view.findViewById(R.id.inputDescription);
        this.inputType = view.findViewById(R.id.spinnerCardType);
        this.inputName = view.findViewById(R.id.inputCardName);
        this.ckbPrioritize = view.findViewById(R.id.ckbPrioritize);

        ImageView icClose = view.findViewById(R.id.icClose);
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.initSpinnerCardType();

        Button save = view.findViewById(R.id.buttonSave);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        this.user = firebaseAuth.getCurrentUser();
        this.getReference = firebaseDatabase.getReference();

        save.setOnClickListener(this);
    }

    private void initSpinnerCardType() {
        String[] CARD_TYPE = {"--Select Type Card--", "Member Card", "Debit Card", "Other"};
        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, CARD_TYPE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputType.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        this.saveCard();
    }

    private void saveCard() {
        String cardType = inputType.getSelectedItem().toString();
        String ownerName = Objects.requireNonNull(inputOwner.getText()).toString();
        String stringNumberCard = Objects.requireNonNull(inputNumber.getText()).toString();
        String descriptionCard = Objects.requireNonNull(inputDesc.getText().toString());
        String cardName = inputName.getText().toString();

        final String encryptedOwner = CartaHelper.encryptString(ownerName);
        final String encryptedNumberCard = CartaHelper.encryptString(stringNumberCard);
        final String encryptedCardType = CartaHelper.encryptString(cardType);
        final String encryptedDescription = CartaHelper.encryptString(descriptionCard);
        final String encryptedName = CartaHelper.encryptString(cardName);
        final String encryptedPrioritize;

        if (this.ckbPrioritize.isChecked()) {
             encryptedPrioritize = CartaHelper.encryptString(Constant.CODE.YES);
        }else{
            encryptedPrioritize = CartaHelper.encryptString(Constant.CODE.NO);
        }

        final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setContentText("Loading");
        dialogProgress.setCancelable(false);
        dialogProgress.show();

        if (inputNumber.getText().toString().isEmpty() || inputOwner.getText().toString().isEmpty() || inputName.getText().toString().isEmpty()) {
            dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            dialogProgress.setContentText("Please Fill all Field!");
        } else {
            String userID;
            if (descriptionCard.isEmpty()) {
                userID = user.getUid();
                this.getReference.child(Constant.CARD_PATH).child(userID).push()
                        .setValue(new CardModel(encryptedNumberCard, encryptedOwner, encryptedCardType, CartaHelper.encryptString("No Description"), encryptedName, encryptedPrioritize))
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    dialogProgress.setContentText("Uh Oh... Error " + task.getException());
                                } else {
                                    dialogProgress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    dialogProgress.setContentText("Your card has been saved!");
                                    dismiss();
                                }
                            }
                        });
            } else {
                userID = user.getUid();
                this.getReference.child(Constant.CARD_PATH).child(userID).push()
                        .setValue(new CardModel(encryptedNumberCard, encryptedOwner, encryptedCardType, encryptedDescription, encryptedName, encryptedPrioritize))
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    dialogProgress.setContentText("Uh Oh... Error " + task.getException());
                                } else {
                                    dialogProgress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    dialogProgress.setContentText("Your card has been saved!");
                                    dismiss();
                                }
                            }
                        });
            }
        }
    }
}
