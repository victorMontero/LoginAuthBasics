package com.android.loginauthbasics.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private val _loginState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

///region forma antiga, derivated state dentro da data class foi a melhoria
//    fun isFormValid(): Boolean {
//        return _loginState.value.loginState.isNotBlank() && _loginState.value.passwordState.isNotBlank()
//    }
///endregion

    fun onLoginChanged(newLogin: String) {
        _loginState.update { it.copy(loginState = newLogin) }
    }

    fun onPasswordChanged(newPassword: String) {
        _loginState.update { it.copy(passwordState = newPassword) }
    }

    fun login() {
        _loginState.update {
            it.copy(
                isLoading = true,
                errorMessage = ""
            )
        }

        viewModelScope.launch {
            val isSuccessful =
                repository.login(_loginState.value.loginState, _loginState.value.passwordState)

            if (isSuccessful) {
                _loginState.update {
                    it.copy(
                        isLoginSuccessful = true,
                        isLoading = false
                    )
                }
            } else {
                _loginState.update {
                    it.copy(
                        isLoginSuccessful = false,
                        isLoading = false,
                        errorMessage = "Login failed"
                    )
                }
            }
        }
    }

    fun resetSnackBarState() {
        _loginState.update { it.copy(isLoginSuccessful = false) }
    }

    fun resetErrorState() {
        _loginState.update { it.copy(errorMessage = "") }
    }

    fun togglePasswordVisibility() {
        _loginState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}

data class LoginUiState(
    val loginState: String = "",
    val passwordState: String = "",
    val isLoginSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isPasswordVisible: Boolean = false
){
    val isButtonEnabled: Boolean
        get() = loginState.isNotBlank() && passwordState.isNotBlank()
}