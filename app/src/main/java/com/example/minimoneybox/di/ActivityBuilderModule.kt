package com.example.minimoneybox.di

import com.example.minimoneybox.di.login.LoginViewModelModule
import com.example.minimoneybox.ui.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [
            LoginViewModelModule::class
        ]
    )
    abstract fun contributeLoginActivity(): LoginActivity;
}