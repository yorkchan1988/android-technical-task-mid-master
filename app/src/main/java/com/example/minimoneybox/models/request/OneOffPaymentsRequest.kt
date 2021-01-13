package com.example.minimoneybox.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OneOffPaymentsRequest(
    @SerializedName("Amount")
    @Expose
    val amount: Double,
    @SerializedName("InvestorProductId")
    @Expose
    val investorProductId: Int
)