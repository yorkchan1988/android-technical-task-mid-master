package com.example.minimoneybox.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.ui.login.LoginActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeAuthStatus()
    }

    private fun subscribeAuthStatus() {
        SessionManager.authStatus.observe(this,
            Observer { authStatus ->
                if (authStatus == SessionManager.AuthStatus.NOT_AUTHENTICATED) {
                    navLoginScreen()
                }
            })
    }

    private fun navLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}