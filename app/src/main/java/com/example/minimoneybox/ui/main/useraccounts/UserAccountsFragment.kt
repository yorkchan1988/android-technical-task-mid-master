package com.example.minimoneybox.ui.main.useraccounts

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.FragmentUseraccountsBinding
import com.example.minimoneybox.network.ApiResource
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
        val binding : FragmentUseraccountsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_useraccounts, container, false)
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(UserAccountsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getInvestorProducts()
        subscribeObservers();
    }

    private fun subscribeObservers() {
        viewModel.accountDetailsApiStatus.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                ApiResource.ApiStatus.LOADING -> {}
                ApiResource.ApiStatus.SUCCESS -> { }
                ApiResource.ApiStatus.ERROR -> {
                    showAlert("Error", it.error?.message ?: "")
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showAlert("Error", it.localizedMessage)
        })
    }

    private fun showAlert(title: String, message: String) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, which: Int ->
                Toast.makeText(
                    activity,
                    android.R.string.yes, Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
    }
}