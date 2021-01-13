package com.example.minimoneybox.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvestorProduct(
    @SerializedName("Id")
    @Expose
    val id: Int,
    @SerializedName("PlanValue")
    @Expose
    val planValue: Double,
    @SerializedName("Moneybox")
    @Expose
    val moneyboxValue: Double,
    @SerializedName("SubscriptionAmount")
    @Expose
    val subscriptionAmount: Double,
    @SerializedName("Product")
    @Expose
    val product: ProductDetail
) : Parcelable

@Parcelize
data class ProductDetail(
    @SerializedName("FriendlyName")
    @Expose
    val name: String
) : Parcelable