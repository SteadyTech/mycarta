package me.muhammadfaisal.mycarta.home.fragment.money.bottom_sheet;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.DrawingContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMoneyManagerBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    public DetailMoneyManagerBottomSheet() {
        // Required empty public constructor
    }

    TextView textTitle, textTotal, textDate, textDescription, textHint;
    ImageView icClose, imageShare, imageCategory;
    CardView cardTotal, cardDescription, cardContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_detail_money_manager, container, false);

        initView(v);

        imageShare.setOnClickListener(this);

        return v;
    }

    private void initView(View v) {
        textTitle = v.findViewById(R.id.textTitle);
        textTotal = v.findViewById(R.id.textTotalExpenseOrIncome);
        textDate = v.findViewById(R.id.textDate);
        textDescription = v.findViewById(R.id.textDescription);
        textHint = v.findViewById(R.id.hintExpenseOrIncome);
        cardContent = v.findViewById(R.id.cardContent);

        icClose = v.findViewById(R.id.icClose);
        imageShare = v.findViewById(R.id.imageShare);
        imageCategory = v.findViewById(R.id.imageCategory);

        cardTotal = v.findViewById(R.id.cardTotalExpenseOrIncome);
        cardDescription = v.findViewById(R.id.cardDescription);

        Long expense = getArguments().getLong("expense", 0);
        Long income = getArguments().getLong("income", 0);
        String date = getArguments().getString("date");
        String description = getArguments().getString("description");
        String name = getArguments().getString("name");
        String category = getArguments().getString("category");


        Resources resources = getResources();

        textTitle.setText(name);

        filterIncomeOrExpense(resources, cardTotal, cardDescription, textTotal, expense, income);

        filterCategory(resources, category);

        textDescription.setText(description);
        textDate.setText(date);

    }

    private void filterIncomeOrExpense(Resources resources, CardView cardTotal, CardView cardDescription, TextView textTotal, Long expense, Long income) {
        if (expense.equals(0L)) {
            cardTotal.setCardBackgroundColor(resources.getColor(R.color.colorIncome));
            cardDescription.setCardBackgroundColor(resources.getColor(R.color.colorIncome));
            textTotal.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(income));
            textHint.setText("Income");
        } else {
            cardTotal.setCardBackgroundColor(resources.getColor(R.color.colorExpense));
            cardDescription.setCardBackgroundColor(resources.getColor(R.color.colorExpense));
            textTotal.setText(NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(expense));
            textHint.setText("Expense");
        }
    }

    private void filterCategory(Resources resources, String category) {
        if (category.equals("Food & Drink")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_food));
        } else if (category.equals("Bills")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_bills));
        } else if (category.equals("Shopping")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_shopping));
        } else if (category.equals("Transportation")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_transportation));
        } else if (category.equals("Electronics")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_electronics));
        } else if (category.equals("Health")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_health));
        } else if (category.equals("Education")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_education));
        } else if (category.equals("Office")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_office));
        } else if (category.equals("Salary")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_salary));
        } else if (category.equals("Rewards")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_rewards));
        } else if (category.equals("Cashback")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_cashback));
        } else if (category.equals("Investment")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_investment));
        } else if (category.equals("Refunds")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_refund));
        } else if (category.equals("Lottery")) {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_lottery));
        } else {
            imageCategory.setImageDrawable(resources.getDrawable(R.drawable.pic_other));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imageShare) {
            try {
                shareImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            dismiss();
        }
    }

    private void shareImage() throws IOException {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), " Allow the Storage Permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 401);
        } else {
            cardTotal.setDrawingCacheEnabled(true);
            Bitmap b = cardTotal.getDrawingCache();

            int random = new Random().nextInt((100000 - 100)+ 1) + 100000;

            Long income = getArguments().getLong("income", 0);
            String shareMessages;

            File root = Environment.getExternalStorageDirectory();
            String location = "/" +textTitle.getText().toString()+random+ "mycarta.png";

            String path = root.toString() + location;

            File imageDir = new File(root,location);

            if (!income.equals(0L)){
                shareMessages = "Hey, i get my income from " + textTitle.getText().toString() + "\n\n" + "MyCarta Make your life smarter,easier, banner_happier!\n\n Get it on Google Play : \n https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            }else {
                shareMessages = "Hey, i was doing " + textTitle.getText().toString() + "\n\n" + "MyCarta Make your life smarter, easier, banner_happier!\n\\n Get it on Google Play : \n https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            }

            try {
                b.compress(Bitmap.CompressFormat.PNG, 95, new FileOutputStream(path));
                System.out.println("SUCCESS============");
                Toast.makeText(getActivity(), "Image Saved !", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("ERROR==============");
            }

            FileInputStream finalPath = new FileInputStream(new File(path));
            finalPath.close();

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("image/png");
            i.putExtra(Intent.EXTRA_TEXT, shareMessages);
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageDir.getAbsolutePath()));
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i,"Share"));
        }
    }
}
