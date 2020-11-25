package com.example.minimoneybox.di.main

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.di.ViewModelKey
import com.example.minimoneybox.ui.main.useraccounts.UserAccountsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserAccountsViewModel::class)
    abstract fun bindUserAccountsViewModel(viewModel: UserAccountsViewModel): ViewModel
}