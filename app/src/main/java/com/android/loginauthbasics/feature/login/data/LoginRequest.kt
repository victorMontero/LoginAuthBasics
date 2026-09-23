package com.android.loginauthbasics.feature.login.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(var email: String, var password: String)