package com.example.minimoneybox.di.main

import com.example.minimoneybox.network.api.InvestorProductsApi
import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.network.api.OneOffPaymentsApi
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.OneOffPaymentsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideInvestorProductsApi(retrofit: Retrofit): InvestorProductsApi {
            return retrofit.create(InvestorProductsApi::class.java)
        }

        @JvmStatic
        @Provides
        fun provideOneOffPaymentsApi(retrofit: Retrofit): OneOffPaymentsApi {
            return retrofit.create(OneOffPaymentsApi::class.java)
        }

        @JvmStatic
        @Provides
        fun provideInvestorProductsRespository(investorProductsApi: InvestorProductsApi): InvestorProductsRepository {
            return InvestorProductsRepository(investorProductsApi)
        }

        @JvmStatic
        @Provides
        fun provideOneOffPaymentsRespository(oneOffPaymentsApi: OneOffPaymentsApi): OneOffPaymentsRepository {
            return OneOffPaymentsRepository(oneOffPaymentsApi)
        }
    }

}