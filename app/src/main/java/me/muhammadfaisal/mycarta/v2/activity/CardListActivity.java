package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;
import me.muhammadfaisal.mycarta.v2.adapter.CardAdapter;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

public class CardListActivity extends AppCompatActivity {

    private RecyclerView recyclerCard;
    private ArrayList<CardModel> cardModels;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private String uid;

    {
        this.cardModels = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_card_list);

        this.init();

        this.query();
    }


    private void init() {
        this.recyclerCard = findViewById(R.id.recyclerCard);
        this.recyclerCard.setLayoutManager(new StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL));

        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.auth = FirebaseAuth.getInstance();

        this.uid = this.auth.getUid();
    }

    private void query() {
        this.databaseReference.child(Constant.CARD_PATH).child(this.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardModel cardModel = snapshot.getValue(CardModel.class);

                    CardListActivity.this.cardModels.add(cardModel);
                }
                CardListActivity.this.recyclerCard.setAdapter(new CardAdapter(CardListActivity.this.cardModels, CardListActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
