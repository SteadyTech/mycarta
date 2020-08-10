package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.account.bottom_sheet.AboutAppBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.account.bottom_sheet.ChangeCartaBoardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;
import me.muhammadfaisal.mycarta.v2.bottomsheet.PinBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;

public class ProfileActivity extends AppCompatActivity {

    private TextView textName;
    private TextView textEmail;
    private CircleImageView civProfile;

    private UserModel userModel;

    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        this.init();

        this.data();
    }

    private void init() {
        this.textName = findViewById(R.id.textName);
        this.textEmail = findViewById(R.id.textEmail);
        this.civProfile = findViewById(R.id.imageProfile);

        this.auth = FirebaseAuth.getInstance();
        this.reference = FirebaseDatabase.getInstance().getReference();
    }

    private void data() {
        if (this.auth.getCurrentUser() != null) {
            this.reference.child(Constant.USER_PATH).child(this.auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);

                    if (user != null) {
                        ProfileActivity.this.userModel = user;
                        ProfileActivity.this.textName.setText(user.getName());
                        ProfileActivity.this.textEmail.setText(user.getEmail());
                        Glide.with(ProfileActivity.this).load(user.getImage()).into(ProfileActivity.this.civProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void changePIN(View view){
        PinBottomSheetFragment pinBottomSheetFragment = new PinBottomSheetFragment();
        pinBottomSheetFragment.show(this.getSupportFragmentManager(), "ChangePIN");
    }

    public void cartaboard(View view){
        ChangeCartaBoardBottomSheetFragment cartaBoardBottomSheetFragment = new ChangeCartaBoardBottomSheetFragment();
        cartaBoardBottomSheetFragment.show(this.getSupportFragmentManager(), "CartaBoard");
    }

    public void aboutApp(View view){
        AboutAppBottomSheetFragment aboutAppBottomSheetFragment = new AboutAppBottomSheetFragment();
        aboutAppBottomSheetFragment.show(this.getSupportFragmentManager(), "AboutApp");
    }

    public void share(View view){
        String shareMessages = "Make your life smarter with MyCarta!. \n\n Get it on Google Play\nhttps://play.google.com/store/apps/details?id=" + this.getPackageName();
        Intent i = new Intent();
        i.setType("text/plain");
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, shareMessages);
        startActivity(Intent.createChooser(i,"Share MyCarta"));
    }

    public void editProfile(View view){
        super.startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class).putExtra(Constant.CODE.USER, this.userModel));
    }
}