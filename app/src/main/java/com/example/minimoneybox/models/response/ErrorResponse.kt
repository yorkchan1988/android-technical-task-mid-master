package com.example.minimoneybox.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("Name")
    @Expose
    var errorName: String?,
    @SerializedName("Message")
    @Expose
    var message: String?,
    @SerializedName("ValidationErrors")
    @Expose
    var validationErrors: List<ValidationError>?
)

data class ValidationError(
    @SerializedName("Name")
    @Expose
    var errorName: String?,
    @SerializedName("Message")
    @Expose
    var message: String?
)