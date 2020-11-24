package com.example.minimoneybox.di

import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        fun testString(): String {
            return "DI is working."
        }
    }
}