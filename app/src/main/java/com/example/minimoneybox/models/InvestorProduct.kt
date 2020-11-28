package com.example.minimoneybox.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InvestorProduct(
    @SerializedName("Id")
    @Expose
    var id: String,
    @SerializedName("PlanValue")
    @Expose
    var planValue: Double,
    @SerializedName("Moneybox")
    @Expose
    var moneyboxValue: Double,
    @SerializedName("SubscriptionAmount")
    @Expose
    var subscriptionAmount: Float,
    @SerializedName("Product")
    @Expose
    var product: ProductDetail
)

data class ProductDetail(
    @SerializedName("FriendlyName")
    @Expose
    var name: String
)