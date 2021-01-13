package com.example.minimoneybox.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.models.ProductDetail
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.repository.OneOffPaymentsRepository
import com.example.minimoneybox.ui.main.individualaccount.IndividualAccountViewModel
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

class IndividualAccountViewModelUnitTest {
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var oneOffPaymentsRepository: OneOffPaymentsRepository

    lateinit var individualAccountsViewModel: IndividualAccountViewModel

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        individualAccountsViewModel = IndividualAccountViewModel(oneOffPaymentsRepository)
        val investorProduct = InvestorProduct(6135,11796.40,80.00,10.00, ProductDetail("General Investment Account"))
        individualAccountsViewModel.investorProduct = investorProduct
    }

    @Test
    fun oneOffPayments_apiStatusSuccess() {
        // GIVEN
        val responseData = OneOffPaymentsResponse(80.00)
        val apiResource = ApiResult.Success(responseData)
        Mockito.`when`(oneOffPaymentsRepository.oneOffPayments(10.00, 6135))
            .thenReturn(Observable.just(apiResource))
        // WHEN
        individualAccountsViewModel.oneOffPayments()
        // THEN
        assertEquals(apiResource, individualAccountsViewModel.apiStatus.value)
        assertEquals(null, individualAccountsViewModel.error.value)
    }

    @Test
    fun oneOffPayments_apiStatusError() {
        // GIVEN
        val responseBody = FileUtils.readTestResourceFile("oneoffpayments_api_bearer_token_expired.json", "oneoffpayments")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)
        val responseData = ErrorResponse.fromHttpException(httpException)
        val apiResource = ApiResult.Error<OneOffPaymentsResponse>(null, responseData)
        Mockito.`when`(oneOffPaymentsRepository.oneOffPayments(10.00, 6135))
            .thenReturn(Observable.just(apiResource))
        // WHEN
        individualAccountsViewModel.oneOffPayments()
        // THEN
        assertEquals(apiResource, individualAccountsViewModel.apiStatus.value)

        val expectedApiException = ApiException("Bearer token expired", "Your session has expired. Please close the app and log in again.")
        val actualApiException = individualAccountsViewModel.error.value as ApiException
        assertEquals(expectedApiException.name, actualApiException.name)
        assertEquals(expectedApiException.message, actualApiException.message)
    }

    @Test
    fun oneOffPayments_unexpectedError() {
        // GIVEN
        Mockito.`when`(oneOffPaymentsRepository.oneOffPayments(10.00, 6135))
            .thenReturn(Observable.error(Exception("Unexpected Error")))
        // WHEN
        individualAccountsViewModel.oneOffPayments()
        // THEN
        assertEquals(null, individualAccountsViewModel.apiStatus.value?.data)
        assertEquals(null, individualAccountsViewModel.apiStatus.value?.error)
        assertEquals("Unexpected Error", individualAccountsViewModel.error.value?.message)
    }
}