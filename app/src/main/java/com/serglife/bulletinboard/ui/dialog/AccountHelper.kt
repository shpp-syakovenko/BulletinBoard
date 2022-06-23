package com.serglife.bulletinboard.ui.dialog

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R

class AccountHelper(private val act: MainActivity) {
    private lateinit var singInClient: GoogleSignInClient

    fun singUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)
                    } else {
                        Toast.makeText(
                            act,
                            act.resources.getString(R.string.sing_up_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun singInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        act.uiUpdate(task.result?.user)
                    } else {
                        Toast.makeText(
                            act,
                            act.resources.getString(R.string.sing_in_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_done),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun getSingInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun singInWithGoogle(){
        singInClient = getSingInClient()
        val intent = singInClient.signInIntent
        act.startActivityForResult(intent, GoogleConst.GOOGLE_SING_IN_REQUEST_CODE)
    }

    fun singInFireBaseWithGoogle(token:String){
        val credential = GoogleAuthProvider.getCredential(token,null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(act,"Sing in google well done!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(act,"Sing in google Error!", Toast.LENGTH_LONG).show()
            }
        }
    }


}