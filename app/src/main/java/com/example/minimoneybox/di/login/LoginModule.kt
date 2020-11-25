package com.example.minimoneybox.di.login

import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.repository.LoginRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoginModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideLoginApi(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }

        @JvmStatic
        @Provides
        fun provideLoginRespository(authApi: LoginApi): LoginRepository {
            return LoginRepository(authApi)
        }
    }
}