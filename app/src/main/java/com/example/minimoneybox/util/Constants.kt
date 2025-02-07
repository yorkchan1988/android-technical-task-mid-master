package com.example.minimoneybox.util

class Constants {
    companion object {

        // API URL
        const val BASE_URL: String = "https://api-test01.moneyboxapp.com"

        // API HEADER
        const val HEADER_KEY_APP_ID: String = "AppId"
        const val HEADER_VALUE_APP_ID: String = "3a97b932a9d449c981b595"
        const val HEADER_KEY_CONTENT_TYPE: String = "Content-Type"
        const val HEADER_VALUE_CONTENT_TYPE: String = "application/json"
        const val HEADER_KEY_APP_VERSION: String = "appVersion"
        const val HEADER_VALUE_APP_VERSION: String = "7.15.0"
        const val HEADER_KEY_API_VERSION: String = "apiVersion"
        const val HEADER_VALUE_API_VERSION: String = "3.0.0"

        // LOGIN API BODY FIELDS
        const val LOGIN_EMAIL: String = "jaeren+androidtest@moneyboxapp.com"
        const val LOGIN_PASSWORD: String = "P455word12"
        const val LOGIN_IDFA: String = "ANYTHING"

        // LOGIN API VALIDATION ERROR KEY
        const val LOGIN_VALIDATION_ERROR_KEY_EMAIL = "Email"
        const val LOGIN_VALIDATION_ERROR_KEY_PASSWORD = "Password"

        // INTENT EXTRA KEY
        const val USERNAME: String = "USERNAME"
        const val INVESTOR_PRODUCT: String = "INVESTOR_PRODUCT"

        // Key Store
        const val SECRET_KEY_ALIAS: String = "It is a secret key alias."
        const val ANDROID_KEY_STORE: String = "AndroidKeyStore"
        const val AES_MODE: String = "AES/GCM/NoPadding"
        const val FIXED_IV: String = "FIXED_IV"
    }
}