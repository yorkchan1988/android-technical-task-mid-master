package com.example.minimoneybox.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.models.*
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.ApiResult
import com.example.minimoneybox.repository.InvestorProductsRepository
import com.example.minimoneybox.ui.main.useraccounts.UserAccountsViewModel
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

class UserAccountsViewModelUnitTest {
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var investorProductsRepository: InvestorProductsRepository

    lateinit var userAccountsViewModel: UserAccountsViewModel

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        userAccountsViewModel = UserAccountsViewModel(investorProductsRepository)
    }

    @Test
    fun setUsername_nonNullValue() {
        // GIVEN
        val username = "Test Username"

        // WHEN
        userAccountsViewModel.username = username

        // THEN
        assertEquals(username, userAccountsViewModel.usernameText.value)
    }

    @Test
    fun setUsername_nullValue() {
        // GIVEN
        val username = null

        // WHEN
        userAccountsViewModel.username = username

        // THEN
        assertEquals("", userAccountsViewModel.usernameText.value)
    }

    @Test
    fun getInvestorProducts_apiStatusSuccess() {
        // GIVEN
        val product1 = InvestorProduct(6137,16729.21,0.00,40.00, ProductDetail("Stocks & Shares ISA"))
        val product2 = InvestorProduct(6136,3524.740000,0.00,25.00, ProductDetail("Lifetime ISA"))
        val product3 = InvestorProduct(6135,11796.40,80.00,10.00, ProductDetail("General Investment Account"))
        val responseData = AccountDetails(32050.350000, listOf(product1,product2,product3))
        val apiResource = ApiResult.Success(responseData)
        Mockito.`when`(investorProductsRepository.getInvestorProducts())
            .thenReturn(Observable.just(apiResource))
        // WHEN
        userAccountsViewModel.getInvestorProducts()
        // THEN
        assertEquals(apiResource, userAccountsViewModel.apiStatus.value)
        assertEquals(null, userAccountsViewModel.error.value)
        assertEquals(responseData.totalPlanValue.toInt(), userAccountsViewModel.totalPlanValue.value)
        assertEquals(responseData.products.size, userAccountsViewModel.products.value?.size ?: 0)
    }

    @Test
    fun getInvestorProducts_apiStatusError() {
        // GIVEN
        val responseBody = FileUtils.readTestResourceFile("investorproducts_api_bearer_token_expired.json", "investorproducts")
        val response = Response.error<HttpException>(HttpURLConnection.HTTP_BAD_REQUEST,responseBody.toResponseBody("text/plain".toMediaTypeOrNull()))
        val httpException = HttpException(response)
        val responseData = ErrorResponse.fromHttpException(httpException)
        val apiResource = ApiResult.Error<AccountDetails>(null, responseData)
        Mockito.`when`(investorProductsRepository.getInvestorProducts())
            .thenReturn(Observable.just(apiResource))
        // WHEN
        userAccountsViewModel.getInvestorProducts()
        // THEN
        assertEquals(apiResource, userAccountsViewModel.apiStatus.value)

        val expectedApiException = ApiException("Bearer token expired","Your session has expired. Please close the app and log in again.")
        val actualApiException = userAccountsViewModel.error.value as ApiException
        assertEquals(expectedApiException.name, actualApiException.name)
        assertEquals(expectedApiException.message, actualApiException.message)
        assertEquals(0, userAccountsViewModel.totalPlanValue.value)
        assertEquals(0, userAccountsViewModel.products.value?.size ?: 0)
    }

    @Test
    fun getInvestorProducts_unexpectedError() {
        // GIVEN
        Mockito.`when`(investorProductsRepository.getInvestorProducts())
            .thenReturn(Observable.error(Exception("Unexpected Error")))
        // WHEN
        userAccountsViewModel.getInvestorProducts()
        // THEN
        assertEquals(null, userAccountsViewModel.apiStatus.value?.data)
        assertEquals(null, userAccountsViewModel.apiStatus.value?.error)
        assertEquals("Unexpected Error", userAccountsViewModel.error.value?.message)
        assertEquals(0, userAccountsViewModel.totalPlanValue.value)
        assertEquals(0, userAccountsViewModel.products.value?.size ?: 0)
    }
}