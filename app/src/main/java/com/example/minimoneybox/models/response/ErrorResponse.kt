package com.example.minimoneybox.models.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException

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
) {
    companion object {
        fun fromHttpException(error: HttpException): ErrorResponse? {
            val errorResponseString = error.response().errorBody()?.string()
            val errorResponse = Gson().fromJson(errorResponseString, ErrorResponse::class.java)
            return errorResponse
        }
    }
}

data class ValidationError(
    @SerializedName("Name")
    @Expose
    var errorName: String?,
    @SerializedName("Message")
    @Expose
    var message: String?
)