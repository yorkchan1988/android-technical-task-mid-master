package com.example.minimoneybox.network

import com.example.minimoneybox.di.AppModule
import com.example.minimoneybox.di.main.MainModule
import com.example.minimoneybox.models.AccountDetails
import com.example.minimoneybox.models.InvestorProduct
import com.example.minimoneybox.models.ProductDetail
import com.example.minimoneybox.models.request.LoginRequest
import com.example.minimoneybox.models.request.OneOffPaymentsRequest
import com.example.minimoneybox.models.response.ErrorResponse
import com.example.minimoneybox.models.response.OneOffPaymentsResponse
import com.example.minimoneybox.network.api.InvestorProductsApi
import com.example.minimoneybox.network.api.OneOffPaymentsApi
import com.example.minimoneybox.util.Constants
import com.example.minimoneybox.util.FileUtils
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class OneOffPaymentsApiUnitTest {

    lateinit var mockWebServer: MockWebServer
    private val testObserver: TestObserver<OneOffPaymentsResponse> = TestObserver()

    @Mock
    lateinit var okHttpClient: OkHttpClient

    lateinit var oneOffPaymentsApi: OneOffPaymentsApi

    private fun createMockResponse(code: Int, jsonFileName: String): MockResponse {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(code)
        mockResponse.setBody(FileUtils.readTestResourceFile(jsonFileName, "oneoffpayments"))
        return mockResponse
    }

    @Before
    fun setUp() {
        // start mock server
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val baseUrl: HttpUrl = mockWebServer.url("")
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        oneOffPaymentsApi = MainModule.provideOneOffPaymentsApi(retrofit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun oneOffPayments_correctInput() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_OK, "oneoffpayments_api_success.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertNoErrors()

        testObserver.assertValue(OneOffPaymentsResponse(80.00))
    }

    @Test
    fun oneOffPayments_bearerTokenMissing() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneoffpayments_api_bearer_token_missing.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

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
    fun oneOffPayments_bearerTokenExpired() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneOffPayments_api_bearer_token_expired.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

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
    fun oneOffPayments_userSessionNotFound() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneoffpayments_api_user_session_not_found.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

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

    @Test
    fun oneOffPayments_accessNotPermitted() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneoffpayments_api_access_not_permitted.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "Access is not permitted"
                    val isMessageValid = res.message == "Access is not permitted for this resource."
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
    fun oneOffPayments_annualLimitExceeded() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneoffpayments_api_annual_limit_exceeded.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "Annual limit exceeded"
                    val isMessageValid = res.message == "Your annual Lifetime ISA limit is £4,000. You may add up to £0."
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
    fun oneOffPayments_combinedAnnualLimitExceeded() {
        // GIVEN
        val mockResponse = createMockResponse(HttpURLConnection.HTTP_UNAUTHORIZED, "oneoffpayments_api_combined_annual_limit_exceeded.json")
        mockWebServer.enqueue(mockResponse)

        // WHEN
        val requestParam = OneOffPaymentsRequest(10.00, 6135)
        oneOffPaymentsApi.oneOffPayments(requestParam).subscribe(testObserver)

        // THEN
        testObserver.assertError(HttpException::class.java)
        testObserver.assertError {error ->
            if (error is HttpException) {
                val errorResponse = ErrorResponse.fromHttpException(error)
                errorResponse?.let {res ->
                    val isNameValid = res.errorName == "Combined annual limit exceeded"
                    val isMessageValid = res.message == "This amount will take you over your annual ISA limit of £20,000. You may add up to £0 into your ISA products this tax year."
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