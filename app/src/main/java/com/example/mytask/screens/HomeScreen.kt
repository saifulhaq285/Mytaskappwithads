package com.example.mytask.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mytask.ads.AdManager
import com.example.mytask.ads.BannerAd
import com.example.mytask.ads.NativeAdCard
import com.example.mytask.components.TaskItem
import com.example.mytask.navigation.Screen
import com.example.mytask.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: TaskViewModel,
    authViewModel: Any? = null
) {
    val allTasks = viewModel.allTasks.collectAsState(initial = emptyList()).value
    var searchQuery by remember { mutableStateOf("") }

    val taskList = allTasks.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    val context = LocalContext.current
    val activity = remember { context as? Activity }

    LaunchedEffect(Unit) {
        activity?.let { AdManager.loadInterstitial(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Tasks",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        bottomBar = {
            BannerAd(modifier = Modifier.fillMaxWidth())
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.selectTask(null)
                    if (activity != null) {
                        AdManager.showInterstitial(activity) {
                            navController.navigate(Screen.AddEdit.route)
                        }
                    } else {
                        navController.navigate(Screen.AddEdit.route)
                    }
                },
                containerColor = Color(0xFF0ED2F7),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
                        )
                    )
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search tasks...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )

                if (taskList.isEmpty()) {
                    Text(
                        text = "No tasks found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 40.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                    ) {
                        itemsIndexed(taskList) { index, task ->
                            TaskItem(
                                task,
                                onEdit = {
                                    viewModel.selectTask(task)
                                    navController.navigate(Screen.AddEdit.route)
                                },
                                onDelete = {
                                    viewModel.deleteTask(task)
                                }
                            )

                            // Insert native ad after every 4 items
                            if ((index + 1) % 4 == 0) {
                                var nativeAd by remember { mutableStateOf<com.google.android.gms.ads.nativead.NativeAd?>(null) }
                                LaunchedEffect(Unit) {
                                    AdManager.loadNativeAd(context) { ad -> nativeAd = ad }
                                }
                                nativeAd?.let { NativeAdCard(it) }
                            }
                        }
                    }
                }
            }
        }
    )
}