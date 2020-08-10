package me.muhammadfaisal.mycarta.v1.home.fragment.card;


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

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.bottomsheet.AddCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher.SwitchCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher.adapter.SwitchCardAdapter;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;
import me.muhammadfaisal.mycarta.v2.bottomsheet.PinBottomSheetFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment implements View.OnClickListener, SwitchCardAdapter.CardBottomSheetCallback {

    CardView cardHidden , cardSwitcher, cardAdd;

    public TextView textCardNumber, textCardName, textCardCVV,textExpiry, hintName , hintNumber, hintCvv, hintExpiry;
    public RelativeLayout backgroundCard;
    public LinearLayout linearPleaseSelect;
    ArrayList<Card> cards;

    LottieAnimationView lottieTapHere;
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

        initWidgets(v);

        initCreditCards(v);

        initActions(v);

        noCardSelected();

        return v;
    }

    private void initWidgets(View v) {
        linearPleaseSelect = v.findViewById(R.id.linearSelectCard);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        buttonDelete = v.findViewById(R.id.buttonDelete);
        buttonEdit = v.findViewById(R.id.buttonEdit);
        buttonPay = v.findViewById(R.id.buttonPay);

        lottieTapHere = v.findViewById(R.id.lottie);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(final Card card, final Bundle bundle) {
        deleteCard = card;
        final CartaHelper helper = new CartaHelper();

        backgroundCard.setBackgroundColor(bundle.getInt("color"));

        Resources resources = getResources();

        final String decryptedCardType = helper.decryptString(card.getType());
        final String decryptedCardNumber = helper.decryptString(card.getNumberCard());
        final String decryptedNameCard = helper.decryptString(card.getName());
        final String decryptedExpiry = helper.decryptString(card.getExp());
        final String decryptedCvv = helper.decryptString(card.getCvv());
        final String decryptedDescription = helper.decryptString(card.getDescripton());

        if (decryptedCardType.equals("Visa")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.visa));
        }else if(decryptedCardType.equals("American Express")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.american_express));
        }else if(decryptedCardType.equals("Mastercard")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.mastercard));
        }else if (decryptedCardType.equals("JCB")){
            imageType.setImageDrawable(resources.getDrawable(R.drawable.jcb));
        }else{
            imageType.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher));
        }

        buttonDelete.setVisibility(View.VISIBLE);
        backgroundCard.setVisibility(View.VISIBLE);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonPay.setVisibility(View.VISIBLE);
        linearPleaseSelect.setVisibility(View.GONE);

        textCardNumber.setText("**** **** **** **** " + decryptedCardNumber.substring(decryptedCardNumber.length() - 4));
        textCardName.setText(decryptedNameCard);
        textCardCVV.setText("**" + decryptedCvv.substring(decryptedCvv.length() - 1));
        textExpiry.setText(decryptedExpiry);

        cardHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", decryptedNameCard);
                bundle.putString("number", decryptedCardNumber);
                bundle.putString("cvv", decryptedCvv);
                bundle.putString("expiry", decryptedExpiry);
                bundle.putString("description", decryptedDescription);
                bundle.putString("type", decryptedCardType);

                BottomSheetDialogFragment bottomSheetDialogFragment = new PinBottomSheetFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "PinCard");
                bottomSheetDialogFragment.setArguments(bundle);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", decryptedNameCard);
                bundle.putString("number", decryptedCardNumber);
                bundle.putString("cvv", decryptedCvv);
                bundle.putString("expiry", decryptedExpiry);
                bundle.putString("description", decryptedDescription);
                bundle.putString("type", decryptedCardType);
                bundle.putString("keyPrimary", card.getKey());

                BottomSheetDialogFragment bottomSheetDialogFragment = new PinBottomSheetFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "PinEditCard");
                bottomSheetDialogFragment.setArguments(bundle);
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("name", decryptedNameCard);
                bundle.putString("number", decryptedCardNumber);
                bundle.putString("cvv", decryptedCvv);
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

                String userID = auth.getUid();
                if (reference != null){
                    reference.child("card").child(userID).child(deleteCard.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialogLoading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialogLoading.setContentText("Deleted");

                                noCardSelected();

                            }else{
                                dialogLoading.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                dialogLoading.setContentText("Error : " + task.getException());
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
                lottieTapHere.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.RubberBand).duration(700).repeat(1).playOn(cardSwitcher);
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
