package com.example.adoptmypet.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.adoptmypet.R

class FactorRiskDialog : AppCompatDialogFragment() {

    private var listener: FactorRiskListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_factor_risk, null)

        builder.setView(view)
            .setTitle("Oops")
            .setPositiveButton("Sunt pregatit") { dialog, which ->
            }
            .setNegativeButton("Voi incerca mai tarziu"){ dialog, which ->
                listener?.backToHome()
            }
        return builder.create()
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        listener = activity as FactorRiskListener
    }

    interface FactorRiskListener{
        fun backToHome()
    }
}