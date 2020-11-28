package com.example.minimoneybox.ui.main.useraccounts

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.FragmentUseraccountsBinding
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class UserAccountsFragment: DaggerFragment() {
    companion object {
        private const val TAG = "UserAccountsFragment"
    }

    lateinit var viewModel: UserAccountsViewModel
    lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var adapter: InvestorProductsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set support action bar title
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_user_accounts)

        // username from LoginActivity
        val username = activity?.intent?.extras?.getString(Constants.USERNAME)

        // create viewModel and assign custom params
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(UserAccountsViewModel::class.java)
        viewModel.username = username

        // assign binding
        val binding : FragmentUseraccountsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_useraccounts, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // init recyclerView
        recyclerView = binding.rvUserAccounts
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // get data from ViewModel
        viewModel.getInvestorProducts()

        // subscribe observers from ViewModel
        subscribeObservers();
    }

    private fun subscribeObservers() {
        viewModel.accountDetailsApiStatus.observe(viewLifecycleOwner, Observer {
            if (it.status == ApiResource.ApiStatus.LOADING) {

            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {exception ->
            exception.message?.let { message ->
                showAlert("Error", message)
            }
        })

        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter.setInvestorProducts(it)
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

    private fun initRecyclerView() {
        recyclerView.setLayoutManager(LinearLayoutManager(activity))
        recyclerView.setAdapter(adapter)
    }
}