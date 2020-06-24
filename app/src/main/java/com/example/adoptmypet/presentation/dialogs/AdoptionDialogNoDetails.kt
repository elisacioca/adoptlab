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

class AdoptionDialogNoDetails: AppCompatDialogFragment() {

    lateinit var phoneno: String
    lateinit var fullname: String
    private var listener: AdoptionDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInf: LayoutInflater = activity?.layoutInflater!!;
        val view: View = layoutInf.inflate(R.layout.alert_adoption, null)

        builder.setView(view)
            .setTitle("Esti sigur ca vrei sa adopti?")
            .setMessage("Adoptia unui implica o responsabilitate mare. Fii sigur ca esti pregatit inainte sa faci acest pas.")
            .setPositiveButton("Sunt sigur!") { dialog, which ->
                phoneno = phoneNo.text.toString()
                fullname = fullName.text.toString()
                listener?.applyText(fullname, phoneno)
            }
            .setNegativeButton("Cancel") { dialog, which ->
            }
        return builder.create()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        listener = activity as AdoptionDialogListener
    }

    interface AdoptionDialogListener{
        fun applyText(fullName: String, phoneNo: String)
    }
}