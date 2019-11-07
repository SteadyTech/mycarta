package me.muhammadfaisal.mycarta.home.fragment.account.bottom_sheet


import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_fragment_about_app.*

import me.muhammadfaisal.mycarta.R

/**
 * A simple [Fragment] subclass.
 */
class AboutAppBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.bottom_sheet_fragment_about_app, container, false)

        initWidget(v)

        return v
    }

    @SuppressLint("SetTextI18n")
    private fun initWidget(v: View?) {
        val textVersion : TextView = v!!.findViewById(R.id.textVersionApp)
        val lottie : LottieAnimationView = v.findViewById(R.id.lottie)
        val icClose : ImageView = v.findViewById(R.id.icClose)

        val packageInfo : PackageInfo = activity?.packageManager!!.getPackageInfo(activity!!.packageName, 0)
        val versionApp = packageInfo.versionName

        textVersion.text = "Version $versionApp"

        textVersion.setOnLongClickListener{
            Toast.makeText(activity, "Thanks for using MyCarta!", Toast.LENGTH_SHORT).show()
            lottie.visibility = View.VISIBLE
            return@setOnLongClickListener true
        }

        icClose.setOnClickListener{
            dismiss()
        }
    }


}
