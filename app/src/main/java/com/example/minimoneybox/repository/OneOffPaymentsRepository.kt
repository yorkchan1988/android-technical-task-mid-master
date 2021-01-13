package com.example.minimoneybox.repository

import com.example.minimoneybox.models.request.OneOffPaymentsRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.network.api.OneOffPaymentsApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class OneOffPaymentsRepository @Inject constructor(private val oneOffPaymentsApi: OneOffPaymentsApi) {
    companion object {
        private const val TAG = "OneOffPaymentsRepository"
    }

    fun oneOffPayments(amount: Double, investorProductId: Int): Observable<ApiResult<OneOffPaymentsResponse>> {

        return Observable.create { emitter ->
            // catch all unexpected errors
            try {
                // start to call api, change api status to loading
                emitter.onNext(ApiResult.Loading())

                oneOffPaymentsApi.oneOffPayments(OneOffPaymentsRequest(amount, investorProductId))
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { data ->
                            // api return success, return the parsed data object
                            emitter.onNext(ApiResult.Success(data))
                            emitter.onComplete()
                        },
                        { error ->
                            if (error is HttpException) {
                                // api returns error, parse error response and return error response object
                                val errorResponse = ErrorResponse.fromHttpException(error)
                                emitter.onNext(ApiResult.Error(null, errorResponse))
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