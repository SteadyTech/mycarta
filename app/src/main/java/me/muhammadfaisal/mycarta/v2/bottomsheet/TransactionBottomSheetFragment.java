package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.adapter.TransactionAdapter;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;
import me.muhammadfaisal.mycarta.v2.model.firebase.TransactionModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerTransaction;
    private ProgressBar progressBar;
    private LinearLayout linearDataNotFound;

    private DatabaseReference databaseReference;
    private Query query;
    private FirebaseAuth firebaseAuth;
    private CardModel cardModel;
    private ArrayList<TransactionModel> transactionModels = new ArrayList<>();

    public ArrayList<HashMap<String, Object>> map;

    public TransactionBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init(view);
        this.data();
    }

    private void init(View view) {
        this.recyclerTransaction = view.findViewById(R.id.recyclerTransaction);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.linearDataNotFound = view.findViewById(R.id.linearNotFound);

        this.recyclerTransaction.setLayoutManager(new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL, false));

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.cardModel = (CardModel) Objects.requireNonNull(this.getArguments()).getSerializable("card");
    }

    private void data() {
        this.query = this.databaseReference.child(Constant.TRANSACTION_PATH).child(firebaseAuth.getUid()).orderByChild("cardNumber").equalTo(cardModel.getCardNumber());
        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TransactionBottomSheetFragment.this.transactionModels.clear();
                String stringDate = null;
                TransactionBottomSheetFragment.this.map = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    HashMap<String, Object> hashMap = new HashMap<>();

                    TransactionModel transactionModel = snapshot.getValue(TransactionModel.class);
                    if (transactionModel != null) {
                        if (stringDate == null) {
                            stringDate = CartaHelper.decryptString(transactionModel.getDate());
                            hashMap.put("header", stringDate);
                            TransactionBottomSheetFragment.this.map.add(hashMap);
                        } else {
                            if (!Objects.equals(stringDate, CartaHelper.decryptString(transactionModel.getDate()))) {
                                stringDate = CartaHelper.decryptString(transactionModel.getDate());
                                hashMap.put("header", stringDate);
                                TransactionBottomSheetFragment.this.map.add(hashMap);
                            }
                        }
                    }

                    TransactionBottomSheetFragment.this.transactionModels.add(transactionModel);
                    TransactionBottomSheetFragment.this.progressBar.setVisibility(View.GONE);
                    hashMap = new HashMap<>();
                    hashMap.put("detail", transactionModel);
                    TransactionBottomSheetFragment.this.map.add(hashMap);
                }

                if (dataSnapshot.getChildrenCount() == 0) {
                    TransactionBottomSheetFragment.this.linearDataNotFound.setVisibility(View.VISIBLE);
                    TransactionBottomSheetFragment.this.progressBar.setVisibility(View.GONE);
                } else {
                    TransactionBottomSheetFragment.this.linearDataNotFound.setVisibility(View.GONE);
                    TransactionBottomSheetFragment.this.progressBar.setVisibility(View.GONE);
                }
                TransactionBottomSheetFragment.this.recyclerTransaction.setAdapter(new TransactionAdapter(TransactionBottomSheetFragment.this.transactionModels, TransactionBottomSheetFragment.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
