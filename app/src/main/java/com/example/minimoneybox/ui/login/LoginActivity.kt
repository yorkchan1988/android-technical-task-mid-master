package com.example.minimoneybox.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.example.minimoneybox.R
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var btn_sign_in: Button
    lateinit var til_email: TextInputLayout
    lateinit var et_email: EditText
    lateinit var til_password: TextInputLayout
    lateinit var et_password: EditText
    lateinit var til_name: TextInputLayout
    lateinit var et_name: EditText
    lateinit var animation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel =
            ViewModelProviders.of(this, viewModelProviderFactory).get(LoginViewModel::class.java)

        setupViews()
    }

    private fun setupViews() {
        btn_sign_in = findViewById(R.id.btn_sign_in)
        til_email = findViewById(R.id.til_email)
        et_email = findViewById(R.id.et_email)
        til_password = findViewById(R.id.til_password)
        et_password = findViewById(R.id.et_password)
        til_name = findViewById(R.id.til_name)
        et_name = findViewById(R.id.et_name)
        animation = findViewById(R.id.animation)

        btn_sign_in.setOnClickListener {
            animation.playAnimation()
        }
    }
}
