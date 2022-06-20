package com.serglife.bulletinboard.ui.dialog

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.SingDialogBinding

class DialogHelper(private val act: MainActivity) {

    private val accHelper = AccountHelper(act)

    fun createDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SingDialogBinding.inflate(act.layoutInflater)
        builder.setView(rootDialogElement.root)

        if (index == DialogConst.SING_UP_STATE) {
            rootDialogElement.tvSingTitle.text = act.resources.getString(R.string.ac_sing_up)
            rootDialogElement.btSingUpin.text = act.resources.getString(R.string.sing_up_action)
        } else {
            rootDialogElement.tvSingTitle.text = act.resources.getString(R.string.ac_sing_in)
            rootDialogElement.btSingUpin.text = act.resources.getString(R.string.sing_in_action)

        }

        val dialog = builder.create()
        rootDialogElement.btSingUpin.setOnClickListener {
            dialog.dismiss()
            if (index == DialogConst.SING_UP_STATE) {
                accHelper.singUpWithEmail(
                    rootDialogElement.edSingEmail.text.toString(),
                    rootDialogElement.edSingPassword.text.toString()
                )
            } else {
                accHelper.singInWithEmail(
                    rootDialogElement.edSingEmail.text.toString(),
                    rootDialogElement.edSingPassword.text.toString()
                )

            }
        }


        dialog.show()
    }
}