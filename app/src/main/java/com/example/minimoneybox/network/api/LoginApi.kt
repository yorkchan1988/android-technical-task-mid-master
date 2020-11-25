package com.example.minimoneybox.network.api

import com.example.minimoneybox.models.LoginResponse
import com.example.minimoneybox.models.request.LoginRequest
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/users/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<LoginResponse>
}