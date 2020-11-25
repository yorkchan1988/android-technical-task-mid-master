package com.example.minimoneybox.di

import com.example.minimoneybox.di.login.LoginModule
import com.example.minimoneybox.di.login.LoginViewModelModule
import com.example.minimoneybox.ui.login.LoginActivity
import com.example.minimoneybox.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [
            LoginViewModelModule::class,
            LoginModule::class
        ]
    )
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}