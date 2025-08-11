package com.example.mytask.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mytask.model.Task
import com.example.mytask.viewmodel.TaskViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mytask.ads.AdManager

@Composable
fun AddEditTaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    val selectedTask by viewModel.selectedTask.collectAsState()
    val isEdit = selectedTask != null

    var title by remember { mutableStateOf(selectedTask?.title ?: "") }
    var description by remember { mutableStateOf(selectedTask?.description ?: "") }
    var date by remember { mutableStateOf(selectedTask?.date ?: "") }
    var priority by remember { mutableStateOf(selectedTask?.priority ?: "Low") }
    var status by remember { mutableStateOf(selectedTask?.status ?: "Pending") }
    var category by remember { mutableStateOf(selectedTask?.category ?: "General") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFBD3E9), Color(0xFFBB377D))))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Space for banner
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isEdit) "Edit Task" else "Add Task",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (dd-mm-yyyy)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            DropdownMenuBox(
                label = "Priority",
                selected = priority,
                options = listOf("Low", "Medium", "High"),
                onSelected = { priority = it }
            )

            DropdownMenuBox(
                label = "Status",
                selected = status,
                options = listOf("Pending", "In Progress", "Completed"),
                onSelected = { status = it }
            )

            DropdownMenuBox(
                label = "Category",
                selected = category,
                options = listOf("Work", "Study", "Personal", "Other"),
                onSelected = { category = it }
            )

            Button(
                onClick = {
                    val task = Task(
                        id = selectedTask?.id ?: 0,
                        title = title,
                        description = description,
                        date = date,
                        priority = priority,
                        status = status,
                        category = category
                    )
                    if (isEdit) viewModel.updateTask(task) else viewModel.addTask(task)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = if (isEdit) "Update" else "Save", color = Color.Black)
            }
        }

        // ✅ Banner Ad at the bottom
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = AdManager.bannerAdUnitId // Load from AdManager
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun DropdownMenuBox(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}