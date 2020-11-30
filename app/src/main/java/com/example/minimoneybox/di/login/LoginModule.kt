package com.example.minimoneybox.di.login

import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.repository.LoginRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoginModule {

    @Module
    companion object {

        @LoginScope
        @JvmStatic
        @Provides
        fun provideLoginApi(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }

        @LoginScope
        @JvmStatic
        @Provides
        fun provideLoginRepository(loginApi: LoginApi, sessionManager: SessionManager): LoginRepository {
            return LoginRepository(loginApi, sessionManager)
        }
    }
}