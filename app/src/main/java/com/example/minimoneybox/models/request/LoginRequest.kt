package com.example.minimoneybox.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("Email")
    @Expose
    val email: String,
    @SerializedName("Password")
    @Expose
    val password: String,
    @SerializedName("Idfa")
    @Expose
    val idfa: String
)