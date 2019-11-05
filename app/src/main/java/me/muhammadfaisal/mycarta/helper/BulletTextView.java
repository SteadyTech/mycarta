package me.muhammadfaisal.mycarta.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.widget.TextView;



import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class BulletTextView extends AppCompatTextView {

    public BulletTextView(Context context) {
        super(context);
        addBullets();
    }

    public BulletTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addBullets();
    }

    public BulletTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addBullets();
    }

    private void addBullets() {
        CharSequence text = getText();
        if (TextUtils.isEmpty(text)){
            return;
        }

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new BulletSpan(16), 0, text.length(), 0);
        setText(spannableString);
    }
}
