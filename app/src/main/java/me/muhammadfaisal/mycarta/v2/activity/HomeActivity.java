package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.bottomsheet.AddCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.adapter.HomeAdapter;
import me.muhammadfaisal.mycarta.v2.bottomsheet.MenuBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<CardModel> cards;

    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.v2_activity_home);

        this.init();
    }

    private void init() {
        this.recyclerView = findViewById(R.id.recyclerCard);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.query = reference.child(Constant.CARD_PATH).child(Objects.requireNonNull(auth.getUid())).orderByChild("prioritize").equalTo(CartaHelper.encryptString(Constant.CODE.YES));
        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    HomeActivity.this.cards = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        CardModel card = snapshot.getValue(CardModel.class);

                        cards.add(card);
                    }

                    HomeActivity.this.recyclerView.setAdapter(new HomeAdapter(HomeActivity.this.cards, HomeActivity.this));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addCard(View v){
        AddCardBottomSheetFragment addCardBottomSheetFragment = new AddCardBottomSheetFragment();
        addCardBottomSheetFragment.show(getSupportFragmentManager(), Constant.TAG.HOME_ACTIVITY_TAG);
    }

    public void menu(View view){
        MenuBottomSheetFragment menuBottomSheetFragment = new MenuBottomSheetFragment();
        menuBottomSheetFragment.show(getSupportFragmentManager(), Constant.TAG.HOME_ACTIVITY_TAG);
    }
}
