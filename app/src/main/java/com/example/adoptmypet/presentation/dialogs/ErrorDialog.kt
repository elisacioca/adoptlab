package com.example.adoptmypet.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.adoptmypet.R


object ErrorDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_error, null)

        builder.setView(view)
            .setTitle("Error")
            .setPositiveButton("ok") { dialog, which ->
            }
        return builder.create()
    }
}