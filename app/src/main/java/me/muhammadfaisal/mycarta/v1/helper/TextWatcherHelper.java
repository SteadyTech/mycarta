package me.muhammadfaisal.mycarta.v1.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class TextWatcherHelper implements TextWatcher{

    private View view;

    public TextWatcherHelper(View view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

