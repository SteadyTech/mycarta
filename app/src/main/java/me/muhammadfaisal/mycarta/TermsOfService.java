package me.muhammadfaisal.mycarta;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsOfService extends BottomSheetDialogFragment implements View.OnClickListener {

    ImageView icClose;

    public TermsOfService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_terms_of, container, false);

        icClose = v.findViewById(R.id.icClose);

        icClose.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (view == icClose){
            this.dismiss();
        }
    }
}
