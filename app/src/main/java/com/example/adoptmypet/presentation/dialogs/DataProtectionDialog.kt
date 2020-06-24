package com.example.adoptmypet.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.adoptmypet.R
import com.example.adoptmypet.presentation.WelcomeActivity


object DataProtectionDialog : AppCompatDialogFragment() {

    private var callback: DataProtectionListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_data_protection, null)

        builder.setView(view)
            .setTitle("Avem nevoie de acceptul dumneavoastra")
            .setPositiveButton("Accept") { _, _ ->
                callback?.acceptDataProtection()
            }
            .setNegativeButton("Nu accept.") { _, _ ->
                callback?.declineDataProtection()
            }
        return builder.create()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = activity as WelcomeActivity
    }

    interface DataProtectionListener {
        fun acceptDataProtection()
        fun declineDataProtection()
    }
}