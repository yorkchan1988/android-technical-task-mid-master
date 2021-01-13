package com.example.minimoneybox.models.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

data class ErrorResponse(
    @SerializedName("Name")
    @Expose
    val errorName: String?,
    @SerializedName("Message")
    @Expose
    val message: String?,
    @SerializedName("ValidationErrors")
    @Expose
    val validationErrors: List<ValidationError>?
) {
    companion object {
        fun fromHttpException(error: HttpException): ErrorResponse {
            val errorResponseString = error.response().errorBody()?.string()
            return Gson().fromJson(errorResponseString, ErrorResponse::class.java)
        }
    }
}

data class ValidationError(
    @SerializedName("Name")
    @Expose
    val errorName: String?,
    @SerializedName("Message")
    @Expose
    val message: String?
)