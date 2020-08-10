package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.bottomsheet.AddCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.adapter.HomeAdapter;
import me.muhammadfaisal.mycarta.v2.bottomsheet.MenuBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ImageView imageProfile;

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
        this.imageProfile = findViewById(R.id.imageProfile);

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

        reference.child(Constant.USER_PATH).child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                if (userModel != null) {
                    Glide.with(HomeActivity.this)
                            .load(userModel.getImage())
                            .into(HomeActivity.this.imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        this.imageProfile.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addCard(View v){
        AddCardBottomSheetFragment addCardBottomSheetFragment = new AddCardBottomSheetFragment();
        addCardBottomSheetFragment.show(getSupportFragmentManager(), Constant.TAG.HOME_ACTIVITY_TAG);
    }

    public void menu(View view){
        MenuBottomSheetFragment menuBottomSheetFragment = new MenuBottomSheetFragment();
        menuBottomSheetFragment.show(getSupportFragmentManager(), Constant.TAG.HOME_ACTIVITY_TAG);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.imageProfile)){
            this.profile();
        }
    }

    private void profile() {
        CartaHelper.move(this, ProfileActivity.class, false);
    }
}
