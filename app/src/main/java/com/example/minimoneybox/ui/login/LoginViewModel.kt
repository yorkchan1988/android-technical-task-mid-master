package com.example.minimoneybox.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.exception.ApiException
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.response.ValidationError
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.util.Constants.Companion.LOGIN_VALIDATION_ERROR_KEY_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_VALIDATION_ERROR_KEY_PASSWORD
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class LoginViewModel @Inject constructor(loginRepository: LoginRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    // LiveData
    var apiStatus : MutableLiveData<ApiResource<LoginSession>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    var emailErrorText: MutableLiveData<String> = MutableLiveData("")
    var passwordErrorText: MutableLiveData<String> = MutableLiveData("")

    // API
    private val loginRepository: LoginRepository = loginRepository

    fun login(email: String, password: String) {
        // if fail with validation errors
        // update livedata of error messages of edit text
        loginRepository.login(email, password).subscribeBy(
            onNext = {
                apiStatus.postValue(it)
                when (it.status) {
                    ApiResource.ApiStatus.SUCCESS -> {
                        // if success
                        // return apiStatus directly

                    }
                    ApiResource.ApiStatus.ERROR -> {
                        // if error
                        // return apiStatus
                        it.error?.let {errorRes ->
                            errorRes.validationErrors?.let {list ->
                                // check if validationErrors.size == 0
                                if (list.size == 0) {
                                    // if yes, return error with ApiException
                                    errorRes.message?.let {msg ->
                                        val name = errorRes.errorName ?: "Error"
                                        error.postValue(ApiException(name, msg))
                                    }
                                }
                                // if no, update errorErrorText and passwordErrorText LiveData
                                else {
                                    val errors = list.toSet()
                                    val emailErrorMsg = getValidationErrorMessage(errors, LOGIN_VALIDATION_ERROR_KEY_EMAIL)
                                    val passwordErrorMsg = getValidationErrorMessage(errors, LOGIN_VALIDATION_ERROR_KEY_PASSWORD)

                                    emailErrorText.postValue(emailErrorMsg)
                                    passwordErrorText.postValue(passwordErrorMsg)
                                }
                            }
                        }
                    }
                }
            },
            onError = {
                apiStatus.postValue(ApiResource.Error(null, null))
                error.postValue(it as Exception)
            }
        )
    }

    fun getValidationErrorMessage(errors: Set<ValidationError>, key: String): String {
        var errorMessage = ""
        val errorMsg = errors.find {
            it.errorName == key
        }
        errorMsg?.let {
            errorMessage = it.message ?: ""
        }
        return errorMessage
    }
}
