package me.muhammadfaisal.mycarta.register.welcome;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.pin.PinBottomSheetFragment;

public class WelcomeNewUserActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearParent;
    CardView cardNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_new_user);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        initView();
    }

    private void initView() {
        linearParent = findViewById(R.id.linearParent);
        cardNext = findViewById(R.id.cardNext);

        TransitionDrawable drawable = (TransitionDrawable) linearParent.getBackground();
        drawable.startTransition(10000);

        cardNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == cardNext) {
            openPinBottomSheet();
        }
    }

    private void openPinBottomSheet() {
        BottomSheetDialogFragment fragment = new PinBottomSheetFragment();
        fragment.show(getSupportFragmentManager(), "PinLogin");
    }
}
