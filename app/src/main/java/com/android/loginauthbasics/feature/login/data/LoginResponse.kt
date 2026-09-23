package com.android.loginauthbasics.feature.login.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(var token: String)