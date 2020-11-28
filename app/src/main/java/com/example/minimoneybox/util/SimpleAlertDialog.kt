package com.example.minimoneybox.util

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object SimpleAlertDialog {
    fun showAlert(activity: Activity?, title: String, message: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, which: Int ->

            }
            builder.show()
        }
    }
}