package com.example.minimoneybox.ui.main.useraccounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.InvestorProductsRepository
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import javax.inject.Inject

class UserAccountsViewModel
@Inject constructor(
    investorProductsRepository: InvestorProductsRepository
) : ViewModel() {

    companion object {
        private const val TAG = "UserAccountsViewModel"
    }

    // LiveData
    var apiStatus: MutableLiveData<ApiResource<AccountDetails>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    var usernameText: MutableLiveData<String> = MutableLiveData("")
    var totalPlanValue: MutableLiveData<Int> = MutableLiveData(0)
    var products: MutableLiveData<List<InvestorProduct>> = MutableLiveData(emptyList())

    // API
    private val investorProductsRepository: InvestorProductsRepository = investorProductsRepository

    // Custom variable
    var username: String? = null
        set(value) {
            field = value
            usernameText.postValue(value)
        }

    fun getInvestorProducts() {
        // if fail with validation errors
        // update livedata of error messages of edit text
        investorProductsRepository.getInvestorProducts().subscribeBy(
            onNext = {
                when (it.status) {
                    ApiResource.ApiStatus.SUCCESS -> {
                        apiStatus.postValue(it)

                        it.data?.let {
                            totalPlanValue.postValue(it.totalPlanValue?.toInt())
                            products.postValue((it.products))
                        }
                    }
                    ApiResource.ApiStatus.ERROR -> {
                        it.error?.let {errorRes ->
                            errorRes.message?.let {msg ->
                                error.postValue(ApiException(msg))
                            }
                        }
                    }
                }
            },
            onError = {
                error.postValue(it as Exception)
            }
        )
    }
}