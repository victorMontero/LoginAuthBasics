package com.android.loginauthbasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.loginauthbasics.feature.home.HomeScreen
import com.android.loginauthbasics.feature.login.LoginScreen
import com.android.loginauthbasics.feature.login.LoginViewModel
import com.android.loginauthbasics.ui.theme.LoginAuthBasicsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            LoginAuthBasicsTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = LoginRoute) {

                    composable<LoginRoute> {
                        val loginViewModel: LoginViewModel = hiltViewModel()

                        LoginScreen(
                            loginViewModel,
                            { userName -> navController.navigate(HomeRoute(userName)) })
                    }

                    composable<HomeRoute> { backStackEntry ->
                        val homeRoute = backStackEntry.toRoute<HomeRoute>()
                        HomeScreen(homeRoute.id)
                    }
                }
            }
        }
    }
}