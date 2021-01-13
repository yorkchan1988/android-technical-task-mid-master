package com.example.minimoneybox.ui.main.useraccounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.repository.InvestorProductsRepository
import io.reactivex.disposables.CompositeDisposable
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
    val apiStatus: MutableLiveData<ApiResult<AccountDetails>> = MutableLiveData()
    val error: MutableLiveData<Exception> = MutableLiveData()

    val usernameText: MutableLiveData<String> = MutableLiveData("")
    val totalPlanValue: MutableLiveData<Int> = MutableLiveData(0)
    val products: MutableLiveData<List<InvestorProduct>> = MutableLiveData(emptyList())

    // API
    private val investorProductsRepository: InvestorProductsRepository = investorProductsRepository

    // Disposable
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    // Custom variable
    var username: String? = ""
        set(value) {
            field = value ?: ""
            usernameText.postValue(field)
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.let {
            this.compositeDisposable.dispose()
        }
    }

    fun getInvestorProducts() {
        // if fail with validation errors
        // update livedata of error messages of edit text
        val disposable = investorProductsRepository.getInvestorProducts().subscribeBy(
            onNext = {
                apiStatus.postValue(it)
                when (it.status) {
                    ApiResult.ApiStatus.SUCCESS -> {
                        it.data?.let {
                            totalPlanValue.postValue(it.totalPlanValue.toInt())
                            products.postValue((it.products))
                        }
                    }
                    ApiResult.ApiStatus.ERROR -> {
                        it.error?.let {errorRes ->
                            errorRes.message?.let {msg ->
                                val name = errorRes.errorName ?: "Error"
                                error.postValue(ApiException(name, msg))
                            }
                        }
                    }
                }
            },
            onError = {
                apiStatus.postValue(ApiResult.Error(null, null))
                error.postValue(it as Exception)
            }
        )

        compositeDisposable.add(disposable)
    }
}