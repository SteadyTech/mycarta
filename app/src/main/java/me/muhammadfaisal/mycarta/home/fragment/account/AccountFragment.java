package me.muhammadfaisal.mycarta.home.fragment.account;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.fragment.account.bottom_sheet.AboutAppBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.account.bottom_sheet.ChangeCartaBoardBottomSheetFragment;
import me.muhammadfaisal.mycarta.home.fragment.account.view.ChangePasswordActivity;
import me.muhammadfaisal.mycarta.login.LoginActivity;
import me.muhammadfaisal.mycarta.pin.PinBottomSheetFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    CircleImageView imageProfile;
    TextView textName,textEmail, textCartaBoard, textAbout, textChangePin;

    Button buttonLogout, buttonChangePassword;

    DatabaseReference reference;
    FirebaseAuth auth;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        initWidget(v);

        return v;
    }

    @SuppressLint("SetTextI18n")
    private void initWidget(View v) {
        imageProfile = v.findViewById(R.id.imageProfile);
        textName = v.findViewById(R.id.textName);
        textEmail = v.findViewById(R.id.textEmail);
        textCartaBoard = v.findViewById(R.id.textCartaBoard);
        textAbout = v.findViewById(R.id.textAbout);
        buttonLogout = v.findViewById(R.id.buttonLogout);
        buttonChangePassword = v.findViewById(R.id.buttonChangePassword);
        textChangePin = v.findViewById(R.id.textChangePin);

        auth = FirebaseAuth.getInstance();

        String email = auth.getCurrentUser().getEmail();
        String name = auth.getCurrentUser().getDisplayName();

        textEmail.setText(email);

        if(name.equals("")){

            String uid = auth.getUid().substring(0, 16);

            textName.setText("User"+uid);
        }else{
            textName.setText(name);
        }

        if (auth.getCurrentUser().getPhotoUrl() != null){
            String picture = auth.getCurrentUser().getPhotoUrl().toString();

            Glide.with(getActivity())
                    .load(picture)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageProfile);
        }

        buttonLogout.setOnClickListener(this);
        buttonChangePassword.setOnClickListener(this);
        textCartaBoard.setOnClickListener(this);
        textChangePin.setOnClickListener(this);
        textAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout){
            methodLogout();
        }
        if (view == buttonChangePassword){
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        }
        if (view == textCartaBoard){
            methodChangeCartaBoard();
        }
        if (view == textChangePin){
            methodChangePIN();
        }
        if (view == textAbout){
            methodAboutApp();
        }
    }

    private void methodAboutApp() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new AboutAppBottomSheetFragment();
        bottomSheetDialogFragment.show(getFragmentManager(), "AboutApp");
    }

    private void methodChangePIN() {
        PinBottomSheetFragment pinBottomSheetFragment = new PinBottomSheetFragment();
        pinBottomSheetFragment.show(getFragmentManager(), "ChangePIN");
    }

    private void methodChangeCartaBoard() {
        BottomSheetDialogFragment dialogFragment = new ChangeCartaBoardBottomSheetFragment();
        dialogFragment.show(getFragmentManager(), "CartaBoard");
    }

    private void methodLogout() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Logout")
                .setMessage("Are you sure want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        callingSharedPrefereces();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        dialog.show();
    }

    private void callingSharedPrefereces() {
        SharedPreferences sp = this.getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("userid");
        editor.apply();
    }

}
