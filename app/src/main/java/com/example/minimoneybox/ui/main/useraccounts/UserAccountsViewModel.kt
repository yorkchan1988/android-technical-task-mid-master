package com.example.minimoneybox.ui.main.useraccounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.network.ApiResource
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
    var apiStatus: MutableLiveData<ApiResource<AccountDetails>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    var usernameText: MutableLiveData<String> = MutableLiveData("")
    var totalPlanValue: MutableLiveData<Int> = MutableLiveData(0)
    var products: MutableLiveData<List<InvestorProduct>> = MutableLiveData(emptyList())

    // API
    private val investorProductsRepository: InvestorProductsRepository = investorProductsRepository

    // Disposable
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

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
                    ApiResource.ApiStatus.SUCCESS -> {
                        it.data?.let {
                            totalPlanValue.postValue(it.totalPlanValue.toInt())
                            products.postValue((it.products))
                        }
                    }
                    ApiResource.ApiStatus.ERROR -> {
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
                apiStatus.postValue(ApiResource.Error(null, null))
                error.postValue(it as Exception)
            }
        )

        compositeDisposable.add(disposable)
    }
}