package me.muhammadfaisal.mycarta.home.fragment.account.bottom_sheet


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import me.muhammadfaisal.mycarta.R

/**
 * A simple [Fragment] subclass.
 */
class AboutAppBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.bottom_sheet_fragment_about_app, container, false)


        return v
    }


}
