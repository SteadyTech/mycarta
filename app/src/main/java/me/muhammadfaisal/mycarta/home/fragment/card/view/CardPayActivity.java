package me.muhammadfaisal.mycarta.home.fragment.card.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.BitSet;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.helper.UniversalHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CardPayActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textBarcode, textQR;
    ImageView numberBarcode, numberQR;
    ExpandableLinearLayout expandBarcode, expandQR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);
        getSupportActionBar().hide();

        initWidget();

    }

    private void initWidget() {
        textBarcode = findViewById(R.id.textBarcode);
        textQR = findViewById(R.id.textQR);
        numberBarcode = findViewById(R.id.imageNumberCardBarcode);
        numberQR = findViewById(R.id.imageNumberCardQR);
        expandBarcode = findViewById(R.id.expandableBarcode);
        expandQR = findViewById(R.id.expandableQR);

        numberCardBarcode();
        numberCardQR();

        textBarcode.setOnClickListener(this);
        textQR.setOnClickListener(this);
    }

    private void numberCardQR() {
        Long numberCard = getIntent().getLongExtra("numberOnCard",0);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(numberCard), BarcodeFormat.QR_CODE, 800,800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            numberQR.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void numberCardBarcode() {
        Long numberCard = getIntent().getLongExtra("numberOnCard",0);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(numberCard), BarcodeFormat.CODE_128, 600,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            numberBarcode.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == textBarcode){
            openBarcode();
        }else{
            openQR();
        }
    }

    private void openQR() {
        if (expandQR.isExpanded()){
            expandQR.collapse();
            expandBarcode.expand();
        }else{
            expandQR.expand();
            expandBarcode.collapse();
        }
    }

    private void openBarcode() {
        if (expandBarcode.isExpanded()){
            expandBarcode.collapse();
            expandQR.expand();
        }else{
            expandBarcode.expand();
            expandQR.collapse();
        }
    }
}
