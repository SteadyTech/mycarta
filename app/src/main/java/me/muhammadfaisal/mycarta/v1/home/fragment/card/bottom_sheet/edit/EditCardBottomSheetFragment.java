package me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.edit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.activity.HomeActivity;
import me.muhammadfaisal.mycarta.v2.adapter.SpinnerAdapter;
import me.muhammadfaisal.mycarta.v2.bottomsheet.CautionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCardBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText inputOwner;
    private EditText inputName;
    private EditText inputNumber;
    private EditText inputDesc;
    private Spinner inputType;
    private CheckBox ckbPrioritize;

    private CardModel cardModel;

    public FirebaseUser user;
    private DatabaseReference databaseReference;

    public EditCardBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_fragment_edit_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.inputOwner = view.findViewById(R.id.inputCardHolderName);
        this.inputNumber = view.findViewById(R.id.inputCardNumber);
        this.inputDesc = view.findViewById(R.id.inputDescription);
        this.inputType = view.findViewById(R.id.spinnerCardType);
        this.inputName = view.findViewById(R.id.inputCardName);
        this.ckbPrioritize = view.findViewById(R.id.ckbPrioritize);

        this.data();

        ImageView icClose = view.findViewById(R.id.icClose);
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.initSpinnerCardType();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Button save = view.findViewById(R.id.buttonSave);
        save.setOnClickListener(this);

        this.user = firebaseAuth.getCurrentUser();
        this.databaseReference = firebaseDatabase.getReference();
    }

    private void data() {
        if (this.getArguments() != null) {
            this.cardModel = (CardModel) this.getArguments().getSerializable("card");
            if (this.cardModel != null) {
                this.inputDesc.setText(CartaHelper.decryptString(this.cardModel.getCardDescription()));
                this.inputName.setText(CartaHelper.decryptString(this.cardModel.getCardName()));
                this.inputNumber.setText(CartaHelper.decryptString(this.cardModel.getCardNumber()));
                this.inputOwner.setText(CartaHelper.decryptString(this.cardModel.getCardOwner()));

                if (this.cardModel.isPrioritize().equals(CartaHelper.encryptString(Constant.CODE.YES))){
                    this.ckbPrioritize.setChecked(true);
                }else{
                    this.ckbPrioritize.setChecked(false);
                }
            }
        }
    }

    private void initSpinnerCardType() {

        if (cardModel != null) {
            String[] CARD_TYPE = {CartaHelper.decryptString(this.cardModel.getCardType()), "Member Card", "Debit Card", "Other"};

            SpinnerAdapter adapter = new SpinnerAdapter(Objects.requireNonNull(this.getActivity()), android.R.layout.simple_spinner_item, CARD_TYPE);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            inputType.setAdapter(adapter);

        }
    }

    @Override
    public void onClick(View v) {
        this.saveCard();
    }

    private void saveCard() {
        Query query = databaseReference.child(Constant.CARD_PATH).child(Objects.requireNonNull(user.getUid())).orderByChild("cardNumber").equalTo(this.cardModel.getCardNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final CardModel cardModel = snapshot.getValue(CardModel.class);

                    if (cardModel != null) {

                        if (EditCardBottomSheetFragment.this.cardModel.getCardNumber().equals(cardModel.getCardNumber())) {
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

                            if (EditCardBottomSheetFragment.this.ckbPrioritize.isChecked()) {
                                encryptedPrioritize = CartaHelper.encryptString(Constant.CODE.YES);
                            } else {
                                encryptedPrioritize = CartaHelper.encryptString(Constant.CODE.NO);
                            }

                            final SweetAlertDialog dialogProgress = new SweetAlertDialog(Objects.requireNonNull(EditCardBottomSheetFragment.this.getActivity()), SweetAlertDialog.PROGRESS_TYPE);
                            dialogProgress.setContentText("Loading");
                            dialogProgress.setCancelable(false);
                            dialogProgress.show();

                            if (inputNumber.getText().toString().isEmpty() || inputOwner.getText().toString().isEmpty() || inputName.getText().toString().isEmpty()) {
                                dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                dialogProgress.setContentText("Please Fill all Field!");
                            } else {
                                String userID = user.getUid();
                                CardModel model;
                                if (descriptionCard.isEmpty()) {
                                    model = new CardModel(encryptedNumberCard, encryptedOwner, encryptedCardType, CartaHelper.encryptString("No Description"), encryptedName, encryptedPrioritize);
                                } else {
                                    model = new CardModel(encryptedNumberCard, encryptedOwner, encryptedCardType, encryptedDescription, encryptedName, encryptedPrioritize);
                                }

                                System.out.println(snapshot.getKey() + " Key Reference");

                                EditCardBottomSheetFragment.this.databaseReference.child(Constant.CARD_PATH).child(userID).child(snapshot.getKey())
                                        .setValue(model)
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                    dialogProgress.setContentText("Ooops... Error " + task.getException());
                                                } else {
                                                    dialogProgress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                    dialogProgress.setContentText("Your card has been Updated!");
                                                    dialogProgress.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            EditCardBottomSheetFragment.this.dismiss();
                                                            sweetAlertDialog.dismissWithAnimation();

                                                            if (EditCardBottomSheetFragment.this.getActivity() != null) {
                                                                CartaHelper.move(EditCardBottomSheetFragment.this.getActivity(), HomeActivity.class, true);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                            }
                        } else {
                            if (EditCardBottomSheetFragment.this.getActivity() != null) {

                                Bundle bundle = new Bundle();
                                bundle.putString(Constant.KEY.TITLE_MESSAGE, getResources().getString(R.string.card_number_can_t_be_edit));
                                bundle.putString(Constant.KEY.DESCRIPTION_MESSAGE, getResources().getString(R.string.mycarta_can_t_update_your_card_number_because_is_it_linked_with_your_transaction));

                                CautionBottomSheetFragment cautionBottomSheetFragment = new CautionBottomSheetFragment();
                                cautionBottomSheetFragment.setArguments(bundle);
                                cautionBottomSheetFragment.show(EditCardBottomSheetFragment.this.getActivity().getSupportFragmentManager(), Constant.TAG.EDITED_CARD_NUMBER);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
