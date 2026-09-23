package com.android.loginauthbasics.feature.login.data

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class LoginRepository @Inject constructor(
//    private val api: LoginApi
) {

    suspend fun login(username: String, password: String): LoginResponse {
        val result = api.login(LoginRequest(username, password))

        return result
    }

    companion object {
        const val BASE_URL = "https://reqres.in/"

        private val retrofit by lazy {
            val networkJson = Json { ignoreUnknownKeys = true }
            val converterFactory = networkJson.asConverterFactory("application/json".toMediaType())

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .client(client)
                .build()
        }

        val api: LoginApi by lazy{
            retrofit.create(LoginApi::class.java)
        }
    }
}