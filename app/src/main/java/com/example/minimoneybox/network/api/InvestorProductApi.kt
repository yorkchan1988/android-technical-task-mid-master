package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.AccountDetails
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface InvestorProductApi {

    @GET("/investorproducts")
    fun getInvestorProduct(): Observable<AccountDetails>
}