package com.android.loginauthbasics.feature.login

import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class LoginRepository @Inject constructor() {

    suspend fun login(username: String, password: String): Boolean {
        delay(2000.milliseconds)
        return username.isNotBlank() && password.isNotBlank()
    }
}