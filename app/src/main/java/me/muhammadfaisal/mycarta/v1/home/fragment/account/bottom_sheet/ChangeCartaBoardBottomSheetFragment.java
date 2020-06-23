package me.muhammadfaisal.mycarta.v1.home.fragment.account.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import me.muhammadfaisal.mycarta.R;

public class ChangeCartaBoardBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    CarouselView carouselView;
    ImageView icCLose;

    Button buttonSwitch;

    TextView textHowToUse;
    ExpandableLinearLayout expandableLinearLayout;

    int[] images = {R.drawable.banner_easier, R.drawable.banner_happier, R.drawable.banner_switch};

    public ChangeCartaBoardBottomSheetFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_change_carta_board, container, false);

        initWidget(v);

        return v;
    }

    private void initWidget(View v) {

        carouselView = v.findViewById(R.id.carouselView);
        buttonSwitch = v.findViewById(R.id.buttonSwitchKeyboard);
        textHowToUse = v.findViewById(R.id.textHowToUse);
        expandableLinearLayout = v.findViewById(R.id.expandableLayout);
        icCLose = v.findViewById(R.id.icClose);

        carouselView.setPageCount(images.length);
        carouselView.setIndicatorVisibility(View.GONE);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setAdjustViewBounds(true);
            }
        });

        buttonSwitch.setOnClickListener(this);
        textHowToUse.setOnClickListener(this);

        icCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSwitch){
            methodChangeKeyboard();
        }else{
            methodExpandHowToUse();
        }
    }

    private void methodExpandHowToUse() {
        if (expandableLinearLayout.isExpanded()){
            expandableLinearLayout.collapse();
        }else{
            expandableLinearLayout.expand();
        }
    }

    private void methodChangeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.showInputMethodPicker();
        }else{
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
