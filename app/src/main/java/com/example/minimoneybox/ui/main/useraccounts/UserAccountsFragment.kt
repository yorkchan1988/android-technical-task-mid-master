package com.example.minimoneybox.ui.main.useraccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.ui.login.LoginViewModel
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserAccountsFragment: DaggerFragment() {
    companion object {
        private const val TAG = "UserAccountsFragment"
    }

    lateinit var viewModel: UserAccountsViewModel

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity, "User Accounts Fragment", Toast.LENGTH_LONG).show()

        return inflater.inflate(R.layout.fragment_useraccounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(UserAccountsViewModel::class.java)
    }
}