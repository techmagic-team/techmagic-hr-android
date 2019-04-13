package co.techmagic.hr.presentation.login

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import co.techmagic.hr.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleLoginDelegate(private val fragment: Fragment,
                          private val callback: GoogleLoginCallback) {

    private val rcSignIn = 1235
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder()
                .requestServerAuthCode(BuildConfig.GOOGLE_SERVER_KEY)
                .requestEmail() // should be requested or sign in will fail with API_NOT_CONNECTED error
                .build()

        googleSignInClient = GoogleSignIn.getClient(fragment.activity as Activity, gso)
    }

    fun login() {
        if (GoogleSignIn.getLastSignedInAccount(fragment.activity) != null) {
            googleSignInClient.signOut().addOnCompleteListener {
                attemptLogin(googleSignInClient)
            }
        } else {
            attemptLogin(googleSignInClient)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == rcSignIn) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            callback.onSuccess(account?.serverAuthCode!!)
        } catch (e: ApiException) {
            callback.onError(e)
        }
    }

    private fun attemptLogin(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        fragment.startActivityForResult(signInIntent, rcSignIn)
    }
}