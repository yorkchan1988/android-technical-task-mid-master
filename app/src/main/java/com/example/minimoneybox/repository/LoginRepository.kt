package com.example.minimoneybox.repository

import android.annotation.SuppressLint
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.exception.UnexpectedException
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.Session
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.network.api.LoginApi
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepository @Inject constructor(loginApi: LoginApi) {

    companion object {
        private const val TAG = "LoginRepository"
    }

    private val loginApi : LoginApi = loginApi

    fun login(email: String, password: String, idfa: String = ""): Observable<ApiResource<LoginSession>> {

        return Observable.create { emitter ->
            // catch all unexpected errors
            try {
                // start to call api, change api status to loading
                emitter.onNext(ApiResource.Loading())

                loginApi.login(LoginRequest(email, password, idfa))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { data ->
                            val token : String? = data.session?.token
                            if (token != null) {
                                // save bearer token to Session Manager
                                SessionManager.login(token)
                                // api returns success, return success response
                                emitter.onNext(ApiResource.Success(data))
                            }
                            else {
                                emitter.onError(UnexpectedException("Empty session Token"))
                            }
                        },
                        { error ->

                            if (error is HttpException) {
                                // api returns error, parse error response and return error response object
                                val errorResponseString = error.response().errorBody()?.string()
                                val errorResponse = Gson().fromJson(errorResponseString, ErrorResponse::class.java)
                                emitter.onNext(ApiResource.Error(null, errorResponse))
                            }
                            else {
                                emitter.onError(error)
                            }
                        }
                    )
            }
            catch (error: Exception) {
                emitter.onError(error)
            }
        }
    }
}