package com.example.minimoneybox.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityLoginBinding
import com.example.minimoneybox.databinding.ActivityMainBinding
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
    lateinit var btnSignIn: Button
    lateinit var tilEmail: TextInputLayout
    lateinit var etEmail: EditText
    lateinit var tilPassword: TextInputLayout
    lateinit var etPassword: EditText
    lateinit var tilName: TextInputLayout
    lateinit var etName: EditText
    lateinit var animation: LottieAnimationView
    lateinit var llprogressBar: View
    lateinit var tvEmailError: TextView
    lateinit var tvPasswordError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this@LoginActivity, R.layout.activity_login)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)

        setupViews(binding)
        subscribeObservers()
    }

    private fun setupViews(binding: ActivityLoginBinding) {
        btnSignIn = binding.btnSignIn
        tilEmail = binding.tilEmail
        etEmail = binding.etEmail
        tilPassword = binding.tilPassword
        etPassword = binding.etPassword
        tilName = binding.tilName
        etName = binding.etName
        animation = binding.animation
        llprogressBar = binding.llProgressBar
        tvEmailError = binding.tvEmailError
        tvPasswordError = binding.tvPasswordError

        btnSignIn.setOnClickListener {
            animation.playAnimation()

            // login
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

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
            tvEmailError.setText(it)
        })

        viewModel.passwordErrorText.observe(this, Observer {
            tvPasswordError.setText(it)
        })
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USERNAME, etName.text.toString().trim())
        startActivity(intent)
        finish()
    }
}
