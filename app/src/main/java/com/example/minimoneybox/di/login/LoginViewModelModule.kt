package com.example.minimoneybox.di.login

import androidx.lifecycle.ViewModel
import com.example.minimoneybox.di.ViewModelKey
import com.example.minimoneybox.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}