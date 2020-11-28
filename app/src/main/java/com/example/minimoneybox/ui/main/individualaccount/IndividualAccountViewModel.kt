package com.example.minimoneybox.ui.main.individualaccount

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.OneOffPaymentsRepository
import io.reactivex.rxkotlin.subscribeBy
import java.lang.Exception
import javax.inject.Inject

class IndividualAccountViewModel @Inject constructor(oneOffPaymentsRepository: OneOffPaymentsRepository): ViewModel() {

    // Custom variable
    var investorProduct: InvestorProduct? = null
        set(value) {
            field = value
            investorProductLiveData.value = value
        }

    var investorProductLiveData: MutableLiveData<InvestorProduct> = MutableLiveData()

    // API
    private val oneOffPaymentsRepository: OneOffPaymentsRepository = oneOffPaymentsRepository

    // LiveData
    var apiStatus: MutableLiveData<ApiResource<OneOffPaymentsResponse>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    fun oneOffPayments() {
        investorProduct?.let { investorProduct ->
            oneOffPaymentsRepository.oneOffPayments(
                investorProduct.subscriptionAmount,
                investorProduct.id
            ).subscribeBy(
                onNext = {
                    when (it.status) {
                        ApiResource.ApiStatus.SUCCESS -> {
                            apiStatus.postValue(it)
                        }
                        ApiResource.ApiStatus.ERROR -> {
                            it.error?.let { errorRes ->
                                errorRes.message?.let { msg ->
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
}