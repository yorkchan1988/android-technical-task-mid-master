package com.example.minimoneybox.ui.main.individualaccount

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.OneOffPaymentsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import javax.inject.Inject

class IndividualAccountViewModel @Inject constructor(// API
    private val oneOffPaymentsRepository: OneOffPaymentsRepository
): ViewModel() {

    // Custom variable
    var investorProduct: InvestorProduct? = null
        set(value) {
            field = value
            investorProductLiveData.value = value
        }

    var investorProductLiveData: MutableLiveData<InvestorProduct> = MutableLiveData()

    // LiveData
    var apiStatus: MutableLiveData<ApiResource<OneOffPaymentsResponse>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    // Disposable
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.let {
            this.compositeDisposable.dispose()
        }
    }

    fun oneOffPayments() {
        investorProduct?.let { investorProduct ->
            val disposable = oneOffPaymentsRepository.oneOffPayments(
                investorProduct.subscriptionAmount,
                investorProduct.id
            ).subscribeBy(
                onNext = {
                    apiStatus.postValue(it)
                    when (it.status) {
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
                    error.postValue(it as Exception)
                }
            )

            compositeDisposable.add(disposable)
        }


    }
}