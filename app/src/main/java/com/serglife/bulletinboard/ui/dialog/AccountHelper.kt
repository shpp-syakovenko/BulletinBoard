package com.serglife.bulletinboard.ui.dialog

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.constans.FireBaseAuthConstants

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
                        Log.d("MyLog", "Exception: ${task.exception}")

                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                val exception = task.exception as FirebaseAuthUserCollisionException

                                if (exception.errorCode == FireBaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                    // Join email with googleAccount
                                    linkEmailToGoogle(email, password)
                                }
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                val exception =
                                    task.exception as FirebaseAuthInvalidCredentialsException

                                if (exception.errorCode == FireBaseAuthConstants.ERROR_INVALID_EMAIL) {
                                    Toast.makeText(
                                        act,
                                        FireBaseAuthConstants.ERROR_INVALID_EMAIL,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            is FirebaseAuthWeakPasswordException -> {
                                val exception = task.exception as FirebaseAuthWeakPasswordException

                                if (exception.errorCode == FireBaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                    Toast.makeText(
                                        act,
                                        FireBaseAuthConstants.ERROR_WEAK_PASSWORD,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
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
                        when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                val exception =
                                    task.exception as FirebaseAuthInvalidCredentialsException

                                if (exception.errorCode == FireBaseAuthConstants.ERROR_INVALID_EMAIL) {
                                    Toast.makeText(
                                        act,
                                        FireBaseAuthConstants.ERROR_INVALID_EMAIL,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            is FirebaseAuthWeakPasswordException -> {
                                val exception = task.exception as FirebaseAuthWeakPasswordException

                                if (exception.errorCode == FireBaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                    Toast.makeText(
                                        act,
                                        FireBaseAuthConstants.ERROR_WEAK_PASSWORD,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            is FirebaseAuthInvalidUserException -> {
                                val exception = task.exception as FirebaseAuthInvalidUserException
                                if (exception.errorCode == FireBaseAuthConstants.ERROR_USER_NOT_FOUND) {
                                    Toast.makeText(
                                        act,
                                        FireBaseAuthConstants.ERROR_USER_NOT_FOUND,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }
                        }

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

    private fun getSingInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun singInWithGoogle() {
        singInClient = getSingInClient()
        val intent = singInClient.signInIntent
        act.startActivityForResult(intent, GoogleConst.GOOGLE_SING_IN_REQUEST_CODE)
    }

    fun singOutGoogle() {
        getSingInClient().signOut()
    }

    fun singInFireBaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                act.uiUpdate(task.result?.user)
            }
        }
    }

    private fun linkEmailToGoogle(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)

        if (act.mAuth.currentUser != null) {
            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.link_done),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        } else {
            Toast.makeText(
                act,
                act.resources.getString(R.string.enter_to_google),
                Toast.LENGTH_LONG
            )
                .show()
        }

    }
}