package com.example.minimoneybox.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginSession (
    @SerializedName("Session")
    @Expose
    val session: Session?
)

data class Session (
    @SerializedName("BearerToken")
    @Expose
    val token: String?
)