package com.example.minimoneybox.ui.main.useraccounts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.LoginRepository
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import javax.inject.Inject

class UserAccountsViewModel @Inject constructor(investorProductsRepository: InvestorProductsRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "UserAccountsViewModel"
    }

    // LiveData
    var accountDetailsApiStatus: MutableLiveData<ApiResource<AccountDetails>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()
    var username: MutableLiveData<String> = MutableLiveData("Default")
    var totalPlanValue: MutableLiveData<Int> = MutableLiveData(0)

    // API
    private val investorProductsRepository: InvestorProductsRepository = investorProductsRepository

    fun getInvestorProducts() {
        // if fail with validation errors
        // update livedata of error messages of edit text
        investorProductsRepository.getInvestorProducts().subscribeBy(
            onNext = {
                accountDetailsApiStatus.postValue(it)
                username.postValue("Test1")
                totalPlanValue.postValue(it.data?.totalPlanValue?.toInt())
            },
            onError = {
                error.postValue(it as Exception)
            }
        )
    }
}