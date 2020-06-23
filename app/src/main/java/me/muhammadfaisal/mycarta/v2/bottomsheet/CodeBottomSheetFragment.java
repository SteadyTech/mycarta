package me.muhammadfaisal.mycarta.v2.bottomsheet;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CodeBottomSheetFragment extends BottomSheetDialogFragment {

    private TextView textView;
    private ImageView imageView;

    private CardModel cardModel;


    public CodeBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.extract();

        this.init(view);

        this.data();
    }

    private void extract() {
        this.cardModel = (CardModel) this.getArguments().getSerializable("card");
    }

    private void data() {
        String numberCard = CartaHelper.decryptString(this.cardModel.getCardNumber());
        if (Objects.equals(this.getTag(), Constant.CODE.QRCODE)){
            System.out.println("Number Card is " + numberCard);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(numberCard, BarcodeFormat.QR_CODE, 800,800);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(bitMatrix);
                Bitmap appLogo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher);
                Bitmap qrCode = CartaHelper.qrCodeLogo(appLogo, bitmap);
                imageView.setImageBitmap(qrCode);
            }catch (Exception e){
                Toast.makeText(getActivity(), "Something Went Wrong, Try Again Later.", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.getMessage());
            }
        }else{
            System.out.println("Number Card is " + numberCard);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(numberCard, BarcodeFormat.CODE_128, 600,200);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(getActivity(), "Something Went Wrong, Try Again Later.", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.getMessage());
            }
        }


    }
    private void init(View view) {
        this.textView = view.findViewById(R.id.text);
        this.imageView = view.findViewById(R.id.image);
    }
}

