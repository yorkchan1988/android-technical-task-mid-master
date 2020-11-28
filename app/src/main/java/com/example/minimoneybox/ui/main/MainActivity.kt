package com.example.minimoneybox.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.ActivityMainBinding
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.ui.BaseActivity
import com.example.minimoneybox.ui.main.individualaccount.IndividualAccountFragment
import com.example.minimoneybox.ui.main.useraccounts.UserAccountsFragment
import com.example.minimoneybox.util.Constants.Companion.INVESTOR_PRODUCT

class MainActivity: BaseActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        initFragment()
    }

    // fragment navigation
    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, UserAccountsFragment(), UserAccountsFragment.TAG)
            .commit()
    }

    fun navigateToIndividualAccount(investorProduct: InvestorProduct) {
        val fragment = IndividualAccountFragment()
        val args = Bundle().apply {
            putParcelable(INVESTOR_PRODUCT, investorProduct)
        }
        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .addToBackStack(IndividualAccountFragment.TAG)
            .replace(R.id.main_container, fragment, IndividualAccountFragment.TAG)
            .commit()
    }
}