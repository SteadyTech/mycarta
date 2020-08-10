package me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.hidden;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.bottomsheet.CodeBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HiddenBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView textCardNumber;
    private TextView textCardName;
    private TextView textCardOwner;
    private TextView textCardType;
    private EditText inputDescription;

    private Button buttonCopy;
    private Button buttonQrCode;
    private Button buttonBarcode;

    private CardModel cardModel;

    public HiddenBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_fragment_hidden, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.extract();

        this.init(view);

        this.data();

        this.methodCopyText();
    }

    private void data() {
        this.textCardNumber.setText(CartaHelper.cardNumberFormat(CartaHelper.decryptString(this.cardModel.getCardNumber())));
        this.textCardName.setText(CartaHelper.decryptString(this.cardModel.getCardName()));
        this.textCardOwner.setText(CartaHelper.decryptString(this.cardModel.getCardOwner()));
        this.inputDescription.setText(CartaHelper.decryptString(this.cardModel.getCardDescription()));
    }

    private void extract()
    {
        if (this.getArguments() != null){
            this.cardModel = (CardModel) getArguments().getSerializable("card");
        }
    }

    private void init(View v) {
        this.textCardNumber = v.findViewById(R.id.textCardNumber);
        this.textCardName = v.findViewById(R.id.textCardName);
        this.textCardOwner = v.findViewById(R.id.textCardOwner);
        this.textCardType = v.findViewById(R.id.textCardType);
        this.inputDescription = v.findViewById(R.id.inputDescription);

        this.buttonBarcode = v.findViewById(R.id.buttonBarcode);
        this.buttonCopy = v.findViewById(R.id.buttonCopy);
        this.buttonQrCode = v.findViewById(R.id.buttonQrCode);

        this.buttonCopy.setOnClickListener(this);
        this.buttonBarcode.setOnClickListener(this);
        this.buttonQrCode.setOnClickListener(this);
    }

    private void methodCopyText() {
        textCardNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                CartaHelper.copy(String.valueOf(textCardNumber.getText()), Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));


                return true;
            }
        });

        textCardName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CartaHelper.copy(String.valueOf(textCardName.getText()), Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));

                return true;
            }
        });

        textCardOwner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CartaHelper.copy(String.valueOf(textCardOwner.getText()), Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));

                return false;
            }
        });

        textCardType.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CartaHelper.copy(String.valueOf(textCardType.getText()), Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));
                return false;
            }
        });

        inputDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CartaHelper.copy(String.valueOf(inputDescription.getText()), Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));

                return true;
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartaHelper.copy("Cardholder Name\n" + textCardName.getText().toString() + "\n\nNumber Card\n" + textCardNumber.getText().toString() + "\n\nDescription\n" + inputDescription.getText().toString() +"\n\nDownload MyCarta! Make your life easier and happier!", Objects.requireNonNull(HiddenBottomSheetFragment.this.getActivity()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.buttonQrCode)){
            Bundle bundle = new Bundle();
            bundle.putSerializable("card", this.cardModel);
            CodeBottomSheetFragment codeBottomSheetFragment = new CodeBottomSheetFragment();
            codeBottomSheetFragment.setArguments(bundle);
            codeBottomSheetFragment.show(getActivity().getSupportFragmentManager(), Constant.CODE.QRCODE);
        }else if (v.equals(this.buttonBarcode)){
            Bundle bundle = new Bundle();
            bundle.putSerializable("card", this.cardModel);
            CodeBottomSheetFragment codeBottomSheetFragment = new CodeBottomSheetFragment();
            codeBottomSheetFragment.setArguments(bundle);
            codeBottomSheetFragment.show(getActivity().getSupportFragmentManager(), Constant.CODE.BARCODE);
        }
    }
}
