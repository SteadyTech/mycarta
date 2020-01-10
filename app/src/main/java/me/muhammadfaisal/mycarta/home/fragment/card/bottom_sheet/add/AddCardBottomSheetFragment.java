package me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.add;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.helper.UniversalHelper;
import me.muhammadfaisal.mycarta.home.fragment.card.model.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardBottomSheetFragment extends BottomSheetDialogFragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference getReference;
    private EditText inputName, inputNumber, inputCVV, inputDesc;
    Spinner inputType, inputMonthExp, inputYearExp;
    Button save;
    ImageView icClose;

    public FirebaseAuth firebaseAuth;
    public String userID;
    public FirebaseUser user;
    public String nameCard, descriptionCard, cardType, stringNumberCard, stringCvvCard, monthExpCard, yearExpCard;

    public AddCardBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_add_card, container, false);

        final UniversalHelper helper = new UniversalHelper();

        inputName = v.findViewById(R.id.inputCardHolderName);
        inputNumber = v.findViewById(R.id.inputCardNumber);
        inputCVV = v.findViewById(R.id.inputCvv);
        inputMonthExp = v.findViewById(R.id.spinnerMonthExp);
        inputYearExp = v.findViewById(R.id.spinnerYearExp);
        inputDesc = v.findViewById(R.id.inputDescription);
        inputType = v.findViewById(R.id.spinnerCardType);

        icClose = v.findViewById(R.id.icClose);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        initSpinnerCardType();

        initSpinnerMonthExp();

        initSpinnerYearExp();

        save = v.findViewById(R.id.buttonSave);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getReference = firebaseDatabase.getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == save) {

                    stringCvvCard = Objects.requireNonNull(inputCVV.getText()).toString();
                    monthExpCard = Objects.requireNonNull(inputMonthExp.getSelectedItem().toString());
                    cardType = inputType.getSelectedItem().toString();
                    yearExpCard = Objects.requireNonNull(inputYearExp.getSelectedItem().toString());
                    nameCard = Objects.requireNonNull(inputName.getText()).toString();
                    stringNumberCard = Objects.requireNonNull(inputNumber.getText()).toString();
                    descriptionCard = Objects.requireNonNull(inputDesc.getText().toString());

                    String finalYear = yearExpCard.substring(Math.max(yearExpCard.length() - 2, 0));
                    String expiry = monthExpCard + "/" + finalYear;

                    final String encryptedName = helper.encryptString(nameCard);
                    final String encryptedNumberCard = helper.encryptString(stringNumberCard);
                    final String encryptedExpiry = helper.encryptString(expiry);
                    final String encryptedCVV = helper.encryptString(stringCvvCard);
                    final String encryptedCardType = helper.encryptString(cardType);
                    final String encryptedDescription = helper.encryptString(descriptionCard);

                    final SweetAlertDialog dialogProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    dialogProgress.setContentText("Loading");
                    dialogProgress.setCancelable(false);
                    dialogProgress.show();

                    if (inputNumber.getText().toString().isEmpty() || inputCVV.getText().toString().isEmpty() || inputName.getText().toString().isEmpty()) {
                        dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        dialogProgress.setContentText("Uh Oh, You haven't fill required fields");
                    } else {
                        if(descriptionCard.isEmpty()){
                            userID = user.getUid();
                            getReference.child("card").child(userID).push()
                                    .setValue(new Card(encryptedName, helper.encryptString("No Description"), encryptedCardType, encryptedExpiry, encryptedNumberCard, encryptedCVV))
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()){
                                                dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                dialogProgress.setContentText("Uh Oh... Error " + task.getException());
                                            }else{
                                                dialogProgress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                dialogProgress.setContentText("Your card has been saved!");
                                                dismiss();
                                            }
                                        }
                                    });
                        }else{
                            userID = user.getUid();
                            getReference.child("card").child(userID).push()
                                    .setValue(new Card(encryptedName, encryptedDescription, encryptedCardType, encryptedExpiry, encryptedNumberCard, encryptedCVV))
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()){
                                                dialogProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                dialogProgress.setContentText("Uh Oh... Error " + task.getException());
                                            }else{
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
        });
        return v;
    }

    private void initSpinnerYearExp() {
        String[] YEAR = {"2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2035", "2036"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, YEAR);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputYearExp.setAdapter(adapter);
    }

    private void initSpinnerMonthExp() {
        String[] MONTH = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, MONTH);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputMonthExp.setAdapter(adapter);
    }

    private void initSpinnerCardType() {
        String[] CARD_TYPE = {"Visa", "Mastercard", "American Express", "JCB" , "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, CARD_TYPE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputType.setAdapter(adapter);

    }
}
