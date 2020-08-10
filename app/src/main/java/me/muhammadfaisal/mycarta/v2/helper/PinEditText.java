package me.muhammadfaisal.mycarta.v2.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import me.muhammadfaisal.mycarta.R;

@SuppressLint("AppCompatCustomView")
public class PinEditText extends EditText {
    private float space = 24; //24 dp by default, space between the lines
    private float numberCharacter = 6;
    private float lineSpacing = 8; //8dp by default, height of the text from our lines
    private int maxLength = 6;
    private float lineStroke = 2;
    private Paint linesPaint;
    private OnClickListener listener;

    public PinEditText(Context context) {
        super(context);
    }

    public PinEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        this.lineStroke = multi * lineStroke;
        this.linesPaint = new Paint(getPaint());
        this.linesPaint.setStrokeWidth(1);
        this.linesPaint.setColor(getResources().getColor(R.color.buttonPrimary));
        this.space = multi * space; //convert to pixels for our density
        this.lineSpacing = multi * this.lineSpacing; //convert to pixels for our density
        this.numberCharacter = this.maxLength;


        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // When tapped, move cursor to end of text.
                setSelection(getText().length());
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        float mCharSize;
        if (space < 0) {
            mCharSize = (availableWidth / (numberCharacter * 2 - 1));
        } else {
            mCharSize = (availableWidth - (space * (numberCharacter - 1))) / numberCharacter;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();
        //Text Width
        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for (int i = 0; i < numberCharacter; i++) {

            canvas.drawLine(startX, bottom, startX + mCharSize, bottom, linesPaint);
            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText("\u2022\u2022\u2022\u2022\u2022\u2022", i, i + 1, middle - textWidths[0] / 2, bottom - lineSpacing, getPaint());
            }
            if (space < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + space;
            }
        }

    }
}