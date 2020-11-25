package com.example.minimoneybox

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.models.LoginSession
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager @Inject constructor() {

    companion object {
        private const val TAG = "SessionManager"
    }

    private val token: MutableLiveData<LoginSession> = MutableLiveData<LoginSession>()
}