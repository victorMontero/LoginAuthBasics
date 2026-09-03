package com.android.loginauthbasics

import android.R.attr.password
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
                isLoading = true
            )
        }
        composeTestRule.onNodeWithContentDescription("loading").assertIsDisplayed()
    }

    @Test
    fun testLoginTextField_emitsCallbackWhenTyped(){
        var login = ""
        var password = ""

        composeTestRule.setContent {
            LoginContent(
                login = "",
                onLoginChange = { login = it} ,
                password = "",
                onPasswordChange = {password = it},
                onButtonClick = {},
                buttonEnabled = true,
                hostState = SnackbarHostState(),
                isLoading = false
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
                onLoginChange = {} ,
                password = "",
                onPasswordChange = {},
                onButtonClick = {buttonClicked = true},
                buttonEnabled = true,
                hostState = SnackbarHostState(),
                isLoading = false
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
                onLoginChange = {} ,
                password = "",
                onPasswordChange = {},
                onButtonClick = {},
                buttonEnabled = false,
                hostState = SnackbarHostState(),
                isLoading = false
            )
        }

        composeTestRule.onNodeWithContentDescription("login_button").assertIsNotEnabled()
    }
}