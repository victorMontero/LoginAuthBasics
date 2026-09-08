package com.android.loginauthbasics.feature.login

import android.R.attr.label
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: (String) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
//            snackBarHostState.showSnackbar("login realizado com suicesso")
            viewModel.resetSnackBarState()

            onNavigateToHome(uiState.loginState)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotEmpty()) {
            snackBarHostState.showSnackbar(uiState.errorMessage)
            viewModel.resetErrorState()
        }
    }

    LoginContent(
        login = uiState.loginState,
        onLoginChange = { viewModel.onLoginChanged(it) },
        password = uiState.passwordState,
        onPasswordChange = { viewModel.onPasswordChanged(it) },
        onButtonClick = { viewModel.login() },
        buttonEnabled = uiState.isButtonEnabled,
        hostState = snackBarHostState,
        isLoading = uiState.isLoading,
        isPasswordVisible = uiState.isPasswordVisible,
        onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() }
    )
}

@Composable
fun LoginContent(
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    buttonEnabled: Boolean,
    hostState: SnackbarHostState,
    isLoading: Boolean,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = hostState) }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "username" },
                value = login,
                onValueChange = { onLoginChange(it) },
                label = { Text("username") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "password" },
                value = password,
                onValueChange = { onPasswordChange(it) },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("password") },
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityChange, content = {
                        if(isPasswordVisible){
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "password_visibility_on"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = "password_visibility_off"

                            )
                        }
                    })
                },
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "login_button" },
                enabled = buttonEnabled,
                onClick = onButtonClick
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .semantics {
                                contentDescription = "loading"
                            },
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }
        }
    }
}