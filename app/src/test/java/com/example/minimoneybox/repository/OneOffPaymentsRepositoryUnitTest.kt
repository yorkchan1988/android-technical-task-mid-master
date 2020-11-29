package com.example.minimoneybox.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.models.LoginSession
import com.example.minimoneybox.models.Session
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.request.OneOffPaymentsRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.network.api.InvestorProductsApi
import com.example.minimoneybox.network.api.OneOffPaymentsApi
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.FileUtils
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
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
import kotlin.test.assertFailsWith

class OneOffPaymentsRepositoryUnitTest {
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var oneOffPaymentsApi: OneOffPaymentsApi

    lateinit var oneOffPaymentsRepository: OneOffPaymentsRepository

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        oneOffPaymentsRepository = OneOffPaymentsRepository(oneOffPaymentsApi)
    }

    @Test
    fun oneOffPayments_correctInput() {
        // GIVEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        val responseData = OneOffPaymentsResponse(80.00)
        Mockito.`when`(oneOffPaymentsApi.oneOffPayments(requestParam))
            .thenReturn(Observable.just(responseData))

        // WHEN
        val result = oneOffPaymentsRepository.oneOffPayments(10.00, 6135)
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.SUCCESS, result.status)
        Assert.assertEquals(responseData, result.data)
    }

    @Test
    fun oneOffPayments_returnHttpException() {
        // GIVEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        val responseBody = FileUtils.readTestResourceFile("oneoffpayments_api_bearer_token_expired.json", "oneoffpayments")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(oneOffPaymentsApi.oneOffPayments(requestParam))
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = oneOffPaymentsRepository.oneOffPayments(10.00, 6135)
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        Assert.assertEquals(errorResponse, result.data)
    }

    @Test
    fun oneOffPayments_returnHttpException_500() {
        // GIVEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_INTERNAL_ERROR,"".toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(oneOffPaymentsApi.oneOffPayments(requestParam))
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = oneOffPaymentsRepository.oneOffPayments(10.00, 6135)
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        Assert.assertEquals(errorResponse, result.data)
    }

    @Test
    fun getInvestorProducts_returnOtherException() {
        // GIVEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        Mockito.`when`(oneOffPaymentsApi.oneOffPayments(requestParam))
            .thenReturn(Observable.error(Exception()))

        // WHEN / THEN
        assertFailsWith<Exception> {
            oneOffPaymentsRepository.oneOffPayments(10.00, 6135)
                .blockingLast()
        }
    }
}