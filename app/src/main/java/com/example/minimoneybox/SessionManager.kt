@file:Suppress("DEPRECATION")

package com.example.minimoneybox

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.minimoneybox.util.Constants.Companion.ANDROID_KEY_STORE
import com.example.minimoneybox.util.Constants.Companion.RSA_ALGO
import com.example.minimoneybox.util.Constants.Companion.SECRET_FILE_NAME
import com.example.minimoneybox.util.Constants.Companion.SECRET_KEY_ALIAS
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.*
import javax.inject.Inject
import javax.security.auth.x500.X500Principal


// A class that used to
// 1. save bearer token to keystore
// 2. get bearer token from keystore
// 3. clear bearer token if session expired
class SessionManager @Inject constructor(context: Context, spec: KeyPairGeneratorSpec) {

    enum class AuthStatus {AUTHENTICATED, NOT_AUTHENTICATED}

    companion object {
        private const val TAG = "SessionManager"
        var authStatus: MutableLiveData<AuthStatus> = MutableLiveData(AuthStatus.NOT_AUTHENTICATED)
    }

    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        val masterKeyAlias: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        } else {

            val kpGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
                RSA_ALGO,
                ANDROID_KEY_STORE
            )
            kpGenerator.initialize(spec)
            val kp: KeyPair = kpGenerator.generateKeyPair()
            masterKeyAlias = kp.public.toString()
        }

        sharedPreferences = EncryptedSharedPreferences.create(
            SECRET_FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        editor = sharedPreferences.edit()
    }

    fun getBearerToken(): String? {
        val token = sharedPreferences.getString("BearerToken", null)
        Log.d(TAG, "SessionManager: "+token)
        return token
    }

    fun login(token: String) {
        authStatus.postValue(AuthStatus.AUTHENTICATED)
        editor.putString("BearerToken", token)
        editor.commit()
    }

    fun logout() {
        authStatus.postValue(AuthStatus.NOT_AUTHENTICATED)
        editor.putString("BearerToken", null)
        editor.commit()
    }
}