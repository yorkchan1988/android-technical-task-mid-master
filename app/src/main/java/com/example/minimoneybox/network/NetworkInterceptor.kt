package com.example.minimoneybox.network

import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_API_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_APP_ID
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_APP_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_CONTENT_TYPE
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_API_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_APP_ID
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_APP_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_CONTENT_TYPE
import okhttp3.*

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // add required headers to each api before calling
        var builder = request.newBuilder()
            .addHeader(HEADER_KEY_APP_ID, HEADER_VALUE_APP_ID)
            .addHeader(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE)
            .addHeader(HEADER_KEY_APP_VERSION, HEADER_VALUE_APP_VERSION)
            .addHeader(HEADER_KEY_API_VERSION, HEADER_VALUE_API_VERSION)

        // check if bearer token is null
        // if yes, do nothing
        // if no, add Authorization header
        val bearerToken = SessionManager.getBearerToken()
        bearerToken?.let {
            builder.addHeader("Authorization", "Bearer "+it)
        }

        request = builder.build()

        return chain.proceed(request)
    }
}