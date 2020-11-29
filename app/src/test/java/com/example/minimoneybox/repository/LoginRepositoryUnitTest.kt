package com.example.minimoneybox.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.exception.UnexpectedException
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.Session
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.network.api.LoginApi
import com.example.minimoneybox.util.Constants.Companion.LOGIN_EMAIL
import com.example.minimoneybox.util.Constants.Companion.LOGIN_IDFA
import com.example.minimoneybox.util.Constants.Companion.LOGIN_PASSWORD
import com.example.minimoneybox.util.FileUtils
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException
import java.net.HttpURLConnection
import kotlin.test.assertFailsWith


class LoginRepositoryUnitTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loginApi: LoginApi

    lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        loginRepository = LoginRepository(loginApi)
    }

    @Test
    fun getLoginApiRequest_correctInput() {
        // GIVEN
        val email = LOGIN_EMAIL
        val password = LOGIN_PASSWORD
        val idfa = LOGIN_IDFA
        // THEN
        val loginRequest = LoginRequest(email, password, idfa)
        // WHEN
        assertEquals(LOGIN_EMAIL, loginRequest.email)
        assertEquals(LOGIN_PASSWORD, loginRequest.password)
        assertEquals(LOGIN_IDFA, loginRequest.idfa)
    }

    @Test
    fun login_correctInput() {
        // GIVEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        val token = "gnOKw1sLpvLeot8IHesQydAyfWtBW5+z4SCCB46UyTY="
        val responseData = LoginSession(Session(token))
        Mockito.`when`(loginApi.login(requestParam))
            .thenReturn(Observable.just(responseData))

        // WHEN
        val result = loginRepository.login(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
             .blockingLast()

        // THEN
        assertEquals(ApiResource.ApiStatus.SUCCESS, result.status)
        assertEquals(responseData, result.data)
        assertEquals(SessionManager.getBearerToken(), token)
    }

    @Test
    fun login_emptySessionToken() {
        // GIVEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        val token = ""
        val responseData = LoginSession(Session(token))
        Mockito.`when`(loginApi.login(requestParam))
            .thenReturn(Observable.just(responseData))

        // WHEN / THEN
        assertFailsWith<UnexpectedException> {
            loginRepository.login(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
                .blockingLast()
        }
    }

    @Test
    fun login_returnHttpException() {
        // GIVEN
        val requestParam = LoginRequest("abc@abc.com", LOGIN_PASSWORD, LOGIN_IDFA)
        val responseBody = FileUtils.readTestResourceFile("login_api_invalid_email.json", "login")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(loginApi.login(requestParam))
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = loginRepository.login("abc@abc.com", LOGIN_PASSWORD, LOGIN_IDFA)
            .blockingLast()

        // THEN
        assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        assertEquals(errorResponse, result.data)
    }

    @Test
    fun login_returnHttpException_500() {
        // GIVEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_INTERNAL_ERROR,"".toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(loginApi.login(requestParam))
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = loginRepository.login(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
            .blockingLast()

        // THEN
        assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        assertEquals(errorResponse, result.data)
    }

    @Test
    fun login_returnOtherException() {
        // GIVEN
        val requestParam = LoginRequest(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
        Mockito.`when`(loginApi.login(requestParam))
            .thenReturn(Observable.error(Exception()))

        // WHEN / THEN
        assertFailsWith<Exception> {
            loginRepository.login(LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_IDFA)
                .blockingLast()
        }
    }
}
