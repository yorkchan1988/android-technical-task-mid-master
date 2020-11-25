package com.example.minimoneybox.di.main

import com.example.minimoneybox.ui.main.useraccounts.UserAccountsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeUserAccountsFragment(): UserAccountsFragment
}