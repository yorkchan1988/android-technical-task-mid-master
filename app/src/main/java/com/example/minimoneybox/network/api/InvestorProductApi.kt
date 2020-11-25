package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.Response
import com.example.minimoneybox.models.request.LoginRequest
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET

interface InvestorProductApi {

    @GET("/investorproducts")
    fun getInvestorProduct(): Flowable<AccountDetails>
}