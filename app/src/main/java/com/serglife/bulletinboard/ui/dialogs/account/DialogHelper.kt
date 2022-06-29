package com.serglife.bulletinboard.ui.dialogs.account

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.SingDialogBinding

class DialogHelper(private val act: MainActivity) {

    val accHelper = AccountHelper(act)

    fun createDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SingDialogBinding.inflate(act.layoutInflater)
        builder.setView(rootDialogElement.root)

        setDialogState(index, rootDialogElement)

        val dialog = builder.create()
        rootDialogElement.btSingUpIn.setOnClickListener {
            setOnClickSingUpIn(dialog, index, rootDialogElement)
        }
        rootDialogElement.btForgetP.setOnClickListener {
            setOnClickResetPassword(rootDialogElement, dialog)
        }
        rootDialogElement.btGoogleSingIn.setOnClickListener {
            accHelper.singInWithGoogle()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setOnClickResetPassword(rootDialogElement: SingDialogBinding, dialog: AlertDialog) {
        if(rootDialogElement.edSingEmail.text.isNotEmpty()){
            act.mAuth.sendPasswordResetEmail(rootDialogElement.edSingEmail.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(act, R.string.email_reset_password, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }else{
                        rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
                    }

            }
        }else{
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }
    }

    private fun setOnClickSingUpIn(
        dialog: AlertDialog,
        index: Int,
        rootDialogElement: SingDialogBinding
    ) {
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

    private fun setDialogState(
        index: Int,
        rootDialogElement: SingDialogBinding
    ) {
        if (index == DialogConst.SING_UP_STATE) {
            rootDialogElement.tvSingTitle.text = act.resources.getString(R.string.ac_sing_up)
            rootDialogElement.btSingUpIn.text = act.resources.getString(R.string.sing_up_action)
        } else {
            rootDialogElement.tvSingTitle.text = act.resources.getString(R.string.ac_sing_in)
            rootDialogElement.btSingUpIn.text = act.resources.getString(R.string.sing_in_action)
            rootDialogElement.btForgetP.visibility = View.VISIBLE
        }
    }
}