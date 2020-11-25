package com.example.minimoneybox.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.models.LoginResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.LoginRepository
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class LoginViewModel @Inject constructor(loginRepository: LoginRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    // LiveData
    var loginApiStatus : MutableLiveData<ApiResource<LoginResponse>> = MutableLiveData()

    // API
    private val loginRepository: LoginRepository = loginRepository

    fun login(email: String, password: String, name: String) {
        // if fail with validation errors
        // update livedata of error messages of edit text
        loginRepository.login(email, password).subscribeBy {
            loginApiStatus.postValue(it)
//            TODO("if fail with validation errors, update livedata of error messages of edit text")
        }
    }
}
