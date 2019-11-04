package me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.add;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private EditText inputName, inputNumber, inputCVV, inputPin, inputDesc;
    Spinner inputType, inputMonthExp, inputYearExp;
    Button save;
    ImageView icClose;

    public FirebaseAuth firebaseAuth;
    public String userID;
    public FirebaseUser user;
    public String nameCard, descCard, cardType, stringNumberCard, stringPinCard, stringCvvCard, monthExpCard, yearExpCard;

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
        inputPin = v.findViewById(R.id.inputPin);
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

                    stringPinCard = Objects.requireNonNull(inputPin.getText()).toString();
                    stringCvvCard = Objects.requireNonNull(inputCVV.getText()).toString();
                    monthExpCard = Objects.requireNonNull(inputMonthExp.getSelectedItem().toString());
                    cardType = inputType.getSelectedItem().toString();
                    yearExpCard = Objects.requireNonNull(inputYearExp.getSelectedItem().toString());
                    nameCard = Objects.requireNonNull(inputName.getText()).toString();
                    stringNumberCard = Objects.requireNonNull(inputNumber.getText()).toString();

                    String encryptedName = helper.encryptString(nameCard);

                    String finalYear = yearExpCard.substring(Math.max(yearExpCard.length() - 2, 0));

                    final String expiry = monthExpCard + "/" + finalYear;

                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitle("Success");
                    sweetAlertDialog.setContentText("Your card has been saved!");

                    if (inputNumber.getText().toString().isEmpty() || inputCVV.getText().toString().isEmpty() || inputName.getText().toString().isEmpty()) {
                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                        dialog.setContentText("Please fill all field");
                        dialog.setTitle("Error");
                        dialog.show();
                    } else {
                        final Long numberCard = Long.parseLong(stringNumberCard);
                        final int pinCard = Integer.parseInt(stringPinCard);
                        final int cvvCard = Integer.parseInt(stringCvvCard);
                        descCard = Objects.requireNonNull(inputDesc.getText()).toString();

                        if(descCard.isEmpty()){
                            userID = user.getUid();
                            getReference.child("card").child(userID).push()
                                    .setValue(new Card(encryptedName, "No Description", cardType, numberCard, pinCard, cvvCard, expiry))
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            sweetAlertDialog.show();
                                            dismiss();
                                        }
                                    });
                        }else{
                            userID = user.getUid();
                            getReference.child("card").child(userID).push()
                                    .setValue(new Card(encryptedName, descCard, cardType, numberCard, pinCard, cvvCard, expiry))
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            sweetAlertDialog.show();
                                            dismiss();
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
