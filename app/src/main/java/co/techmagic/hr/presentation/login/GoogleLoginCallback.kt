package co.techmagic.hr.presentation.login

import com.google.android.gms.common.api.ApiException

interface GoogleLoginCallback {

    fun onSuccess(authCode: String)

    fun onError(e: ApiException)
}