package com.example.minimoneybox.network

import com.example.minimoneybox.di.AppModule
import com.example.minimoneybox.di.login.LoginModule
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.Session
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.ValidationError
import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_IDFA
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import com.example.minimoneybox.util.FileUtils
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class LoginApiUnitTest {

    lateinit var mockWebServer: MockWebServer
    val testObserver: TestObserver<LoginSession> = TestObserver()

    lateinit var loginApi: LoginApi

    private fun createMockResponse(code: Int, jsonFileName: String): MockResponse {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(code)
        mockResponse.setBody(FileUtils.readTestResourceFile(jsonFileName))
        return mockResponse
    }

    @Before
    fun setUp() {
        // start mock server
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val baseUrl: HttpUrl = mockWebServer.url("")
        val okHttpClient = AppModule.provideOkHttpClientInstance()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        loginApi = LoginModule.provideLoginApi(retrofit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun login_correctInput() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_OK, "login_api_success.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertNoErrors()
        testObserver.assertValue(LoginSession(Session("gnOKw1sLpvLeot8IHesQydAyfWtBW5+z4SCCB46UyTY=")))
    }

    @Test
    fun login_emptyEmail() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_empty_email.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest("", LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Validation failed"
                val isMessageValid = errorResponse.message == "Please correct the following fields:"
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 2
                    isValidationValid = isValidationValid && it[0].errorName == "Email"
                    isValidationValid = isValidationValid && it[0].message == "The email address you entered is not valid."
                    isValidationValid = isValidationValid && it[1].errorName == "Email"
                    isValidationValid = isValidationValid && it[1].message == "The email address you entered is not valid."
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_emailWithInvalidFormat() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_email_with_invalid_format.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest("abc", LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Validation failed"
                val isMessageValid = errorResponse.message == "Please correct the following fields:"
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 1
                    isValidationValid = isValidationValid && it[0].errorName == "Email"
                    isValidationValid = isValidationValid && it[0].message == "The email address you entered is not valid."
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_invalidEmail() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_invalid_email.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest("abc@abc.com", LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Login failed"
                val isMessageValid = errorResponse.message == "Incorrect email address or password. Please check and try again."
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 0
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_emptyPassword() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_empty_password.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Validation failed"
                val isMessageValid = errorResponse.message == "Please correct the following fields:"
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 1
                    isValidationValid = isValidationValid && it[0].errorName == "Password"
                    isValidationValid = isValidationValid && it[0].message == "Your password must be at least 10 characters and include one number, one upper case character and one lower case character."
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_invalidPassword() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_invalid_password.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Login failed"
                val isMessageValid = errorResponse.message == "Incorrect email address or password. Please check and try again."
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 0
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_emptyEmailAndEmptyPassword() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_api_empty_email_and_empty_password.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == "Validation failed"
                val isMessageValid = errorResponse.message == "Please correct the following fields:"
                var isValidationValid = errorResponse.validationErrors != null
                errorResponse.validationErrors?.let {
                    isValidationValid = isValidationValid && it.size == 3
                    isValidationValid = isValidationValid && it[0].errorName == "Email"
                    isValidationValid = isValidationValid && it[0].message == "The email address you entered is not valid."
                    isValidationValid = isValidationValid && it[1].errorName == "Email"
                    isValidationValid = isValidationValid && it[1].message == "The email address you entered is not valid."
                    isValidationValid = isValidationValid && it[2].errorName == "Password"
                    isValidationValid = isValidationValid && it[2].message == "Your password must be at least 10 characters and include one number, one upper case character and one lower case character."
                }
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }

    @Test
    fun login_unexpectedEmptyResponse() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, "login_unexpected_empty_response.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        loginApi.login(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                val isNameValid = errorResponse.errorName == null
                val isMessageValid = errorResponse.message == null
                var isValidationValid = errorResponse.validationErrors == null
                isNameValid && isMessageValid && isValidationValid
            }
            else {
                false
            }
        }
    }
}