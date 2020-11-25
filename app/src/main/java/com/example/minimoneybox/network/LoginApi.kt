package com.example.minimoneybox.network

import com.example.minimoneybox.models.Response
import com.example.minimoneybox.models.request.LoginRequest
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/users/login")
    fun login(
        @Body body: LoginRequest
    ): Flowable<Response>
}