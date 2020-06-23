package me.muhammadfaisal.mycarta.v1.home.fragment.money.bottom_sheet;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.TransactionModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMoneyManagerBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private TextView textTitle;
    private TextView textTotal;
    private TextView textDate;
    private TextView textDescription;
    private TextView textHint;

    private ImageView icClose;
    private ImageView imageShare;
    private ImageView imageCategory;

    private CardView cardContent;
    private CardView cardHeader;
    private CardView cardFooter;

    private TransactionModel transactionModel;

    public DetailMoneyManagerBottomSheet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.bottom_sheet_fragment_detail_money_manager, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initView(view);

        this.data();

        this.imageShare.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void data() {
        this.textTitle.setText(CartaHelper.decryptString(this.transactionModel.getName()));
        this.textTotal.setText(CartaHelper.currencyFormat(Long.parseLong(CartaHelper.decryptString(this.transactionModel.getAmount()))));
        this.textDate.setText(CartaHelper.decryptString(this.transactionModel.getDate()));
        this.textDescription.setText(CartaHelper.decryptString(this.transactionModel.getDescription()));
        this.textHint.setText(CartaHelper.decryptString(this.transactionModel.getType()));

        if (this.transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))) {
            this.cardHeader.setCardBackgroundColor(this.getActivity().getResources().getColor(R.color.colorIncome));
            this.cardFooter.setCardBackgroundColor(this.getActivity().getResources().getColor(R.color.colorIncome));
        }else{
            this.cardHeader.setCardBackgroundColor(this.getActivity().getResources().getColor(R.color.colorExpense));
            this.cardFooter.setCardBackgroundColor(this.getActivity().getResources().getColor(R.color.colorExpense));
        }

        this.filterCategory(this.getActivity().getResources(), CartaHelper.decryptString(this.transactionModel.getCategory()));
    }

    private void initView(View v) {

        this.transactionModel = (TransactionModel) this.getArguments().getSerializable("transaction");

        this.textTitle = v.findViewById(R.id.textTitle);
        this.textTotal = v.findViewById(R.id.textTotalExpenseOrIncome);
        this.textDate = v.findViewById(R.id.textDate);
        this.textDescription = v.findViewById(R.id.textDescription);
        this.textHint = v.findViewById(R.id.hintExpenseOrIncome);
        this.cardHeader = v.findViewById(R.id.cardContent);

        this.icClose = v.findViewById(R.id.icClose);
        this.imageShare = v.findViewById(R.id.imageShare);
        this.imageCategory = v.findViewById(R.id.imageCategory);

        this.cardHeader = v.findViewById(R.id.cardHeader);
        this.cardFooter = v.findViewById(R.id.cardFooter);
    }

    private void filterCategory(Resources resources, String category) {
        if (category.equals(Constant.CODE.FOOD_AND_DRINK)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_food));
        } else if (category.equals(Constant.CODE.BILLS)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_bills));
        } else if (category.equals(Constant.CODE.SHOPPING)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_shopping));
        } else if (category.equals(Constant.CODE.TRANSPORTATION)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_transportation));
        } else if (category.equals(Constant.CODE.ELECTRONICS)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_electronics));
        } else if (category.equals(Constant.CODE.HEALTH)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_health));
        } else if (category.equals(Constant.CODE.EDUCATION)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_education));
        } else if (category.equals(Constant.CODE.OFFICE)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_office));
        } else if (category.equals(Constant.CODE.SALARY)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_salary));
        } else if (category.equals(Constant.CODE.REWARDS)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_rewards));
        } else if (category.equals(Constant.CODE.CASHBACK)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_cashback));
        } else if (category.equals(Constant.CODE.INVESTMENT)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_investment));
        } else if (category.equals(Constant.CODE.REFUND)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_refund));
        } else if (category.equals(Constant.CODE.LOTTERY)) {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_lottery));
        } else {
            this.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_other));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(imageShare)) {
            try {
                share();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }

    private void share() throws IOException {
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Allow the Storage Permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 401);
        } else {
            cardHeader.setDrawingCacheEnabled(true);
            Bitmap b = cardHeader.getDrawingCache();

            int random = new Random().nextInt((100000 - 100)+ 1) + 100000;

            String shareMessages;

            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String location = "/" +textTitle.getText().toString()+random+ "mycarta.png";

            String path = root.toString() + location;

            File imageDir = new File(root,location);

            if (CartaHelper.encryptString(Constant.CODE.INCOME).equals(this.transactionModel.getType())){
                shareMessages = "Hey, i get my income from " + textTitle.getText().toString() + "\n" + textDescription.getText().toString() + "\n\n" + "MyCarta Make your life easier and happier!\n\n Get it on Google Play : \n https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            }else {
                shareMessages = "Hey, i was doing " + textTitle.getText().toString() + "\n" + textDescription.getText().toString() + "\n\n" + "MyCarta Make your life easier and happier!\n\n Get it on Google Play : \n https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            }

            try {
                b.compress(Bitmap.CompressFormat.PNG, 95, new FileOutputStream(path));
                Log.d("SUCCESS SHARE IMAGE", "SUCCESS==============");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("FAIL SHARE IMAGE", "ERROR==============");
            }

            FileInputStream finalPath = new FileInputStream(new File(path));
            finalPath.close();

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("image/png");
            i.putExtra(Intent.EXTRA_TEXT, shareMessages);
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageDir.getAbsolutePath()));
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i,"Share Your Transaction"));
        }
    }
}
