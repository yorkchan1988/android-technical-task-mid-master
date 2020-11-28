package com.example.minimoneybox.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.minimoneybox.R
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.ui.main.MainActivity
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import com.example.minimoneybox.util.Constants.Companion.USERNAME
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

        btn_sign_in.setOnClickListener {
            animation.playAnimation()

            // login
            val email = et_email.text.toString()
            var password = et_password.text.toString()

            viewModel.login(email, password)
        }
    }

    // observe live data of viewmodel
    private fun subscribeObservers() {
        // observe loginApiStatus
        // success then go to user account page
        // fail with message then display alert
        viewModel.loginApiStatus.observe(this, Observer {
            when(it.status) {
                ApiResource.ApiStatus.LOADING -> {}
                ApiResource.ApiStatus.SUCCESS -> {
                    onLoginSuccess()
                }
                ApiResource.ApiStatus.ERROR -> {
                    showAlert("Error", it.error?.message ?: "")
                }
            }
        })
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USERNAME, et_name.text.toString())
        startActivity(intent)
        finish()
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) {
            dialog: DialogInterface?, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
}
