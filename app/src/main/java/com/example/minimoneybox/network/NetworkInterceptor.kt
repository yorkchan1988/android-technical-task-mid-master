package com.example.minimoneybox.network

import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_API_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_APP_ID
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_APP_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_KEY_CONTENT_TYPE
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_API_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_APP_ID
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_APP_VERSION
import com.example.minimoneybox.util.Constants.Companion.HEADER_VALUE_CONTENT_TYPE
import okhttp3.*

class NetworkInterceptor: Interceptor, Authenticator {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request?.newBuilder()
            ?.addHeader(HEADER_KEY_APP_ID, HEADER_VALUE_APP_ID)
            ?.addHeader(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE)
            ?.addHeader(HEADER_KEY_APP_VERSION, HEADER_VALUE_APP_VERSION)
            ?.addHeader(HEADER_KEY_API_VERSION, HEADER_VALUE_API_VERSION)
            ?.build()

        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        TODO("Not yet implemented")
    }
}