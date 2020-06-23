package me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.CardFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher.adapter.SwitchCardAdapter;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwitchCardBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private ArrayList<Card> cards;
    SwitchCardAdapter adapter;

    RecyclerView rv;

    ImageView icClose;

    FirebaseAuth auth;
    DatabaseReference reference;
    CardFragment card;
    RecyclerView.LayoutManager layoutManager;

    LinearLayout lottieAnimationView;

    ProgressBar progressBar;


    public SwitchCardBottomSheetFragment(Fragment fragment) {
        // Required empty public constructor
        this.card = (CardFragment) fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.bottom_sheet_fragment_switch_card, container, false);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        icClose = v.findViewById(R.id.icClose);
        progressBar = v.findViewById(R.id.progressBar);

        lottieAnimationView = v.findViewById(R.id.linearNotFound);

        rv = v.findViewById(R.id.recyclerSwitchCard);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        rv.setVisibility(View.GONE);

        icClose.setOnClickListener(this);

        lottieAnimationView.setVisibility(View.GONE);

        reference.child("card").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                methodData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void methodData(DataSnapshot dataSnapshot) {
        try {
            cards = new ArrayList<>();

            rv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                Card card = snapshot.getValue(Card.class);
                card.setKey(snapshot.getKey());

                cards.add(card);
            }
            if (dataSnapshot.getChildrenCount() != 0){
                adapter = new SwitchCardAdapter(cards, card, this);
                rv.setAdapter(adapter);
                lottieAnimationView.setVisibility(View.GONE);
            }else{
                lottieAnimationView.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == icClose){
            this.dismiss();
        }
    }
}
