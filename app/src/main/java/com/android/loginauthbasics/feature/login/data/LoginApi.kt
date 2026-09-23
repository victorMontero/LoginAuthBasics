package com.android.loginauthbasics.feature.login.data

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}