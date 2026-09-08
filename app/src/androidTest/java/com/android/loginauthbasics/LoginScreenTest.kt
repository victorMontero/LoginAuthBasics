package com.android.loginauthbasics

import android.R.attr.password
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.android.loginauthbasics.feature.login.LoginContent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingIndicatorIsDisplayed() {
        composeTestRule.setContent {
            LoginContent(
                login = "",
                onLoginChange = {},
                password = "",
                onPasswordChange = {},
                onButtonClick = {},
                buttonEnabled = true,
                hostState = SnackbarHostState(),
                isLoading = true,
                isPasswordVisible = false,
                onPasswordVisibilityChange = {}
            )
        }
        composeTestRule.onNodeWithContentDescription("loading").assertIsDisplayed()
    }

    @Test
    fun testLoginTextField_emitsCallbackWhenTyped(){
        var login by mutableStateOf("")
        var password by mutableStateOf("")

        composeTestRule.setContent {
            LoginContent(
                login = login,
                onLoginChange = { login = it },
                password = password,
                onPasswordChange = { password = it },
                onButtonClick = {},
                buttonEnabled = true,
                hostState = SnackbarHostState(),
                isLoading = false,
                isPasswordVisible = true,
                onPasswordVisibilityChange = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("username").performTextInput("admin")
        composeTestRule.onNodeWithContentDescription("password").performTextInput("1234")

        assertEquals("admin", login)
        assertEquals("1234", password)
    }

    @Test
    fun testLoginButton_triggersCallbackWhenClicked(){
        var buttonClicked = false

        composeTestRule.setContent {
            LoginContent(
                login = "",
                onLoginChange = {},
                password = "",
                onPasswordChange = {},
                onButtonClick = { buttonClicked = true },
                buttonEnabled = true,
                hostState = SnackbarHostState(),
                isLoading = false,
                isPasswordVisible = false,
                onPasswordVisibilityChange = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("login_button").performClick()
        assertEquals(true, buttonClicked)
    }

    @Test
    fun testLoginButton_disabledWhenButtonNodeIsNotEnabled(){

        composeTestRule.setContent {
            LoginContent(
                login = "",
                onLoginChange = {},
                password = "",
                onPasswordChange = {},
                onButtonClick = {},
                buttonEnabled = false,
                hostState = SnackbarHostState(),
                isLoading = false,
                isPasswordVisible = false,
                onPasswordVisibilityChange = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("login_button").assertIsNotEnabled()
    }
}