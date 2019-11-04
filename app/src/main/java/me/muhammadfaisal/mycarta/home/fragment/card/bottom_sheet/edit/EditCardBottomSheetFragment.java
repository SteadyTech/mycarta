package me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.edit;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import me.muhammadfaisal.mycarta.home.fragment.card.CardFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.model.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCardBottomSheetFragment extends BottomSheetDialogFragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference getReference;
    public MaterialEditText inputName, inputNumber, inputCVV, inputPin, inputDesc;
    private Spinner inputType, inputMonthExp, inputYearExp;
    private Button save;
    private ImageView icClose;

    private UniversalHelper helper = new UniversalHelper();

    public FirebaseAuth firebaseAuth;
    public String userID;
    public FirebaseUser user;
    public String nameCard, descCard, cardType, stringNumberCard, stringPinCard, stringCvvCard, monthExpCard, yearExpCard;


    public EditCardBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_edit_card, container, false);

        initWidget(v);

        initSpinnerCardType();

        initSpinnerMonthExp();

        initSpinnerYearExp();

        setTextForEditText();

        return  v;
    }

    private void initWidget(View v) {
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

        save = v.findViewById(R.id.buttonSave);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getReference = firebaseDatabase.getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == save) {

                    Card card = new Card();

                    stringPinCard = Objects.requireNonNull(inputPin.getText()).toString();
                    stringCvvCard = Objects.requireNonNull(inputCVV.getText()).toString();
                    monthExpCard = Objects.requireNonNull(inputMonthExp.getSelectedItem().toString());
                    cardType = inputType.getSelectedItem().toString();
                    yearExpCard = Objects.requireNonNull(inputYearExp.getSelectedItem().toString());
                    nameCard = Objects.requireNonNull(inputName.getText()).toString();
                    stringNumberCard = Objects.requireNonNull(inputNumber.getText()).toString();

                    if (inputDesc.getText().toString().equals("")){
                        descCard = "No Description";
                    }else{
                        descCard = inputDesc.getText().toString();
                    }


                    String finalYear = yearExpCard.substring(Math.max(yearExpCard.length() - 2, 0));

                    final String expiry = monthExpCard + "/" + finalYear;

                    card.setName(helper.encryptString(inputName.getText().toString()));
                    card.setCvv(Integer.parseInt(stringCvvCard));
                    card.setDescripton(descCard);
                    card.setExp(expiry);
                    card.setNumberCard(Long.parseLong(stringNumberCard));
                    card.setPin(Integer.parseInt(stringPinCard));
                    card.setType(cardType);

                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitle("Success");
                    sweetAlertDialog.setContentText("Your card has been edited!");

                    if (inputNumber.getText().toString().isEmpty() || inputCVV.getText().toString().isEmpty() || inputName.getText().toString().isEmpty()) {
                        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                        dialog.setContentText("Please fill all field");
                        dialog.setTitle("Error");
                        dialog.show();
                    } else {
                        updateWithDescription(card);
                    }
                }
            }
        });
    }

    private void updateWithDescription(Card card) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle("Success");
        sweetAlertDialog.setContentText("Your card has been edited!");

        userID = user.getUid();
        getReference.child("card").child(userID).child(getArguments().getString("keyPrimary")).setValue(card)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        sweetAlertDialog.show();
                        dismiss();
                    }
                });
    }


    private void setTextForEditText() {
        inputName.setText(helper.decryptString(getArguments().getString("nameOnCard")));
        inputCVV.setText(String.valueOf(getArguments().getInt("cvv")));
        inputNumber.setText(String.valueOf(getArguments().getLong("numberOnCard")));
        inputPin.setText(String.valueOf(getArguments().getInt("pin")));
        inputDesc.setText(getArguments().getString("desc"));

        String type = getArguments().getString("typeOnCard");

        switch (type) {
            case "Visa":
                inputType.setSelection(0);
                break;
            case "Mastercard":
                inputType.setSelection(1);
                break;
            case "American Express":
                inputType.setSelection(2);
                break;
            case "JCB":
                inputType.setSelection(3);
                break;
            default:
                inputType.setSelection(4);
                break;
        }
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
