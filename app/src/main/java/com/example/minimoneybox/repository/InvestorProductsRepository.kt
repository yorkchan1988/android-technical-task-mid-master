package com.example.minimoneybox.repository

import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.network.api.InvestorProductsApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class InvestorProductsRepository @Inject constructor(private val investorProductsApi: InvestorProductsApi) {
    companion object{
        private const val TAG = "InvestorProductsRepository"
    }

    fun getInvestorProducts(): Observable<ApiResult.Success<AccountDetails>> {

        return investorProductsApi.getInvestorProduct()
            .startWith {
                ApiResult.Loading<AccountDetails>()
            }
            .subscribeOn(Schedulers.io())
            .map {
                ApiResult.Success(it)
            }
            .doOnError {
                if (it is HttpException) {
                    // api returns error, parse error response and return error response object
                    val errorResponse = ErrorResponse.fromHttpException(it)
                    ApiResult.Error(null, errorResponse)
                } else {
                    it
                }
            }
    }
}