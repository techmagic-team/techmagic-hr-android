package co.techmagic.hr.presentation.login

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.R
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
//                    .requestIdToken("server client id")
                    .requestEmail()
                    .build()

            val googleSignInClient = GoogleSignIn.getClient(context!!, gso)
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, rcSignIn)
            }
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        tvCopyright.text = getString(R.string.login_copyright, currentYear)
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
            val account2 = Account(account.email!!, "com.google")
//            val token = GoogleAuthUtil.getToken(context, account2, "oauth2: https://www.googleapis.com/auth/userinfo.email")
            System.out.println()
//            presenter?.handleLoginClick()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google SignIn", "signInResult:failed code=" + e.statusCode)
        }

    }
}