package com.example.mytask.navigation

import com.example.mytask.screens.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mytask.screens.AddEditTaskScreen
import com.example.mytask.screens.HomeScreen
import com.example.mytask.screens.auth.LoginScreen
import com.example.mytask.screens.auth.RegisterScreen
import com.example.mytask.viewmodel.AuthViewModel
import com.example.mytask.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddEdit : Screen("add_edit")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }

        // ✅ Pass both viewModels to HomeScreen
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = taskViewModel,
                authViewModel = authViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, authViewModel)
        }

        composable(Screen.AddEdit.route) {
            AddEditTaskScreen(navController = navController, viewModel = taskViewModel)
        }
    }
}