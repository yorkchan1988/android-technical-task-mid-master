package com.example.minimoneybox.repository

import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.errorhandling.ErrorHandler
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.network.api.LoginApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginApi: LoginApi,
    private val sessionManager: SessionManager,
    private val errorHandler: ErrorHandler) {

    companion object {
        private const val TAG = "LoginRepository"
    }

    fun getLoginApiRequest(email: String, password: String, idfa: String = ""): LoginRequest {
        return LoginRequest(email, password, idfa)
    }

    fun login(email: String, password: String, idfa: String = ""): Observable<ApiResult<LoginSession>> {

        val requestParam = getLoginApiRequest(email, password, idfa)
        return loginApi.login(requestParam)
            .startWith{
                ApiResult.Loading<LoginSession>()
            }
            .subscribeOn(Schedulers.io())
            .map { data: LoginSession ->
                val token: String? = data.session?.token
                if (token != null && token.isNotEmpty()) {
                    // save bearer token to Session Manager
                    sessionManager.login(token)
                    // api returns success, return success response
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error<LoginSession>(errorHandler.getError("Unexpected empty token."))
                }
            }
            .onErrorReturn {
                ApiResult.Error<LoginSession>(errorHandler.getError(it))
            }
    }
}
