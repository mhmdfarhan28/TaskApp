package com.example.daggerhilttest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.daggerhilttest.Greeting
import com.example.daggerhilttest.ui.navigation.ListOfTask
import com.example.daggerhilttest.viewmodel.MyViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: MyViewModel
){
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    LaunchedEffect(viewModel.titleScreen) {
        viewModel.setTitleScreen("Add task")
    }
    Greeting(
        navController = navController,
        viewModel = viewModel
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Title") },
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            OutlinedTextField(
                label = { Text(text = "Description") },
                value = description,
                minLines = 3,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ){
                Button(
                    onClick = {
                        MainScope().launch {
                            viewModel.addTask(title, description, false)
                            viewModel.setTitleScreen("All tasks")
                            navController.navigate(ListOfTask.route)
                        }
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 3.dp)
                ) {
                    Text(text = "Add Task")
                }
                OutlinedButton(
                    onClick = {
                        viewModel.setTitleScreen("All tasks")
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 3.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
