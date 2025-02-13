package com.example.minimoneybox.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccountDetails(
    @SerializedName("TotalPlanValue")
    @Expose
    var totalPlanValue: Double,
    @SerializedName("ProductResponses")
    @Expose
    var products: List<InvestorProduct>
)