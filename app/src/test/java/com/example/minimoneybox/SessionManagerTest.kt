package com.example.minimoneybox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.repository.LoginRepository
import com.example.minimoneybox.ui.login.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class SessionManagerTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun login_saveToken() {
        // GIVEN
        val token = "test"

        // WHEN
        SessionManager.login(token)

        // THEN
        assertEquals(token, SessionManager.getBearerToken())
        assertEquals(SessionManager.AuthStatus.AUTHENTICATED, SessionManager.authStatus.value)
    }

    @Test
    fun logout_normalCase() {
        // GIVEN

        // WHEN
        SessionManager.logout()

        // THEN
        assertEquals(null, SessionManager.getBearerToken())
        assertEquals(SessionManager.AuthStatus.NOT_AUTHENTICATED, SessionManager.authStatus.value)
    }
}