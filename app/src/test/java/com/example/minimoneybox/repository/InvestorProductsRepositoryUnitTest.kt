package com.example.minimoneybox.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.SessionManager
import com.example.minimoneybox.models.*
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResource
import com.example.minimoneybox.network.api.InvestorProductsApi
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

class InvestorProductsRepositoryUnitTest {
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var investorProductsApi: InvestorProductsApi

    lateinit var investorProductsRepository: InvestorProductsRepository

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        investorProductsRepository = InvestorProductsRepository(investorProductsApi)
    }

    @Test
    fun getInvestorProducts_correctInput() {
        // GIVEN
        val product1 = InvestorProduct(6137,16729.21,0.00,40.00, ProductDetail("Stocks & Shares ISA"))
        val product2 = InvestorProduct(6136,3524.740000,0.00,25.00, ProductDetail("Lifetime ISA"))
        val product3 = InvestorProduct(6135,11796.40,80.00,10.00, ProductDetail("General Investment Account"))
        val responseData = AccountDetails(32050.350000, listOf(product1,product2,product3))
        Mockito.`when`(investorProductsApi.getInvestorProduct())
            .thenReturn(Observable.just(responseData))

        // WHEN
        val result = investorProductsRepository.getInvestorProducts()
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.SUCCESS, result.status)
        Assert.assertEquals(responseData, result.data)
    }

    @Test
    fun getInvestorProducts_returnHttpException() {
        // GIVEN
        val responseBody = FileUtils.readTestResourceFile("investorproducts_api_bearer_token_expired.json", "investorproducts")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(investorProductsApi.getInvestorProduct())
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = investorProductsRepository.getInvestorProducts()
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        Assert.assertEquals(errorResponse, result.data)
    }

    @Test
    fun getInvestorProducts_returnHttpException_500() {
        // GIVEN
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_INTERNAL_ERROR,"".toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)

        Mockito.`when`(investorProductsApi.getInvestorProduct())
            .thenReturn(Observable.error(httpException))

        // WHEN
        val result = investorProductsRepository.getInvestorProducts()
            .blockingLast()

        // THEN
        Assert.assertEquals(ApiResource.ApiStatus.ERROR, result.status)

        val errorResponse = ErrorResponse.fromHttpException(httpException)
        Assert.assertEquals(errorResponse, result.data)
    }

    @Test
    fun getInvestorProducts_returnOtherException() {
        // GIVEN
        Mockito.`when`(investorProductsApi.getInvestorProduct())
            .thenReturn(Observable.error(Exception()))

        // WHEN / THEN
        assertFailsWith<Exception> {
            investorProductsRepository.getInvestorProducts()
                .blockingLast()
        }
    }
}