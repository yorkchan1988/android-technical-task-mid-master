package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.request.OneOffPaymentsRequest
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface OneOffPaymentsApi {
    @POST("/oneoffpayments")
    fun oneOffPayments(
        @Body body: OneOffPaymentsRequest
    ): Flowable<AccountDetails>
}