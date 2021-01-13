package com.example.minimoneybox.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccountDetails(
    @SerializedName("TotalPlanValue")
    @Expose
    val totalPlanValue: Double,
    @SerializedName("ProductResponses")
    @Expose
    val products: List<InvestorProduct>
)