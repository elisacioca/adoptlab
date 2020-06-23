package com.example.adoptmypet.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.example.adoptmypet.R


object ErrorDialog {

    fun show(activity: Activity?, callBack: () -> Unit = {}) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        builder?.setView(R.layout.alert_error)
        builder?.setPositiveButton("ok") { dialog: DialogInterface, _: Int ->
            callBack
            dialog.dismiss()
        }
        builder?.show()
    }
}