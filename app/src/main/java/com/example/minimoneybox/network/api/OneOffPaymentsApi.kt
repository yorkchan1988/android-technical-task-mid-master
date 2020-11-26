package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.request.OneOffPaymentsRequest
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface OneOffPaymentsApi {
    @POST("/oneoffpayments")
    fun oneOffPayments(
        @Body body: OneOffPaymentsRequest
    ): Observable<OneOffPaymentsResponse>
}