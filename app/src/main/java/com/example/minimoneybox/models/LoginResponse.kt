package com.example.minimoneybox.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("Session")
    @Expose
    var session: Session?
)

data class Session (
    @SerializedName("BearerToken")
    @Expose
    var token: String?
)