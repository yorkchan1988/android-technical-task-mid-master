package com.example.minimoneybox.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }
    init {
        Log.d(TAG, "LoginViewModel : viewmodel is working...")
    }
}