package com.example.minimoneybox.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.response.ValidationError
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.network.ApiResult.*
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.util.Constants.Companion.LOGIN_VALIDATION_ERROR_KEY_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_VALIDATION_ERROR_KEY_PASSWORD
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import com.example.minimoneybox.errorhandling.ErrorEntity.*

class LoginViewModel @Inject constructor(loginRepository: LoginRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    // LiveData
    var apiResult : MutableLiveData<ApiResult<LoginSession>> = MutableLiveData()
    var error: MutableLiveData<Exception> = MutableLiveData()

    var emailErrorText: MutableLiveData<String> = MutableLiveData("")
    var passwordErrorText: MutableLiveData<String> = MutableLiveData("")

    // API
    private val loginRepository: LoginRepository = loginRepository

    // Disposable
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.let {
            this.compositeDisposable.dispose()
        }
    }

    fun login(email: String, password: String) {
        // if fail with validation errors
        // update livedata of error messages of edit text
        val disposable = loginRepository.login(email, password)
            .subscribeBy(
                onNext = {
                    when(it) {
                        is Loading -> {
                            apiResult.postValue(it)
                        }
                        is Success -> {
                            apiResult.postValue(it)
                        }
                        is Error -> {
                            // if error
                            // return apiStatus
                            when (it.error) {
                                is ApiError -> {
                                    val errorName = it.error.errorResponse.errorName
                                    val message = it.error.errorResponse.message
                                    val validationError = it.error.errorResponse.validationErrors
                                    validationError?.let {list ->
                                        // check if validationErrors.size == 0
                                        if (list.size == 0) {
                                            // if yes, return error with ApiException
                                            message?.let {msg ->
                                                val name = errorName ?: "Error"
                                                apiResult.postValue(Error(UnexpectedError(it.message ?: "")))
//                                                error.postValue(ErrorhandlingApiException(name, msg))
                                            }

                                            // reset error text
                                            emailErrorText.postValue("")
                                            passwordErrorText.postValue("")
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
                    }
                },
                onError = {
                    apiResult.postValue(Error(UnexpectedError(it.message ?: "")))
                    // reset error text
                    emailErrorText.postValue("")
                    passwordErrorText.postValue("")
                }
            )

        compositeDisposable.add(disposable)
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
