package com.example.minimoneybox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.models.LoginSession
import javax.inject.Inject
import javax.inject.Singleton


// A class that used to
// 1. save bearer token to keystore
// 2. get bearer token from keystore
// 3. clear bearer token if session expired
@Singleton
class SessionManager @Inject constructor() {

    companion object {
        private const val TAG = "SessionManager"
    }

    private var token: String? = null

    fun getBearerToken(): String? {
        return token
    }

    fun login(token: String) {
        this.token = token
    }

    fun logout() {
        this.token = null
    }
}