package com.example.minimoneybox.di.main

import com.example.minimoneybox.network.api.InvestorProductsApi
import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.network.api.OneOffPaymentsApi
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.repository.OneOffPaymentsRepository
import com.example.minimoneybox.ui.main.useraccounts.InvestorProductsRecyclerAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Module
    companion object {

        @MainScope
        @JvmStatic
        @Provides
        fun provideInvestorProductsApi(retrofit: Retrofit): InvestorProductsApi {
            return retrofit.create(InvestorProductsApi::class.java)
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideOneOffPaymentsApi(retrofit: Retrofit): OneOffPaymentsApi {
            return retrofit.create(OneOffPaymentsApi::class.java)
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideInvestorProductsRespository(investorProductsApi: InvestorProductsApi): InvestorProductsRepository {
            return InvestorProductsRepository(investorProductsApi)
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideOneOffPaymentsRespository(oneOffPaymentsApi: OneOffPaymentsApi): OneOffPaymentsRepository {
            return OneOffPaymentsRepository(oneOffPaymentsApi)
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideInvestorProductsRecyclerAdapter(): InvestorProductsRecyclerAdapter {
            return InvestorProductsRecyclerAdapter()
        }
    }

}