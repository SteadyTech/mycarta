package me.muhammadfaisal.mycarta.v1.keyboard;

import android.annotation.SuppressLint;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.List;

import me.muhammadfaisal.mycarta.R;

public class CartaBoardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    KeyboardView normalKeyboard, symbolKeyboard;
    boolean shifted;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateInputView() {
        normalKeyboard = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        symbolKeyboard = (KeyboardView) getLayoutInflater().inflate(R.layout.symbol_keyboard_view, null);

        Keyboard keyboard = new Keyboard(this, R.xml.carta_board);

        normalKeyboard.setKeyboard(keyboard);
        normalKeyboard.setOnKeyboardActionListener(this);

        return normalKeyboard;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        List<KeyEvent> keyEvents;
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch (i) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selected = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selected)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
                    break;

                case Keyboard.KEYCODE_SHIFT:
                    shifted = !shifted;
                    normalKeyboard.setShifted(shifted);
                    normalKeyboard.invalidateAllKeys();
                    break;

                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;

                default:
                    char code = (char) i;

                    if (Character.isLetter(code) && shifted) {
                        code = Character.toUpperCase(code);
                    }
                    inputConnection.commitText(String.valueOf(code), 1);
            }
        }

    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
