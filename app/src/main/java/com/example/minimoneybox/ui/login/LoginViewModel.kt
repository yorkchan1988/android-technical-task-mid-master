package com.example.minimoneybox.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.network.LoginApi
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_IDFA
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(loginApi: LoginApi) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    val loginApi: LoginApi

    init {
        this.loginApi = loginApi

        loginApi.login(LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA))
            .toObservable()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result -> Log.d(TAG, "result : "+result.session?.token)},
                { error -> Log.e(TAG, "error : "+error.localizedMessage )}
            )
    }
}
