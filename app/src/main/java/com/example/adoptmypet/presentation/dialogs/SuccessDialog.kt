package com.example.adoptmypet.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.adoptmypet.R
import kotlinx.android.synthetic.main.alert_adoption.*

class SuccessDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_succes, null)

        builder.setView(view)
            .setTitle("Success")
            .setPositiveButton("Multumesc!") { dialog, which ->
            }
        return builder.create()
    }
}