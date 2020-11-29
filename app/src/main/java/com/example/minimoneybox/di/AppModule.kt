package com.example.minimoneybox.di

import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.network.NetworkInterceptor
import com.example.minimoneybox.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {
    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideRetrofitInstance(client: OkHttpClient): Retrofit {

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideOkHttpClientInstance(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(NetworkInterceptor())
            return builder.build()
        }
    }
}