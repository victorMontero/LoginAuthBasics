package com.android.loginauthbasics

import com.android.loginauthbasics.feature.login.data.LoginRepository
import com.android.loginauthbasics.feature.login.ui.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var repo: LoginRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repo = LoginRepository()
        viewModel = LoginViewModel(repo)

    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun `when login and password state is not empty then form must be valid`(){

        val loginState = "admin"
        val passwordState = "1234"

        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        val result = viewModel.uiState.value.isButtonEnabled

        assertTrue(result)

    }

    @Test
    fun `when login and password state is empty then form must be not valid`(){

        val loginState = ""
        val passwordState = ""

        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        val result = viewModel.uiState.value.isButtonEnabled

        assertFalse(result)

    }

    @Test
    fun `when login state is empty then form must be not valid`(){

        val loginState = ""
        val passwordState = "1234"

        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        val result = viewModel.uiState.value.isButtonEnabled

        assertFalse(result)

    }

    @Test
    fun `when password state is empty then form must be not valid`(){

        val loginState = "admin"
        val passwordState = ""

        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        val result = viewModel.uiState.value.isButtonEnabled

        assertFalse(result)

    }

    @Test
    fun `when login is called with login and password then login must be successful`() = runTest {

        val loginState = "admin"
        val passwordState = "1234"


        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoginSuccessful)

    }

    @Test
    fun `when login is called without login then login must be unsuccessful`() = runTest {

        val loginState = ""
        val passwordState = "1234"


        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoginSuccessful)
        assertEquals("Login failed", viewModel.uiState.value.errorMessage)

    }

    @Test
    fun `when login is called without password then login must be unsuccessful`() = runTest {

        val loginState = "admin"
        val passwordState = ""


        viewModel.onLoginChanged(loginState)
        viewModel.onPasswordChanged(passwordState)

        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoginSuccessful)
        assertEquals("Login failed", viewModel.uiState.value.errorMessage)

    }

    @Test
    fun `when login is called without login and password then login must be unsuccessful`() = runTest {


        viewModel.login()

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoginSuccessful)
        assertEquals("Login failed", viewModel.uiState.value.errorMessage)
    }
}