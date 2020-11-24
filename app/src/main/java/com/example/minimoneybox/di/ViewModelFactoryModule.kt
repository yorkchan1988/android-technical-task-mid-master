package com.example.minimoneybox.di

import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory;
}