package com.example.minimoneybox.di

import com.example.minimoneybox.LoginActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity;

    @Module companion object {
        @JvmStatic
        @Provides
        fun testString(): String {
            return "DI is working."
        }
    }
}