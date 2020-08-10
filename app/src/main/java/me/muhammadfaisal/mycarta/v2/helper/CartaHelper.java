package me.muhammadfaisal.mycarta.v2.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.bottomsheet.CautionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.bottomsheet.ConfirmationBottomSheetFragment;

public class CartaHelper {

    private static final String ALGORITHM = "Blowfish";
    private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    private static final String IV = "abcdefgh";
    private static final String KEY = "Cards";

    public static String encryptString(String value) {

        if (value == null || value.isEmpty())
            return "";
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(MODE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] values = new byte[0];
        try {
            values = cipher.doFinal(value.getBytes());
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    public static String decryptString(String value) {

        if (value == null || value.isEmpty())
            return "";

        byte[] values = new byte[0];
        try {
            values = Base64.decode(value, Base64.DEFAULT);
        } catch (Exception e) {
            values = value.getBytes();
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(MODE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            return new String(cipher.doFinal(values));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String lastCardNumber(String cardNumber){
        return CartaHelper.decryptString(cardNumber).substring(CartaHelper.decryptString(cardNumber).length() - 4);
    }

    public static void copy(String text, Activity activity){
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", text);
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(activity, "Copied To Clipboard!", Toast.LENGTH_SHORT).show();
    }

    public static String dot(){
        return "\u2022 ";
    }

    public static Locale idr(){
        return new Locale("id", "id");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String currencyFormat(long data){
        return NumberFormat.getCurrencyInstance(CartaHelper.idr()).format(data);
    }

    public static Bitmap qrCodeLogo(Bitmap logo, Bitmap qrcode) {
        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) /2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }

    public static String cardNumberFormat(String text){
        StringBuilder stringBuilder = new StringBuilder(text);

        for (int i = 4; i < stringBuilder.length(); i+= 5){
            stringBuilder.insert(i, " ");
        }

        return stringBuilder.toString();
    }
    public static void move(Activity context, Class activity, boolean forget){
        if (forget) {
            context.startActivity(new Intent(context, activity));
            context.finish();
        }else{
            context.startActivity(new Intent(context, activity));
        }
    }

    public static void showCaution(AppCompatActivity activity, String messageTitle, String messageBody, String errorCode){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY.TITLE_MESSAGE, messageTitle);
        bundle.putString(Constant.KEY.DESCRIPTION_MESSAGE, messageBody);

        CautionBottomSheetFragment cautionBottomSheetFragment = new CautionBottomSheetFragment();
        cautionBottomSheetFragment.setArguments(bundle);
        cautionBottomSheetFragment.show(activity.getSupportFragmentManager(), errorCode);
    }
    public static void showConfirmation(AppCompatActivity activity, String messageTitle, String messageBody, int code, String tag, long delay, Serializable serializable){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY.TITLE_MESSAGE, messageTitle);
        bundle.putString(Constant.KEY.DESCRIPTION_MESSAGE, messageBody);
        bundle.putInt(Constant.KEY.CODE, code);
        bundle.putLong(Constant.KEY.DELAY, delay);
        if (serializable != null) {
            bundle.putSerializable(Constant.CODE.USER, serializable);
        }

        ConfirmationBottomSheetFragment confirmationBottomSheetFragment = new ConfirmationBottomSheetFragment();
        confirmationBottomSheetFragment.setArguments(bundle);
        confirmationBottomSheetFragment.setCancelable(false);
        confirmationBottomSheetFragment.show(activity.getSupportFragmentManager(), tag);
    }
}
