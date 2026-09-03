package com.android.loginauthbasics

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoginApplication : Application() {
    init {
        Log.d("MEU_APP", "chamaaa")
    }
}