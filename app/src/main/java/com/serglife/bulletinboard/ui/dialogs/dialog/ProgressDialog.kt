package com.serglife.bulletinboard.ui.dialogs.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.serglife.bulletinboard.databinding.ProgressDialogLayoutBinding
import com.serglife.bulletinboard.databinding.SingDialogBinding

object ProgressDialog {
    fun createProgressDialog(act: Activity): AlertDialog {
        val builder = AlertDialog.Builder(act)
        val binding = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        dialog.setCancelable(false) // You can only stop the dialog with code (dialog.dismiss())

        dialog.show()
        return dialog
    }
}