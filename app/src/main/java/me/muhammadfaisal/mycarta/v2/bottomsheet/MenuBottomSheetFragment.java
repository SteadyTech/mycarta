package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.hidden.HiddenBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.activity.CardListActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private CardView cardTransaction;
    private CardView cardInfo;
    private LinearLayout linearFromCard;
    private LinearLayout linearFromHome;

    private ConstraintLayout constraintCard;

    private CardModel cardModel;

    public MenuBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.init(view);
    }

    private void init(View view) {
        this.cardTransaction = view.findViewById(R.id.cardTransaction);
        this.cardInfo = view.findViewById(R.id.cardInfo);
        this.linearFromCard = view.findViewById(R.id.linearFromCard);
        this.linearFromHome = view.findViewById(R.id.linearFromHome);
        this.constraintCard = view.findViewById(R.id.constraintCard);

        if (this.getTag().equals(Constant.TAG.DETAIL_CARD_ACTIVITY_TAG)){
            this.fromDetailCard();
            this.cardModel = (CardModel) Objects.requireNonNull(this.getArguments()).getSerializable("card");
            this.cardTransaction.setOnClickListener(this);
            this.cardInfo.setOnClickListener(this);
        }else {
            this.fromHome();
        }

        this.constraintCard.setOnClickListener(this);
    }

    public void transaction(){
        this.dismiss();
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", cardModel);
        CreateTransactionBottomSheetFragment createTransactionBottomSheetFragment = new CreateTransactionBottomSheetFragment();
        createTransactionBottomSheetFragment.setArguments(bundle);
        createTransactionBottomSheetFragment.show(this.getActivity().getSupportFragmentManager(), this.getTag());
    }

    private void info(){
        this.dismiss();
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", cardModel);
        HiddenBottomSheetFragment hiddenBottomSheetFragment = new HiddenBottomSheetFragment();
        hiddenBottomSheetFragment.setArguments(bundle);
        hiddenBottomSheetFragment.show(this.getActivity().getSupportFragmentManager(), this.getTag());
    }

    public void cardList(){
        startActivity(new Intent(this.getActivity(), CardListActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.cardTransaction)){
            this.transaction();
        }else if (v.equals(this.cardInfo)){
            this.info();
        }else if (v.equals(this.constraintCard)){
            this.cardList();
        }
    }

    private void fromHome(){
        this.linearFromCard.setVisibility(View.GONE);
    }

    private void fromDetailCard(){
        this.linearFromHome.setVisibility(View.GONE);
    }
}
