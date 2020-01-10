package me.muhammadfaisal.mycarta.home.fragment.card.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

    TextView textBarcode, textQR, textHow, textWhy;
    ImageView numberBarcode, numberQR;
    ExpandableLinearLayout expandBarcode, expandQR, expandHow, expandWhy;
    ImageView imageBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);

        initWidget();

        setToFullBrightness();
    }

    private void setToFullBrightness() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1F;
        getWindow().setAttributes(layoutParams);
    }

    private void initWidget() {
        textBarcode = findViewById(R.id.textBarcode);
        textQR = findViewById(R.id.textQR);
        textHow = findViewById(R.id.textHowToUse);
        textWhy = findViewById(R.id.textWhy);
        numberBarcode = findViewById(R.id.imageNumberCardBarcode);
        numberQR = findViewById(R.id.imageNumberCardQR);
        expandBarcode = findViewById(R.id.expandableBarcode);
        expandQR = findViewById(R.id.expandableQR);
        expandHow = findViewById(R.id.expandableHowToUse);
        expandWhy = findViewById(R.id.expandableWHy);
        imageBack = findViewById(R.id.imageBack);

        numberCardBarcode();
        numberCardQR();

        textBarcode.setOnClickListener(this);
        textQR.setOnClickListener(this);
        textHow.setOnClickListener(this);
        textWhy.setOnClickListener(this);
        imageBack.setOnClickListener(this);
    }

    private void numberCardQR() {
        String numberCard = getIntent().getExtras().getString("numberOnCard");

        System.out.println("Number Card is " + numberCard);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(numberCard, BarcodeFormat.QR_CODE, 800,800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            numberQR.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "Uh Oh...QR Code getting trouble :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void numberCardBarcode() {
        String numberCard = getIntent().getExtras().getString("numberOnCard");

        System.out.println("Number Card is Barcode " + numberCard);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(numberCard), BarcodeFormat.CODE_128, 600,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            numberBarcode.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "Uh Oh...Barcode getting trouble ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == textBarcode) {
            openBarcode();
        } else if (view == textQR) {
            openQR();
        } else if (view == textHow) {
            openHowToUse();
        } else if (view == textWhy) {
            openWhyCartaPay();
        } else{
            finish();
        }

    }

    private void openWhyCartaPay() {
        if (expandWhy.isExpanded()){
            expandWhy.collapse();
        }else{
            expandWhy.expand();
        }
    }

    private void openHowToUse() {
        if (expandHow.isExpanded()){
            expandHow.collapse();
        }else{
            expandHow.expand();
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
