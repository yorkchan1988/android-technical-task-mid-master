package com.example.minimoneybox.network

import com.example.minimoneybox.di.AppModule
import com.example.minimoneybox.di.main.MainModule
import com.example.minimoneybox.models.*
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.network.api.InvestorProductsApi
import com.example.minimoneybox.util.FileUtils
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class InvestorProductsApiUnitTest {

    private lateinit var mockWebServer: MockWebServer
    private val testObserver: TestObserver<AccountDetails> = TestObserver()

    private lateinit var investorProductsApi: InvestorProductsApi

    private fun createMockResponse(code: Int, jsonFileName: String): MockResponse {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(code)
        mockResponse.setBody(FileUtils.readTestResourceFile(jsonFileName, "investorproducts"))
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
        investorProductsApi = MainModule.provideInvestorProductsApi(retrofit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getInvestorProduct_correctInput() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_OK, "investorproducts_api_success.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        investorProductsApi.getInvestorProduct().subscribe(testObserver)

        // THEN
        testObserver.assertNoErrors()
        val product1 = InvestorProduct(6137,16729.21,0.00,40.00, ProductDetail("Stocks & Shares ISA"))
        val product2 = InvestorProduct(6136,3524.740000,0.00,25.00, ProductDetail("Lifetime ISA"))
        val product3 = InvestorProduct(6135,11796.40,80.00,10.00, ProductDetail("General Investment Account"))
        testObserver.assertValue(AccountDetails(32050.350000, listOf(product1,product2,product3)))
    }

    @Test
    fun getInvestorProduct_bearerTokenMissing() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "investorproducts_api_bearer_token_missing.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        investorProductsApi.getInvestorProduct().subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "Bearer token missing"
                    val isMessageValid = res.message == "Sorry but it looks like you are not properly logged in. Please close the app and log in again."
                    var isValidationValid = res.validationErrors != null
                    res.validationErrors?.let {
                        isValidationValid = isValidationValid && it.isEmpty()
                    }
                    isNameValid && isMessageValid && isValidationValid
                } ?: false
            }
            else {
                false
            }
        }
    }

    @Test
    fun getInvestorProduct_bearerTokenExpired() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "investorproducts_api_bearer_token_expired.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        investorProductsApi.getInvestorProduct().subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "Bearer token expired"
                    val isMessageValid = res.message == "Your session has expired. Please close the app and log in again."
                    var isValidationValid = res.validationErrors != null
                    res.validationErrors?.let {
                        isValidationValid = isValidationValid && it.isEmpty()
                    }
                    isNameValid && isMessageValid && isValidationValid
                } ?: false
            }
            else {
                false
            }
        }
    }

    @Test
    fun getInvestorProduct_userSessionNotFound() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "investorproducts_api_user_session_not_found.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        investorProductsApi.getInvestorProduct().subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "User session not found"
                    val isMessageValid = res.message == "Sorry but it looks like your session is not valid. Please close the app and log in again."
                    var isValidationValid = res.validationErrors != null
                    res.validationErrors?.let {
                        isValidationValid = isValidationValid && it.isEmpty()
                    }
                    isNameValid && isMessageValid && isValidationValid
                } ?: false
            }
            else {
                false
            }
        }
    }
}