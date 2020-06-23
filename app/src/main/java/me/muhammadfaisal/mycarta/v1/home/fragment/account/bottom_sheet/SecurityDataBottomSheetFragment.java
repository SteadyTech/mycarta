package me.muhammadfaisal.mycarta.v1.home.fragment.account.bottom_sheet;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.muhammadfaisal.mycarta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityDataBottomSheetFragment extends BottomSheetDialogFragment {

    ImageView icClose;


    public SecurityDataBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_fragment_security_data, container, false);

        icClose = v.findViewById(R.id.icClose);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return  v;
    }

}