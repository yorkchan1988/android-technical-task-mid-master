package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.AccountDetails
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface InvestorProductsApi {

    @GET("/investorproducts")
    fun getInvestorProduct(): Observable<AccountDetails>
}