package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.realm.Realm;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.edit.EditCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.hidden.HiddenBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.activity.ArticleListActivity;
import me.muhammadfaisal.mycarta.v2.activity.CardListActivity;
import me.muhammadfaisal.mycarta.v2.activity.HomeActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;
import me.muhammadfaisal.mycarta.v2.model.firebase.TransactionModel;
import me.muhammadfaisal.mycarta.v2.model.realm.BalanceDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private CardView cardTransaction;
    private CardView cardInfo;
    private CardView cardDelete;
    private CardView cardEdit;
    private LinearLayout linearFromCard;
    private LinearLayout linearFromHome;

    private ConstraintLayout constraintCard;
    private ConstraintLayout constraintArticle;

    private CardModel cardModel;

    public MenuBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        this.cardDelete = view.findViewById(R.id.cardDelete);
        this.cardEdit = view.findViewById(R.id.cardEdit);
        this.linearFromCard = view.findViewById(R.id.linearFromCard);
        this.linearFromHome = view.findViewById(R.id.linearFromHome);
        this.constraintCard = view.findViewById(R.id.constraintCard);
        this.constraintArticle = view.findViewById(R.id.constraintArticle);

        if (this.getTag() != null) {
            if (this.getTag().equals(Constant.TAG.DETAIL_CARD_ACTIVITY_TAG)){
                this.fromDetailCard();
                this.cardModel = (CardModel) Objects.requireNonNull(this.getArguments()).getSerializable("card");
                this.cardTransaction.setOnClickListener(this);
                this.cardInfo.setOnClickListener(this);
            }else {
                this.fromHome();
            }
        }

        this.constraintCard.setOnClickListener(this);
        this.constraintArticle.setOnClickListener(this);
        this.cardDelete.setOnClickListener(this);
        this.cardEdit.setOnClickListener(this);
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
        }else if (v.equals(this.cardDelete)){
            this.deleteCard();
        }else if (v.equals(this.constraintArticle)){
            this.articleList();
        }else if (v.equals(this.cardEdit)){
            this.editCard();
        }
    }

    private void editCard() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", cardModel);
        PinBottomSheetFragment pinBottomSheetFragment = new PinBottomSheetFragment();
        pinBottomSheetFragment.setArguments(bundle);
        if (this.getActivity() != null) {
            pinBottomSheetFragment.show(this.getActivity().getSupportFragmentManager(), "PinEditCard");
        }
    }

    private void articleList() {
        startActivity(new Intent(this.getActivity(), ArticleListActivity.class));
    }

    private void deleteCard() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d("Card Key", this.cardModel.getCardNumber());

        try{
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NotNull Realm realm1) {
                    BalanceDatabase balanceDatabase = realm1.where(BalanceDatabase.class).equalTo("cardID", MenuBottomSheetFragment.this.cardModel.getCardNumber()).findFirst();
                    if (balanceDatabase != null) {
                        balanceDatabase.deleteFromRealm();
                    }else {
                        Log.d("BalanceNull", "Balance Null");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        Query query = databaseReference.child(Constant.CARD_PATH).child(Objects.requireNonNull(auth.getUid())).orderByChild("cardNumber").equalTo(this.cardModel.getCardNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   final CardModel cardModel = snapshot.getValue(CardModel.class);

                   if (cardModel != null) {
                       databaseReference.child(Constant.CARD_PATH).child(auth.getUid()).child(Objects.requireNonNull(snapshot.getKey())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(MenuBottomSheetFragment.this.getActivity(), MenuBottomSheetFragment.this.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(MenuBottomSheetFragment.this.getActivity(), HomeActivity.class));
                               }else{
                                   Toast.makeText(MenuBottomSheetFragment.this.getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query qTransaction = databaseReference.child(Constant.TRANSACTION_PATH).child(auth.getUid()).orderByChild("cardNumber").equalTo(this.cardModel.getCardNumber());
        qTransaction.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final TransactionModel transactionModel = snapshot.getValue(TransactionModel.class);

                    if (transactionModel != null) {
                        databaseReference.child(Constant.TRANSACTION_PATH).child(auth.getUid()).child(Objects.requireNonNull(snapshot.getKey())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MenuBottomSheetFragment.this.getActivity(), "Success Delete Record Transaction!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MenuBottomSheetFragment.this.getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fromHome(){
        this.linearFromCard.setVisibility(View.GONE);
    }

    private void fromDetailCard(){
        this.linearFromHome.setVisibility(View.GONE);
    }
}
