package com.example.minimoneybox.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.Session
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.ValidationError
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.ui.login.LoginViewModel
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import com.example.minimoneybox.util.FileUtils
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.net.HttpURLConnection
import kotlin.test.assertEquals

class LoginViewModelUnitTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loginRepository: LoginRepository

    lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun getValidationErrorMessage_getEmailErrorMessage() {
        // GIVEN
        val errors = listOf(
            ValidationError("Email", "The email address you entered is not valid."),
            ValidationError("Password", "Your password must be at least 10 characters and include one number, one upper case character and one lower case character.")
        )
        // WHEN
        val errorMessage = loginViewModel.getValidationErrorMessage(errors.toSet(),
            Constants.LOGIN_VALIDATION_ERROR_KEY_EMAIL
        )
        // THEN
        assertEquals("The email address you entered is not valid.", errorMessage)
    }

    @Test
    fun getValidationErrorMessage_getPasswordErrorMessage() {
        // GIVEN
        val errors = listOf(
            ValidationError("Email", "The email address you entered is not valid."),
            ValidationError("Password", "Your password must be at least 10 characters and include one number, one upper case character and one lower case character.")
        )
        // WHEN
        val errorMessage = loginViewModel.getValidationErrorMessage(errors.toSet(),
            Constants.LOGIN_VALIDATION_ERROR_KEY_PASSWORD
        )
        // THEN
        assertEquals("Your password must be at least 10 characters and include one number, one upper case character and one lower case character.", errorMessage)
    }

    @Test
    fun getValidationErrorMessage_NoSuchKey() {
        // GIVEN
        val errors = listOf<ValidationError>()
        // WHEN
        val errorMessage = loginViewModel.getValidationErrorMessage(errors.toSet(),
            Constants.LOGIN_VALIDATION_ERROR_KEY_EMAIL
        )
        // THEN
        assertEquals("", errorMessage)
    }

    @Test
    fun login_apiStatusSuccess() {
        // GIVEN
        val email = LOGIN_EMAIL
        val password = LOGIN_PASSWORD
        val token = "gnOKw1sLpvLeot8IHesQydAyfWtBW5+z4SCCB46UyTY="
        val responseData = LoginSession(Session(token))
        val apiResource = ApiResult.Success(responseData)
        Mockito.`when`(loginRepository.login(email, password))
            .thenReturn(Observable.just(apiResource))
        // WHEN
        loginViewModel.login(email, password)
        // THEN
        assertEquals(apiResource, loginViewModel.apiStatus.value)
        assertEquals(null, loginViewModel.error.value)
        assertEquals("", loginViewModel.emailErrorText.value)
        assertEquals("", loginViewModel.passwordErrorText.value)
    }

    @Test
    fun login_apiStatusErrorWithoutValidationErrors() {
        // GIVEN
        val email = "abc@abc.com"
        val password = LOGIN_PASSWORD
        // create httpException
        val responseBody = FileUtils.readTestResourceFile("login_api_invalid_email.json", "login")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)
        // create ApiResource
        val errorResponse = ErrorResponse.fromHttpException(httpException)
        val apiResource = ApiResult.Error<LoginSession>(null, errorResponse)
        // mock response
        Mockito.`when`(loginRepository.login(email, password))
            .thenReturn(Observable.just(apiResource))
        // WHEN
        loginViewModel.login(email, password)
        // THEN
        assertEquals(apiResource, loginViewModel.apiStatus.value)
        val expectedApiException = ApiException("Login failed","Incorrect email address or password. Please check and try again.")
        val actualApiException = loginViewModel.error.value as ApiException
        assertEquals(expectedApiException.name, actualApiException.name)
        assertEquals(expectedApiException.message, actualApiException.message)
        assertEquals("", loginViewModel.emailErrorText.value)
        assertEquals("", loginViewModel.passwordErrorText.value)
    }

    @Test
    fun login_apiStatusErrorWithValidationErrors() {
        // GIVEN
        val email = ""
        val password = ""
        // create httpException
        val responseBody = FileUtils.readTestResourceFile("login_api_empty_email_and_empty_password.json", "login")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)
        // create ApiResource
        val errorResponse = ErrorResponse.fromHttpException(httpException)
        val apiResource = ApiResult.Error<LoginSession>(null, errorResponse)
        // mock response
        Mockito.`when`(loginRepository.login(email, password))
            .thenReturn(Observable.just(apiResource))
        // WHEN
        loginViewModel.login(email, password)
        // THEN
        assertEquals(apiResource, loginViewModel.apiStatus.value)
        assertEquals(null, loginViewModel.error.value)
        assertEquals("The email address you entered is not valid.", loginViewModel.emailErrorText.value)
        assertEquals("Your password must be at least 10 characters and include one number, one upper case character and one lower case character.", loginViewModel.passwordErrorText.value)
    }

    @Test
    fun login_unexpectedError() {
        // GIVEN
        val email = ""
        val password = ""
        // mock response
        Mockito.`when`(loginRepository.login(email, password))
            .thenReturn(Observable.error(Exception("Unexpected Error")))
        // WHEN
        loginViewModel.login(email, password)
        // THEN
        assertEquals(null, loginViewModel.apiStatus.value?.data)
        assertEquals(null, loginViewModel.apiStatus.value?.error)
        assertEquals("Unexpected Error", loginViewModel.error.value?.message)
        assertEquals("", loginViewModel.emailErrorText.value)
        assertEquals("", loginViewModel.passwordErrorText.value)
    }

}