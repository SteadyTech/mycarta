package me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.edit;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCardBottomSheetFragment extends BottomSheetDialogFragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference getReference;
    public TextInputEditText inputName, inputNumber, inputCVV, inputDesc;
    private Spinner inputType, inputMonthExp, inputYearExp;
    private Button save;
    private ImageView icClose;

    private CartaHelper helper = new CartaHelper();

    public FirebaseAuth firebaseAuth;
    public String userID;
    public FirebaseUser user;
    public String nameCard, descCard, cardType, stringNumberCard, stringCvvCard, monthExpCard, yearExpCard;

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
                    card.setCvv(helper.encryptString(stringCvvCard));
                    card.setDescripton(helper.encryptString(descCard));
                    card.setExp(helper.encryptString(expiry));
                    card.setNumberCard(helper.encryptString(stringNumberCard));
                    card.setType(helper.encryptString(cardType));

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

        final SweetAlertDialog loading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitle("Loading");
        loading.show();

        userID = user.getUid();
        getReference.child("card").child(userID).child(getArguments().getString("keyPrimary")).setValue(card)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loading.cancel();
                            sweetAlertDialog.show();
                            dismiss();
                        }else{
                            loading.cancel();
                            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void setTextForEditText() {
        inputName.setText(getArguments().getString("nameOnCard"));
        inputCVV.setText(getArguments().getString("cvv"));
        inputNumber.setText(getArguments().getString("numberOnCard"));
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
