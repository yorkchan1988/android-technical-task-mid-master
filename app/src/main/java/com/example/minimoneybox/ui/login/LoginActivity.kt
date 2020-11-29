package com.example.minimoneybox.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.minimoneybox.R
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.ui.main.MainActivity
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import com.example.minimoneybox.util.Constants.Companion.USERNAME
import com.example.minimoneybox.util.SimpleAlertDialog
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
    lateinit var llprogressBar: LinearLayout
    lateinit var tv_email_error: TextView
    lateinit var tv_password_error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)

        setupViews()
        subscribeObservers()
    }

    private fun setupViews() {
        btn_sign_in = findViewById(R.id.btn_sign_in)
        til_email = findViewById(R.id.til_email)
        et_email = findViewById(R.id.et_email)
        et_email.setText(LOGIN_EMAIL)
        til_password = findViewById(R.id.til_password)
        et_password = findViewById(R.id.et_password)
        et_password.setText(LOGIN_PASSWORD)
        til_name = findViewById(R.id.til_name)
        et_name = findViewById(R.id.et_name)
        et_name.setText("York")
        animation = findViewById(R.id.animation)
        llprogressBar = findViewById(R.id.ll_progress_bar)
        tv_email_error = findViewById(R.id.tv_email_error)
        tv_password_error = findViewById(R.id.tv_password_error)

        btn_sign_in.setOnClickListener {
            animation.playAnimation()

            // login
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            viewModel.login(email, password)
        }
    }

    // observe live data of viewmodel
    private fun subscribeObservers() {
        // observe loginApiStatus
        // success then go to user account page
        // fail with message then display alert
        viewModel.apiStatus.observe(this, Observer {
            when(it.status) {
                ApiResource.ApiStatus.LOADING -> {
                    llprogressBar.visibility = View.VISIBLE
                }
                ApiResource.ApiStatus.SUCCESS -> {
                    llprogressBar.visibility = View.GONE
                    onLoginSuccess()
                }
                ApiResource.ApiStatus.ERROR -> {
                    llprogressBar.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(this, Observer {exception ->
            if (exception is ApiException) {
                val name = exception.name
                val message = exception.message
                SimpleAlertDialog.showAlert(this,name, message)
            }
            else {
                exception.message?.let { message ->
                    SimpleAlertDialog.showAlert(this,"Error", message)
                }
            }
        })

        viewModel.emailErrorText.observe(this, Observer {
            tv_email_error.setText(it)
        })

        viewModel.passwordErrorText.observe(this, Observer {
            tv_password_error.setText(it)
        })
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USERNAME, et_name.text.toString())
        startActivity(intent)
        finish()
    }
}
