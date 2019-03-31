package co.techmagic.hr.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.BuildConfig
import co.techmagic.hr.R
import co.techmagic.hr.data.exception.NetworkConnectionException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.techmagic.viper.base.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.item_list_select_filter.*
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.*


class LoginFragment : BaseViewFragment<LoginPresenter>(), LoginView {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private var googleLoginDelegate: GoogleLoginDelegate? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun initView() {
        super.initView()

        googleLoginDelegate = GoogleLoginDelegate(this, object : GoogleLoginCallback {
            override fun onSuccess(authCode: String) {
                presenter?.handleLoginClick(authCode)
            }

            override fun onError(e: ApiException) {
                if (e.statusCode == ConnectionResult.NETWORK_ERROR) {
                    showConnectionErrorMessage()
                } else {
                    showMessage("Google Sign In error. Code: " + e.statusCode)
                }
            }
        })

        tvSignIn.setOnClickListener {

            googleLoginDelegate?.login()
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        tvCopyright.text = getString(R.string.login_copyright, currentYear)
    }

    override fun handleError(e: Throwable) {
        if (e is NetworkConnectionException || e is SocketTimeoutException) {
            showConnectionErrorMessage()
        } else {
            showMessage("Error " + e.message)
        }

        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleLoginDelegate?.onActivityResult(requestCode, resultCode, data)
    }

    fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun showConnectionErrorMessage() {
        showMessage(getString(R.string.message_connection_error))
    }
}