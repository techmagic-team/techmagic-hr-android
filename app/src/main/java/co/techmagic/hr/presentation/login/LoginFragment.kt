package co.techmagic.hr.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.BuildConfig
import co.techmagic.hr.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techmagic.viper.base.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*


class LoginFragment : BaseViewFragment<LoginPresenter>(), LoginView {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val rcSignIn = 1235

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun initView() {
        super.initView()
        tvSignIn.setOnClickListener {

            val gso = GoogleSignInOptions.Builder()
                    .requestServerAuthCode(BuildConfig.GOOGLE_SERVER_KEY)
                    .requestEmail() // should be requested or sign in will fail with API_NOT_CONNECTED error
                    .build()

            val googleSignInClient = GoogleSignIn.getClient(context!!, gso)

            if (GoogleSignIn.getLastSignedInAccount(context) != null) {
                googleSignInClient.signOut().addOnCompleteListener {
                    attemptLogin(googleSignInClient)
                }
            } else {
                attemptLogin(googleSignInClient)
            }
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        tvCopyright.text = getString(R.string.login_copyright, currentYear)
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun attemptLogin(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
            presenter?.handleLoginClick(account?.serverAuthCode!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            val msg = "signInResult:failed code=" + e.statusCode
            Log.w("Google SignIn", msg)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

    }
}