package me.muhammadfaisal.mycarta.v2.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private CircleImageView imageProfile;

    private Button buttonSave;
    private UserModel userModel;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit_profile);

        this.init();
    }

    private void init() {
        this.inputName = findViewById(R.id.inputName);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.imageProfile = findViewById(R.id.imageProfile);
        this.buttonSave = findViewById(R.id.buttonSave);

        if (this.getIntent() != null) {
            this.userModel = (UserModel) this.getIntent().getSerializableExtra(Constant.CODE.USER);
        }

        if (this.userModel != null) {
            this.inputName.setText(this.userModel.getName());
            this.inputEmail.setText(this.userModel.getEmail());

            if (!this.userModel.getImage().equals("")){
                this.uri = Uri.parse(this.userModel.getImage());

                Glide.with(this).load(this.userModel.getImage()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).into(this.imageProfile);
            }

        }

        this.buttonSave.setOnClickListener(this);
        this.imageProfile.setOnClickListener(this);
    }

    public void back(View view){
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.buttonSave)){
            if (this.inputName.getText() != null) {
                if (!this.inputName.getText().toString().equals(this.userModel.getName())){
                    this.userModel.setName(this.inputName.getText().toString());
                }
            }
            CartaHelper.showConfirmation(this, this.getResources().getString(R.string.update_profile), this.getResources().getString(R.string.are_you_sure_want_to_update), 100, Constant.TAG.UPDATE_PROFILE, 5000L, userModel);
        }else if (v.equals(this.imageProfile)){
            this.uploadProfilePicture();
        }
    }

    private void uploadProfilePicture() {
        super.startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_PICK), Constant.CODE.GET_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.CODE.GET_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            this.uri = data.getData();
            try{
                InputStream imageStream = this.getContentResolver().openInputStream(this.uri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                this.imageProfile.setImageBitmap(bitmap);
                Log.d("EditProfileSelectImage", this.uri.toString());
                this.userModel.setImage(this.uri.toString());
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}