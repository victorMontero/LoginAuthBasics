package com.android.loginauthbasics.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginauthbasics.feature.login.data.ErrorResponse
import com.android.loginauthbasics.feature.login.data.LoginRepository
import com.android.loginauthbasics.feature.login.data.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    ///region forma antiga de implementar backing properties
//    private val _loginState = mutableStateOf("")
//    val loginState: State<String> = _loginState
//
//    private val _passwordState = mutableStateOf("")
//    val passwordState: State<String> = _passwordState
//
//    private val _loginResult = mutableStateOf(false)
//    val loginResult: State<Boolean> = _loginResult
///endregion
///region forma nova de implementar backing properties, porém é melhor usar o StateFlow e Data class
//    var loginStateV1 by mutableStateOf("")
//        private set
//
//    var passwordState by mutableStateOf("")
//        private set
//
//    var isLoginSuccessful by mutableStateOf(false)
//        private set
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var errorMessage by mutableStateOf("")
//        private set
///endregion
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

///region forma antiga, derivated state dentro da data class foi a melhoria
//    fun isFormValid(): Boolean {
//        return _loginState.value.loginState.isNotBlank() && _loginState.value.passwordState.isNotBlank()
//    }
///endregion

    fun onLoginChanged(newLogin: String) {
        _uiState.update { it.copy(username = newLogin) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun login() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = ""
            )
        }

        viewModelScope.launch {

            try {
                val response =
                    repository.login(_uiState.value.username, _uiState.value.password)

                if (response.token.isNotBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoginSuccessful = true,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoginSuccessful = false,
                        isLoading = false,
                        errorMessage = if (e is HttpException) {
                            val errorText = e.response()?.errorBody()?.string()
                            if (errorText != null){
                                try {
                                    val errorFormatted = Json.decodeFromString<ErrorResponse>(errorText)
                                    errorFormatted.error
                                } catch (_: Exception) {
                                    errorText
                                }
                            } else {
                                e.message ?: "Ocorreu um erro inesperado"
                            }
                        } else {
                            e.message  ?: "Ocorreu um erro inesperado"
                        }
                    )
                }
            }
        }
    }

    fun resetSnackBarState() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }

    fun resetErrorState() {
        _uiState.update { it.copy(errorMessage = "") }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}

///region single source of truth
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isPasswordVisible: Boolean = false
) {
    val isButtonEnabled: Boolean
        get() = username.isNotBlank() && password.isNotBlank() && !isLoading && !isLoginSuccessful
}

///endregion