package com.example.minimoneybox.ui.main.useraccounts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.FragmentUseraccountsBinding
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.ui.main.MainActivity
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.SimpleAlertDialog
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_useraccounts.*
import javax.inject.Inject


class UserAccountsFragment: DaggerFragment() {
    companion object {
        const val TAG = "UserAccountsFrag"
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

        viewModel.error.observe(viewLifecycleOwner, Observer {exception ->
            if (exception is ApiException) {
                val apiException = exception as ApiException
                val name = apiException.name
                val message = apiException.message
                SimpleAlertDialog.showAlert(activity,name, message)
            }
            else {
                exception.message?.let { message ->
                    SimpleAlertDialog.showAlert(activity,"Error", message)
                }
            }
        })

        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter.setInvestorProducts(it)
            adapter.setListener {
                Log.d(TAG, "subscribeObservers: "+it.planValue)
                navigateToIndividualAccount(it)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView.setLayoutManager(LinearLayoutManager(activity))
        recyclerView.setAdapter(adapter)
    }

    private fun navigateToIndividualAccount(investorProduct: InvestorProduct) {
        (activity as MainActivity).navigateToIndividualAccount(investorProduct)
    }
}