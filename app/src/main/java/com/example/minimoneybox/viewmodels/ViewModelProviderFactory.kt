package com.example.minimoneybox.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider


class ViewModelProviderFactory
@Inject
constructor(
    private val viewModelMap: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {

    companion object {
        private val TAG: String? = "ViewModelProviderFactory"
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        viewModelMap.let {
            it[modelClass]?.get()?.let { viewModel ->
                return viewModel as T
            }
            throw IllegalArgumentException("Unknown ViewModel type")
        }
    }
}