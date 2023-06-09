package com.example.facilRecetas.utils.services

import android.app.Activity
import android.app.AlertDialog
import com.example.facilRecetas.R

class LoadingDialog(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog)
        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun dismissDialog() {
        alertDialog!!.dismiss()
    }
}