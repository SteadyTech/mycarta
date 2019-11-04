package me.muhammadfaisal.mycarta.home.fragment.card;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.helper.UniversalHelper;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.add.AddCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.switcher.SwitchCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.bottom_sheet.switcher.adapter.SwitchCardAdapter;
import me.muhammadfaisal.mycarta.home.fragment.card.model.Card;
import me.muhammadfaisal.mycarta.pin.PinBottomSheetFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment implements View.OnClickListener, SwitchCardAdapter.CardBottomSheetCallback {

    CardView cardHidden , cardSwitcher, cardAdd;

    public TextView textCardNumber, textCardName, textCardCVV,textExpiry, hintName , hintNumber, hintCvv, hintExpiry;
    public RelativeLayout backgroundCard;
    public LinearLayout linearPleaseSelect;
    ArrayList<Card> cards;

    ImageView imageType;
    public Button buttonDelete, buttonEdit, buttonPay;

    private Card deleteCard;

    SwitchCardAdapter switchCardAdapter;

    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;

    public CardFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_card, container, false);

        initWidget(v);

        initCreditCards(v);

        initActions(v);

        noCardSelected();

        return v;
    }

    private void initWidget(View v) {
        linearPleaseSelect = v.findViewById(R.id.linearSelectCard);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        buttonDelete = v.findViewById(R.id.buttonDelete);
        buttonEdit = v.findViewById(R.id.buttonEdit);
        buttonPay = v.findViewById(R.id.buttonPay);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(final Card card, final Bundle bundle) {
        deleteCard = card;
        final UniversalHelper helper = new UniversalHelper();

        backgroundCard.setBackgroundColor(bundle.getInt("color"));

        Resources resources = getResources();

        if (card.getType().equals("Visa")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.visa));
        }else if(card.getType().equals("American Express")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.american_express));
        }else if(card.getType().equals("Mastercard")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.mastercard));
        }else if (card.getType().equals("JCB")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.jcb));
        }else{
            imageType.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher));
        }

        buttonDelete.setVisibility(View.VISIBLE);
        backgroundCard.setVisibility(View.VISIBLE);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonPay.setVisibility(View.VISIBLE);
        linearPleaseSelect.setVisibility(View.GONE);

        textCardNumber.setText("**** **** **** **** " + String.valueOf(card.getNumberCard()).substring(String.valueOf(card.getNumberCard()).length() - 4));
        textCardName.setText(helper.decryptString(card.getName()));
        textCardCVV.setText("**" + String.valueOf(card.getCvv()).substring(String.valueOf(card.getCvv()).length() - 1));
        textExpiry.setText(card.getExp());

        cardHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", helper.decryptString(card.getName()));
                bundle.putLong("number", card.getNumberCard());
                bundle.putInt("cvv", card.getCvv());
                bundle.putString("expiry", card.getExp());
                bundle.putString("description", card.getDescripton());
                bundle.putInt("pin", card.getPin());
                bundle.putString("type", card.getType());

                BottomSheetDialogFragment bottomSheetDialogFragment = new PinBottomSheetFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "PinCard");
                bottomSheetDialogFragment.setArguments(bundle);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", card.getName());
                bundle.putLong("number", card.getNumberCard());
                bundle.putInt("cvv", card.getCvv());
                bundle.putString("expiry", card.getExp());
                bundle.putString("description", card.getDescripton());
                bundle.putInt("pin", card.getPin());
                bundle.putString("keyPrimary", card.getKey());
                bundle.putString("type", card.getType());

                BottomSheetDialogFragment bottomSheetDialogFragment = new PinBottomSheetFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "PinEditCard");
                bottomSheetDialogFragment.setArguments(bundle);
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", card.getName());
                bundle.putLong("number", card.getNumberCard());
                bundle.putInt("cvv", card.getCvv());
                bundle.putString("keyPrimary", card.getKey());

                BottomSheetDialogFragment bottomSheetDialogFragment = new PinBottomSheetFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "PinPay");
                bottomSheetDialogFragment.setArguments(bundle);
            }
        });

        if(switchCardAdapter.isDark(bundle.getInt("color"))){
            textCardName.setTextColor(Color.WHITE);
            textCardNumber.setTextColor(Color.WHITE);
            textCardCVV.setTextColor(Color.WHITE);
            textExpiry.setTextColor(Color.WHITE);
            hintNumber.setTextColor(Color.WHITE);
            hintCvv.setTextColor(Color.WHITE);
            hintName.setTextColor(Color.WHITE);
            hintExpiry.setTextColor(Color.WHITE);
        }else{
            textCardName.setTextColor(Color.BLACK);
            textCardNumber.setTextColor(Color.BLACK);
            textCardCVV.setTextColor(Color.BLACK);
            textExpiry.setTextColor(Color.BLACK);
            hintNumber.setTextColor(Color.BLACK);
            hintCvv.setTextColor(Color.BLACK);
            hintName.setTextColor(Color.BLACK);
            hintExpiry.setTextColor(Color.BLACK);
        }

    }

    private void initActions(View v) {
        cardHidden = v.findViewById(R.id.cardHiddenInformation);
        cardAdd = v.findViewById(R.id.cardAddNew);
        cardSwitcher = v.findViewById(R.id.cardSwitcher);

        cardAdd.setOnClickListener(this);
        cardSwitcher.setOnClickListener(this);
        cardHidden.setOnClickListener(this);
        switchCardAdapter = new SwitchCardAdapter(cards,this, null);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SweetAlertDialog dialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                dialogLoading.setContentText("Loading");
                dialogLoading.setCancelable(false);
                dialogLoading.show();

                final SweetAlertDialog dialogSuccess = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialogSuccess.setContentText("Deleted");
                dialogSuccess.show();

                final SweetAlertDialog dialogError = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);


                String userID = auth.getUid();
                if (reference != null){
                    reference.child("card").child(userID).child(deleteCard.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialogSuccess.show();
                                dialogLoading.cancel();

                                noCardSelected();

                            }else{
                                dialogError.setContentText("Error " + task.getException());
                                dialogError.show();
                                dialogLoading.cancel();
                            }
                        }
                    });
                }
            }
        });
    }

    public void noCardSelected() {
        linearPleaseSelect.setVisibility(View.VISIBLE);
        backgroundCard.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.GONE);
        buttonEdit.setVisibility(View.GONE);
        buttonPay.setVisibility(View.GONE);
        cardHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Please Select Card Before!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initCreditCards(View v) {
        textCardNumber = v.findViewById(R.id.textCardNumber);
        textCardName = v.findViewById(R.id.textCardName);
        textCardCVV = v.findViewById(R.id.textCardCvv);
        backgroundCard = v.findViewById(R.id.backgroundCard);
        backgroundCard.setVisibility(View.GONE);
        textExpiry = v.findViewById(R.id.textExpiry);
        hintCvv = v.findViewById(R.id.hintCvv);
        hintExpiry = v.findViewById(R.id.hintExpiry);
        hintNumber = v.findViewById(R.id.hintCardNumber);
        hintName = v.findViewById(R.id.hintCardName);

        imageType = v.findViewById(R.id.imageType);
    }

    @Override
    public void onClick(View view) {
        if (view == cardAdd) {
            methodAddNewCard();
        }
        if (view == cardSwitcher) {
            methodSwitchCard();
        }
    }
    private void methodSwitchCard() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new SwitchCardBottomSheetFragment(this);
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "Switcher Bottom Sheet");
    }

    private void methodAddNewCard() {
        BottomSheetDialogFragment addCardBottomSheetFragment = new AddCardBottomSheetFragment();
        addCardBottomSheetFragment.show(getActivity().getSupportFragmentManager(),"Bottom Sheet");
    }
}
