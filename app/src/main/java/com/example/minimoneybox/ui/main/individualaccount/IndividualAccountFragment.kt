package com.example.minimoneybox.ui.main.individualaccount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.FragmentIndividualaccountBinding
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
        Log.d(TAG, "onCreateView: "+investorProduct?.planValue)
        // create viewModel and assign custom params
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(IndividualAccountViewModel::class.java)
        viewModel.investorProduct = investorProduct

        // assign binding
        var binding : FragmentIndividualaccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_individualaccount, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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
                    backToUserAccounts()
                }
                ApiResource.ApiStatus.LOADING -> {

                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {exception ->
            exception.message?.let { message ->
                SimpleAlertDialog.showAlert(activity,"Error", message)
            }
        })
    }

    private fun backToUserAccounts() {
        fragmentManager?.popBackStack()
    }
}