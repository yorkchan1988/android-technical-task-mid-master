package com.example.minimoneybox.di

import android.app.Application
import android.content.Context
import android.security.KeyPairGeneratorSpec
import com.example.minimoneybox.BaseApplication
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
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.security.auth.x500.X500Principal


@Module
class AppModule {
    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun bindContext(application: BaseApplication): Context {
            return application.applicationContext
        }

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
        fun provideOkHttpClientInstance(networkInterceptor: NetworkInterceptor): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(networkInterceptor)
            return builder.build()
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideNetworkInterceptor(sessionManager: SessionManager): NetworkInterceptor {
            return NetworkInterceptor(sessionManager)
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideSessionManager(context: Context, spec: KeyPairGeneratorSpec): SessionManager {
            return SessionManager(context, spec)
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideKeyPairGeneratorSpec(context: Context): KeyPairGeneratorSpec {
            val alias = Constants.SECRET_KEY_ALIAS
            val start: Calendar = GregorianCalendar()
            val end: Calendar = GregorianCalendar()
            end.add(Calendar.YEAR, 30)

            return KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=$alias"))
                .setSerialNumber(
                    BigInteger.valueOf(
                        Math.abs(alias.hashCode()).toLong()
                    )
                )
                .setStartDate(start.time).setEndDate(end.time)
                .build()
        }
    }
}