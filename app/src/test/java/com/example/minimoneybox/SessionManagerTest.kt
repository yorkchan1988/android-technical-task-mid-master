package com.example.minimoneybox

import android.app.Application
import android.content.Context
import android.security.KeyPairGeneratorSpec
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.ui.login.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import java.security.KeyPairGenerator
import kotlin.test.assertEquals

@RunWith(PowerMockRunner::class)
@PrepareForTest(SessionManager::class)
class SessionManagerTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var spec: KeyPairGeneratorSpec

    lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        // create mock objects
        MockitoAnnotations.initMocks(this)
        sessionManager = SessionManager(context, spec)
    }

    @Test
    fun login_saveToken() {
        // GIVEN
        val token = "test"

        // WHEN
        sessionManager.login(token)

        // THEN
        assertEquals(token, sessionManager.getBearerToken())
        assertEquals(SessionManager.AuthStatus.AUTHENTICATED, SessionManager.authStatus.value)
    }

    @Test
    fun logout_normalCase() {
        // GIVEN

        // WHEN
        sessionManager.logout()

        // THEN
        assertEquals(null, sessionManager.getBearerToken())
        assertEquals(SessionManager.AuthStatus.NOT_AUTHENTICATED, SessionManager.authStatus.value)
    }
}