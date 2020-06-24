package com.example.adoptmypet.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.adoptmypet.R

class ErasePetDialog : AppCompatDialogFragment() {

    private var listener: ErasePetDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_erase_pet, null)

        builder.setView(view)
            .setTitle("Esti sigur?")
            .setPositiveButton("Sunt sigur!") { dialog, which ->
                listener?.erasePet()
            }
            .setNegativeButton("Cancel") { dialog, which ->
            }
        return builder.create()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        listener = activity as ErasePetDialogListener
    }

    interface ErasePetDialogListener{
        fun erasePet()
    }
}