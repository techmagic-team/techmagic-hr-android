package co.techmagic.hr.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import co.techmagic.hr.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.techmagic.viper.base.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_login.*
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
                when (e.statusCode) {
                    ConnectionResult.NETWORK_ERROR -> showErrorMessage(getString(R.string.message_connection_error))

                    ConnectionResult.UNKNOWN, ConnectionResult.SUCCESS, ConnectionResult.SERVICE_MISSING,
                    ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, ConnectionResult.SERVICE_DISABLED,
                    ConnectionResult.SIGN_IN_REQUIRED, ConnectionResult.INVALID_ACCOUNT,
                    ConnectionResult.RESOLUTION_REQUIRED, ConnectionResult.SERVICE_INVALID,
                    ConnectionResult.INTERNAL_ERROR, ConnectionResult.DEVELOPER_ERROR,
                    ConnectionResult.LICENSE_CHECK_FAILED, ConnectionResult.CANCELED,
                    ConnectionResult.TIMEOUT, ConnectionResult.INTERRUPTED,
                    ConnectionResult.API_UNAVAILABLE, ConnectionResult.SIGN_IN_FAILED,
                    ConnectionResult.SERVICE_UPDATING, ConnectionResult.SERVICE_MISSING_PERMISSION,
                    ConnectionResult.RESTRICTED_PROFILE -> showMessage("Google Sign In error. Code: " + e.statusCode)
                }
            }
        })

        tvSignIn.setOnClickListener {

            googleLoginDelegate?.login()
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        tvCopyright.text = getString(R.string.login_copyright, currentYear)
    }

    override fun showProgress(show: Boolean) {
        tvSignIn.visibility = if (show) GONE else VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleLoginDelegate?.onActivityResult(requestCode, resultCode, data)
    }

    fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}