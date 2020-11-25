package com.example.minimoneybox.ui.main.useraccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.minimoneybox.R
import dagger.android.support.DaggerFragment

class UserAccountsFragment: DaggerFragment() {
    companion object {
        private const val TAG = "UserAccountsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity, "User Accounts Fragment", Toast.LENGTH_LONG).show()

        return inflater.inflate(R.layout.fragment_useraccounts, container, false)
    }
}