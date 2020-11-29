package com.example.minimoneybox.repository

import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.network.api.InvestorProductsApi
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class InvestorProductsRepository @Inject constructor(investorProductsApi: InvestorProductsApi) {
    companion object{
        private const val TAG = "InvestorProductsRepository"
    }

    private val investorProductsApi : InvestorProductsApi = investorProductsApi

    fun getInvestorProducts(): Observable<ApiResource<AccountDetails>> {

        return Observable.create { emitter ->
            // catch all unexpected errors
            try {
                // start to call api, change api status to loading
                emitter.onNext(ApiResource.Loading())

                investorProductsApi.getInvestorProduct()
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { data ->
                            // api return success, return the parsed data object
                            emitter.onNext(ApiResource.Success(data))
                            emitter.onComplete()
                        },
                        { error ->

                            if (error is HttpException) {
                                // api returns error, parse error response and return error response object
                                val errorResponse = ErrorResponse.fromHttpException(error)
                                emitter.onNext(ApiResource.Error(null, errorResponse))
                            }
                            else {
                                emitter.onError(error)
                            }
                            emitter.onComplete()
                        }
                    )
            }
            catch (error: Exception) {
                emitter.onError(error)
                emitter.onComplete()
            }
        }
    }
}