package com.example.minimoneybox.ui.main.individualaccount

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.FragmentIndividualaccountBinding
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.SimpleAlertDialog
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class IndividualAccountFragment: DaggerFragment() {

    companion object {
        const val TAG = "IndividualAccountFrag"
    }

    lateinit var viewModel: IndividualAccountViewModel

    lateinit var llprogressBar: View

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set support action bar title
        (activity as AppCompatActivity).supportActionBar?.title = "Individual Account"

        // investorProduct from UserAccountsFragment
        val investorProduct : InvestorProduct? = arguments?.getParcelable(Constants.INVESTOR_PRODUCT)

        // create viewModel and assign custom params
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(IndividualAccountViewModel::class.java)
        viewModel.investorProduct = investorProduct

        // assign binding
        val binding : FragmentIndividualaccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_individualaccount, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        llprogressBar = binding.llProgressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // subscribe observers from ViewModel
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer {
            when(it.status){
                ApiResource.ApiStatus.SUCCESS -> {
                    llprogressBar.visibility = View.GONE
                    SimpleAlertDialog.showAlert(activity, "Success", "Moneybox is added successfully!") { dialog: DialogInterface?, which: Int ->
                        backToUserAccounts()
                    }
                }
                ApiResource.ApiStatus.LOADING -> {
                    llprogressBar.visibility = View.VISIBLE
                }
                ApiResource.ApiStatus.ERROR -> {
                    llprogressBar.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {exception ->
            if (exception is ApiException) {
                val name = exception.name
                val message = exception.message
                SimpleAlertDialog.showAlert(activity,name, message)
            }
            else {
                exception.message?.let { message ->
                    SimpleAlertDialog.showAlert(activity,"Error", message)
                }
            }
        })
    }

    private fun backToUserAccounts() {
        fragmentManager?.popBackStack()
    }
}