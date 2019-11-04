package me.muhammadfaisal.mycarta.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;
    EditText editText;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
         editText = editTextWeakReference.get();
        if (editText.equals(null)) return;

        String s = editable.toString();
        if (s.isEmpty()) return;
        editText.removeTextChangedListener(this);

        String clearString = s.replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(clearString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(1000), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
