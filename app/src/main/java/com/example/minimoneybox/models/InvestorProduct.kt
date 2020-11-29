package com.example.minimoneybox.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvestorProduct(
    @SerializedName("Id")
    @Expose
    var id: Int,
    @SerializedName("PlanValue")
    @Expose
    var planValue: Double,
    @SerializedName("Moneybox")
    @Expose
    var moneyboxValue: Double,
    @SerializedName("SubscriptionAmount")
    @Expose
    var subscriptionAmount: Double,
    @SerializedName("Product")
    @Expose
    var product: ProductDetail
) : Parcelable

@Parcelize
data class ProductDetail(
    @SerializedName("FriendlyName")
    @Expose
    var name: String
) : Parcelable