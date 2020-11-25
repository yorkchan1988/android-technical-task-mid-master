package com.example.minimoneybox.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OneOffPaymentsRequest(
    @SerializedName("Amount")
    @Expose
    var amount: Int,
    @SerializedName("InvestorProductId")
    @Expose
    var investorProductId: Int
)