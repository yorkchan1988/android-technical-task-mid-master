package com.example.minimoneybox.ui.main.useraccounts

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class UserAccountsViewModel @Inject constructor(): ViewModel() {

    companion object {
        private const val TAG = "UserAccountsViewModel"
    }

    init {
        Log.d(TAG, "UserAccountsViewModel: ViewModel is ready.")
    }
}