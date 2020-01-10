package me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.hidden;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.helper.UniversalHelper;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.switcher.adapter.SwitchCardAdapter;
import me.muhammadfaisal.mycarta.home.fragment.card.model.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class HiddenBottomSheetFragment extends BottomSheetDialogFragment{

    private TextView textCardNumber, textCardName, textCardCVV,textExpiry, typeCard;
    private EditText inputDescription;
    private ImageView imageType;
    private Button buttonCopy;

    UniversalHelper helper;

    public HiddenBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_hidden, container, false);

        initWidget(v);

        gettingData(imageType);

        return v;
    }

    private void initWidget(View v) {
        ImageView icClose = v.findViewById(R.id.icClose);
        imageType = v.findViewById(R.id.imageType);

        textCardNumber = v.findViewById(R.id.textCardNumber);
        textCardName = v.findViewById(R.id.textCardName);
        textCardCVV = v.findViewById(R.id.textCardCvv);
        textExpiry = v.findViewById(R.id.textExpiry);

        inputDescription = v.findViewById(R.id.inputDescription);

        typeCard  = v.findViewById(R.id.typeCard);

        buttonCopy = v.findViewById(R.id.buttonCopy);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void gettingData(ImageView imageType) {
        StringBuilder s;
        s = new StringBuilder(getArguments().getString("numberOnCard"));

        for (int i = 4; i < s.length(); i += 5){
            s.insert(i, " ");
        }
        textCardName.setText(getArguments().getString("nameOnCard"));
        textCardNumber.setText(String.valueOf(s));
        textCardCVV.setText(getArguments().getString("cvv"));
        textExpiry.setText(getArguments().getString("expiry"));
        inputDescription.setText(String.valueOf(getArguments().getString("desc")));

        String type = getArguments().getString("typeOnCard");

        typeCard.setText(getArguments().getString("typeOnCard"));

        filteringImageCardType(type, imageType);

        methodCopyText();
    }

    private void methodCopyText() {
        textCardNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",textCardNumber.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        textCardName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",textCardName.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        textCardCVV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",textCardCVV.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        textExpiry.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",textExpiry.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        inputDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",inputDescription.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy", "Cardholder Name\n" + textCardName.getText().toString() + "\n\nNumber Card\n" + textCardNumber.getText().toString() + "\n\nCVV\n" + textCardCVV.getText().toString() + "\n\nExpiry\n" + textExpiry.getText().toString()  + "\n\nDescription\n" + inputDescription.getText().toString() +"\n\nDownload MyCarta! Make your life easier and happier!");
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied To Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filteringImageCardType(String type, ImageView imageType) {
        Resources resources = getResources();

        if (type.equals("Visa")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.visa));
        }else if(type.equals("American Express")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.american_express));
        }else if(type.equals("Mastercard")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.mastercard));
        }else if (type.equals("JCB")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.jcb));
        }else{
            imageType.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher));
        }

    }
}
