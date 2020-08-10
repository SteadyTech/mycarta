package me.muhammadfaisal.mycarta.v2.bottomsheet;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;
import me.muhammadfaisal.mycarta.v2.activity.EditProfileActivity;
import me.muhammadfaisal.mycarta.v2.activity.ProfileActivity;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;

public class ConfirmationBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private String title;
    private String message;
    private Integer code;
    private Long delay;

    private TextView textTitle;
    private TextView textMessage;
    private TextView textCancel;
    private Button buttonOk;
    private LinearLayout linearContent;
    private LottieAnimationView lottieAnimationView;

    private UserModel userModel;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Uri uri;

    public ConfirmationBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.textCancel = view.findViewById(R.id.textCancel);
        this.buttonOk = view.findViewById(R.id.buttonOk);
        this.textTitle = view.findViewById(R.id.textTitle);
        this.textMessage = view.findViewById(R.id.textMessage);
        this.lottieAnimationView = view.findViewById(R.id.lottie2);
        this.linearContent = view.findViewById(R.id.linearContent);

        if (this.getArguments() != null) {
            this.title = this.getArguments().getString(Constant.KEY.TITLE_MESSAGE);
            this.message = this.getArguments().getString(Constant.KEY.DESCRIPTION_MESSAGE);
            this.code = this.getArguments().getInt(Constant.KEY.CODE, 0);
            this.delay = this.getArguments().getLong(Constant.KEY.DELAY, 1);
            this.userModel = (UserModel) this.getArguments().getSerializable(Constant.CODE.USER);

            Log.d("UserModel", userModel.getImage());

            Log.d("Delay", this.delay.toString());

            this.uri = Uri.parse(this.userModel.getImage());

            this.textTitle.setText(this.title);
            this.textMessage.setText(this.message);
        }

        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = this.firebaseStorage.getReference(Constant.IMAGE_PATH);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = this.firebaseAuth.getCurrentUser();

        if (this.delay != 1){
            new CountDownTimer( this.delay, 1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    if (ConfirmationBottomSheetFragment.this.getActivity() != null) {
                        ConfirmationBottomSheetFragment.this.buttonOk.setBackgroundColor(ConfirmationBottomSheetFragment.this.getActivity().getResources().getColor(R.color.colorAccent));
                        ConfirmationBottomSheetFragment.this.buttonOk.setEnabled(false);
                        ConfirmationBottomSheetFragment.this.buttonOk.setText(getResources().getString(R.string.yes_update).concat("(").concat(String.valueOf(millisUntilFinished / 1000)).concat(")"));
                    }
                }

                @Override
                public void onFinish() {
                    if (ConfirmationBottomSheetFragment.this.getActivity() != null) {
                        ConfirmationBottomSheetFragment.this.buttonOk.setBackgroundColor(ConfirmationBottomSheetFragment.this.getResources().getColor(R.color.colorBluePrimary));
                        ConfirmationBottomSheetFragment.this.buttonOk.setEnabled(true);
                        ConfirmationBottomSheetFragment.this.buttonOk.setText(getResources().getString(R.string.yes_update));
                    }
                }
            }.start();
        }

        this.textCancel.setOnClickListener(this);
        this.buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.textCancel)){
            this.dismiss();
        }else if (v.equals(this.buttonOk)){
            this.process();
        }
    }

    private void process() {
        if (this.getTag() != null) {
            if (this.getTag().equals(Constant.TAG.UPDATE_PROFILE)){
                this.updateProfile();
            }
        }
    }

    private void updateProfile() {
        if (this.uri != null ) {
            ConfirmationBottomSheetFragment.this.loading();
            StorageReference sfImage = this.storageReference.child(Constant.IMAGE_PATH + "/" +  firebaseUser.getUid() + "/" + this.getFileExtension(this.uri));

            if (sfImage != null) {
                UserModel user = new UserModel();
                user.setImage(userModel.getImage());
                user.setName(userModel.getName());
                user.setEmail(userModel.getEmail());
                ConfirmationBottomSheetFragment.this.databaseReference.child(Constant.USER_PATH).child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            CartaHelper.move(getActivity(), ProfileActivity.class, false);
                        }else{
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                sfImage.putFile(this.uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null){
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        UserModel user = new UserModel();
                                        user.setImage(uri.toString());
                                        user.setName(userModel.getName());
                                        user.setEmail(userModel.getEmail());
                                        ConfirmationBottomSheetFragment.this.databaseReference.child(Constant.USER_PATH).child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    CartaHelper.move(getActivity(), ProfileActivity.class, false);
                                                }else{
                                                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    private void loading(){
        this.linearContent.setVisibility(View.GONE);
        this.lottieAnimationView.setVisibility(View.VISIBLE);
    }
    private void unloading(){
        this.dismiss();
    }
}