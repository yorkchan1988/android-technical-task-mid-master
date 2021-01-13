package com.example.minimoneybox.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OneOffPaymentsResponse(
    @SerializedName("Moneybox")
    @Expose
    val moneybox : Double
)