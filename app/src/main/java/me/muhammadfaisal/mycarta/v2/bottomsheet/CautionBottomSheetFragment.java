package me.muhammadfaisal.mycarta.v2.bottomsheet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.Constant;

public class CautionBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView textMessageTitle;
    private TextView textMessageDescription;
    private MaterialButton buttonOk;

    public CautionBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caution_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.init(view);
        this.data();
    }

    private void init(View view) {
        this.buttonOk = view.findViewById(R.id.buttonOk);
        this.textMessageTitle = view.findViewById(R.id.textTitle);
        this.textMessageDescription = view.findViewById(R.id.textMessage);

        this.buttonOk.setOnClickListener(this);
    }

    private void data(){
        if (this.getArguments() != null) {
            String title = this.getArguments().getString(Constant.KEY.TITLE_MESSAGE);
            String description = this.getArguments().getString(Constant.KEY.DESCRIPTION_MESSAGE);

            this.textMessageTitle.setText(title);
            this.textMessageDescription.setText(description);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.buttonOk)){
            this.dismiss();
        }
    }
}