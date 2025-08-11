package com.example.mytask

import com.example.mytask.ads.AdManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mytask.data.TaskDatabase
import com.example.mytask.navigation.NavGraph
import com.example.mytask.repository.TaskRepository
import com.example.mytask.ui.theme.MyTaskTheme
import com.example.mytask.viewmodel.AuthViewModel
import com.example.mytask.viewmodel.TaskViewModel
import com.example.mytask.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ads
        AdManager.init(applicationContext)
        AdManager.loadInterstitial(this)
        // ✅ Create TaskRepository from Database
        val database = TaskDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())

        setContent {
            val navController = rememberNavController()

            // ✅ Create ViewModels
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModelFactory(repository)
            )

            val authViewModel: AuthViewModel = viewModel()

            MyTaskTheme {
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}